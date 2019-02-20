package com.lockdown.tcctoy.core.processor;

import com.lockdown.tcctoy.api.TccTransactionType;

public interface TransactionTypeProcessor extends TransactionProcessor {
	
	TccTransactionType supportedType();

}
