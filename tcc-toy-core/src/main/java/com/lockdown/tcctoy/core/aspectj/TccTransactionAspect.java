package com.lockdown.tcctoy.core.aspectj;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lockdown.tcctoy.core.TransactionPermit;
import com.lockdown.tcctoy.core.processor.TransactionProcessor;
import com.lockdown.tcctoy.core.util.LogUtils;
import com.lockdown.tcctoy.core.util.RequestUtils;

@Aspect
public class TccTransactionAspect{
	
	private TransactionProcessor transactionProcessor;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	

	public TccTransactionAspect(TransactionProcessor transactionProcessor) {
		this.transactionProcessor = transactionProcessor;
	}



	/**
	 * tcc 事务开始,
	 * 是否需要开启事务,
	 * 如果是的话，是否有事务,如果有的话，当前事务是哪个阶段
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	@Around("@annotation(com.lockdown.tcctoy.core.ATccTransaction)")
	public Object around(ProceedingJoinPoint pjp) throws Throwable{;
		TransactionPermit permit = RequestUtils.getValidRequestPermit();
		logger.info("{} current transaction permit {}",LogUtils.LOG_PREFFIX,permit);
		return transactionProcessor.processTransaction(pjp,permit);
	}
	

}
