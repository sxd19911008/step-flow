package io.github.kentasun.stepflow.api.exception;

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
