package com.lockdown.tcctoy.api;

import java.util.Date;

public interface TccTransaction extends TccParticipant{
	
	public TccTransactionStatus getTransactionStatus();
	
	public void registerParticipant(TccParticipant participant);
	
	public TransactionId transactionId();
	
	public TccTransaction changeStatus(TccTransactionStatus newStatus);
	
	public TccTransactionType getTransactionType();

	public void plusInvokeCount();

	public TccTransaction updateVersion();

	public Date getUpdateTime();

	public int getVerion();

	public int getInvokeCount();

	public Date getCreateTime();

}
