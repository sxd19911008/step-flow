package com.eredar.stepflow.starter;

import com.eredar.stepflow.StepFlowExecutor;
import com.eredar.stepflow.config.StepFlowConfigProperties;
import com.eredar.stepflow.engine.BusinessExpressionEngine;
import com.eredar.stepflow.engine.ConditionExpressionEngine;
import com.eredar.stepflow.engine.EngineCustomizer;
import com.eredar.stepflow.engine.ParamExpressionEngine;
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

    @Bean
    public StepFlowExecutor stepFlowExecutor(StepDataProvider stepDataProvider,
                                             FlowProvider flowProvider,
                                             StepFlowConfigProperties stepFlowConfigProperties,
                                             @Nullable Map<String, JavaStep> javaStepMap,
                                             @Qualifier("stepFlowParallelThreadPool") ExecutorService stepFlowParallelThreadPool,
                                             @Nullable ParamExpressionEngine paramExpressionEngine,
                                             @Nullable ConditionExpressionEngine conditionExpressionEngine,
                                             @Nullable BusinessExpressionEngine businessExpressionEngine,
                                             @Nullable EngineCustomizer<?> paramEngineCustomizer,
                                             @Nullable EngineCustomizer<?> conditionEngineCustomizer,
                                             @Nullable EngineCustomizer<?> businessEngineCustomizer) {
        return StepFlowExecutor.builder(stepDataProvider, flowProvider)
                .configProperties(stepFlowConfigProperties)
                .javaStepMap(javaStepMap)
                .parallelThreadPool(stepFlowParallelThreadPool)
                .paramExpressionEngine(paramExpressionEngine)
                .conditionExpressionEngine(conditionExpressionEngine)
                .businessExpressionEngine(businessExpressionEngine)
                .paramEngineCustomizer(paramEngineCustomizer)
                .conditionEngineCustomizer(conditionEngineCustomizer)
                .businessEngineCustomizer(businessEngineCustomizer)
                .build();
    }
}
