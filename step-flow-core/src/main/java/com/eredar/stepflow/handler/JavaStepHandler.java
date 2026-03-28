package com.eredar.stepflow.handler;

import com.eredar.stepflow.dto.OneOffStepParams;
import com.eredar.stepflow.dto.StepContext;
import com.eredar.stepflow.dto.StepInfo;
import com.eredar.stepflow.exception.StepJavaMethodNotFoundException;
import com.eredar.stepflow.intf.JavaStep;
import com.eredar.stepflow.intf.StepHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Java 步骤处理器
 * <P>执行 Java 方法
 */
@Component
public class JavaStepHandler implements StepHandler, ApplicationRunner {

    @Autowired
    private ApplicationContext applicationContext;

    private final Map<String, JavaStep> methodMap = new ConcurrentHashMap<>();

    @Override
    public void run(ApplicationArguments args) {
        Map<String, JavaStep> map = applicationContext.getBeansOfType(JavaStep.class);
        methodMap.putAll(map);
    }

    @Override
    public Object execute(StepInfo stepInfo, StepContext stepContext, OneOffStepParams oneOffStepParams) {
        String beanName = stepInfo.getContent().getJavaMethod();
        JavaStep javaStep = methodMap.get(beanName);
        if (javaStep == null) {
            throw new StepJavaMethodNotFoundException(beanName);
        }
        // 执行 Java 方法
        return javaStep.invoke(stepInfo, stepContext, oneOffStepParams);
    }
}
