package com.drisk.domain.exceptions;

public class ExceededAvailableTanksException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public ExceededAvailableTanksException() {}
	
	public ExceededAvailableTanksException(String message) {
		super(message);
	}

}
