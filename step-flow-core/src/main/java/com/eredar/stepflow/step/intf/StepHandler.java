package com.eredar.stepflow.step.intf;

import com.eredar.stepflow.dto.ExecutorsContext;
import com.eredar.stepflow.dto.OneOffParams;
import com.eredar.stepflow.dto.StepFlowContext;
import com.eredar.stepflow.step.dto.StepData;

/**
 * step 的执行类型，详情见 StepContentTypeEnum。
 * 每个枚举类型都会有一个对应的 Handler 实现。
 */
public interface StepHandler {

    /**
     * step 行为的抽象方法
     *
     * @param stepData 步骤信息
     * @param stepFlowContext 步骤上下文，用于传递
     * @param oneOffParams 1次性参数，仅供当前 step 使用
     * @return 计算结果
     */
    Object execute(StepData stepData, StepFlowContext stepFlowContext, OneOffParams oneOffParams, ExecutorsContext executorsContext);
}
