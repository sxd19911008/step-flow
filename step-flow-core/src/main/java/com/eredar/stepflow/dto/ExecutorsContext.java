package com.eredar.stepflow.dto;

import com.eredar.stepflow.config.StepFlowConfigProperties;
import com.eredar.stepflow.engine.BusinessExpressionEngine;
import com.eredar.stepflow.engine.ConditionExpressionEngine;
import com.eredar.stepflow.engine.ParamExpressionEngine;
import com.eredar.stepflow.flow.FlowExecutor;
import com.eredar.stepflow.step.StepExecutor;
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
    // 参数获取引擎
    private final ParamExpressionEngine paramExpressionEngine;
    // 条件表达式引擎
    private final ConditionExpressionEngine conditionExpressionEngine;
    // 业务表达式引擎
    private final BusinessExpressionEngine businessExpressionEngine;

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
     * 获取参数
     *
     * @param expression 用于获取参数的表达式
     * @param vars 表达式参数
     * @return 获取到的参数
     */
    public Object getParam(String expression, Map<String, Object> vars) {
        return paramExpressionEngine.getParam(expression, vars);
    }

    /**
     * 执行条件表达式
     *
     * @param expression 条件表达式
     * @param vars 表达式参数
     * @return 表达式结果: true / false
     */
    public Boolean isTrue(String expression, Map<String, Object> vars) {
        return conditionExpressionEngine.isTrue(expression, vars);
    }

    /**
     * 执行 业务表达式
     *
     * @param expression 计算表达式
     * @param vars 表达式参数
     * @return 表达式结果
     */
    public Object executeBusinessExpression(String expression, Map<String, Object> vars) {
        return businessExpressionEngine.execute(expression, vars);
    }
}
