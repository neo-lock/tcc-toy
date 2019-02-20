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
	
	private int recoveIntervalSeconds = 30;
	
	private String tableName;

	

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public int getRecoveIntervalSeconds() {
		return recoveIntervalSeconds;
	}

	public void setRecoveIntervalSeconds(int recoveIntervalSeconds) {
		this.recoveIntervalSeconds = recoveIntervalSeconds;
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
