package io.github.kentasun.stepflow.engine.impl;

import io.github.kentasun.stepflow.engine.AbstractExpressionEngineProvider;
import io.github.kentasun.stepflow.engine.ExpressionEngine;

/**
 * 基于 AviatorScript 的表达式引擎提供者，实现 {@link AbstractExpressionEngineProvider} SPI 接口。
 */
public class AviatorExpressionEngineProvider extends AbstractExpressionEngineProvider {

    /**
     * SPI 专用无参构造器。
     */
    public AviatorExpressionEngineProvider() {}

    @Override
    public ExpressionEngine buildExpressionEngine() {
        return new AviatorExpressionEngine(engineProperties, engineCustomizer);
    }
}
