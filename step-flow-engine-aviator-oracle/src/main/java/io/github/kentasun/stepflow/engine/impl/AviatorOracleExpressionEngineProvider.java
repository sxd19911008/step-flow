package io.github.kentasun.stepflow.engine.impl;

import io.github.kentasun.stepflow.engine.AbstractExpressionEngineProvider;
import io.github.kentasun.stepflow.engine.ExpressionEngine;

/**
 * 基于 AviatorScript 的表达式引擎提供者，实现 {@link AbstractExpressionEngineProvider} SPI 接口。
 */
public class AviatorOracleExpressionEngineProvider extends AbstractExpressionEngineProvider {

    /**
     * SPI 专用无参构造器。
     */
    public AviatorOracleExpressionEngineProvider() {}

    @Override
    public ExpressionEngine buildExpressionEngine() {
        return new AviatorOracleExpressionEngine(engineProperties, engineCustomizer);
    }
}
