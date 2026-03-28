package com.eredar.stepflow.handler;

import com.eredar.stepflow.dto.OneOffStepParams;
import com.eredar.stepflow.dto.StepContext;
import com.eredar.stepflow.dto.StepInfo;
import com.eredar.stepflow.engine.ExpressionEngine;
import com.eredar.stepflow.intf.StepHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 表达式引擎步骤处理器
 */
@Component
public class ExpressionEngineHandler implements StepHandler {

    @Autowired
    private ExpressionEngine expressionEngine;

    @Override
    public Object execute(StepInfo stepInfo, StepContext stepContext, OneOffStepParams oneOffStepParams) {
        // 表达式
        String expression = stepInfo.getContent().getExpression();

        // 计算表达式并返回
        return expressionEngine.execute(expression, oneOffStepParams.getVars());
    }
}
