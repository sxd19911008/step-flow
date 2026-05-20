package io.github.kentasun.stepflow.aviatororacle.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * aviator-oracle 表达式引擎配置项
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AviatorOracleStepHandlerProperties {

    /**
     * 表达式缓存最大条数。
     */
    private Integer useLRUExpressionCache;

    /**
     * 脚本最大循环次数，防止死循环耗尽线程资源。
     */
    private Integer maxLoopCount;

    /**
     * 是否开启引擎调试日志
     */
    private Boolean traceEval;
}
