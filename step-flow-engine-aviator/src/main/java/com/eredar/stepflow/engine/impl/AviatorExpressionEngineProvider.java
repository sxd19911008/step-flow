package com.eredar.stepflow.engine.impl;

import com.eredar.stepflow.engine.AbstractExpressionEngineProvider;
import com.eredar.stepflow.engine.BusinessExpressionEngine;
import com.eredar.stepflow.engine.ConditionExpressionEngine;
import com.eredar.stepflow.engine.ParamExpressionEngine;

/**
 * 基于 AviatorScript 的表达式引擎提供者，实现 {@link AbstractExpressionEngineProvider} SPI 接口。
 */
public class AviatorExpressionEngineProvider extends AbstractExpressionEngineProvider {

    /**
     * SPI 专用无参构造器。
     */
    public AviatorExpressionEngineProvider() {}

    @Override
    public ParamExpressionEngine buildParamExpressionEngine() {
        return new AviatorParamExpressionEngine(paramEngineProperties, paramEngineCustomizer);
    }

    @Override
    public ConditionExpressionEngine buildConditionExpressionEngine() {
        return new AviatorConditionExpressionEngine(conditionEngineProperties, conditionEngineCustomizer);
    }

    @Override
    public BusinessExpressionEngine buildBusinessExpressionEngine() {
        return new AviatorBusinessExpressionEngine(businessEngineProperties, businessEngineCustomizer);
    }
}
