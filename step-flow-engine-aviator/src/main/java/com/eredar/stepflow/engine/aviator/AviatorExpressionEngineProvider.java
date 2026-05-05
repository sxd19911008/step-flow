package com.eredar.stepflow.engine.aviator;

import com.eredar.stepflow.config.StepFlowEngineProperties;
import com.eredar.stepflow.engine.BusinessExpressionEngine;
import com.eredar.stepflow.engine.ConditionExpressionEngine;
import com.eredar.stepflow.engine.ExpressionEngineProvider;
import com.eredar.stepflow.engine.ParamExpressionEngine;
import com.eredar.stepflow.engine.impl.AviatorBusinessExpressionEngine;
import com.eredar.stepflow.engine.impl.AviatorConditionExpressionEngine;
import com.eredar.stepflow.engine.impl.AviatorParamExpressionEngine;
import lombok.Setter;

/**
 * 基于 AviatorScript 的表达式引擎提供者，实现 {@link ExpressionEngineProvider} SPI 接口。
 */
@Setter
public class AviatorExpressionEngineProvider implements ExpressionEngineProvider {

    private StepFlowEngineProperties paramEngineProperties;

    private StepFlowEngineProperties conditionEngineProperties;

    private StepFlowEngineProperties businessEngineProperties;

    /**
     * SPI 专用无参构造器。
     */
    public AviatorExpressionEngineProvider() {}

    /**
     * 创建参数取值引擎
     */
    @Override
    public ParamExpressionEngine buildParamExpressionEngine() {
        return new AviatorParamExpressionEngine(this.paramEngineProperties);
    }

    /**
     * 创建条件判断引擎
     */
    @Override
    public ConditionExpressionEngine buildConditionExpressionEngine() {
        return new AviatorConditionExpressionEngine(this.conditionEngineProperties);
    }

    /**
     * 创建业务计算引擎
     */
    @Override
    public BusinessExpressionEngine buildBusinessExpressionEngine() {
        return new AviatorBusinessExpressionEngine(this.businessEngineProperties);
    }
}
