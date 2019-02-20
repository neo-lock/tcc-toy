package com.lockdown.tcctoy.api.exception;

public class TransactionRollbackException extends TccTransactionException {

	private static final long serialVersionUID = 8670431882057389878L;
	
	public TransactionRollbackException(Throwable t) {
		super(t);
	}

	

}
