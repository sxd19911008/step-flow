package io.github.kentasun.stepflow.engine;

import io.github.kentasun.stepflow.config.StepFlowEngineProperties;
import lombok.Setter;

/**
 * 表达式引擎提供者抽象基类
 */
@Setter
public abstract class AbstractExpressionEngineProvider {

    // 表达式引擎通用配置
    protected StepFlowEngineProperties engineProperties;

    // 表达式引擎定制回调
    protected EngineCustomizer engineCustomizer;

    /**
     * 构建表达式引擎。
     *
     * @return 表达式引擎
     */
    public abstract ExpressionEngine buildExpressionEngine();
}
