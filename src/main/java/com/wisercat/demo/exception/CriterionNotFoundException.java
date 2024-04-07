package com.wisercat.demo.exception;

public class CriterionNotFoundException extends RuntimeException {
    public static final String ERROR_MESSAGE = "Criterion with provided id=%s was not found.";

    public CriterionNotFoundException(Long id) {
        super(ERROR_MESSAGE.formatted(id));
    }
}
