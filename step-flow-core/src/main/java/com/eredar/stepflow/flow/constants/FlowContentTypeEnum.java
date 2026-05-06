package com.eredar.stepflow.flow.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 流程节点类型枚举
 */
@Getter
@AllArgsConstructor
public enum FlowContentTypeEnum {


    STEP("STEP", "单个步骤"),
    SUB_FLOW("FLOW", "单个流程"),
    SEQUENCE("SEQUENCE", "多个步骤/流程顺序执行"),
    PARALLEL("PARALLEL", "多个步骤/流程并发执行"),
    IF_ELSE("IF_ELSE", "if-else判断后执行步骤/流程"),
    ;

    // 类型code
    private final String typeCode;
    // 类型描述
    private final String description;
}
