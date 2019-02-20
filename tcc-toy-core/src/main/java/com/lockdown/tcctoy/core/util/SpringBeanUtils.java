package com.lockdown.tcctoy.core.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringBeanUtils implements ApplicationContextAware {

	private static ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringBeanUtils.applicationContext = applicationContext;
	}
	
	
	public static <T> T getBean(Class<T> target) {
		return SpringBeanUtils.applicationContext.getBean(target);
	}

}
