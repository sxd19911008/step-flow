package com.eredar.stepflow.exception;

/**
 * 步骤不存在 异常
 */
public class StepNotFoundException extends StepException {

    public StepNotFoundException(String stepCode) {
        super(String.format("【%s】步骤不存在", stepCode));
    }
}
