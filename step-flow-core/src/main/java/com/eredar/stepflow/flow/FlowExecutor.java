package com.eredar.stepflow.flow;

import com.eredar.stepflow.dto.ExecutorsContext;
import com.eredar.stepflow.dto.StepFlowContext;
import com.eredar.stepflow.exception.StepFlowException;
import com.eredar.stepflow.flow.dto.node.FlowNode;
import com.eredar.stepflow.flow.dto.InputFlow;
import com.eredar.stepflow.flow.intf.FlowProvider;
import com.eredar.stepflow.utils.StepFlowJsonUtils;
import com.eredar.stepflow.utils.StepFlowUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * flow 功能总入口
 */
public class FlowExecutor {

    private final Map<String, Flow> flowMap;

    public FlowExecutor(FlowProvider flowProvider) {
        flowMap = new ConcurrentHashMap<>();
        List<InputFlow> inputFlowList = null;
        if (flowProvider != null) {
            inputFlowList = flowProvider.loadFlowList();
        }
        /* 组装 flow 对象 */
        if (StepFlowUtils.isNotEmpty(inputFlowList)) {
            // 组装 flow
            for (InputFlow inputFlow : inputFlowList) {
                // 校验流程信息是否合法
                FlowNode flowNode = StepFlowJsonUtils.readValue(inputFlow.getContent(), new TypeReference<FlowNode>() {});
                // TODO 校验流程信息是否合法
                // 放入 flowMap
                flowMap.put(
                        inputFlow.getFlowCode(),
                        Flow.builder()
                                .flowCode(inputFlow.getFlowCode())
                                .flowName(inputFlow.getFlowName())
                                .flowType(inputFlow.getFlowType())
                                .content(flowNode)
                                .returnFieldList(inputFlow.getReturnFieldList())
                                .build()
                );
            }
        }
    }

    /**
     * 执行流程
     *
     * @param flowCode 流程代码
     * @param stepFlowContext 上下文对象
     * @return 流程执行结果
     */
    public Map<String, Object> executeByFLowCode(final String flowCode, StepFlowContext stepFlowContext, ExecutorsContext executorsContext) {
        Flow flow = flowMap.get(flowCode);
        if (flow == null) {
            throw new StepFlowException(String.format("【%s】流程不存在", flowCode));
        }
        return flow.execute(stepFlowContext, executorsContext);
    }
}
