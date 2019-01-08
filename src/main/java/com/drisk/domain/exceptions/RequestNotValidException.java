package com.drisk.domain.exceptions;

public class RequestNotValidException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public RequestNotValidException() {}
	
	public RequestNotValidException(String message) {
		super(message);
	}
}
