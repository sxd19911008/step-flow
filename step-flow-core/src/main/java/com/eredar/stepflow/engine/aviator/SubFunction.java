package com.eredar.stepflow.engine.aviator;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.AviatorLong;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorRuntimeJavaType;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 自定义减法函数
 */
public class SubFunction extends AbstractFunction {

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
        Object v1 = arg1.getValue(env);
        Object v2 = arg2.getValue(env);

        if (v1 instanceof OraDecimal && v2 instanceof OraDecimal) {
            OraDecimal a = (OraDecimal) v1;
            OraDecimal b = (OraDecimal) v2;
            return AviatorRuntimeJavaType.valueOf(a.subtract(b));
        }
        if (v1 instanceof OraDecimal && CalcUtils.isSupportedInteger(v2)) {
            OraDecimal a = (OraDecimal) v1;
            return AviatorRuntimeJavaType.valueOf(a.subtract(new OraDecimal(String.valueOf(v2))));
        }
        if (CalcUtils.isSupportedInteger(v1) && v2 instanceof OraDecimal) {
            OraDecimal b = (OraDecimal) v2;
            return AviatorRuntimeJavaType.valueOf(new OraDecimal(String.valueOf(v1)).subtract(b));
        }

        if (CalcUtils.isSupportedInteger(v1) && CalcUtils.isSupportedInteger(v2)) {
            // 统一转为 long 进行运算，防止溢出
            long res = ((Number) v1).longValue() - ((Number) v2).longValue();
            // 如果结果在 Integer 范围内，返回 Integer
            if (res >= Integer.MIN_VALUE && res <= Integer.MAX_VALUE) {
                return AviatorRuntimeJavaType.valueOf((int) res);
            }
            // 返回 Long 类型
            return AviatorLong.valueOf(res);
        }

        // 2个日期相减，得到间隔天数，可以为负值。
        if (v1 instanceof LocalDateTime && v2 instanceof LocalDateTime) {
            LocalDateTime a = (LocalDateTime) v1;
            LocalDateTime b = (LocalDateTime) v2;
            return AviatorRuntimeJavaType.valueOf(CalcUtils.oracleDaysBetween(a, b));
        }

        throw new IllegalArgumentException(String.format(
                "不支持的类型: %s (%s) 和 %s (%s). 仅支持 Integer, Long 和 OraDecimal。",
                v1,
                v1 != null ? v1.getClass().getSimpleName() : "null",
                v2,
                v2 != null ? v2.getClass().getSimpleName() : "null"
        ));
    }

    @Override
    public String getName() {
        return "sub";
    }
}

