package com.eredar.stepflow.flow.dto;

import com.eredar.stepflow.dto.ExecutorsContext;
import com.eredar.stepflow.dto.StepFlowContext;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * 运行另一个 Flow 的 FlowNode
 */
public class FlowFlowNode extends FlowNode {

    @Getter
    private final String flowCode;

    @JsonCreator
    public FlowFlowNode(@JsonProperty("type") String type,
                        @JsonProperty("flowCode") String flowCode) {
        super(type);
        this.flowCode = flowCode;
    }

    @Override
    public void execute(StepFlowContext stepFlowContext, ExecutorsContext executorsContext) {
        // 执行流程
        executorsContext.executeByFLowCode(flowCode, stepFlowContext);
    }
}
