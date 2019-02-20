package com.lockdown.tcctoy.api.exception;

public class TransactionCommitException extends TccTransactionException {

	public TransactionCommitException(Throwable t) {
		super(t);
	}

	private static final long serialVersionUID = -6318998771107910663L;

}
