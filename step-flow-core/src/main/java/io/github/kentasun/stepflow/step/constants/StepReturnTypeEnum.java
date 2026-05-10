package io.github.kentasun.stepflow.step.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * step 类型为 StepContentType.CONSTANT 时，常量对应的类型
 * 由于常量配置在数据库中都是字符串，需要根据该枚举类的配置进行类型转换
 */
@Getter
@AllArgsConstructor
public enum StepReturnTypeEnum {

    DECIMAL("DECIMAL"),
    STRING("STRING"),
    BOOLEAN("BOOLEAN"),
    DATE("DATE"),
    ;

    private final String typeCode;
}
