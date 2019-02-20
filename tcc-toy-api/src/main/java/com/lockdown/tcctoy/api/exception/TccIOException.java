package com.lockdown.tcctoy.api.exception;

import java.sql.SQLException;

public class TccIOException extends TccTransactionException {

	
	public TccIOException(SQLException e) {
		super(e);
	}

	private static final long serialVersionUID = -6285582148769278594L;

}
