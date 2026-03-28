package com.eredar.stepflow.intf;

import com.eredar.stepflow.dto.OneOffStepParams;
import com.eredar.stepflow.dto.StepContext;
import com.eredar.stepflow.dto.StepInfo;
import com.eredar.stepflow.engine.ExpressionEngine;
import com.eredar.stepflow.utils.StepUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 步骤
 */
public class Step {

    private final StepInfo stepInfo;
    private final StepHandler stepHandler;
    private final ExpressionEngine expressionEngine;

    public Step(StepInfo stepInfo, StepHandler stepHandler, ExpressionEngine expressionEngine) {
        this.stepInfo = stepInfo;
        this.stepHandler = stepHandler;
        this.expressionEngine = expressionEngine;
    }

    /**
     * 执行步骤
     *
     * @param stepContext 步骤上下文，用于传递
     * @param oneOffStepParams 1次性参数，仅供当前 step 使用
     * @return 返回步骤设置的参数
     */
    public Map<String, Object> execute(StepContext stepContext, OneOffStepParams oneOffStepParams) {
        /* 准备参数 */
        // 该步骤用到的参数名称
        List<String> paramNameList = stepInfo.getParamNameList();
        // 参数集合
        Map<String, Object> paramsMap = stepContext.getParamsMap();
        // 参数名称在参数集合中的映射
        Map<String, String> paramNameMap = StepUtils.getParamNameMap(oneOffStepParams);

        // 准备计算用到的参数
        if (StepUtils.isNotEmpty(paramNameList)) {
            Map<String, Object> vars = new HashMap<>();
            for (String paramName : paramNameList) {
                Object value;
                String tempName = this.getTempName(paramName, paramNameMap);
                // 不为空说明需要映射
                if (StepUtils.isNotBlank(tempName)) {
                    if (tempName.contains(".")) {
                        // 说明存在`policyInfo.applyDate`类型的参数获取，需要调用表达式引擎的getParam方法
                        value = expressionEngine.getParam(tempName, paramsMap);
                    } else {
                        // 普通情况下直接从 paramsMap 中获取
                        value = paramsMap.get(tempName);
                    }
                } else {
                    value = paramsMap.get(paramName);
                }
                if (value != null) {
                    vars.put(paramName, value);
                }
            }
            if (oneOffStepParams == null) oneOffStepParams = new OneOffStepParams();
            oneOffStepParams.setVars(vars);
        }

        /* 执行步骤 */
        Object result = stepHandler.execute(stepInfo, stepContext, oneOffStepParams);

        /* 处理返回值类型、名称 */
        if (result != null) {
            if (result instanceof Map) {
                return (Map<String, Object>) result;
            } else {
                String stepName = stepInfo.getStepName();
                Map<String, Object> resMap = new HashMap<>();
                resMap.put(stepName, result);
                return resMap;
            }
        }
        return null;
    }

    private String getTempName(String paramName, Map<String, String> map) {
        if (StepUtils.isNotEmpty(map)) {
            return map.get(paramName);
        } else {
            return null;
        }
    }
}
