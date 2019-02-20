package com.lockdown.tcctoy.core.processor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lockdown.tcctoy.api.TccTransaction;
import com.lockdown.tcctoy.api.TccTransactionType;
import com.lockdown.tcctoy.api.exception.TransactionRollbackException;
import com.lockdown.tcctoy.core.ParticipantBeanFactory;
import com.lockdown.tcctoy.core.TccTransactionManager;
import com.lockdown.tcctoy.core.TransactionPermit;
import com.lockdown.tcctoy.core.util.LogUtils;

public class RootTransactionProcessor implements TransactionTypeProcessor {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public Object processTransaction(ProceedingJoinPoint pjp, TransactionPermit permit) throws Throwable {
		Object result = null;
		TccTransaction transaction = TccTransactionManager.beginTransaction();
		ParticipantBeanFactory.registerLocalPartipant(pjp, transaction);
		try {
			try {
				logger.debug("=================================== start tcc transaction try ! ===================================");
				result = pjp.proceed(pjp.getArgs());
			}catch(Throwable t) {
				logger.debug("=================================== start tcc transaction cancel ! ===================================");
				try {
					TccTransactionManager.rollbackTransaction();
				}catch(TransactionRollbackException ex) {
					//如果是根事务，并且没有分支，意味着，本地服务调用远程服务失败，没在远程服务创建事务，此时可以尝试立即删除，不需要恢复
					if(transaction.getTransactionType() == TccTransactionType.ROOT) {
						logger.info("{} root transaction rollback failed, try to delete if no children !",LogUtils.LOG_PREFIX);
						TccTransactionManager.tryToDelete(transaction);
					}
				}
				throw t;
			}
			logger.debug("=================================== start tcc transaction confirm ! ===================================");
			TccTransactionManager.commitTransaction();
		}finally {
			TccTransactionManager.releaseTransaction();
		}
		return result;
	}

	@Override
	public TccTransactionType supportedType() {
		return TccTransactionType.ROOT;
	}

}
