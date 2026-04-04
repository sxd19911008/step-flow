package com.eredar.stepflow.step.constants;

import com.eredar.stepflow.utils.StepFlowUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * step 类型为 StepContentTypeEnum.CONSTANT 时，常量对应的类型
 * 由于常量配置在数据库中都是字符串，需要根据该枚举类的配置进行类型转换
 * TODO 改名 StepReturnTypeEnum
 */
@Getter
@AllArgsConstructor
public enum StepConstantTypeEnum {

    DECIMAL("DECIMAL"),
    STRING("STRING"),
    BOOLEAN("BOOLEAN"),
    DATE("DATE"),
    ;

    private final String typeCode;

    /**
     * 校验 constantType 是否有错误
     * @return true-有错误；false-正确
     */
    public static boolean isConstantTypeIllegal(String constantType) {
        if (StepFlowUtils.isBlank(constantType)) {
            return true;
        }
        for (StepConstantTypeEnum anEnum : values()) {
            if (anEnum.getTypeCode().equals(constantType)) {
                return false;
            }
        }
        return true;
    }
}
