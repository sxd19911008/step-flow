package com.eredar.stepflow.config;

import lombok.Data;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * step-flow 核心配置项。
 * 仅包含与具体引擎实现无关的通用配置（如线程池）。
 * 各引擎插件的专属配置由对应的插件包自行定义（例如 StepFlowAviatorProperties）。
 */
@Data
public class StepFlowConfigProperties {

    // 异步执行 FLowNode 的线程池
    private ThreadPoolProperty parallelThreadPool;

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
