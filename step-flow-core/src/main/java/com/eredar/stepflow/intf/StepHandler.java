package com.eredar.stepflow.intf;

import com.eredar.stepflow.dto.OneOffStepParams;
import com.eredar.stepflow.dto.StepContext;
import com.eredar.stepflow.dto.StepInfo;

/**
 * step 的执行类型，详情见 StepContentTypeEnum。
 * 每个枚举类型都会有一个对应的 Handler 实现。
 */
public interface StepHandler {

    /**
     * step 行为的抽象方法
     *
     * @param stepInfo 步骤信息
     * @param stepContext 步骤上下文，用于传递
     * @param oneOffStepParams 1次性参数，仅供当前 step 使用
     * @return 计算结果
     */
    Object execute(StepInfo stepInfo, StepContext stepContext, OneOffStepParams oneOffStepParams);
}
