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
 * 运行另一个 Flow 的 FlowNode
 */
public class SubFlowNode extends FlowNode {

    @JsonSetter(nulls = Nulls.FAIL)
    @Getter
    private final String flowCode;

    @JsonCreator
    public SubFlowNode(@JsonProperty("type") String type,
                       @JsonProperty("flowCode") String flowCode) {
        super(type);
        this.flowCode = flowCode;
    }

    @Override
    public void execute(StepFlowContext stepFlowContext, ExecutorsContext executorsContext) {
        // 执行流程
        executorsContext.executeByFLowCode(flowCode, stepFlowContext);
    }

    @Override
    public void validate(FlowNodeValidateContext context, String globalFlowCode) {
        // 校验 flowCode 是否存在
        if (context.flowCodeNotExist(this.flowCode)) {
            context.saveErrMsg(globalFlowCode, String.format("flowCode[%s] not exist", this.flowCode));
        }
    }
}
