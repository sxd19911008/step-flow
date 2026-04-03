package com.eredar.stepflow.step.handler;

import com.eredar.stepflow.dto.ExecutorsContext;
import com.eredar.stepflow.dto.OneOffParams;
import com.eredar.stepflow.dto.StepFlowContext;
import com.eredar.stepflow.step.dto.StepData;
import com.eredar.stepflow.step.intf.StepHandler;

/**
 * 表达式引擎步骤处理器
 */
public class ExpressionStepHandler implements StepHandler {

    @Override
    public Object execute(StepData stepData, StepFlowContext stepFlowContext, OneOffParams oneOffParams, ExecutorsContext executorsContext) {
        // 表达式
        String expression = stepData.getContent();
        // 执行表达式并返回
        return executorsContext.executeBusinessExpression(expression, oneOffParams.getVars());
    }
}
