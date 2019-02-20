package com.lockdown.tcctoy.api.exception;

public class TccTransactionException  extends RuntimeException{

	private static final long serialVersionUID = -1184608562994415500L;
	
	public TccTransactionException() {
		
	}
	
	public TccTransactionException(String message) {
		super(message);
	}
	
	public TccTransactionException(Throwable t) {
		super(t);
	}

}
