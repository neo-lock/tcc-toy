package com.lockdown.tcctoy.core.aspectj;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lockdown.tcctoy.api.TccTransaction;
import com.lockdown.tcctoy.api.TccTransactionStatus;
import com.lockdown.tcctoy.api.TransactionId;
import com.lockdown.tcctoy.core.ParticipantBeanFactory;
import com.lockdown.tcctoy.core.TccTransactionManager;
import com.lockdown.tcctoy.core.util.LogUtils;
import com.lockdown.tcctoy.core.util.TccSignatureUtils;

@Aspect
public class TccRemoteParticipantAspect{
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	
	@Around("@annotation(com.lockdown.tcctoy.core.ATccRemoteParticipant)")
	public Object around(ProceedingJoinPoint pjp) throws Throwable{
		
		try {
			TccTransaction transaction = TccTransactionManager.getCurrentTransaction();
			if(null==transaction) {
				return pjp.proceed(pjp.getArgs());
			}
			
			TransactionId  branchId = getBranchTransactionId(transaction, pjp);
			if(transaction.getTransactionStatus() == TccTransactionStatus.PREPARATION) {
				ParticipantBeanFactory.registerRemoteParticipant(pjp, transaction, branchId);
			}
			
			TccTransactionManager.generateRemotePermit(branchId,
					transaction.getTransactionStatus(),
					TccSignatureUtils.remoteSignature(((MethodSignature)pjp.getSignature()).getMethod()));
			
			logger.info("{} remote permit {}",LogUtils.LOG_PREFIX,TccTransactionManager.getRemotePermit());
			
			return pjp.proceed(pjp.getArgs());
		}finally {
			TccTransactionManager.clearRemotePermit();
		}
	}
	
	private TransactionId getBranchTransactionId(TccTransaction transaction,ProceedingJoinPoint pjp) {
		Method method = ((MethodSignature)pjp.getSignature()).getMethod();
		TransactionId tid = transaction.transactionId();
		tid.setBranchQualifier(TccSignatureUtils.methodSignature(method));
		return tid;
	}
	
	
	
	
	
	

}
