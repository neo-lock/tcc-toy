package com.lockdown.tcctoy.core.processor;

import org.aspectj.lang.ProceedingJoinPoint;

import com.lockdown.tcctoy.core.TransactionPermit;

public interface TransactionProcessor {
	
	
	public Object processTransaction(ProceedingJoinPoint pjp, TransactionPermit permit) throws Throwable;
	

}
