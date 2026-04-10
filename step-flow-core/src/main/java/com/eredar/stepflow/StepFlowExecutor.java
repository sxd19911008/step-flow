package com.eredar.stepflow;

import com.eredar.stepflow.config.StepFlowConfigProperties;
import com.eredar.stepflow.dto.ExecutorsContext;
import com.eredar.stepflow.dto.StepFlowContext;
import com.eredar.stepflow.engine.BusinessExpressionEngine;
import com.eredar.stepflow.engine.ConditionExpressionEngine;
import com.eredar.stepflow.engine.ParamExpressionEngine;
import com.eredar.stepflow.engine.impl.AviatorBusinessExpressionEngine;
import com.eredar.stepflow.engine.impl.AviatorConditionExpressionEngine;
import com.eredar.stepflow.engine.impl.AviatorParamExpressionEngine;
import com.eredar.stepflow.flow.FlowExecutor;
import com.eredar.stepflow.flow.intf.FlowProvider;
import com.eredar.stepflow.step.StepExecutor;
import com.eredar.stepflow.step.handler.ConstantStepHandler;
import com.eredar.stepflow.step.handler.ExpressionStepHandler;
import com.eredar.stepflow.step.handler.JavaStepHandler;
import com.eredar.stepflow.step.intf.JavaStep;
import com.eredar.stepflow.step.intf.StepDataProvider;
import com.eredar.stepflow.step.intf.StepHandler;
import com.eredar.stepflow.threadpool.StepFlowThreadPoolFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public class StepFlowExecutor {

    // 执行器上下文
    private final ExecutorsContext executorsContext;

    private StepFlowExecutor(ExecutorsContext executorsContext) {
        this.executorsContext = executorsContext;
    }

    public Map<String, Object> executeByFLowCode(String flowCode, Map<String, Object> contextMap) {
        return executorsContext.executeByFLowCode(
                flowCode,
                StepFlowContext.builder().contextMap(contextMap).build()
        );
    }

    /**
     * StepFlowExecutor 构建方法
     *
     * @param stepDataProvider stepData 提供者
     * @param flowProvider     flow 提供者
     * @return StepFlowExecutor 的 Builder 对象
     */
    public static Builder builder(StepDataProvider stepDataProvider, FlowProvider flowProvider) {
        return new Builder(stepDataProvider, flowProvider);
    }

    public static class Builder {

        private final StepDataProvider stepDataProvider;

        private final FlowProvider flowProvider;

        private StepFlowConfigProperties configProperties;

        private Map<String, JavaStep> javaStepMap;

        private ExecutorService parallelThreadPool;

        private ParamExpressionEngine paramExpressionEngine;

        private ConditionExpressionEngine conditionExpressionEngine;

        private BusinessExpressionEngine businessExpressionEngine;

        public Builder(StepDataProvider stepDataProvider, FlowProvider flowProvider) {
            this.stepDataProvider = stepDataProvider;
            this.flowProvider = flowProvider;
        }

        public Builder javaStepMap(Map<String, JavaStep> javaStepMap) {
            this.javaStepMap = javaStepMap;
            return this;
        }

        public Builder configProperties(StepFlowConfigProperties configProperties) {
            this.configProperties = configProperties;
            return this;
        }

        public Builder parallelThreadPool(ExecutorService parallelThreadPool) {
            this.parallelThreadPool = parallelThreadPool;
            return this;
        }

        public Builder paramExpressionEngine(ParamExpressionEngine paramExpressionEngine) {
            this.paramExpressionEngine = paramExpressionEngine;
            return this;
        }

        public Builder conditionExpressionEngine(ConditionExpressionEngine conditionExpressionEngine) {
            this.conditionExpressionEngine = conditionExpressionEngine;
            return this;
        }

        public Builder businessExpressionEngine(BusinessExpressionEngine businessExpressionEngine) {
            this.businessExpressionEngine = businessExpressionEngine;
            return this;
        }

        public StepFlowExecutor build() {
            // 给一些Bean设置默认实现
            this.setDefaultBean();
            // 构建 step 执行器
            StepExecutor stepExecutor = this.buildStepExecutor();
            // 构建 flow 执行器
            FlowExecutor flowExecutor = new FlowExecutor(flowProvider, stepExecutor);

            return new StepFlowExecutor(ExecutorsContext.builder()
                    .configProperties(configProperties)
                    .stepExecutor(stepExecutor)
                    .flowExecutor(flowExecutor)
                    .stepFlowParallelThreadPool(parallelThreadPool)
                    .paramExpressionEngine(paramExpressionEngine)
                    .conditionExpressionEngine(conditionExpressionEngine)
                    .businessExpressionEngine(businessExpressionEngine)
                    .build());
        }

        /**
         * 给一些Bean设置默认实现
         */
        private void setDefaultBean() {

            // 防止空指针
            if (this.configProperties == null) {
                this.configProperties = new StepFlowConfigProperties();
            }

            if (this.parallelThreadPool == null) {
                // 构建线程池工厂
                StepFlowThreadPoolFactory stepFlowThreadPoolFactory = new StepFlowThreadPoolFactory(configProperties);
                // 创建线程池
                this.parallelThreadPool = stepFlowThreadPoolFactory.getStepFlowParallelThreadPool();
            }

            // 如果用户不传入，则使用默认实现
            if (this.paramExpressionEngine == null) {
                this.paramExpressionEngine = new AviatorParamExpressionEngine(configProperties.getParamExpressionEngine());
            }

            // 如果用户不传入，则使用默认实现
            if (this.conditionExpressionEngine == null) {
                this.conditionExpressionEngine = new AviatorConditionExpressionEngine(configProperties.getConditionExpressionEngine());
            }

            // 如果用户不传入，则使用默认实现
            if (this.businessExpressionEngine == null) {
                this.businessExpressionEngine = new AviatorBusinessExpressionEngine(configProperties.getBusinessExpressionEngine());
            }

            // 防止空指针
            if (this.javaStepMap == null) {
                this.javaStepMap = new HashMap<>();
            }
        }

        /**
         * 构建 StepExecutor
         */
        private StepExecutor buildStepExecutor() {
            ConstantStepHandler constantStepHandler = new ConstantStepHandler();
            JavaStepHandler javaStepHandler = new JavaStepHandler(this.javaStepMap);
            ExpressionStepHandler expressionStepHandler = new ExpressionStepHandler();

            Map<String, StepHandler> stepHandlerMap = new HashMap<>();
            stepHandlerMap.put("constantStepHandler", constantStepHandler);
            stepHandlerMap.put("javaStepHandler", javaStepHandler);
            stepHandlerMap.put("expressionStepHandler", expressionStepHandler);

            return new StepExecutor(this.stepDataProvider, stepHandlerMap);
        }
    }
}
