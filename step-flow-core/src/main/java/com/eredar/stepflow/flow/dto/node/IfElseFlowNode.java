package com.eredar.stepflow.flow.dto.node;

import com.eredar.stepflow.dto.ExecutorsContext;
import com.eredar.stepflow.dto.StepFlowContext;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * 条件判断 FlowNode。
 * <p>if (condition) {trueFlowNode} else {falseFlowNode}
 */
public class IfElseFlowNode extends FlowNode {

    @Getter
    private final String condition;

    @Getter
    private final FlowNode trueFlowNode;

    @Getter
    private final FlowNode falseFlowNode;

    @JsonCreator
    public IfElseFlowNode(@JsonProperty("type") String type,
                          @JsonProperty("condition") String condition,
                          @JsonProperty("trueFlowNode") FlowNode trueFlowNode,
                          @JsonProperty("falseFlowNode") FlowNode falseFlowNode) {
        super(type);
        this.condition = condition;
        this.trueFlowNode = trueFlowNode;
        this.falseFlowNode = falseFlowNode;
    }

    @Override
    public void execute(StepFlowContext stepFlowContext, ExecutorsContext executorsContext) {
        Boolean isTrue = executorsContext.isTrue(condition, stepFlowContext.getContextMap());
        if (isTrue) {
            trueFlowNode.execute(stepFlowContext, executorsContext);
        } else {
            falseFlowNode.execute(stepFlowContext, executorsContext);
        }
    }
}
