package com.eredar.stepflow.engine.aviator.starter;

import com.eredar.stepflow.engine.aviator.dto.StepFlowAviatorConfigProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * AviatorScript 引擎的 Spring Boot 配置属性，绑定 application.yml 中的 stepflow.aviator.* 配置节。
 *
 * <p>示例配置：
 * <pre>
 * stepflow:
 *   aviator:
 *     business-expression-engine:
 *       use-lru-expression-cache: 2048
 *       max-loop-count: 10000
 *       trace-eval: false
 *     condition-expression-engine:
 *       use-lru-expression-cache: 2048
 *     param-expression-engine:
 *       use-lru-expression-cache: 4096
 * </pre>
 */
@Data
@ConfigurationProperties(prefix = "stepflow.aviator")
public class StepFlowAviatorProperties {

    /** 业务表达式引擎配置（对应 EXPRESSION 类型的步骤） */
    @NestedConfigurationProperty
    private StepFlowAviatorConfigProperties businessExpressionEngine;

    /** 条件表达式引擎配置（对应 IF_ELSE 节点的条件判断） */
    @NestedConfigurationProperty
    private StepFlowAviatorConfigProperties conditionExpressionEngine;

    /** 参数取值引擎配置（对应 paramNameMap 中的取值表达式） */
    @NestedConfigurationProperty
    private StepFlowAviatorConfigProperties paramExpressionEngine;
}
