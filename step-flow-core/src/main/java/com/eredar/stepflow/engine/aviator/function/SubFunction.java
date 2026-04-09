package com.eredar.stepflow.engine.aviator.function;

import com.eredar.stepflow.engine.aviator.object.SFAviatorJavaType;
import com.eredar.stepflow.engine.aviator.object.SFAviatorNumber;
import com.googlecode.aviator.lexer.token.OperatorType;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.AviatorJavaType;
import com.googlecode.aviator.runtime.type.AviatorNumber;
import com.googlecode.aviator.runtime.type.AviatorObject;

import java.util.Map;

/**
 * 自定义减法函数
 */
public class SubFunction extends AbstractFunction {

    private static final long serialVersionUID = -5555693700627703384L;

    @Override
    public String getName() {
        return OperatorType.SUB.token;
    }

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
        // AviatorJavaType 代表从上下文map获取数据后，刚刚进入计算
        if (arg1.getClass() == AviatorJavaType.class) {
            // 类型转换
            SFAviatorJavaType sfAviatorJavaType = new SFAviatorJavaType(((AviatorJavaType) arg1).getName());
            // 计算
            return sfAviatorJavaType.sub(arg2, env);
        } else {
            // AviatorNumber 需要转换成自定义的 SFAviatorNumber 计算
            if (arg1 instanceof AviatorNumber) {
                SFAviatorNumber sfArg1 = SFAviatorNumber.toSFAviatorNumber(arg1, env);
                return sfArg1.sub(arg2, env);
            } else { // 其他场景直接计算
                return arg1.sub(arg2, env);
            }
        }
    }
}

