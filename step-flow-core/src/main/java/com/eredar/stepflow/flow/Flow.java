package com.eredar.stepflow.flow;

import com.eredar.stepflow.dto.ExecutorsContext;
import com.eredar.stepflow.dto.StepFlowContext;
import com.eredar.stepflow.flow.dto.FlowNode;
import com.eredar.stepflow.utils.StepFlowUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Flow {

    // 流程标识
    private String flowCode;

    // 流程名称
    private String flowName;

    // 流程类型
    private String flowType;

    // 流程正文
    private FlowNode content;

    // 返回字段列表，多个返回字段配置在这里，否则为空
    private List<String> returnFieldList;

    /**
     * 执行流程
     *
     * @param stepFlowContext 步骤上下文，用于传递
     * @param executorsContext 执行器上下文
     * @return 返回 returnFieldList 指定的参数
     */
    public Map<String, Object> execute(StepFlowContext stepFlowContext, ExecutorsContext executorsContext) {
        /* 执行 flow */
        content.execute(stepFlowContext, executorsContext);

        /* flow 如果配置了返回值，则直接从上下文中获取 */
        if (StepFlowUtils.isNotEmpty(returnFieldList)) {
            Map<String, Object> returnMap = new HashMap<>();
            for (String key : returnFieldList) {
                Object value = stepFlowContext.get(key);
                returnMap.put(key, value);
            }
            return returnMap;
        }

        /* 默认无返回值 */
        return null;
    }
}
