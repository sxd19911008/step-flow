package com.eredar.stepflow.engine.aviator.object;

import com.eredar.stepflow.engine.aviator.number.OraDecimal;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorType;

import java.util.Map;

public class SFAviatorDecimal extends SFAviatorNumber {

    private static final long serialVersionUID = -5451818688834249245L;

    public SFAviatorDecimal(final OraDecimal number) {
        super(number);
    }


    public static SFAviatorDecimal valueOf(final OraDecimal d) {
        return new SFAviatorDecimal(d);
    }


    @Override
    public AviatorObject innerSub(final Map<String, Object> env, final SFAviatorNumber other) {
        return SFAviatorDecimal.valueOf(toDecimal().subtract(other.toDecimal()));
    }


    @Override
    public AviatorObject neg(final Map<String, Object> env) {
        return SFAviatorDecimal.valueOf(toDecimal().negate());
    }


    @Override
    public AviatorObject innerMult(final Map<String, Object> env, final SFAviatorNumber other) {
        return SFAviatorDecimal.valueOf(toDecimal().multiply(other.toDecimal()));
    }


    @Override
    public AviatorObject innerMod(final Map<String, Object> env, final SFAviatorNumber other) {
        return SFAviatorDecimal.valueOf(toDecimal().remainder(other.toDecimal()));
    }


    @Override
    public AviatorObject innerDiv(final Map<String, Object> env, final SFAviatorNumber other) {
        return SFAviatorDecimal.valueOf(toDecimal().divide(other.toDecimal()));
    }


    @Override
    public SFAviatorNumber innerAdd(final Map<String, Object> env, final SFAviatorNumber other) {
        return SFAviatorDecimal.valueOf(toDecimal().add(other.toDecimal()));
    }


    @Override
    public int innerCompare(final Map<String, Object> env, final SFAviatorNumber other) {
        return toDecimal().compareTo(other.toDecimal());
    }


    @Override
    public AviatorType getAviatorType() {
        return AviatorType.Decimal;
    }
}
