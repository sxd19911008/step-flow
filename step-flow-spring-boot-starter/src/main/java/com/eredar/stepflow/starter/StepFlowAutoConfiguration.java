package com.eredar.stepflow.starter;

import com.eredar.stepflow.StepFlowExecutor;
import com.eredar.stepflow.config.StepFlowConfigProperties;
import com.eredar.stepflow.engine.BusinessExpressionEngine;
import com.eredar.stepflow.engine.ConditionExpressionEngine;
import com.eredar.stepflow.engine.ParamExpressionEngine;
import com.eredar.stepflow.engine.impl.AviatorBusinessExpressionEngine;
import com.eredar.stepflow.engine.impl.AviatorConditionExpressionEngine;
import com.eredar.stepflow.engine.impl.AviatorParamExpressionEngine;
import com.eredar.stepflow.flow.intf.FlowProvider;
import com.eredar.stepflow.step.intf.JavaStep;
import com.eredar.stepflow.step.intf.StepDataProvider;
import com.eredar.stepflow.threadpool.StepFlowThreadPoolFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.Nullable;

import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * step-flow Spring Boot Starter 自动装配入口。
 */
public class StepFlowAutoConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "stepflow")
    public StepFlowConfigProperties stepFlowConfigProperties() {
        return new StepFlowConfigProperties();
    }

    @Bean
    public StepFlowThreadPoolFactory stepFlowThreadPoolFactory(StepFlowConfigProperties stepFlowConfigProperties) {
        return new StepFlowThreadPoolFactory(stepFlowConfigProperties);
    }

    @Bean(name = "stepFlowParallelThreadPool")
    @ConditionalOnMissingBean(name = "stepFlowParallelThreadPool")
    public ExecutorService stepFlowParallelThreadPool(StepFlowThreadPoolFactory stepFlowThreadPoolFactory) {
        return stepFlowThreadPoolFactory.getStepFlowParallelThreadPool();
    }

    /**
     * 注入业务表达式引擎，允许开发者自己替换
     */
    @Bean
    @ConditionalOnMissingBean(value = BusinessExpressionEngine.class)
    public BusinessExpressionEngine businessExpressionEngine() {
        return new AviatorBusinessExpressionEngine();
    }

    /**
     * 注入条件表达式引擎，允许开发者自己替换
     */
    @Bean
    @ConditionalOnMissingBean(value = ConditionExpressionEngine.class)
    public ConditionExpressionEngine conditionExpressionEngine() {
        return new AviatorConditionExpressionEngine();
    }

    /**
     * 注入参数获取引擎，允许开发者自己替换
     */
    @Bean
    @ConditionalOnMissingBean(value = ParamExpressionEngine.class)
    public ParamExpressionEngine paramExpressionEngine() {
        return new AviatorParamExpressionEngine();
    }

    @Bean
    public StepFlowExecutor stepFlowExecutor(StepDataProvider stepDataProvider,
                                             FlowProvider flowProvider,
                                             StepFlowConfigProperties stepFlowConfigProperties,
                                             @Nullable Map<String, JavaStep> javaStepMap,
                                             @Qualifier("stepFlowParallelThreadPool") ExecutorService stepFlowParallelThreadPool,
                                             @Nullable ParamExpressionEngine paramExpressionEngine,
                                             @Nullable ConditionExpressionEngine conditionExpressionEngine,
                                             @Nullable BusinessExpressionEngine businessExpressionEngine) {
        return StepFlowExecutor.builder(stepDataProvider, flowProvider)
                .configProperties(stepFlowConfigProperties)
                .javaStepMap(javaStepMap)
                .parallelThreadPool(stepFlowParallelThreadPool)
                .paramExpressionEngine(paramExpressionEngine)
                .conditionExpressionEngine(conditionExpressionEngine)
                .businessExpressionEngine(businessExpressionEngine)
                .build();
    }
}
