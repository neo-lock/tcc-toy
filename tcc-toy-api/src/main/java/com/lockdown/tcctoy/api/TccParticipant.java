package com.lockdown.tcctoy.api;

import java.io.Serializable;

public interface TccParticipant extends Serializable {
	
	public void commit();
	
	public void rollback();
	
	public TransactionId transactionId();
	
}
