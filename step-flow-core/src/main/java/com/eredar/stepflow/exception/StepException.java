package com.eredar.stepflow.exception;

public class StepException extends RuntimeException {

    public StepException() {
    }

    public StepException(String message) {
        super(message);
    }

    public StepException(String message, Throwable cause) {
        super(message, cause);
    }

    public StepException(Throwable cause) {
        super(cause);
    }
}
