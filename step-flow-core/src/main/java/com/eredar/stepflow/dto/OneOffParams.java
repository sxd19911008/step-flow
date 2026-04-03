package com.eredar.stepflow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * StepHandler所需要的1次性参数
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OneOffParams {

    // 调用该步骤需要映射的参数，解决当前 contextMap 中的参数名和步骤需要的参数名对不上的问题。
    private Map<String, String> paramNameMap;

    // 调用该步骤需要的参数，单独隔离可以防止参数污染。
    private Map<String, Object> vars;
}
