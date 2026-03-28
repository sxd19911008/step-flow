package com.eredar.stepflow;

import com.eredar.stepflow.constants.StepContentTypeEnum;
import com.eredar.stepflow.dto.OneOffStepParams;
import com.eredar.stepflow.dto.StepContext;
import com.eredar.stepflow.dto.StepInfo;
import com.eredar.stepflow.engine.ExpressionEngine;
import com.eredar.stepflow.exception.IllegalStepException;
import com.eredar.stepflow.exception.StepNotFoundException;
import com.eredar.stepflow.intf.Step;
import com.eredar.stepflow.intf.StepHandler;
import com.eredar.stepflow.intf.StepInfoProvider;
import com.eredar.stepflow.utils.JsonUtils;
import com.eredar.stepflow.utils.StepUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * step 功能总入口
 * mvn -s /Users/eredar/IDE_plugin/maven_repository/Aphrodite/settings.xml -pl aphrodite-insurance -am -DskipTests clean compile
 */
@Component
public class StepExecutor implements ApplicationRunner {

    @Autowired
    private StepInfoProvider stepInfoProvider;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private ExpressionEngine expressionEngine;

    private final Map<String, Step> stepMap = new ConcurrentHashMap<>();

    /**
     * 初始化
     */
    @Override
    public void run(ApplicationArguments args) {
        List<StepInfo> stepInfoList = stepInfoProvider.loadStepInfoList();
        /* 组装 step 对象 */
        if (StepUtils.isNotEmpty(stepInfoList)) {
            // 查询所有 StepHandler 对象
            Map<String, StepHandler> stepHandlerMap = applicationContext.getBeansOfType(StepHandler.class);
            // 组装 Step
            for (StepInfo stepInfo : stepInfoList) {
                // 校验步骤信息是否合法
                this.validateStepInfo(stepInfo);
                // 查找对应的 StepHandler
                String beanName = StepContentTypeEnum.getBeanName(stepInfo.getContentType());
                StepHandler stepHandler = stepHandlerMap.get(beanName);
                // 放入 stepMap
                stepMap.put(stepInfo.getStepCode(), new Step(stepInfo, stepHandler, expressionEngine));
            }
        }
    }

    /**
     * 合法性校验
     */
    private void validateStepInfo(StepInfo stepInfo) {
        if (StepContentTypeEnum.isStepInfoIllegal(stepInfo)) {
            throw new IllegalStepException("stepInfo 对象不合法：" + JsonUtils.writeValueAsString(stepInfo));
        }
    }

    /**
     * 执行步骤
     *
     * @param stepCode 步骤代码
     * @param stepContext 步骤上下文，用于传递
     * @param oneOffStepParams 1次性参数，仅供当前 step 使用
     * @return 步骤执行结果
     */
    public Map<String, Object> executeByStepCode(final String stepCode, StepContext stepContext, OneOffStepParams oneOffStepParams) {
        Step step = stepMap.get(stepCode);
        if (step == null) {
            throw new StepNotFoundException(stepCode);
        }
        return step.execute(stepContext, oneOffStepParams);
    }
}
