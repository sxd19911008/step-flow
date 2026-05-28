package io.github.kentasun.stepflow.dto;

import io.github.kentasun.stepflow.api.dto.OneOffParams;
import io.github.kentasun.stepflow.api.dto.StepFlowContext;
import io.github.kentasun.stepflow.flow.FlowExecutor;
import io.github.kentasun.stepflow.step.StepExecutor;
import io.github.kentasun.stepflow.step.dto.Step;

import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * 用于随着上下文一起传递的各种执行器
 */
public class ExecutorsContext {

    // 步骤执行器
    private final StepExecutor stepExecutor;
    // 流程执行器
    private final FlowExecutor flowExecutor;
    // 多个 FlowNode 多线程并发执行的线程池
    private final ExecutorService stepFlowParallelThreadPool;

    public ExecutorsContext(StepExecutor stepExecutor, FlowExecutor flowExecutor, ExecutorService stepFlowParallelThreadPool) {
        this.stepExecutor = stepExecutor;
        this.flowExecutor = flowExecutor;
        this.stepFlowParallelThreadPool = stepFlowParallelThreadPool;
    }

    /**
     * 执行步骤
     *
     * @param stepCode        步骤代码
     * @param stepFlowContext 上下文对象
     * @param oneOffParams    1次性参数，仅供当前 step 使用
     * @return 步骤执行结果
     */
    public Object executeByStepCode(final String stepCode, StepFlowContext stepFlowContext, OneOffParams oneOffParams) {
        return stepExecutor.executeByStepCode(stepCode, stepFlowContext, oneOffParams);
    }

    /**
     * 执行流程
     *
     * @param flowCode        流程代码
     * @param stepFlowContext 上下文对象
     * @return 流程执行结果
     */
    public Map<String, Object> executeByFlowCode(final String flowCode, StepFlowContext stepFlowContext) {
        return flowExecutor.executeByFlowCode(flowCode, stepFlowContext, this);
    }

    public Step getStep(String stepCode) {
        return stepExecutor.getStep(stepCode);
    }

    /**
     * 使用已注册 StepHandler 执行 IF 内联表达式条件。
     *
     * @param contentType     StepContentType，如 AVIATOR
     * @param expression      表达式正文
     * @param stepFlowContext 流程上下文
     * @return 表达式执行结果
     */
    public Object executeInlineExpression(String contentType,
                                          String expression,
                                          StepFlowContext stepFlowContext) {
        return stepExecutor.executeInlineExpression(contentType, expression, stepFlowContext);
    }

    public ExecutorService getStepFlowParallelThreadPool() {
        return this.stepFlowParallelThreadPool;
    }

    public static ExecutorsContextBuilder builder() {
        return new ExecutorsContextBuilder();
    }

    public static class ExecutorsContextBuilder {
        private StepExecutor stepExecutor;
        private FlowExecutor flowExecutor;
        private ExecutorService stepFlowParallelThreadPool;

        ExecutorsContextBuilder() {
        }

        public ExecutorsContextBuilder stepExecutor(StepExecutor stepExecutor) {
            this.stepExecutor = stepExecutor;
            return this;
        }

        public ExecutorsContextBuilder flowExecutor(FlowExecutor flowExecutor) {
            this.flowExecutor = flowExecutor;
            return this;
        }

        public ExecutorsContextBuilder stepFlowParallelThreadPool(ExecutorService stepFlowParallelThreadPool) {
            this.stepFlowParallelThreadPool = stepFlowParallelThreadPool;
            return this;
        }

        public ExecutorsContext build() {
            return new ExecutorsContext(this.stepExecutor, this.flowExecutor, this.stepFlowParallelThreadPool);
        }

        public String toString() {
            return "ExecutorsContext.ExecutorsContextBuilder(stepExecutor=" + this.stepExecutor + ", flowExecutor=" + this.flowExecutor + ", stepFlowParallelThreadPool=" + this.stepFlowParallelThreadPool + ")";
        }
    }
}
