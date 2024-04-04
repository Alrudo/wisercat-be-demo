package com.wisercat.demo.exception;

public class FilterNotFoundException extends RuntimeException {
    private static final String ERROR_MESSAGE = "Filter with provided id=%s was not found.";
    public FilterNotFoundException(Long id) {
        super(ERROR_MESSAGE.formatted(id));
    }
}
