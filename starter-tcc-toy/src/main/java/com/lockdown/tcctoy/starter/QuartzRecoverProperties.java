package com.lockdown.tcctoy.starter;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="tcc.transaction.recovery.quartz")
public class QuartzRecoverProperties {

    private boolean enable = false;

    private String jobDetailName = "RecoverJobDetail";

    private String jobTriggerName = "RecoverJobTrigger";

    private String schedulerName = "RecoverScheduler";

    public String getSchedulerName() {
        return schedulerName;
    }

    public void setSchedulerName(String schedulerName) {
        this.schedulerName = schedulerName;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getJobDetailName() {
        return jobDetailName;
    }

    public void setJobDetailName(String jobDetailName) {
        this.jobDetailName = jobDetailName;
    }

    public String getJobTriggerName() {
        return jobTriggerName;
    }

    public void setJobTriggerName(String jobTriggerName) {
        this.jobTriggerName = jobTriggerName;
    }
}
