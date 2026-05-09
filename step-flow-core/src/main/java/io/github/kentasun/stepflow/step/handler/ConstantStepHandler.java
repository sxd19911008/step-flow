package io.github.kentasun.stepflow.step.handler;

import io.github.kentasun.stepflow.dto.ExecutorsContext;
import io.github.kentasun.stepflow.dto.OneOffParams;
import io.github.kentasun.stepflow.dto.StepFlowContext;
import io.github.kentasun.stepflow.exception.StepFlowException;
import io.github.kentasun.stepflow.step.constants.StepContentType;
import io.github.kentasun.stepflow.step.constants.StepReturnTypeEnum;
import io.github.kentasun.stepflow.step.dto.StepData;
import io.github.kentasun.stepflow.step.intf.StepHandler;
import io.github.kentasun.stepflow.utils.StepFlowUtils;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * 常量步骤处理器
 * <P>将常量值按照配置的类型转换成对象
 */
public class ConstantStepHandler implements StepHandler {

    @Override
    public String getStepContentType() {
        return StepContentType.CONSTANT;
    }

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

    @Override
    public boolean isStepDataIllegal(StepData stepData) {
        return StepFlowUtils.isBlank(stepData.getContent()) || this.isConstantTypeIllegal(stepData.getReturnType());
    }

    /**
     * 校验 constantType 是否有错误
     * @return true-有错误；false-正确
     */
    private boolean isConstantTypeIllegal(String constantType) {
        if (StepFlowUtils.isBlank(constantType)) {
            return true;
        }
        for (StepReturnTypeEnum anEnum : StepReturnTypeEnum.values()) {
            if (anEnum.getTypeCode().equals(constantType)) {
                return false;
            }
        }
        return true;
    }
}
