package com.eredar.stepflow.intf;

import com.eredar.stepflow.dto.StepInfo;

import java.util.List;

/**
 * Step 信息提供者接口。
 */
public interface StepInfoProvider {

    /**
     * 加载并返回所有 StepInfo。
     *
     * @return 已拼装完成的 StepInfo 列表
     */
    List<StepInfo> loadStepInfoList();
}
