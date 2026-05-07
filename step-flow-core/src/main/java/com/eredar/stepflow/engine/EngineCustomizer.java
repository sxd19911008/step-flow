package com.eredar.stepflow.engine;

/**
 * 实现该类可以自定义引擎的配置
 * @param <T> 引擎对象或者引擎builder对象的类型
 */
public interface EngineCustomizer<T> {

    void customize(T targetEngine);
}
