package io.github.kentasun.stepflow.step.handler.provider;

import io.github.kentasun.stepflow.step.handler.impl.AviatorOracleStepHandler;
import io.github.kentasun.stepflow.step.handler.StepHandler;

/**
 * 基于 aviator-oracle 的 {@link StepHandler} 提供者，实现 {@link AbstractStepHandlerProvider} SPI 接口。
 */
public class AviatorOracleStepHandlerProvider extends AbstractStepHandlerProvider {

    /**
     * SPI 专用无参构造器。
     */
    public AviatorOracleStepHandlerProvider() {}

    @Override
    public StepHandler buildStepHandler() {
        return new AviatorOracleStepHandler(engineProperties, stepHandlerCustomizer);
    }
}
