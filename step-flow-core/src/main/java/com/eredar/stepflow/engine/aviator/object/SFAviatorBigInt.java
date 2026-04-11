package com.eredar.stepflow.engine.aviator.object;

import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorType;

import java.math.BigInteger;
import java.util.Map;


/**
 * Aviator Big Integer
 */
public class SFAviatorBigInt extends SFAviatorLong {

    private static final long serialVersionUID = 3431817954508387226L;

    private static class BigIntCache {
        private BigIntCache() {
        }

        static final SFAviatorBigInt[] cache = new SFAviatorBigInt[256];

        static {
            for (long i = 0; i < cache.length; i++) {
                cache[(int) i] = new SFAviatorBigInt(BigInteger.valueOf(i - 128));
            }
        }
    }


    @Override
    public Object getValue(Map<String, Object> env) {
        return this.number;
    }


    @Override
    public long longValue() {
        return this.number.longValue();
    }


    public SFAviatorBigInt(Number number) {
        super(number);
    }


    public static SFAviatorBigInt valueOf(BigInteger v) {
        return new SFAviatorBigInt(v);
    }


    public static SFAviatorBigInt valueOf(String v) {
        return new SFAviatorBigInt(new BigInteger(v));
    }


    public static SFAviatorBigInt valueOf(long l) {
        final int offset = 128;
        if (l >= -128 && l <= 127) {
            return BigIntCache.cache[(int) l + offset];
        }
        return valueOf(BigInteger.valueOf(l));
    }


    @Override
    public AviatorObject neg(Map<String, Object> env) {
        return SFAviatorBigInt.valueOf(this.toBigInt().negate());
    }


    @Override
    public AviatorObject innerSub(Map<String, Object> env, SFAviatorNumber other) {
        switch (other.getAviatorType()) {
            case Decimal:
            case Double:
                return SFAviatorDecimal.valueOf(this.toDecimal().subtract(other.toDecimal()));
            default:
                return SFAviatorBigInt.valueOf(this.toBigInt().subtract(other.toBigInt()));
        }
    }


    @Override
    public AviatorObject innerMult(Map<String, Object> env, SFAviatorNumber other) {
        switch (other.getAviatorType()) {
            case Decimal:
            case Double:
                return SFAviatorDecimal.valueOf(this.toDecimal().multiply(other.toDecimal()));
            default:
                return SFAviatorBigInt.valueOf(this.toBigInt().multiply(other.toBigInt()));
        }
    }


    @Override
    public AviatorObject innerMod(Map<String, Object> env, SFAviatorNumber other) {
        switch (other.getAviatorType()) {
            case Decimal:
            case Double:
                return SFAviatorDecimal.valueOf(this.toDecimal().remainder(other.toDecimal()));
            default:
                return SFAviatorBigInt.valueOf(this.toBigInt().mod(other.toBigInt()));
        }
    }


    @Override
    public AviatorObject innerDiv(Map<String, Object> env, SFAviatorNumber other) {
        switch (other.getAviatorType()) {
            case Decimal:
            case Double:
                return SFAviatorDecimal.valueOf(this.toDecimal().divide(other.toDecimal()));
            default:
                return SFAviatorBigInt.valueOf(this.toBigInt().divide(other.toBigInt()));
        }
    }


    @Override
    public SFAviatorNumber innerAdd(Map<String, Object> env, SFAviatorNumber other) {
        switch (other.getAviatorType()) {
            case Decimal:
            case Double:
                return SFAviatorDecimal.valueOf(this.toDecimal().add(other.toDecimal()));
            default:
                return SFAviatorBigInt.valueOf(this.toBigInt().add(other.toBigInt()));
        }
    }


    @Override
    public int innerCompare(Map<String, Object> env, SFAviatorNumber other) {
        switch (other.getAviatorType()) {
            case Decimal:
            case Double:
                return this.toDecimal().compareTo(other.toDecimal());
            default:
                return this.toBigInt().compareTo(other.toBigInt());
        }
    }


    @Override
    protected AviatorObject innerBitAnd(AviatorObject other) {
        return SFAviatorBigInt.valueOf(this.toBigInt().and(((SFAviatorNumber) other).toBigInt()));
    }


    @Override
    protected AviatorObject innerBitOr(AviatorObject other) {
        return SFAviatorBigInt.valueOf(this.toBigInt().or(((SFAviatorNumber) other).toBigInt()));
    }


    @Override
    protected AviatorObject innerBitXor(AviatorObject other) {
        return SFAviatorBigInt.valueOf(this.toBigInt().xor(((SFAviatorNumber) other).toBigInt()));
    }


    @Override
    protected AviatorObject innerShiftLeft(AviatorObject other) {
        this.ensureLong(other);
        return SFAviatorBigInt
                .valueOf(this.toBigInt().shiftLeft((int) ((SFAviatorNumber) other).longValue()));
    }


    @Override
    protected AviatorObject innerShiftRight(AviatorObject other) {
        this.ensureLong(other);
        return SFAviatorBigInt
                .valueOf(this.toBigInt().shiftRight((int) ((SFAviatorNumber) other).longValue()));
    }


    @Override
    protected AviatorObject innerUnsignedShiftRight(AviatorObject other) {
        return this.innerShiftRight(other);
    }


    @Override
    public AviatorType getAviatorType() {
        return AviatorType.BigInt;
    }

}
