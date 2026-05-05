package com.eredar.stepflow.engine.aviator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AviatorScript 引擎配置项。
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class StepFlowAviatorConfigProperties {

    /** 表达式缓存最大数量 */
    private Integer maxExpressionCache;
    /** 最大循环次数，防止死循环 */
    private Integer maxLoopCount;
    /** 是否开启调试日志 */
    private Boolean logEnabled;
}
