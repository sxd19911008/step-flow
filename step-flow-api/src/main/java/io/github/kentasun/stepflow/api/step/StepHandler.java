package io.github.kentasun.stepflow.api.step;

import io.github.kentasun.stepflow.api.dto.OneOffParams;
import io.github.kentasun.stepflow.api.step.dto.StepData;

/**
 * step 的执行类型，每个 Handler 实现都对应一个 StepContentType。
 */
public interface StepHandler {

    /**
     * 该 Handler 对应的 StepContentType
     *
     * @return {@code StepHandler} 对应的 StepContentType 类型
     */
    String getStepContentType();

    /**
     * step 行为的抽象方法
     *
     * @param stepData 步骤信息
     * @param oneOffParams 1次性参数，仅供当前 step 使用
     * @return 计算结果
     */
    Object execute(StepData stepData, OneOffParams oneOffParams);

    /**
     * 校验 {@code StepData} 是否非法
     *
     * @param stepData 待校验的 {@code StepData}
     * @return true-非法；false-合法
     */
    boolean isStepDataIllegal(StepData stepData);
}
