package com.employee.exception;

public class DatabaseException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DatabaseException(String message, Throwable couse) {
		super(message, couse);
	}

	public DatabaseException(String message) {
		super(message);
	}

}
