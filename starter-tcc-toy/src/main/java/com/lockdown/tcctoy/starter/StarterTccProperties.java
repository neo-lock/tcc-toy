package com.lockdown.tcctoy.starter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.lockdown.tcctoy.core.TccTransactionProperties;

@ConfigurationProperties(prefix = "tcc.transaction") 
public class StarterTccProperties implements TccTransactionProperties{

	@Value("${spring.application.name}")
	private String domain;
	
	private long beforeSeconds = 60*30;
	
	private int limitRetry = 5;
	
	private int recoverIntervalSeconds = 30;
	
	private String tableName;

	private boolean enableRecovery = true;

	public boolean isEnableRecovery() {
		return enableRecovery;
	}

	public void setEnableRecovery(boolean enableRecovery) {
		this.enableRecovery = enableRecovery;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public int getRecoverIntervalSeconds() {
		return recoverIntervalSeconds;
	}

	public void setRecoverIntervalSeconds(int recoverIntervalSeconds) {
		this.recoverIntervalSeconds = recoverIntervalSeconds;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public long getBeforeSeconds() {
		return beforeSeconds;
	}

	public void setBeforeSeconds(long beforeSeconds) {
		this.beforeSeconds = beforeSeconds;
	}

	public int getLimitRetry() {
		return limitRetry;
	}

	public void setLimitRetry(int limitRetry) {
		this.limitRetry = limitRetry;
	}
	
	
	
	
	

}
