package com.lockdown.tcctoy.support;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.lockdown.tcctoy.api.support.TccTransactionRecovery;

public class QuartzTransactionRecovery extends QuartzJobBean{

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private TccTransactionRecovery recovery;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		logger.info(" start recovery transaction !");
		recovery.startRecoverTransaction();
	}

	
	

}
