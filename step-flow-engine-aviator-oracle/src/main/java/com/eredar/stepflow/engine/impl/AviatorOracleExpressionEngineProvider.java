package com.eredar.stepflow.engine.impl;

import com.eredar.stepflow.config.StepFlowEngineProperties;
import com.eredar.stepflow.engine.BusinessExpressionEngine;
import com.eredar.stepflow.engine.ConditionExpressionEngine;
import com.eredar.stepflow.engine.ExpressionEngineProvider;
import com.eredar.stepflow.engine.ParamExpressionEngine;
import lombok.Setter;

/**
 * 基于 AviatorScript 的表达式引擎提供者，实现 {@link ExpressionEngineProvider} SPI 接口。
 */
@Setter
public class AviatorOracleExpressionEngineProvider implements ExpressionEngineProvider {

    private StepFlowEngineProperties paramEngineProperties;

    private StepFlowEngineProperties conditionEngineProperties;

    private StepFlowEngineProperties businessEngineProperties;

    /**
     * SPI 专用无参构造器。
     */
    public AviatorOracleExpressionEngineProvider() {}

    /**
     * 创建参数取值引擎
     */
    @Override
    public ParamExpressionEngine buildParamExpressionEngine() {
        return new AviatorOracleParamExpressionEngine(this.paramEngineProperties);
    }

    /**
     * 创建条件判断引擎
     */
    @Override
    public ConditionExpressionEngine buildConditionExpressionEngine() {
        return new AviatorOracleConditionExpressionEngine(this.conditionEngineProperties);
    }

    /**
     * 创建业务计算引擎
     */
    @Override
    public BusinessExpressionEngine buildBusinessExpressionEngine() {
        return new AviatorOracleBusinessExpressionEngine(this.businessEngineProperties);
    }
}
