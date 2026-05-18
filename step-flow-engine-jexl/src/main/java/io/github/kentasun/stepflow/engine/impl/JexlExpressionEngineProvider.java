package io.github.kentasun.stepflow.engine.impl;

import io.github.kentasun.stepflow.engine.AbstractExpressionEngineProvider;
import io.github.kentasun.stepflow.engine.ExpressionEngine;

/**
 * 基于 Apache Commons JEXL 的表达式引擎提供者，继承 {@link AbstractExpressionEngineProvider}。
 */
public class JexlExpressionEngineProvider extends AbstractExpressionEngineProvider {

    public JexlExpressionEngineProvider() {}

    @Override
    public ExpressionEngine buildExpressionEngine() {
        return new JexlExpressionEngine(engineProperties, engineCustomizer);
    }
}
