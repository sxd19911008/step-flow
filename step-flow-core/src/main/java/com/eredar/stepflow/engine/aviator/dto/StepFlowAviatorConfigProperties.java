package com.eredar.stepflow.engine.aviator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Aviator 框架配置项
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class StepFlowAviatorConfigProperties {

    // 最大缓存表达式数量
    private Integer useLRUExpressionCache;
    // 最大循环次数，防止死循环
    private Integer maxLoopCount;
    // 调试日志
    private Boolean traceEval;
}
