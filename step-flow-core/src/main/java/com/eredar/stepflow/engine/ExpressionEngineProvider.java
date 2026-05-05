package com.eredar.stepflow.engine;

import com.eredar.stepflow.config.StepFlowEngineProperties;

/**
 * 表达式引擎提供者 SPI 接口。
 */
public interface ExpressionEngineProvider {

    void setParamEngineProperties(StepFlowEngineProperties paramEngineProperties);

    void setConditionEngineProperties(StepFlowEngineProperties conditionEngineProperties);

    void setBusinessEngineProperties(StepFlowEngineProperties businessEngineProperties);

    /**
     * 获取已初始化的参数取值引擎。
     */
    ParamExpressionEngine buildParamExpressionEngine();

    /**
     * 获取已初始化的条件判断引擎。
     */
    ConditionExpressionEngine buildConditionExpressionEngine();

    /**
     * 获取已初始化的业务计算引擎。
     */
    BusinessExpressionEngine buildBusinessExpressionEngine();
}
