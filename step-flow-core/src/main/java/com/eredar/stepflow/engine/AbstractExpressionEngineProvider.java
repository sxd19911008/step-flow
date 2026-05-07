package com.eredar.stepflow.engine;

import com.eredar.stepflow.config.StepFlowEngineProperties;

import java.util.function.Consumer;

/**
 * 表达式引擎提供者抽象基类
 *
 * <p>统一持有三组"声明式配置（{@link StepFlowEngineProperties}）"
 * 和"编程式定制回调（{@link Consumer}）"，
 * 子类通过实现三个 {@code do*} 抽象方法，专注于构建各自引擎即可。
 *
 * <p><b>定制回调说明：</b>
 * 各引擎实现类的构造方法负责在合适时机调用 {@code customizer}：
 * <ul>
 *   <li>Aviator 系：底层实例（{@code AviatorEvaluatorInstance}）可变，
 *       在构建完成后回调，入参为该实例。</li>
 *   <li>JEXL 系：底层实例（{@code JexlEngine}）创建后不可变，
 *       在 {@code JexlBuilder.create()} 之前回调，入参为 {@code JexlBuilder}。</li>
 * </ul>
 */
public abstract class AbstractExpressionEngineProvider {

    // 参数取值引擎通用配置
    protected StepFlowEngineProperties paramEngineProperties;

    // 条件判断引擎通用配置
    protected StepFlowEngineProperties conditionEngineProperties;

    // 业务计算引擎通用配置
    protected StepFlowEngineProperties businessEngineProperties;

    // 参数取值引擎编程式定制回调
    protected EngineCustomizer paramEngineCustomizer;

    // 条件判断引擎编程式定制回调
    protected EngineCustomizer conditionEngineCustomizer;

    // 业务计算引擎编程式定制回调
    protected EngineCustomizer businessEngineCustomizer;

    public void setParamEngineProperties(StepFlowEngineProperties paramEngineProperties) {
        this.paramEngineProperties = paramEngineProperties;
    }

    public void setConditionEngineProperties(StepFlowEngineProperties conditionEngineProperties) {
        this.conditionEngineProperties = conditionEngineProperties;
    }

    public void setBusinessEngineProperties(StepFlowEngineProperties businessEngineProperties) {
        this.businessEngineProperties = businessEngineProperties;
    }

    public void setParamEngineCustomizer(EngineCustomizer paramEngineCustomizer) {
        this.paramEngineCustomizer = paramEngineCustomizer;
    }

    public void setConditionEngineCustomizer(EngineCustomizer conditionEngineCustomizer) {
        this.conditionEngineCustomizer = conditionEngineCustomizer;
    }

    public void setBusinessEngineCustomizer(EngineCustomizer businessEngineCustomizer) {
        this.businessEngineCustomizer = businessEngineCustomizer;
    }


    /**
     * 构建参数取值引擎。
     */
    public abstract ParamExpressionEngine buildParamExpressionEngine();

    /**
     * 构建条件判断引擎。
     */
    public abstract ConditionExpressionEngine buildConditionExpressionEngine();

    /**
     * 构建业务计算引擎。
     */
    public abstract BusinessExpressionEngine buildBusinessExpressionEngine();
}
