package com.employee.exception;

public class EmployeeNotFoundException extends Exception {

	public EmployeeNotFoundException(String message) {
		super(message);
	}

	public EmployeeNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
