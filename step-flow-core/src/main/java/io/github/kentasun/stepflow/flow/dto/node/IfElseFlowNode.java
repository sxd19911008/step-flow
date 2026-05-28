package io.github.kentasun.stepflow.flow.dto.node;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import io.github.kentasun.stepflow.api.dto.StepFlowContext;
import io.github.kentasun.stepflow.dto.ExecutorsContext;
import io.github.kentasun.stepflow.exception.StepFlowException;
import io.github.kentasun.stepflow.flow.dto.FlowNodeValidateContext;

import java.util.Collections;
import java.util.List;

/**
 * IF_ELSE 流程节点。
 */
public class IfElseFlowNode extends FlowNode {

    /** 至少包含首段 IF...THEN；后续项对应 ELSIF...THEN */
    @JsonSetter(nulls = Nulls.FAIL)
    private final List<IfBranch> branches;

    /** 对应 SFL {@code ELSE(...)}；省略 ELSE 时为 null */
    private final FlowNode elseFlowNode;

    @JsonCreator
    public IfElseFlowNode(@JsonProperty("type") String type,
                          @JsonProperty("branches") List<IfBranch> branches,
                          @JsonProperty("elseFlowNode") FlowNode elseFlowNode) {
        super(type);
        if (branches == null || branches.isEmpty()) {
            throw new IllegalArgumentException("IF_ELSE 的 branches 不能为空");
        }
        this.branches = Collections.unmodifiableList(branches);
        this.elseFlowNode = elseFlowNode;
    }

    @Override
    public void execute(StepFlowContext stepFlowContext, ExecutorsContext executorsContext) {
        for (IfBranch branch : branches) {
            if (evalConditionAsBoolean(branch.getCondition(), stepFlowContext, executorsContext)) {
                branch.getThenFlowNode().execute(stepFlowContext, executorsContext);
                return;
            }
        }
        if (elseFlowNode != null) {
            elseFlowNode.execute(stepFlowContext, executorsContext);
        }
    }

    /**
     * 执行条件步骤并转为 boolean；null 或非 Boolean 类型抛 {@link StepFlowException}。
     */
    private static boolean evalConditionAsBoolean(StepFlowNode condition,
                                                  StepFlowContext stepFlowContext,
                                                  ExecutorsContext executorsContext) {
        Object res = condition.executeThenReturnRes(stepFlowContext, executorsContext);
        if (res instanceof Boolean) {
            return (Boolean) res;
        }
        if (res == null) {
            throw new StepFlowException(
                    String.format("执行step[%s]，返回null", condition.getStepCode()));
        }
        throw new StepFlowException(String.format(
                "执行step[%s]，返回错误类型：%s",
                condition.getStepCode(),
                res.getClass().getName()));
    }

    @Override
    public void validate(FlowNodeValidateContext context, String globalFlowCode) {
        for (IfBranch branch : branches) {
            branch.validate(context, globalFlowCode);
        }
        if (elseFlowNode != null) {
            elseFlowNode.validate(context, globalFlowCode);
        }
    }

    public List<IfBranch> getBranches() {
        return branches;
    }

    public FlowNode getElseFlowNode() {
        return elseFlowNode;
    }
}
