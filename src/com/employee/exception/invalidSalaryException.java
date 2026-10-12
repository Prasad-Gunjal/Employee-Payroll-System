package com.employee.exception;

public class invalidSalaryException extends RuntimeException {

    private static final long serialVersionUID = 1L;

	public invalidSalaryException(String message) {
		super(message);
	}

}
