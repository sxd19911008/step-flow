package com.eredar.stepflow.constants;

import com.eredar.stepflow.utils.StepUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * step 类型为 StepContentTypeEnum.CONSTANT 时，常量对应的类型
 * 由于常量配置在数据库中都是字符串，需要根据该枚举类的配置进行类型转换
 */
@Getter
@AllArgsConstructor
public enum StepConstantTypeEnum {

    DECIMAL("decimal"),
    STRING("String"),
    BOOLEAN("Boolean"),
    ;

    private final String typeCode;

    /**
     * 校验 constantType 是否有错误
     * @return true-有错误；false-正确
     */
    public static boolean isConstantTypeIllegal(String constantType) {
        if (StepUtils.isBlank(constantType)) {
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
