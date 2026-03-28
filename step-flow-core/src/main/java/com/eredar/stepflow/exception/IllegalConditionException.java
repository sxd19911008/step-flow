package com.eredar.stepflow.exception;

/**
 * 判断条件表达式不合法
 */
public class IllegalConditionException extends StepException {

    public IllegalConditionException(String message) {
        super(message);
    }
}
