package com.eredar.stepflow.engine.aviator.function;

import com.eredar.stepflow.engine.aviator.CalcUtils;
import com.eredar.stepflow.engine.aviator.OraDecimal;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.AviatorLong;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorRuntimeJavaType;

import java.util.Map;

/**
 * 自定义加法函数
 */
public class AddFunction extends AbstractFunction {

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
        Object v1 = arg1.getValue(env);
        Object v2 = arg2.getValue(env);

        if (v1 instanceof OraDecimal && v2 instanceof OraDecimal) {
            OraDecimal a = (OraDecimal) v1;
            OraDecimal b = (OraDecimal) v2;
            return AviatorRuntimeJavaType.valueOf(a.add(b));
        }
        if (v1 instanceof OraDecimal && CalcUtils.isSupportedInteger(v2)) {
            OraDecimal a = (OraDecimal) v1;
            return AviatorRuntimeJavaType.valueOf(a.add(new OraDecimal(String.valueOf(v2))));
        }
        if (CalcUtils.isSupportedInteger(v1) && v2 instanceof OraDecimal) {
            OraDecimal b = (OraDecimal) v2;
            return AviatorRuntimeJavaType.valueOf(new OraDecimal(String.valueOf(v1)).add(b));
        }

        // 处理纯整数情况
        if (CalcUtils.isSupportedInteger(v1) && CalcUtils.isSupportedInteger(v2)) {
            // 统一转为 long 进行运算，防止溢出
            long res = ((Number) v1).longValue() + ((Number) v2).longValue();
            // 如果结果在 Integer 范围内，返回 Integer
            if (res >= Integer.MIN_VALUE && res <= Integer.MAX_VALUE) {
                return AviatorRuntimeJavaType.valueOf((int) res);
            }
            // 返回 Long 类型
            return AviatorLong.valueOf(res);
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
        return "add";
    }
}

