package io.github.kentasun.stepflow.step.handler.provider;

import io.github.kentasun.stepflow.step.handler.impl.AviatorStepHandler;
import io.github.kentasun.stepflow.step.handler.StepHandler;

/**
 * 基于 AviatorScript 的 {@link StepHandler} 提供者，实现 {@link AbstractStepHandlerProvider} SPI 接口。
 */
public class AviatorStepHandlerProvider extends AbstractStepHandlerProvider {

    /**
     * SPI 专用无参构造器。
     */
    public AviatorStepHandlerProvider() {}

    @Override
    public StepHandler buildStepHandler() {
        return new AviatorStepHandler(engineProperties, stepHandlerCustomizer);
    }
}
