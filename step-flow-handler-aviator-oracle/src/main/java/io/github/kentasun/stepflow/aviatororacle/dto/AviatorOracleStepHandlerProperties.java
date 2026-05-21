package io.github.kentasun.stepflow.aviatororacle.dto;

/**
 * aviator-oracle 表达式引擎配置项
 */
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

    public AviatorOracleStepHandlerProperties(Integer useLRUExpressionCache, Integer maxLoopCount, Boolean traceEval) {
        this.useLRUExpressionCache = useLRUExpressionCache;
        this.maxLoopCount = maxLoopCount;
        this.traceEval = traceEval;
    }

    public AviatorOracleStepHandlerProperties() {
    }

    public Integer getUseLRUExpressionCache() {
        return this.useLRUExpressionCache;
    }

    public Integer getMaxLoopCount() {
        return this.maxLoopCount;
    }

    public Boolean getTraceEval() {
        return this.traceEval;
    }

    public void setUseLRUExpressionCache(Integer useLRUExpressionCache) {
        this.useLRUExpressionCache = useLRUExpressionCache;
    }

    public void setMaxLoopCount(Integer maxLoopCount) {
        this.maxLoopCount = maxLoopCount;
    }

    public void setTraceEval(Boolean traceEval) {
        this.traceEval = traceEval;
    }

    public String toString() {
        return "AviatorOracleStepHandlerProperties(useLRUExpressionCache=" + this.getUseLRUExpressionCache() + ", maxLoopCount=" + this.getMaxLoopCount() + ", traceEval=" + this.getTraceEval() + ")";
    }

    public static AviatorOracleStepHandlerPropertiesBuilder builder() {
        return new AviatorOracleStepHandlerPropertiesBuilder();
    }

    public static class AviatorOracleStepHandlerPropertiesBuilder {
        private Integer useLRUExpressionCache;
        private Integer maxLoopCount;
        private Boolean traceEval;

        AviatorOracleStepHandlerPropertiesBuilder() {
        }

        public AviatorOracleStepHandlerPropertiesBuilder useLRUExpressionCache(Integer useLRUExpressionCache) {
            this.useLRUExpressionCache = useLRUExpressionCache;
            return this;
        }

        public AviatorOracleStepHandlerPropertiesBuilder maxLoopCount(Integer maxLoopCount) {
            this.maxLoopCount = maxLoopCount;
            return this;
        }

        public AviatorOracleStepHandlerPropertiesBuilder traceEval(Boolean traceEval) {
            this.traceEval = traceEval;
            return this;
        }

        public AviatorOracleStepHandlerProperties build() {
            return new AviatorOracleStepHandlerProperties(this.useLRUExpressionCache, this.maxLoopCount, this.traceEval);
        }

        public String toString() {
            return "AviatorOracleStepHandlerProperties.AviatorOracleStepHandlerPropertiesBuilder(useLRUExpressionCache=" + this.useLRUExpressionCache + ", maxLoopCount=" + this.maxLoopCount + ", traceEval=" + this.traceEval + ")";
        }
    }
}
