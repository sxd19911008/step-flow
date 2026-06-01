package io.github.kentasun.stepflow.config;

import io.github.kentasun.stepflow.api.exception.StepFlowException;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * step-flow 核心配置项
 */
public class StepFlowConfigProperties {

    // 异步执行 FLowNode 的线程池
    private ThreadPoolProperty parallelThreadPool;

    public StepFlowConfigProperties() {
    }

    public ThreadPoolProperty getParallelThreadPool() {
        return this.parallelThreadPool;
    }

    public void setParallelThreadPool(ThreadPoolProperty parallelThreadPool) {
        this.parallelThreadPool = parallelThreadPool;
    }

    public String toString() {
        return "StepFlowConfigProperties(parallelThreadPool=" + this.getParallelThreadPool() + ")";
    }

    public static class ThreadPoolProperty {

        private Integer corePoolSize;
        private Integer maximumPoolSize;
        private Long keepAliveTime;
        private String unit;
        private Integer workQueueSize;
        private String rejectedHandler;

        public ThreadPoolProperty() {
        }

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
                throw new StepFlowException(String.format("TimeUnit[%s] not found", this.unit));
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
                throw new StepFlowException(String.format("RejectedExecutionHandler[%s] not found", this.rejectedHandler));
            }
            return null;
        }

        public Integer getCorePoolSize() {
            return this.corePoolSize;
        }

        public Integer getMaximumPoolSize() {
            return this.maximumPoolSize;
        }

        public Long getKeepAliveTime() {
            return this.keepAliveTime;
        }

        public Integer getWorkQueueSize() {
            return this.workQueueSize;
        }

        public void setCorePoolSize(Integer corePoolSize) {
            this.corePoolSize = corePoolSize;
        }

        public void setMaximumPoolSize(Integer maximumPoolSize) {
            this.maximumPoolSize = maximumPoolSize;
        }

        public void setKeepAliveTime(Long keepAliveTime) {
            this.keepAliveTime = keepAliveTime;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public void setWorkQueueSize(Integer workQueueSize) {
            this.workQueueSize = workQueueSize;
        }

        public void setRejectedHandler(String rejectedHandler) {
            this.rejectedHandler = rejectedHandler;
        }

        public String toString() {
            return "StepFlowConfigProperties.ThreadPoolProperty(corePoolSize=" + this.getCorePoolSize() + ", maximumPoolSize=" + this.getMaximumPoolSize() + ", keepAliveTime=" + this.getKeepAliveTime() + ", unit=" + this.getUnit() + ", workQueueSize=" + this.getWorkQueueSize() + ", rejectedHandler=" + this.getRejectedHandler() + ")";
        }
    }
}
