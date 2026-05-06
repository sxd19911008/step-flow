package com.eredar.stepflow.flow.dto.node;

import com.eredar.stepflow.dto.ExecutorsContext;
import com.eredar.stepflow.dto.StepFlowContext;
import com.eredar.stepflow.flow.dto.FlowNodeValidateContext;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.Getter;

/**
 * 流程类的公共父类，所有流程实现类都必须继承该类
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY, // 告诉 Jackson 这个字段类里本来就有
        property = "type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = StepFlowNode.class, name = "STEP"),
        @JsonSubTypes.Type(value = SubFlowNode.class, name = "FLOW"),
        @JsonSubTypes.Type(value = SequenceFlowNode.class, name = "SEQUENCE"),
        @JsonSubTypes.Type(value = ParallelFlowNode.class, name = "PARALLEL"),
        @JsonSubTypes.Type(value = IfElseFlowNode.class, name = "IF_ELSE")
        // 添加2种循环的 flow
})
public abstract class FlowNode {

    // 节点类型
    @JsonSetter(nulls = Nulls.FAIL)
    @Getter
    protected final String type;

    // 强迫子类在创建时必须明确给出自己的类型
    protected FlowNode(String type) {
        this.type = type;
    }

    public abstract void execute(StepFlowContext stepFlowContext, ExecutorsContext executorsContext);

    /**
     * 递归校验节点合法性
     */
    public abstract void validate(FlowNodeValidateContext context, String globalFlowCode);
}
