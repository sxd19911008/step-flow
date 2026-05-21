package io.github.kentasun.stepflow.jexl.dto;

/**
 * 表达式引擎通用配置项
 */
public class JexlStepHandlerProperties {

    /**
     * 表达式缓存最大条数。
     */
    private Integer cache;

    /**
     * 是否开启引擎调试日志
     */
    private Boolean debug;

    public JexlStepHandlerProperties(Integer cache, Boolean debug) {
        this.cache = cache;
        this.debug = debug;
    }

    public JexlStepHandlerProperties() {
    }

    public Integer getCache() {
        return this.cache;
    }

    public Boolean getDebug() {
        return this.debug;
    }

    public void setCache(Integer cache) {
        this.cache = cache;
    }

    public void setDebug(Boolean debug) {
        this.debug = debug;
    }

    public String toString() {
        return "JexlStepHandlerProperties(cache=" + this.getCache() + ", debug=" + this.getDebug() + ")";
    }

    public static JexlStepHandlerPropertiesBuilder builder() {
        return new JexlStepHandlerPropertiesBuilder();
    }

    public static class JexlStepHandlerPropertiesBuilder {
        private Integer cache;
        private Boolean debug;

        JexlStepHandlerPropertiesBuilder() {
        }

        public JexlStepHandlerPropertiesBuilder cache(Integer cache) {
            this.cache = cache;
            return this;
        }

        public JexlStepHandlerPropertiesBuilder debug(Boolean debug) {
            this.debug = debug;
            return this;
        }

        public JexlStepHandlerProperties build() {
            return new JexlStepHandlerProperties(this.cache, this.debug);
        }

        public String toString() {
            return "JexlStepHandlerProperties.JexlStepHandlerPropertiesBuilder(cache=" + this.cache + ", debug=" + this.debug + ")";
        }
    }
}
