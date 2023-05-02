package com.agl.tenpo.domain.exceptions;

public class PercentageCachedNotFoundException extends RuntimeException{

    private static final String MESSAGE = "Percentage not found into cache";

    public PercentageCachedNotFoundException() {
        super(MESSAGE);
    }
}
