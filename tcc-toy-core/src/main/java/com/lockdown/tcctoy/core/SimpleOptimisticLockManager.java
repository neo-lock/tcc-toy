package com.lockdown.tcctoy.core;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lockdown.tcctoy.api.TccTransaction;
import com.lockdown.tcctoy.api.TransactionId;
import com.lockdown.tcctoy.api.exception.TccOptimisticLockException;
import com.lockdown.tcctoy.api.support.TccTransactionRepository;
import com.lockdown.tcctoy.core.util.LogUtils;

public class SimpleOptimisticLockManager implements OptimisticLockManager {
	
	
	private final TccTransactionRepository repository;
	private Logger logger = LoggerFactory.getLogger(getClass());

	public SimpleOptimisticLockManager(TccTransactionRepository repository) {
		this.repository = repository;
	}


	@Override
	public int create(TccTransaction transaction) {
		logger.debug("{} create tcc transaction {}",LogUtils.LOG_PREFIX,transaction.transactionId());
		return repository.create(transaction);
	}
	
	

	@Override
	public int update(TccTransaction transaction) {
		logger.debug("{} update tcc transaction {}",LogUtils.LOG_PREFIX,transaction.transactionId());
		int result = repository.update(transaction);
		validateResult(result);
		return result;
	}


	@Override
	public int delete(TccTransaction transaction) {
		logger.debug("{} delete tcc transaction {}",LogUtils.LOG_PREFIX,transaction.transactionId());
		return repository.delete(transaction);
	}

	@Override
	public TccTransaction findTransaction(TransactionId id) {
		logger.debug("{} find tcc transaction {}",LogUtils.LOG_PREFIX,id);
		return repository.findTransaction(id);
	}


	@Override
	public void setDomain(String domain) {
		repository.setDomain(domain);
	}


	@Override
	public List<TccTransaction>  loadRetryTransaction(Date date, int limitRetry) {
		return repository.loadRetryTransaction(date, limitRetry);	
	}


	@Override
	public int validateVersion(TccTransaction transaction) {
		int result = repository.validateVersion(transaction);
		validateResult(result);
		return result;
	}


	@Override
	public int updateTransactionVersion(TccTransaction transaction) {
		int result = repository.updateTransactionVersion(transaction);
		validateResult(result);
		return result;
	}
	
	private void validateResult(int result) {
		if(result <=0 ) {
			throw new TccOptimisticLockException();
		}
	}


	@Override
	public int hasBranch(TccTransaction transaction) {
		return repository.hasBranch(transaction);
	}


	@Override
	public List<TccTransaction> loadRetryTransaction(Date startTime, Date endTime, int limitRetry) {
		return repository.loadRetryTransaction(startTime, endTime, limitRetry);
	}

}
