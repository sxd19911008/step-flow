package com.eredar.stepflow.handler;

import com.eredar.stepflow.StepExecutor;
import com.eredar.stepflow.dto.CompositeStepInfo;
import com.eredar.stepflow.dto.OneOffStepParams;
import com.eredar.stepflow.dto.StepContext;
import com.eredar.stepflow.dto.StepInfo;
import com.eredar.stepflow.engine.ExpressionEngine;
import com.eredar.stepflow.exception.IllegalConditionException;
import com.eredar.stepflow.intf.StepHandler;
import com.eredar.stepflow.utils.StepUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 组合步骤处理器
 * <p>会循环执行子步骤，将计算结果放入上下文
 */
@Component
public class CompositeStepHandler implements StepHandler {

    @Autowired
    private StepExecutor stepExecutor;
    @Autowired
    private ExpressionEngine expressionEngine;

    @Override
    public Object execute(StepInfo stepInfo, StepContext stepContext, OneOffStepParams oneOffStepParams) {
        Map<Integer, List<CompositeStepInfo>> stepList = stepInfo.getContent().getStepList();
        for (int i = 0; i < stepList.size(); i++) {
            // 由于步骤配置顺序时都从1开始，所以这里需要i加1。
            List<CompositeStepInfo> compositeStepInfoList = stepList.get(i + 1);
            if (compositeStepInfoList.size() == 1) {
                this.executeChildStep(compositeStepInfoList.get(0), stepContext);
            } else {
                // TODO 异步执行
                for (CompositeStepInfo compositeStepInfo : compositeStepInfoList) {
                    this.executeChildStep(compositeStepInfo, stepContext);
                }
            }
        }

        /* 组合步骤如果配置了返回值，则直接从上下文中获取 */
        List<String> returnFieldList = stepInfo.getReturnFieldList();
        if (StepUtils.isNotEmpty(returnFieldList)) {
            Map<String, Object> returnMap = new HashMap<>();
            for (String key : returnFieldList) {
                Object value = stepContext.get(key);
                returnMap.put(key, value);
            }
            return returnMap;
        }

        /* 默认无返回值 */
        return null;
    }

    /**
     * 执行单个子步骤
     *
     * @param childStepInfo 子步骤信息
     * @param stepContext 上下文
     */
    private void executeChildStep(CompositeStepInfo childStepInfo, StepContext stepContext) {
        /* 判断子步骤执行条件 */
        String condition = childStepInfo.getCondition();
        Object conditionBool;
        if (StepUtils.isBlank(condition)) {
            conditionBool = Boolean.TRUE;
        } else {
            conditionBool = expressionEngine.execute(condition, stepContext.getParamsMap());
        }
        if (conditionBool instanceof Boolean) {
            // 不通过则直接返回，不再执行。
            if (!((Boolean) conditionBool)) {
                return;
            }
        } else {
            throw new IllegalConditionException("判断条件不是布尔型：" + conditionBool);
        }

        /* 执行子步骤 */
        Map<String, Object> resMap = stepExecutor.executeByStepCode(
                childStepInfo.getStepCode(),
                stepContext,
                OneOffStepParams.builder()
                        .paramNameMap(childStepInfo.getParamNameMap())
                        .build()
        );
        /* 将计算结果放入上下文 */
        if (StepUtils.isNotEmpty(resMap)) {
            // 有映射，先处理映射字段
            Map<String, String> resultFieldMap = childStepInfo.getResultFieldMap();
            if (StepUtils.isNotEmpty(resultFieldMap)) {
                for (Map.Entry<String, String> resEntry : resultFieldMap.entrySet()) {
                    String resMapKey = resEntry.getKey(); // 子步骤默认返回的字段名
                    String stepContextKey = resEntry.getValue(); // 映射期望的字段名
                    // 获取 value，同时在 resMap 中删除，防止重复放入相同字段
                    Object value = resMap.remove(resMapKey);
                    if (value != null) {
                        // 用 映射期望的字段名 放入上下文
                        stepContext.put(stepContextKey, value);
                    }
                }
            }
            // 将所有默认字段名的字段放入上下文
            stepContext.putAll(resMap);
        }
    }
}
