package com.eredar.stepflow.engine.impl;

import com.eredar.stepflow.config.StepFlowEngineProperties;
import com.eredar.stepflow.engine.BusinessExpressionEngine;
import com.eredar.stepflow.engine.ConditionExpressionEngine;
import com.eredar.stepflow.engine.ExpressionEngineProvider;
import com.eredar.stepflow.engine.ParamExpressionEngine;
import lombok.Setter;

/**
 * 基于 Apache Commons JEXL 的表达式引擎提供者，实现 {@link ExpressionEngineProvider} SPI。
 * <p>
 * 通过 {@link com.eredar.stepflow.engine.jexl.JexlInstanceBuilder} 分别为参数、条件、业务三类引擎构建独立实例，
 * 以支持在 {@link com.eredar.stepflow.config.StepFlowConfigProperties} 中为三者配置不同的缓存大小等选项。
 */
@Setter
public class JexlExpressionEngineProvider implements ExpressionEngineProvider {

    /** 参数取值引擎专用配置（通常调用更频繁，可单独放大缓存） */
    private StepFlowEngineProperties paramEngineProperties;

    /** 条件判断引擎专用配置 */
    private StepFlowEngineProperties conditionEngineProperties;

    /** 业务计算引擎专用配置 */
    private StepFlowEngineProperties businessEngineProperties;

    /**
     * SPI / 反射 所需的公有无参构造器。
     */
    public JexlExpressionEngineProvider() {
    }

    @Override
    public ParamExpressionEngine buildParamExpressionEngine() {
        return new JexlParamExpressionEngine(this.paramEngineProperties);
    }

    @Override
    public ConditionExpressionEngine buildConditionExpressionEngine() {
        return new JexlConditionExpressionEngine(this.conditionEngineProperties);
    }

    @Override
    public BusinessExpressionEngine buildBusinessExpressionEngine() {
        return new JexlBusinessExpressionEngine(this.businessEngineProperties);
    }
}
