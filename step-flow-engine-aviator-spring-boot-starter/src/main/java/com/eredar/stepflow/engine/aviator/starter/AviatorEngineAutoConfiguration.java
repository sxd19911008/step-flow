package com.eredar.stepflow.engine.aviator.starter;

import com.eredar.stepflow.engine.BusinessExpressionEngine;
import com.eredar.stepflow.engine.ConditionExpressionEngine;
import com.eredar.stepflow.engine.ParamExpressionEngine;
import com.eredar.stepflow.engine.impl.AviatorBusinessExpressionEngine;
import com.eredar.stepflow.engine.impl.AviatorConditionExpressionEngine;
import com.eredar.stepflow.engine.impl.AviatorParamExpressionEngine;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * AviatorScript 引擎的 Spring Boot 自动装配类。
 *
 * <p>引入 step-flow-engine-aviator-spring-boot-starter 后，此配置类会自动将3个 Aviator
 * 引擎实现注册为 Spring Bean。使用 {@link ConditionalOnMissingBean} 确保用户可以
 * 通过自定义 Bean 替换任意引擎，与 Spring Boot 官方 Starter 惯例保持一致。
 */
@AutoConfiguration
@EnableConfigurationProperties(StepFlowAviatorProperties.class)
public class AviatorEngineAutoConfiguration {

    /**
     * 注册业务表达式引擎 Bean。
     * 若用户已自定义 {@link BusinessExpressionEngine} Bean，则此方法不生效。
     */
    @Bean
    @ConditionalOnMissingBean(BusinessExpressionEngine.class)
    public BusinessExpressionEngine businessExpressionEngine(StepFlowAviatorProperties aviatorProperties) {
        return new AviatorBusinessExpressionEngine(aviatorProperties.getBusinessExpressionEngine());
    }

    /**
     * 注册条件表达式引擎 Bean。
     * 若用户已自定义 {@link ConditionExpressionEngine} Bean，则此方法不生效。
     */
    @Bean
    @ConditionalOnMissingBean(ConditionExpressionEngine.class)
    public ConditionExpressionEngine conditionExpressionEngine(StepFlowAviatorProperties aviatorProperties) {
        return new AviatorConditionExpressionEngine(aviatorProperties.getConditionExpressionEngine());
    }

    /**
     * 注册参数取值引擎 Bean。
     * 若用户已自定义 {@link ParamExpressionEngine} Bean，则此方法不生效。
     */
    @Bean
    @ConditionalOnMissingBean(ParamExpressionEngine.class)
    public ParamExpressionEngine paramExpressionEngine(StepFlowAviatorProperties aviatorProperties) {
        return new AviatorParamExpressionEngine(aviatorProperties.getParamExpressionEngine());
    }
}
