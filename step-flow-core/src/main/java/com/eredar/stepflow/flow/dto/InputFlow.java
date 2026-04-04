package com.eredar.stepflow.flow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 开发者传入的 Flow 信息
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class InputFlow {

    // 流程标识
    private String flowCode;

    // 流程名称
    private String flowName;

    // 流程类型
    private String flowType;

    // 流程正文，要求是JSON格式的字符串
    private String content;

    // 返回字段列表，多个返回字段配置在这里，否则为空
    private List<String> returnFieldList;
}
