package com.eredar.stepflow.flow.dto.node;

import com.eredar.stepflow.dto.ExecutorsContext;
import com.eredar.stepflow.dto.StepFlowContext;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

/**
 * 多个 FlowNode 顺序同步执行的 FlowNode
 */
public class SequenceFlowNode extends FlowNode {

    @Getter
    private final List<FlowNode> flowNodeList;

    @JsonCreator
    protected SequenceFlowNode(@JsonProperty("type") String type,
                               @JsonProperty("flowNodeList") List<FlowNode> flowNodeList) {
        super(type);
        this.flowNodeList = flowNodeList;
    }

    @Override
    public void execute(StepFlowContext stepFlowContext, ExecutorsContext executorsContext) {
        for (FlowNode flowNode : flowNodeList) {
            flowNode.execute(stepFlowContext, executorsContext);
        }
    }
}
