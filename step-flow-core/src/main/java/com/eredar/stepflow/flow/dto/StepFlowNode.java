package com.eredar.stepflow.flow.dto;


import com.eredar.stepflow.dto.ExecutorsContext;
import com.eredar.stepflow.dto.OneOffParams;
import com.eredar.stepflow.dto.StepFlowContext;
import com.eredar.stepflow.utils.StepFlowUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Map;

/**
 * 执行一个 Step 的 FlowNode
 */
public class StepFlowNode extends FlowNode {

    @Getter
    private final String stepCode;
    // 调用该步骤需要映射的参数，解决当前 contextMap 中的参数名与步骤需要的参数名对不上的问题。
    private final Map<String, String> paramNameMap;
    /*
     * 调用该步骤需要映射的返回值，可以自定义该step在本flow中返回值的名字。
     * 解决期望的返回值名与步骤默认的返回值名对不上的问题。
     */
    private final Map<String, String> resultNameMap;

    @JsonCreator
    public StepFlowNode(@JsonProperty("type") String type,
                        @JsonProperty("stepCode") String stepCode,
                        @JsonProperty("paramNameMap") Map<String, String> paramNameMap,
                        @JsonProperty("resultNameMap") Map<String, String> resultNameMap) {
        super(type);
        this.stepCode = stepCode;
        this.paramNameMap = paramNameMap;
        this.resultNameMap = resultNameMap;
    }

    @Override
    public void execute(StepFlowContext stepFlowContext, ExecutorsContext executorsContext) {
        /* 执行 step */
        Map<String, Object> resMap = executorsContext.executeByStepCode(
                stepCode,
                stepFlowContext,
                OneOffParams.builder()
                        .paramNameMap(paramNameMap)
                        .build());
        /* 将计算结果放入上下文 */
        if (StepFlowUtils.isNotEmpty(resMap)) {
            // 有映射，先处理映射字段
            if (StepFlowUtils.isNotEmpty(resultNameMap)) {
                for (Map.Entry<String, String> resEntry : resultNameMap.entrySet()) {
                    String resMapKey = resEntry.getKey(); // 子步骤默认返回的字段名
                    String stepContextKey = resEntry.getValue(); // 映射期望的字段名
                    // 获取 value，同时在 resMap 中删除，防止重复放入相同字段
                    Object value = resMap.remove(resMapKey);
                    if (value != null) {
                        // 用 映射期望的字段名 放入上下文
                        stepFlowContext.put(stepContextKey, value);
                    }
                }
            }
            // 将所有默认字段名的字段放入上下文
            stepFlowContext.putAll(resMap);
        }
    }
}
