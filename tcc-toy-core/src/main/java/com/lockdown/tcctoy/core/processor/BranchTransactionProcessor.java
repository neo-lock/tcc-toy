package com.lockdown.tcctoy.core.processor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lockdown.tcctoy.api.TccTransaction;
import com.lockdown.tcctoy.api.TccTransactionType;
import com.lockdown.tcctoy.api.exception.TransactionNotFoundException;
import com.lockdown.tcctoy.core.ParticipantBeanFactory;
import com.lockdown.tcctoy.core.TccTransactionManager;
import com.lockdown.tcctoy.core.TransactionPermit;


public class BranchTransactionProcessor implements TransactionTypeProcessor {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public Object processTransaction(ProceedingJoinPoint pjp, TransactionPermit permit) throws Throwable {
		switch (permit.getTransactionStatus()) {
			case CONFIRMING:{
				try {
					logger.info(" branch restart confirming transaction {}",permit.getId());
					TccTransactionManager.restarExistTransaction(permit);
					TccTransactionManager.commitTransaction();
				}catch(TransactionNotFoundException ex) {
					//ignore
				}
				break;
			}
			case CANCELING:{
				try {
					logger.info(" branch restart canceling transaction {}",permit.getId());
					TccTransactionManager.restarExistTransaction(permit);
					TccTransactionManager.rollbackTransaction();
				}catch(TransactionNotFoundException ex) {
					//ignore
				}
				break;
			}
			default:{
				TccTransaction transaction = TccTransactionManager.propagationBeginTransaction(permit);
				ParticipantBeanFactory.registerLocalPartipant(pjp, transaction);
				return pjp.proceed(pjp.getArgs());
			}
		}
		return null;
	}

	@Override
	public TccTransactionType supportedType() {
		return TccTransactionType.BRANCH;
	}

}
