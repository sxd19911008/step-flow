package com.eredar.stepflow.constants;

import com.eredar.stepflow.dto.CompositeStepInfo;
import com.eredar.stepflow.dto.StepInfo;
import com.eredar.stepflow.exception.IllegalStepContentTypeException;
import com.eredar.stepflow.utils.StepUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum StepContentTypeEnum {

    CONSTANT("constant", "常量", "constantStepHandler", StepContentTypeEnum::constantValidation),
    JAVA("java", "Java 方法", "javaStepHandler", stepInfo -> StepUtils.isBlank(stepInfo.getContent().getJavaMethod())),
    COMPOSITE("composite", "多个步骤组合成的步骤", "compositeStepHandler", StepContentTypeEnum::compositeValidation),
    ENGINE("engine", "表达式引擎", "expressionEngineHandler", stepInfo -> StepUtils.isBlank(stepInfo.getContent().getExpression())),
    ;

    private final String typeCode;
    private final String typeName;
    private final String beanName;
    private final Function<StepInfo, Boolean> validation;



    public static StepContentTypeEnum getStepContentType(String typeCode) {
        for (StepContentTypeEnum anEnum : StepContentTypeEnum.values()) {
            if (anEnum.getTypeCode().equals(typeCode)) {
                return anEnum;
            }
        }
        throw new IllegalStepContentTypeException(String.format("【%s】不存在", typeCode));
    }

    public static String getBeanName(String typeCode) {
        return getStepContentType(typeCode).getBeanName();
    }

    /**
     * 校验是 StepInfo 否有错误
     * @return true-有错误；false-正确
     */
    public static boolean isStepInfoIllegal(StepInfo stepInfo) {
        return getStepContentType(stepInfo.getContentType()).validation.apply(stepInfo);
    }

    /**
     * 校验【常量】步骤正文是否合法
     *
     * @return true: 不合法; false: 合法
     */
    private static boolean constantValidation(StepInfo stepInfo) {
        return StepUtils.isBlank(stepInfo.getContent().getConstant()) || StepConstantTypeEnum.isConstantTypeIllegal(stepInfo.getContent().getConstantType());
    }

    /**
     * 校验【聚合】步骤正文是否合法
     *
     * @return true: 不合法; false: 合法
     */
    private static boolean compositeValidation(StepInfo stepInfo) {
        Map<Integer, List<CompositeStepInfo>> stepList = stepInfo.getContent().getStepList();
        // 聚合步骤为空
        if (StepUtils.isEmpty(stepList)) {
            return true;
        }
        // 聚合步骤中，某些步骤的列表为空
        for (Map.Entry<Integer, List<CompositeStepInfo>> entry : stepList.entrySet()) {
            if (StepUtils.isEmpty(entry.getValue())) {
                return true;
            }
        }
        // 聚合步骤的编号不连贯
        List<Integer> keys = new ArrayList<>(stepList.keySet()).stream().sorted().collect(Collectors.toList());
        for (Integer i = 0; i < keys.size(); i++) {
            // 排序后，i一定等于key的值。否则返回true，表示不合法。
            if (!i.equals(keys.get(i) - 1)) {
                return true;
            }
        }

        return false;
    }
}
