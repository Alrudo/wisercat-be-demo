package com.wisercat.demo.exception;

public class InvalidFilterException extends RuntimeException {
    private static final String ERROR_MESSAGE = "Filter with invalid value was provided.";

    public InvalidFilterException() {
        super(ERROR_MESSAGE);
    }
}
