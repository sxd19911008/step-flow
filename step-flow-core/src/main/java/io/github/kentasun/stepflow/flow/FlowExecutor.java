package io.github.kentasun.stepflow.flow;

import io.github.kentasun.stepflow.dto.ExecutorsContext;
import io.github.kentasun.stepflow.dto.StepFlowContext;
import io.github.kentasun.stepflow.exception.StepFlowException;
import io.github.kentasun.stepflow.flow.dto.FlowNodeValidateContext;
import io.github.kentasun.stepflow.flow.dto.InputFlow;
import io.github.kentasun.stepflow.flow.dto.node.FlowNode;
import io.github.kentasun.stepflow.flow.intf.FlowProvider;
import io.github.kentasun.stepflow.step.StepExecutor;
import io.github.kentasun.stepflow.utils.StepFlowJsonUtils;
import io.github.kentasun.stepflow.utils.StepFlowUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * flow 功能总入口
 */
@Slf4j
public class FlowExecutor {

    private final Map<String, Flow> flowMap;

    public FlowExecutor(FlowProvider flowProvider, StepExecutor stepExecutor) {
        flowMap = new ConcurrentHashMap<>();
        List<InputFlow> inputFlowList = null;
        if (flowProvider != null) {
            inputFlowList = flowProvider.loadFlowList();
        }
        /* 组装 flow 对象 */
        if (StepFlowUtils.isNotEmpty(inputFlowList)) {
            // 所有的 flowCode 的 set
            Set<String> flowCodeSet = new HashSet<>();
            // 校验用上下文
            FlowNodeValidateContext validateContext = FlowNodeValidateContext.builder()
                    .illegalMsgMap(new HashMap<>())
                    .stepExecutor(stepExecutor)
                    .flowCodeSet(inputFlowList.stream().map(InputFlow::getFlowCode).collect(Collectors.toSet()))
                    .build();

            // 组装 flow
            for (InputFlow inputFlow : inputFlowList) {
                if (flowCodeSet.contains(inputFlow.getFlowCode())) {
                    validateContext.saveErrMsg(
                            inputFlow.getFlowCode(),
                            "flowCode重复"
                    );
                }
                flowCodeSet.add(inputFlow.getFlowCode());
                // 校验流程信息是否合法
                FlowNode flowNode = null;
                try {
                    flowNode = StepFlowJsonUtils.readValue(inputFlow.getContent(), new TypeReference<FlowNode>() {});
                    // 校验 flowNode
                    flowNode.validate(validateContext, inputFlow.getFlowCode());
                } catch (Throwable e) {
                    log.error("Flow 的 content 解析异常，Flow 信息：{}", inputFlow, e);
                    validateContext.saveErrMsg(
                            inputFlow.getFlowCode(),
                            "content解析异常: " + e.getMessage()
                    );
                }

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

            String errMsg = validateContext.buildErrMsg();
            if (StepFlowUtils.isNotBlank(errMsg)) {
                throw new StepFlowException(errMsg);
            }
            log.info("FlowExecutor has been created, {} flows has been loaded.", flowMap.size());
        }
    }

    /**
     * 执行流程
     *
     * @param flowCode 流程代码
     * @param stepFlowContext 上下文对象
     * @param executorsContext 用于随着上下文一起传递的各种执行器
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
