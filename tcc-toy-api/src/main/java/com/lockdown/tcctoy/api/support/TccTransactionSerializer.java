package com.lockdown.tcctoy.api.support;

import com.lockdown.tcctoy.api.TccTransaction;

public interface TccTransactionSerializer {
	
	
	public TccTransaction parseBytes(byte[] content);
	
	public byte[] serializeTransaction(TccTransaction transaction);

}
