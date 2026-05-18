package io.github.kentasun.stepflow.dto;

import io.github.kentasun.stepflow.config.StepFlowConfigProperties;
import io.github.kentasun.stepflow.engine.ExpressionEngine;
import io.github.kentasun.stepflow.exception.StepFlowException;
import io.github.kentasun.stepflow.flow.FlowExecutor;
import io.github.kentasun.stepflow.step.StepExecutor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * 用于随着上下文一起传递的各种执行器
 */
@AllArgsConstructor
@Builder
public class ExecutorsContext {

    // 配置信息
    private final StepFlowConfigProperties configProperties;
    // 步骤执行器
    private final StepExecutor stepExecutor;
    // 流程执行器
    private final FlowExecutor flowExecutor;
    // 多个 FlowNode 多线程并发执行的线程池
    @Getter
    private final ExecutorService stepFlowParallelThreadPool;
    // 表达式引擎
    private final ExpressionEngine expressionEngine;

    /**
     * 执行步骤
     *
     * @param stepCode 步骤代码
     * @param stepFlowContext 上下文对象
     * @param oneOffParams 1次性参数，仅供当前 step 使用
     * @return 步骤执行结果
     */
    public Map<String, Object> executeByStepCode(final String stepCode, StepFlowContext stepFlowContext, OneOffParams oneOffParams) {
        return stepExecutor.executeByStepCode(stepCode, stepFlowContext, oneOffParams, this);
    }

    /**
     * 执行流程
     *
     * @param flowCode 流程代码
     * @param stepFlowContext 上下文对象
     * @return 流程执行结果
     */
    public Map<String, Object> executeByFLowCode(final String flowCode, StepFlowContext stepFlowContext) {
        return flowExecutor.executeByFLowCode(flowCode, stepFlowContext, this);
    }

    /**
     * 执行条件表达式
     *
     * @param expression 条件表达式
     * @param vars 表达式参数
     * @return 表达式结果: true / false
     */
    public Boolean isTrue(String expression, Map<String, Object> vars) {
        Object res = expressionEngine.execute(expression, vars);
        if (res instanceof Boolean) {
            return (Boolean) res;
        } else if (res == null) {
            throw new StepFlowException(String.format("执行条件表达式[%s]，返回null", expression));
        } else {
            throw new StepFlowException(String.format("执行条件表达式[%s]，返回错误类型：%s", expression, res.getClass().getName()));
        }
    }

    /**
     * 执行表达式
     *
     * @param expression 表达式
     * @param vars 表达式参数
     * @return 表达式结果
     */
    public Object executeExpression(String expression, Map<String, Object> vars) {
        return expressionEngine.execute(expression, vars);
    }
}
