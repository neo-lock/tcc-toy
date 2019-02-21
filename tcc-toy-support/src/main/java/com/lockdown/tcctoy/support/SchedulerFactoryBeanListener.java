package com.lockdown.tcctoy.support;

import org.springframework.scheduling.quartz.SchedulerFactoryBean;

public interface SchedulerFactoryBeanListener {

    public void schedulerFactoryBeanCreated(SchedulerFactoryBean bean);

}
