package com.eredar.stepflow;

import com.eredar.stepflow.config.StepFlowConfigProperties;
import com.eredar.stepflow.dto.ExecutorsContext;
import com.eredar.stepflow.dto.StepFlowContext;
import com.eredar.stepflow.engine.BusinessExpressionEngine;
import com.eredar.stepflow.engine.ConditionExpressionEngine;
import com.eredar.stepflow.engine.ParamExpressionEngine;
import com.eredar.stepflow.exception.StepFlowException;
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
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
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
         * 给未显式设置的组件填充默认实现。
         *
         * <p>引擎实现通过 Java SPI（{@link ServiceLoader}）自动发现，无需在 core 包中
         * 硬编码任何具体引擎类。只要 classpath 上存在实现了引擎接口的插件包
         * （如 step-flow-engine-aviator），SPI 便会自动加载。
         * 若 classpath 上找不到任何实现，则抛出明确的异常提示用户引入插件包。
         */
        private void setDefaultBean() {

            if (this.configProperties == null) {
                this.configProperties = new StepFlowConfigProperties();
            }

            if (this.parallelThreadPool == null) {
                StepFlowThreadPoolFactory factory = new StepFlowThreadPoolFactory(configProperties);
                this.parallelThreadPool = factory.getStepFlowParallelThreadPool();
            }

            // 通过 SPI 发现引擎实现，避免 core 直接依赖任何引擎库
            if (this.paramExpressionEngine == null) {
                this.paramExpressionEngine = loadSpi(ParamExpressionEngine.class);
            }

            if (this.conditionExpressionEngine == null) {
                this.conditionExpressionEngine = loadSpi(ConditionExpressionEngine.class);
            }

            if (this.businessExpressionEngine == null) {
                this.businessExpressionEngine = loadSpi(BusinessExpressionEngine.class);
            }

            if (this.javaStepMap == null) {
                this.javaStepMap = new HashMap<>();
            }
        }

        /**
         * 通过 Java SPI 加载指定接口的第一个实现类。
         *
         * @param spiClass 引擎接口类型
         * @param <T>      引擎接口泛型
         * @return 第一个可用的实现实例
         * @throws StepFlowException 若 classpath 上没有任何实现时，抛出含引导信息的异常
         */
        private <T> T loadSpi(Class<T> spiClass) {
            Iterator<T> it = ServiceLoader.load(spiClass).iterator();
            if (it.hasNext()) {
                return it.next();
            }
            throw new StepFlowException(
                    "No " + spiClass.getSimpleName() + " implementation found on classpath. " +
                    "Please add an engine plugin dependency, e.g. step-flow-engine-aviator."
            );
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
