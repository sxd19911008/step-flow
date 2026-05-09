package io.github.kentasun.stepflow.engine.impl;

import io.github.kentasun.stepflow.engine.AbstractExpressionEngineProvider;
import io.github.kentasun.stepflow.engine.BusinessExpressionEngine;
import io.github.kentasun.stepflow.engine.ConditionExpressionEngine;
import io.github.kentasun.stepflow.engine.ParamExpressionEngine;

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
