package com.eredar.stepflow.step;

import com.eredar.stepflow.dto.ExecutorsContext;
import com.eredar.stepflow.dto.OneOffParams;
import com.eredar.stepflow.dto.StepFlowContext;
import com.eredar.stepflow.exception.StepFlowException;
import com.eredar.stepflow.step.constants.StepContentTypeEnum;
import com.eredar.stepflow.step.dto.Step;
import com.eredar.stepflow.step.dto.StepData;
import com.eredar.stepflow.step.intf.StepDataProvider;
import com.eredar.stepflow.step.intf.StepHandler;
import com.eredar.stepflow.utils.StepFlowJsonUtils;
import com.eredar.stepflow.utils.StepFlowUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * step 功能总入口
 */
public class StepExecutor {

    private final Map<String, Step> stepMap;

    public StepExecutor(StepDataProvider stepDataProvider, Map<String, StepHandler> stepHandlerMap) {
        this.stepMap = new ConcurrentHashMap<>();
        List<StepData> stepDataList = null;
        if (stepDataProvider != null) {
            stepDataList = stepDataProvider.loadStepDataList();
        }
        /* 组装 step 对象 */
        if (StepFlowUtils.isNotEmpty(stepDataList)) {
            // 查询所有 StepHandler 对象
            // 组装 Step
            for (StepData stepData : stepDataList) {
                // 校验步骤信息是否合法
                this.validateStepData(stepData);
                // 查找对应的 StepHandler
                String beanName = StepContentTypeEnum.getBeanName(stepData.getContentType());
                StepHandler stepHandler = stepHandlerMap.get(beanName);
                // 放入 stepMap
                stepMap.put(stepData.getStepCode(), new Step(stepData, stepHandler));
            }
        }
    }

    /**
     * 合法性校验
     */
    private void validateStepData(StepData stepData) {
        if (StepContentTypeEnum.isStepDataIllegal(stepData)) {
            throw new StepFlowException("stepData 对象不合法：" + StepFlowJsonUtils.writeValueAsString(stepData));
        }
    }

    /**
     * 执行步骤
     *
     * @param stepCode        步骤代码
     * @param stepFlowContext 上下文对象
     * @param oneOffParams    1次性参数，仅供当前 step 使用
     * @return 步骤执行结果
     */
    public Map<String, Object> executeByStepCode(final String stepCode, StepFlowContext stepFlowContext, OneOffParams oneOffParams, ExecutorsContext executorsContext) {
        Step step = stepMap.get(stepCode);
        if (step == null) {
            throw new StepFlowException(String.format("【%s】步骤不存在", stepCode));
        }
        return step.execute(stepFlowContext, oneOffParams, executorsContext);
    }
}
