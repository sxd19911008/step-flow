package com.eredar.stepflow.flow.intf;

import com.eredar.stepflow.flow.Flow;

import java.util.List;

/**
 * flow 信息提供者接口。
 */
public interface FlowProvider {

    /**
     * 加载并返回所有 Flow。
     *
     * @return 已拼装完成的 Flow 列表
     */
    List<Flow> loadFlowList();
}
