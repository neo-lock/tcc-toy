package com.lockdown.tcctoy.core.processor;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.lockdown.tcctoy.api.TccTransactionType;
import com.lockdown.tcctoy.core.TransactionPermit;

public class TccTransactionTypeProcessor  implements TransactionProcessor ,ApplicationContextAware{
	
	private Map<TccTransactionType,TransactionTypeProcessor>  processorContext;
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public Object processTransaction(ProceedingJoinPoint pjp, TransactionPermit permit) throws Throwable {
		if(!processorContext.containsKey(permit.getTransactionType())) {
			throw new IllegalArgumentException(" can't supported current transaction type : "+permit.getTransactionType()+" !");
		}
		return processorContext.get(permit.getTransactionType()).processTransaction(pjp, permit);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		processorContext = new HashMap<TccTransactionType,TransactionTypeProcessor>();
		applicationContext.getBeansOfType(TransactionTypeProcessor.class).values().forEach((processor)->{
			processorContext.put(processor.supportedType(), processor);
			logger.info("registered {} type transaction processor ",processor.supportedType());
		});
	}

}
