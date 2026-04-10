package com.eredar.stepflow.flow.dto.node;

import com.eredar.stepflow.dto.ExecutorsContext;
import com.eredar.stepflow.dto.StepFlowContext;
import com.eredar.stepflow.flow.dto.FlowNodeValidateContext;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.Getter;

/**
 * 条件判断 FlowNode。
 * <p>if (condition) {trueFlowNode} else {falseFlowNode}
 */
public class IfElseFlowNode extends FlowNode {

    @JsonSetter(nulls = Nulls.FAIL)
    @Getter
    private final String condition;

    @JsonSetter(nulls = Nulls.FAIL)
    @Getter
    private final FlowNode trueFlowNode;

    @JsonSetter(nulls = Nulls.FAIL)
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

    @Override
    public void validate(FlowNodeValidateContext context, String globalFlowCode) {
        // 校验2个子节点
        trueFlowNode.validate(context, globalFlowCode);
        falseFlowNode.validate(context, globalFlowCode);
    }
}
