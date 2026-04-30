package com.eredar.stepflow.step.handler;

import com.eredar.stepflow.dto.ExecutorsContext;
import com.eredar.stepflow.dto.OneOffParams;
import com.eredar.stepflow.dto.StepFlowContext;
import com.eredar.stepflow.exception.StepFlowException;
import com.eredar.stepflow.step.constants.StepReturnTypeEnum;
import com.eredar.stepflow.step.dto.StepData;
import com.eredar.stepflow.step.intf.StepHandler;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

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
            return new BigDecimal(constant);
        } else if (StepReturnTypeEnum.STRING.getTypeCode().equals(returnType)) {
            return constant;
        } else if (StepReturnTypeEnum.BOOLEAN.getTypeCode().equals(returnType)) {
            return Boolean.valueOf(constant);
        } else if (StepReturnTypeEnum.DATE.getTypeCode().equals(returnType)) {
            // 必须是 ISO-8601 日期字符串，如：2026-04-10T10:57:30+08:00  或者  2026-04-10T10:57:30+08:00[Asia/Shanghai]
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(constant);
            return zonedDateTime.toInstant();
        }

        throw new StepFlowException("未知的returnType类型：" + returnType);
    }
}
