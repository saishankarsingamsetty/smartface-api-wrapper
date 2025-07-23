package com.smartface.exception;

public class SmartfaceException extends RuntimeException {

	private String message;
	private int statusCode;

	public SmartfaceException(String message, int statusCode) {
		super(message);
		this.message = message;
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public int getStatusCode() {
		return statusCode;
	}
	
	
}
