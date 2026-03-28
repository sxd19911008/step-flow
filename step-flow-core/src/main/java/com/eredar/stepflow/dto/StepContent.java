package com.eredar.stepflow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class StepContent {

    // 常量
    private String constant;
    // 常量类型，详情见 StepConstantTypeEnum
    private String constantType;
    // Java 方法
    private String javaMethod;
    // step 组合列表，Map格式，但是key是序号，从0开始
    private Map<Integer, List<CompositeStepInfo>> stepList;
    // 表达式引擎的公式
    private String expression;
    // 返回值类型
    private String returnType;
}
