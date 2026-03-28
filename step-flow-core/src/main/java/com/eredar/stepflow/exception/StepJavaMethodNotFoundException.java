package com.eredar.stepflow.exception;

/**
 * 步骤不存在 异常
 */
public class StepJavaMethodNotFoundException extends StepException {

    public StepJavaMethodNotFoundException(String beanName) {
        super(String.format("【%s】不存在", beanName));
    }
}
