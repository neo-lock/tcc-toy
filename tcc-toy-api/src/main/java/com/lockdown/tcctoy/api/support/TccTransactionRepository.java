package com.lockdown.tcctoy.api.support;

import java.util.Date;
import java.util.List;

import com.lockdown.tcctoy.api.TccTransaction;
import com.lockdown.tcctoy.api.TransactionId;

public interface TccTransactionRepository {

	public void setDomain(String domain);
	
	int create(TccTransaction transaction);

	int update(TccTransaction transaction);

	int delete(TccTransaction transaction);
	
	int hasBranch(TccTransaction transaction);

	TccTransaction findTransaction(TransactionId id);

	public List<TccTransaction>  loadRetryTransaction(Date startTime, Date endTime, int limitRetry);
	
	public List<TccTransaction>  loadRetryTransaction(Date startTime, int limitRetry);

	public int validateVersion(TccTransaction transaction);

	public int updateTransactionVersion(TccTransaction updateVersion);
	
	
}
