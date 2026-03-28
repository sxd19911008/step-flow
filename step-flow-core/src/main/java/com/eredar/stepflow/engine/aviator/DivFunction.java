package com.eredar.stepflow.engine.aviator;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorRuntimeJavaType;

import java.util.Map;

/**
 * 自定义除法函数
 */
public class DivFunction extends AbstractFunction {

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
        Object v1 = arg1.getValue(env);
        Object v2 = arg2.getValue(env);

        // 检查除数是否为 0
        if (CalcUtils.isZero(v2)) {
            throw new IllegalArgumentException("Division by zero");
        }

        OraDecimal a = null;
        if (v1 instanceof OraDecimal) {
            a = (OraDecimal) v1;
        } else if (CalcUtils.isSupportedInteger(v1)) {
            a = new OraDecimal(String.valueOf(v1));
        }

        OraDecimal b = null;
        if (v2 instanceof OraDecimal) {
            b = (OraDecimal) v2;
        } else if (CalcUtils.isSupportedInteger(v2)) {
            b = new OraDecimal(String.valueOf(v2));
        }

        // 都不为null说明类型没问题，可以计算
        if (a != null && b != null) {
            return AviatorRuntimeJavaType.valueOf(a.divide(b));
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
        return "div";
    }
}

