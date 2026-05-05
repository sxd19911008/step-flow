package com.eredar.stepflow.engine.aviator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AviatorScript 引擎配置项。
 * 非 Spring 场景：通过构造器传入；Spring Boot 场景：由 AviatorEngineAutoConfiguration 从
 * application.yml 中绑定 stepflow.aviator.* 属性后注入。
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class StepFlowAviatorConfigProperties {

    /** LRU 表达式缓存最大数量，默认值由各引擎实现类内部处理 */
    private Integer useLRUExpressionCache;
    /** 最大循环次数，防止死循环 */
    private Integer maxLoopCount;
    /** 是否开启调试日志（TRACE_EVAL） */
    private Boolean traceEval;
}
