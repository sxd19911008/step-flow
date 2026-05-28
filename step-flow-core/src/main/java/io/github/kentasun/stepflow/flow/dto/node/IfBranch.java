package io.github.kentasun.stepflow.flow.dto.node;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import io.github.kentasun.stepflow.flow.dto.FlowNodeValidateContext;

/**
 * IF_ELSE 流程节点中的一条分支：{@code IF/ELSIF(条件)} 与紧随其后的 {@code THEN(分支体)}。
 * <p>
 * 对应 SFL 中一段 {@code IF(...)THEN(...)} 或 {@code ELSIF(...)THEN(...)}。
 * </p>
 */
public class IfBranch {

    /** 条件步骤，执行后须返回 {@link Boolean} */
    @JsonSetter(nulls = Nulls.FAIL)
    private final StepFlowNode condition;

    /** 条件为 true 时执行的子流程 */
    @JsonSetter(nulls = Nulls.FAIL)
    private final FlowNode thenFlowNode;

    @JsonCreator
    public IfBranch(@JsonProperty("condition") StepFlowNode condition,
                    @JsonProperty("thenFlowNode") FlowNode thenFlowNode) {
        this.condition = condition;
        this.thenFlowNode = thenFlowNode;
    }

    /**
     * 递归校验本分支的条件与子流程。
     */
    public void validate(FlowNodeValidateContext context, String globalFlowCode) {
        condition.validate(context, globalFlowCode);
        thenFlowNode.validate(context, globalFlowCode);
    }

    public StepFlowNode getCondition() {
        return condition;
    }

    public FlowNode getThenFlowNode() {
        return thenFlowNode;
    }
}
