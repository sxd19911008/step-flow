package com.eredar.stepflow.config;

import com.eredar.stepflow.engine.ExpressionEngine;
import com.eredar.stepflow.engine.impl.AviatorExpressionEngine;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StepConfig {

    /**
     * 注入表达式引擎，允许开发者自己替换
     */
    @Bean
    @ConditionalOnMissingBean(value = ExpressionEngine.class)
    public ExpressionEngine expressionEngine() {
        return new AviatorExpressionEngine();
    }
}
