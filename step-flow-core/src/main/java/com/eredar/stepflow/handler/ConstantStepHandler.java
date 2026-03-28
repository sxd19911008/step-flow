package com.eredar.stepflow.handler;

import com.eredar.stepflow.constants.StepConstantTypeEnum;
import com.eredar.stepflow.dto.OneOffStepParams;
import com.eredar.stepflow.dto.StepContext;
import com.eredar.stepflow.dto.StepInfo;
import com.eredar.stepflow.intf.StepHandler;
import com.eredar.stepflow.engine.aviator.OraDecimal;
import org.springframework.stereotype.Component;

/**
 * 常量步骤处理器
 * <P>将常量值按照配置的类型转换成对象
 */
@Component
public class ConstantStepHandler implements StepHandler {

    @Override
    public Object execute(StepInfo stepInfo, StepContext stepContext, OneOffStepParams oneOffStepParams) {
        String constant = stepInfo.getContent().getConstant();
        String constantType = stepInfo.getContent().getConstantType();

        if (StepConstantTypeEnum.DECIMAL.getTypeCode().equals(constantType)) {
            return new OraDecimal(constant);
        } else if (StepConstantTypeEnum.STRING.getTypeCode().equals(constantType)) {
            return constant;
        } else if (StepConstantTypeEnum.BOOLEAN.getTypeCode().equals(constantType)) {
            return Boolean.valueOf(constant);
        }
        return null;
    }
}
