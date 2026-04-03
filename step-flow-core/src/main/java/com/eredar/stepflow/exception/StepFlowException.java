package com.eredar.stepflow.exception;

public class StepFlowException extends RuntimeException {

    public StepFlowException() {
    }

    public StepFlowException(String message) {
        super(message);
    }

    public StepFlowException(String message, Throwable cause) {
        super(message, cause);
    }

    public StepFlowException(Throwable cause) {
        super(cause);
    }
}
