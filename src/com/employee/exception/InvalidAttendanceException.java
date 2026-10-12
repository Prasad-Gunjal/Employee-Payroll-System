package com.employee.exception;

public class InvalidAttendanceException extends Exception {

    private static final long serialVersionUID = 1L;

    public InvalidAttendanceException(String message) {
        super(message);
    }
}
