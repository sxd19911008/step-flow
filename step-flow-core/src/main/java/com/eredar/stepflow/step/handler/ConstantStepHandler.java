package com.eredar.stepflow.step.handler;

import com.eredar.stepflow.dto.ExecutorsContext;
import com.eredar.stepflow.step.constants.StepReturnTypeEnum;
import com.eredar.stepflow.dto.OneOffParams;
import com.eredar.stepflow.dto.StepFlowContext;
import com.eredar.stepflow.step.dto.StepData;
import com.eredar.stepflow.engine.aviator.number.OraDecimal;
import com.eredar.stepflow.step.intf.StepHandler;

/**
 * 常量步骤处理器
 * <P>将常量值按照配置的类型转换成对象
 */
public class ConstantStepHandler implements StepHandler {

    @Override
    public Object execute(StepData stepData, StepFlowContext stepFlowContext, OneOffParams oneOffParams, ExecutorsContext executorsContext) {
        String constant = stepData.getContent();
        String returnType = stepData.getReturnType();

        if (StepReturnTypeEnum.DECIMAL.getTypeCode().equals(returnType)) {
            return new OraDecimal(constant);
        } else if (StepReturnTypeEnum.STRING.getTypeCode().equals(returnType)) {
            return constant;
        } else if (StepReturnTypeEnum.BOOLEAN.getTypeCode().equals(returnType)) {
            return Boolean.valueOf(constant);
        } else if (StepReturnTypeEnum.DATE.getTypeCode().equals(returnType)) {
            // TODO 修正 Date 类型后再实现，要求用 ISO-8601 日期字符串格式
            throw new RuntimeException("暂不支持DATE类型");
        }
        return null;
    }
}
