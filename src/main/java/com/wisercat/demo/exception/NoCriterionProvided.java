package com.wisercat.demo.exception;

public class NoCriterionProvided extends RuntimeException {
    public static final String ERROR_MESSAGE = "No criterion provided. There should be at least one.";

    public NoCriterionProvided() {
        super(ERROR_MESSAGE);
    }
}
