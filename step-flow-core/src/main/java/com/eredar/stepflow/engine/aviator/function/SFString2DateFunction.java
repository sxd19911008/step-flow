package com.eredar.stepflow.engine.aviator.function;

import com.eredar.stepflow.engine.aviator.object.SFAviatorRuntimeJavaType;
import com.eredar.stepflow.engine.aviator.utils.SFDateFormatCache;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorObject;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Map;


/**
 * string_to_date function
 */
public class SFString2DateFunction extends AbstractFunction {

    private static final long serialVersionUID = -8780463814840818949L;

    @Override
    public String getName() {
        return "string_to_date";
    }

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2, AviatorObject arg3) {
        // 准备入参
        String source = FunctionUtils.getStringValue(arg1, env);
        String format = FunctionUtils.getStringValue(arg2, env);
        String zoneId = FunctionUtils.getStringValue(arg3, env);
        // 获取 DateTimeFormatter 对象
        DateTimeFormatter dtf = SFDateFormatCache.getOrCreateDateFormat(format, zoneId);
        // 解析日期字符串
        Instant from = Instant.from(dtf.parse(source));
        // 返回结果
        return SFAviatorRuntimeJavaType.valueOf(from);
    }
}
