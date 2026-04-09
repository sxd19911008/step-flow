package com.eredar.stepflow.step.constants;

import com.eredar.stepflow.step.dto.StepData;
import com.eredar.stepflow.utils.StepFlowUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Function;

@Getter
@AllArgsConstructor
public enum StepContentTypeEnum {

    CONSTANT("CONSTANT", "常量", "constantStepHandler", StepContentTypeEnum::constantValidation),
    JAVA("JAVA", "Java 方法", "javaStepHandler", stepData -> StepFlowUtils.isBlank(stepData.getContent())),
    EXPRESSION("EXPRESSION", "表达式引擎", "expressionStepHandler", stepData -> StepFlowUtils.isBlank(stepData.getContent())),
    ;

    private final String typeCode;
    private final String typeName;
    private final String beanName;
    private final Function<StepData, Boolean> validation;

    public static String getBeanName(String typeCode) {
        return StepContentTypeEnum.valueOf(typeCode).getBeanName();
    }

    /**
     * 校验是 StepData 否有错误
     * @return true-有错误；false-正确
     */
    public static boolean isStepDataIllegal(StepData stepData) {
        return StepContentTypeEnum.valueOf(stepData.getContentType()).validation.apply(stepData);
    }

    /**
     * 校验【常量】步骤正文是否合法
     *
     * @return true: 不合法; false: 合法
     */
    private static boolean constantValidation(StepData stepData) {
        return StepFlowUtils.isBlank(stepData.getContent()) || StepReturnTypeEnum.isConstantTypeIllegal(stepData.getReturnType());
    }
}
