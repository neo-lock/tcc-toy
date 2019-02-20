package com.lockdown.tcctoy.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lockdown.tcctoy.api.TccTransaction;
import com.lockdown.tcctoy.api.exception.TccOptimisticLockException;
import com.lockdown.tcctoy.api.support.TccTransactionRecovery;
import com.lockdown.tcctoy.api.support.TccTransactionRepository;
import com.lockdown.tcctoy.core.util.LogUtils;

public class SimpleTransactionRecovery implements TccTransactionRecovery {
	
	
	private TccTransactionRepository repository;
	
	private TccTransactionProperties properties;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	
	

	public SimpleTransactionRecovery(TccTransactionRepository repository, TccTransactionProperties properties) {
		super();
		this.repository = repository;
		this.properties = properties;
	}

	@Override
	public void startRecoverTransaction() {
		retryInvokeTransaction(loadRecoveryTransaction());
	}
	
	private void retryInvokeTransaction(List<TccTransaction> transactions) {
		for(TccTransaction transaction : transactions) {
			try {
				transaction.plusInvokeCount();
				TccTransactionManager.registerTransaction(transaction);
				switch (transaction.getTransactionStatus()) {
				case CONFIRMING:{
					logger.info("{} transaction {} recover confirm  =========== ",LogUtils.LOG_PREFIX,transaction.transactionId());
					TccTransactionManager.commitTransaction();
					break;
				}
				case CANCELING:{
					logger.info("{} transaction {} recover cancel  =========== ",LogUtils.LOG_PREFIX,transaction.transactionId());
					TccTransactionManager.rollbackTransaction();
					break;
				}
				case PREPARATION:{
					logger.info("{} transaction {} is preparetion status, try to delete !",LogUtils.LOG_PREFIX,transaction.transactionId());
					TccTransactionManager.tryToDelete(transaction);
					break;
				}
				default:
					throw new IllegalStateException(LogUtils.LOG_PREFIX +" can't supported recovery current "+transaction.getTransactionStatus()+" state transaction , id is "+transaction.transactionId());
				}
			}catch(Throwable ex) {
				logger.warn("{}  transaction {}  recover failed ! error : {} ",LogUtils.LOG_PREFIX,transaction.transactionId(),ex.getMessage());
			}
		}
		TccTransactionManager.clearRemotePermit();
		TccTransactionManager.clearTransaction();
		
	}


	private List<TccTransaction> loadRecoveryTransaction(){
		long before = (1000*properties.getBeforeSeconds());
		List<TccTransaction> errorTransaction =  repository.loadRetryTransaction(new Date(System.currentTimeMillis()-before),properties.getLimitRetry());
		logger.warn(" found error transaction size: {} ",LogUtils.LOG_PREFIX,errorTransaction.size());
		List<TccTransaction> retryTransaction = new ArrayList<TccTransaction>();
		
		//在方法上面加上分布式锁，可以不进行乐观锁检查
		for(TccTransaction transaction : errorTransaction) {
			try {
				repository.validateVersion(transaction);
			}catch(TccOptimisticLockException ex) {
				logger.warn("{} transaction {} version mismatch,skip current transaction !",LogUtils.LOG_PREFIX,transaction.transactionId());
				continue;
			}
			retryTransaction.add(transaction);
		}
		logger.warn("{} recover transaction size: {}",LogUtils.LOG_PREFIX,retryTransaction.size());
		return retryTransaction;
		
	}
	
	
	
	
	
	

}
