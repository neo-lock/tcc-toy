package com.lockdown.tcctoy.core;

public interface TccTransactionProperties {


	public String getDomain();

	public long getBeforeSeconds();

	public int getLimitRetry();


	public int getRecoverIntervalSeconds();

	
}
