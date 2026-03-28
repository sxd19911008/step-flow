package com.eredar.stepflow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 聚合步骤信息
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CompositeStepInfo {

    // 步骤代码
    private String stepCode;
    // 表达式引擎执行的判断公式。判断为true则执行；判断为false则不执行。为空则不做判断，直接执行
    private String condition;
    // 调用该步骤需要映射的参数，解决当前 paramsMap 中的参数名与步骤需要的参数名对不上的问题。
    private Map<String, String> paramNameMap;
    // 调用该步骤需要映射的返回值，解决期望的返回值名与步骤默认的返回值名对不上的问题。
    private Map<String, String> resultFieldMap;
}
