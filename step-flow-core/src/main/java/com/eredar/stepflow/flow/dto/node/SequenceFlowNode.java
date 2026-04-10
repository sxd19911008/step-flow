package com.eredar.stepflow.flow.dto.node;

import com.eredar.stepflow.dto.ExecutorsContext;
import com.eredar.stepflow.dto.StepFlowContext;
import com.eredar.stepflow.flow.dto.FlowNodeValidateContext;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.Getter;

import java.util.List;

/**
 * 多个 FlowNode 顺序同步执行的 FlowNode
 */
public class SequenceFlowNode extends FlowNode {

    @JsonSetter(nulls = Nulls.FAIL)
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

    @Override
    public void validate(FlowNodeValidateContext context, String globalFlowCode) {
        // 校验所有子节点
        for (FlowNode flowNode : flowNodeList) {
            flowNode.validate(context, globalFlowCode);
        }
    }
}
