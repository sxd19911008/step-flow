package com.eredar.stepflow.engine.impl;

import com.eredar.stepflow.engine.AbstractExpressionEngineProvider;
import com.eredar.stepflow.engine.BusinessExpressionEngine;
import com.eredar.stepflow.engine.ConditionExpressionEngine;
import com.eredar.stepflow.engine.ParamExpressionEngine;

/**
 * 基于 Apache Commons JEXL 的表达式引擎提供者，继承 {@link AbstractExpressionEngineProvider}。
 */
public class JexlExpressionEngineProvider extends AbstractExpressionEngineProvider {

    public JexlExpressionEngineProvider() {}

    @Override
    public ParamExpressionEngine buildParamExpressionEngine() {
        return new JexlParamExpressionEngine(paramEngineProperties, paramEngineCustomizer);
    }

    @Override
    public ConditionExpressionEngine buildConditionExpressionEngine() {
        return new JexlConditionExpressionEngine(conditionEngineProperties, conditionEngineCustomizer);
    }

    @Override
    public BusinessExpressionEngine buildBusinessExpressionEngine() {
        return new JexlBusinessExpressionEngine(businessEngineProperties, businessEngineCustomizer);
    }
}
