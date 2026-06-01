package io.github.kentasun.stepflow.api.step;

/**
 * 实现该类可以自定义 {@link AbstractStepHandler} 的配置
 *
 * @param <T> 需要自定义的对象的类型
 */
public interface StepHandlerCustomizer<T> {

    void customize(T target);
}
