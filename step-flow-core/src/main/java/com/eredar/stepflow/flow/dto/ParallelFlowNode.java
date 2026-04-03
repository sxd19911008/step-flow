package com.eredar.stepflow.flow.dto;

import com.eredar.stepflow.dto.ExecutorsContext;
import com.eredar.stepflow.dto.StepFlowContext;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

/**
 * 多个 FlowNode 多线程并发执行的 FlowNode
 */
public class ParallelFlowNode extends FlowNode {

    @Getter
    private final List<FlowNode> flowNodeList;

    @JsonCreator
    protected ParallelFlowNode(@JsonProperty("type") String type,
                               @JsonProperty("flowNodeList") List<FlowNode> flowNodeList) {
        super(type);
        this.flowNodeList = flowNodeList;
    }

    @Override
    public void execute(StepFlowContext stepFlowContext, ExecutorsContext executorsContext) {
        ExecutorService stepFlowParallelThreadPool = executorsContext.getStepFlowParallelThreadPool();

        // 异步执行所有任务 flowNode
        CompletableFuture<?>[] futures = flowNodeList.stream()
                .map(flowNode -> CompletableFuture.runAsync(
                        () -> flowNode.execute(stepFlowContext, executorsContext),
                        stepFlowParallelThreadPool
                ))
                .toArray(CompletableFuture[]::new);

        // 等待所有 flowNode 执行完成
        CompletableFuture.allOf(futures).join();
    }
}
