package com.eredar.stepflow.config;

import com.eredar.stepflow.engine.aviator.dto.StepFlowAviatorConfigProperties;
import lombok.Data;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Data
public class StepFlowConfigProperties {

    // 异步执行 FLowNode 的线程池
    private ThreadPoolProperty parallelThreadPool;
    // 业务表达式引擎
    private StepFlowAviatorConfigProperties businessExpressionEngine;
    // 条件表达式引擎
    private StepFlowAviatorConfigProperties ConditionExpressionEngine;
    // 参数获取引擎
    private StepFlowAviatorConfigProperties paramExpressionEngine;

    @Data
    public static class ThreadPoolProperty {

        private Integer corePoolSize;
        private Integer maximumPoolSize;
        private Long keepAliveTime;
        private String unit;
        private Integer workQueueSize;
        private String rejectedHandler;

        public TimeUnit getUnit() {
            if (this.unit != null) {
                switch (this.unit) {
                    case "NANOSECONDS":
                        return TimeUnit.NANOSECONDS;
                    case "MICROSECONDS":
                        return TimeUnit.MICROSECONDS;
                    case "MILLISECONDS":
                        return TimeUnit.MILLISECONDS;
                    case "SECONDS":
                        return TimeUnit.SECONDS;
                    case "MINUTES":
                        return TimeUnit.MINUTES;
                    case "HOURS":
                        return TimeUnit.HOURS;
                    case "DAYS":
                        return TimeUnit.DAYS;
                }
            }
            return null;
        }

        public RejectedExecutionHandler getRejectedHandler() {
            if (this.rejectedHandler != null) {
                switch (this.rejectedHandler) {
                    case "CallerRunsPolicy":
                        return new ThreadPoolExecutor.CallerRunsPolicy();
                    case "AbortPolicy":
                        return new ThreadPoolExecutor.AbortPolicy();
                    case "DiscardPolicy":
                        return new ThreadPoolExecutor.DiscardPolicy();
                    case "DiscardOldestPolicy":
                        return new ThreadPoolExecutor.DiscardOldestPolicy();
                }
            }
            return null;
        }
    }
}
