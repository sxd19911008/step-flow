package com.eredar.stepflow.step.handler;

import com.eredar.stepflow.dto.ExecutorsContext;
import com.eredar.stepflow.dto.OneOffParams;
import com.eredar.stepflow.dto.StepFlowContext;
import com.eredar.stepflow.step.dto.StepData;
import com.eredar.stepflow.exception.StepFlowException;
import com.eredar.stepflow.step.intf.JavaStep;
import com.eredar.stepflow.step.intf.StepHandler;
import com.eredar.stepflow.utils.StepFlowUtils;

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
    public Object execute(StepData stepData, StepFlowContext stepFlowContext, OneOffParams oneOffParams, ExecutorsContext executorsContext) {
        String beanName = stepData.getContent();
        JavaStep javaStep = methodMap.get(beanName);
        if (javaStep == null) {
            throw new StepFlowException(String.format("【%s】不存在", beanName));
        }
        // 执行 Java 方法
        return javaStep.invoke(stepData, stepFlowContext, oneOffParams);
    }
}
