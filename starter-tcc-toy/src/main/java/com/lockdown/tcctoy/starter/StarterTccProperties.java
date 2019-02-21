package com.lockdown.tcctoy.starter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.lockdown.tcctoy.core.TccTransactionProperties;

@ConfigurationProperties(prefix = "tcc.transaction") 
public class StarterTccProperties implements TccTransactionProperties{

	@Value("${spring.application.name}")
	private String domain;

	private String tableName;

	private long beforeSeconds = 60*30;

	private int limitRetry = 5;

	private int recoverIntervalSeconds = 30;

	@Override
	public long getBeforeSeconds() {
		return beforeSeconds;
	}

	public void setBeforeSeconds(long beforeSeconds) {
		this.beforeSeconds = beforeSeconds;
	}

	@Override
	public int getLimitRetry() {
		return limitRetry;
	}

	public void setLimitRetry(int limitRetry) {
		this.limitRetry = limitRetry;
	}

	@Override
	public int getRecoverIntervalSeconds() {
		return recoverIntervalSeconds;
	}

	public void setRecoverIntervalSeconds(int recoverIntervalSeconds) {
		this.recoverIntervalSeconds = recoverIntervalSeconds;
	}

	@Override
	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
}
