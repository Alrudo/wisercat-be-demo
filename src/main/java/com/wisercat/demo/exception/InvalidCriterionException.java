package com.wisercat.demo.exception;

public class InvalidCriterionException extends RuntimeException {
    public static final String ERROR_MESSAGE = "Criterion with invalid value was provided.";

    public InvalidCriterionException() {
        super(ERROR_MESSAGE);
    }
}
