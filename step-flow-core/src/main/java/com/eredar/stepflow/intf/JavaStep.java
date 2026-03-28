package com.eredar.stepflow.intf;

import com.eredar.stepflow.dto.OneOffStepParams;
import com.eredar.stepflow.dto.StepContext;
import com.eredar.stepflow.dto.StepInfo;

/**
 * Java 步骤使用的 Java 方法
 */
public interface JavaStep {

    Object invoke(StepInfo stepInfo, StepContext stepContext, OneOffStepParams oneOffStepParams);
}
