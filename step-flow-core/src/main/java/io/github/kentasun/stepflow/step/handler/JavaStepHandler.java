package io.github.kentasun.stepflow.step.handler;

import io.github.kentasun.stepflow.dto.ExecutorsContext;
import io.github.kentasun.stepflow.dto.OneOffParams;
import io.github.kentasun.stepflow.dto.StepFlowContext;
import io.github.kentasun.stepflow.step.constants.StepContentType;
import io.github.kentasun.stepflow.step.dto.StepData;
import io.github.kentasun.stepflow.exception.StepFlowException;
import io.github.kentasun.stepflow.step.intf.JavaStep;
import io.github.kentasun.stepflow.step.intf.StepHandler;
import io.github.kentasun.stepflow.utils.StepFlowUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Java 步骤处理器
 * <P>执行 Java 方法
 */
public class JavaStepHandler implements StepHandler {

    private final Map<String, JavaStep> methodMap;

    public JavaStepHandler(Map<String, JavaStep> javaStepMap) {
        this.methodMap = new ConcurrentHashMap<>();
        if (StepFlowUtils.isNotEmpty(javaStepMap)) {
            this.methodMap.putAll(javaStepMap);
        }
    }

    @Override
    public String getStepContentType() {
        return StepContentType.JAVA;
    }

    @Override
    public Object execute(StepData stepData, StepFlowContext stepFlowContext, OneOffParams oneOffParams, ExecutorsContext executorsContext) {
        String beanName = stepData.getContent();
        JavaStep javaStep = methodMap.get(beanName);
        if (javaStep == null) {
            throw new StepFlowException(String.format("【%s】不存在", beanName));
        }
        // 执行 Java 方法
        return javaStep.invoke(stepData, stepFlowContext, oneOffParams);
    }

    @Override
    public boolean isStepDataIllegal(StepData stepData) {
        return  StepFlowUtils.isBlank(stepData.getContent());
    }
}
