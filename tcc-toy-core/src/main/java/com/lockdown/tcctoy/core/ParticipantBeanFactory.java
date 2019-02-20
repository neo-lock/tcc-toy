package com.lockdown.tcctoy.core;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.ClassUtils;

import com.lockdown.tcctoy.api.TccParticipant;
import com.lockdown.tcctoy.api.TccParticipantType;
import com.lockdown.tcctoy.api.TccTransaction;
import com.lockdown.tcctoy.api.TransactionId;
import com.lockdown.tcctoy.core.util.TccSignatureUtils;

public class ParticipantBeanFactory {

	
	public static void registerLocalPartipant(ProceedingJoinPoint pjp,TccTransaction transaction) throws Exception {
		Method method = ((MethodSignature)pjp.getSignature()).getMethod();
		ATccTransaction annotation = method.getAnnotation(ATccTransaction.class);
		TransactionId tid = transaction.transactionId();
		tid.setBranchQualifier(TccSignatureUtils.methodSignature(method));
		TccParticipant participant = new DefaultTccParticipant(tid, 
				createMethodInvocation(method,annotation.confirmClass(),annotation.confirmMethodName(),pjp),
				createMethodInvocation(method,annotation.cancelClass(),annotation.cancelMethodName(),pjp),TccParticipantType.LOCAL);
		transaction.registerParticipant(participant);
		TccTransactionManager.getRepository().update(transaction);
	}
	
	public static TccParticipant registerRemoteParticipant(ProceedingJoinPoint pjp,TccTransaction transaction,TransactionId transactionId)throws Exception {
		Method method = ((MethodSignature)pjp.getSignature()).getMethod();
		TccParticipant participant = new DefaultTccParticipant(transactionId, 
				createMethodInvocation(method,method.getDeclaringClass(),method.getName(),pjp),
				createMethodInvocation(method,method.getDeclaringClass(),method.getName(),pjp),TccParticipantType.REMOTE);
		transaction.registerParticipant(participant);
		TccTransactionManager.getRepository().update(transaction);
		return participant;
	}
	
	
	
	
	
	private static MethodInvocation createMethodInvocation(Method method,Class<?> methodClass,String methodName,ProceedingJoinPoint pjp) {
		Class<?> target = methodClass== Void.class ? pjp.getTarget().getClass() : methodClass;
		validateMethod(target, methodName, method.getParameterTypes());
		return new MethodInvocation(target, methodName, method.getParameterTypes(), pjp.getArgs());
	}
	
	
	private static void validateMethod(Class<?> methodClass,String methodName,Class<?>[] parameterTypes) {
		try {
			ClassUtils.getMethod(methodClass, methodName,parameterTypes);
		}catch(IllegalStateException ex) {
			throw ex;
		}
	}
}
