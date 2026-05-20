package io.github.kentasun.stepflow.api.step;

import io.github.kentasun.stepflow.api.dto.OneOffParams;
import io.github.kentasun.stepflow.api.dto.StepFlowContext;
import io.github.kentasun.stepflow.api.step.dto.StepData;

/**
 * Java 步骤使用的 Java 方法
 */
public interface JavaStep {

    Object invoke(StepData stepData, StepFlowContext stepFlowContext, OneOffParams oneOffParams);
}
