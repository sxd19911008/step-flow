/**
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License along with this program;
 * if not, write to the Free Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 **/
package com.eredar.stepflow.engine.aviator.object;

import com.googlecode.aviator.exception.CompareNotSupportedException;
import com.googlecode.aviator.exception.ExpressionRuntimeException;
import com.googlecode.aviator.runtime.type.*;
import com.googlecode.aviator.utils.TypeUtils;

import java.util.Map;


/**
 * Aviator long type
 */
public class SFAviatorLong extends SFAviatorNumber {


    private static final long serialVersionUID = -3021194615848429190L;

    private static class LongCache {
        private LongCache() {
        }

        static final SFAviatorLong[] cache = new SFAviatorLong[256];

        static {
            for (long i = 0; i < cache.length; i++) {
                cache[(int) i] = new SFAviatorLong(i - 128);
            }
        }
    }

    SFAviatorLong(final long i) {
        super(i);
    }


    SFAviatorLong(final Number number) {
        super(number);

    }


    public static SFAviatorLong valueOf(final long l) {
        final int offset = 128;
        if (l >= -128 && l <= 127) { // will cache
            return LongCache.cache[(int) l + offset];
        }
        return new SFAviatorLong(l);
    }


    public static SFAviatorLong valueOf(final Long l) {
        return valueOf(l.longValue());
    }


    @Override
    public AviatorObject neg(final Map<String, Object> env) {
        return SFAviatorLong.valueOf(-this.longValue);
    }


    @Override
    public int innerCompare(final Map<String, Object> env, final SFAviatorNumber other) {
        if (other.getAviatorType() == AviatorType.Long) {
            return TypeUtils.comapreLong(longValue(), other.longValue());
        }

        switch (other.getAviatorType()) {
            case BigInt:
                return toBigInt().compareTo(other.toBigInt());
            case Decimal:
            case Double:
                return toDecimal().compareTo(other.toDecimal());
            default:
                throw new CompareNotSupportedException(
                        "Could not compare " + desc(env) + " with " + other.desc(env));
        }
    }


    @Override
    public AviatorObject innerDiv(final Map<String, Object> env, final SFAviatorNumber other) {
        switch (other.getAviatorType()) {
            case BigInt:
                return AviatorBigInt.valueOf(toBigInt().divide(other.toBigInt()));
            case Long:
                return SFAviatorLong.valueOf(this.longValue / other.longValue());
//            case Decimal:
            default:
                return AviatorDecimal.valueOf(toDecimal().divide(other.toDecimal()));
        }
    }


    @Override
    public AviatorObject innerAdd(final Map<String, Object> env, final SFAviatorNumber other) {
        switch (other.getAviatorType()) {
            case BigInt:
                return AviatorBigInt.valueOf(toBigInt().add(other.toBigInt()));
            case Long:
                return SFAviatorLong.valueOf(this.longValue + other.longValue());
//            case Decimal:
            default:
                return AviatorDecimal.valueOf(toDecimal().add(other.toDecimal()));
        }
    }


    @Override
    public AviatorObject innerMod(final Map<String, Object> env, final SFAviatorNumber other) {
        switch (other.getAviatorType()) {
            case BigInt:
                return AviatorBigInt.valueOf(toBigInt().mod(other.toBigInt()));
            case Long:
                return SFAviatorLong.valueOf(this.longValue % other.longValue());
//            case Decimal:
            default:
                return AviatorDecimal.valueOf(toDecimal().remainder(other.toDecimal()));
        }
    }


    @Override
    public AviatorObject innerMult(final Map<String, Object> env, final SFAviatorNumber other) {
        switch (other.getAviatorType()) {
            case BigInt:
                return AviatorBigInt.valueOf(toBigInt().multiply(other.toBigInt()));
            case Long:
                return SFAviatorLong.valueOf(this.longValue * other.longValue());
//            case Decimal:
            default:
                return AviatorDecimal.valueOf(toDecimal().multiply(other.toDecimal()));
        }
    }


    protected void ensureLong(final AviatorObject other) {
        if (other.getAviatorType() != AviatorType.Long) {
            throw new ExpressionRuntimeException(
                    other + " is not long type,could not be used as a bit operand.");
        }
    }


    @Override
    public AviatorObject bitAnd(final AviatorObject other, final Map<String, Object> env) {
        switch (other.getAviatorType()) {
            case BigInt:
            case Decimal:
            case Long:
            case Double:
                return innerBitAnd(other);
            case JavaType:
                AviatorJavaType otherJavaType = (AviatorJavaType) other;
                final Object otherValue = otherJavaType.getValue(env);
                if (otherValue instanceof Number) {
                    return innerBitAnd(SFAviatorNumber.valueOf(otherValue));
                } else {
                    return super.bitAnd(other, env);
                }
            default:
                return super.bitAnd(other, env);
        }
    }


    protected AviatorObject innerBitAnd(final AviatorObject other) {
        ensureLong(other);
        SFAviatorLong otherLong = (SFAviatorLong) other;
        return SFAviatorLong.valueOf(this.longValue & otherLong.longValue());
    }


    protected AviatorObject innerBitOr(final AviatorObject other) {
        ensureLong(other);
        SFAviatorLong otherLong = (SFAviatorLong) other;
        return SFAviatorLong.valueOf(this.longValue | otherLong.longValue());
    }


    protected AviatorObject innerBitXor(final AviatorObject other) {
        ensureLong(other);
        SFAviatorLong otherLong = (SFAviatorLong) other;
        return SFAviatorLong.valueOf(this.longValue ^ otherLong.longValue());
    }


    protected AviatorObject innerShiftLeft(final AviatorObject other) {
        ensureLong(other);
        SFAviatorLong otherLong = (SFAviatorLong) other;
        return SFAviatorLong.valueOf(this.longValue << otherLong.longValue());
    }


    protected AviatorObject innerShiftRight(final AviatorObject other) {
        ensureLong(other);
        SFAviatorLong otherLong = (SFAviatorLong) other;
        return SFAviatorLong.valueOf(this.longValue >> otherLong.longValue());
    }


    protected AviatorObject innerUnsignedShiftRight(final AviatorObject other) {
        ensureLong(other);
        SFAviatorLong otherLong = (SFAviatorLong) other;
        return SFAviatorLong.valueOf(this.longValue >>> otherLong.longValue());
    }


    @Override
    public AviatorObject bitNot(final Map<String, Object> env) {
        return SFAviatorLong.valueOf(~this.longValue);
    }


    @Override
    public Object getValue(final Map<String, Object> env) {
        return this.longValue;
    }


    @Override
    public long longValue() {
        return this.longValue;
    }


    @Override
    public AviatorObject bitOr(final AviatorObject other, final Map<String, Object> env) {
        switch (other.getAviatorType()) {
            case BigInt:
            case Decimal:
            case Long:
            case Double:
                return innerBitOr(other);
            case JavaType:
                AviatorJavaType otherJavaType = (AviatorJavaType) other;
                final Object otherValue = otherJavaType.getValue(env);
                if (otherValue instanceof Number) {
                    return innerBitOr(SFAviatorNumber.valueOf(otherValue));
                } else {
                    return super.bitOr(other, env);
                }
            default:
                return super.bitOr(other, env);
        }
    }


    @Override
    public AviatorObject bitXor(final AviatorObject other, final Map<String, Object> env) {
        switch (other.getAviatorType()) {
            case BigInt:
            case Decimal:
            case Long:
            case Double:
                return innerBitXor(other);
            case JavaType:
                AviatorJavaType otherJavaType = (AviatorJavaType) other;
                final Object otherValue = otherJavaType.getValue(env);
                if (otherValue instanceof Number) {
                    return innerBitXor(SFAviatorNumber.valueOf(otherValue));
                } else {
                    return super.bitXor(other, env);
                }
            default:
                return super.bitXor(other, env);
        }
    }


    @Override
    public AviatorObject shiftLeft(final AviatorObject other, final Map<String, Object> env) {
        switch (other.getAviatorType()) {
            case BigInt:
            case Decimal:
            case Long:
            case Double:
                return innerShiftLeft(other);
            case JavaType:
                AviatorJavaType otherJavaType = (AviatorJavaType) other;
                final Object otherValue = otherJavaType.getValue(env);
                if (otherValue instanceof Number) {
                    return innerShiftLeft(SFAviatorNumber.valueOf(otherValue));
                } else {
                    return super.shiftLeft(other, env);
                }
            default:
                return super.shiftLeft(other, env);
        }
    }


    @Override
    public AviatorObject shiftRight(final AviatorObject other, final Map<String, Object> env) {
        switch (other.getAviatorType()) {
            case BigInt:
            case Decimal:
            case Long:
            case Double:
                return innerShiftRight(other);
            case JavaType:
                AviatorJavaType otherJavaType = (AviatorJavaType) other;
                final Object otherValue = otherJavaType.getValue(env);
                if (otherValue instanceof Number) {
                    return innerShiftRight(SFAviatorNumber.valueOf(otherValue));
                } else {
                    return super.shiftRight(other, env);
                }
            default:
                return super.shiftRight(other, env);
        }
    }


    @Override
    public AviatorObject unsignedShiftRight(final AviatorObject other,
                                            final Map<String, Object> env) {
        switch (other.getAviatorType()) {
            case BigInt:
            case Decimal:
            case Long:
            case Double:
                return innerUnsignedShiftRight(other);
            case JavaType:
                AviatorJavaType otherJavaType = (AviatorJavaType) other;
                final Object otherValue = otherJavaType.getValue(env);
                if (otherValue instanceof Number) {
                    return innerUnsignedShiftRight(SFAviatorNumber.valueOf(otherValue));
                } else {
                    return super.unsignedShiftRight(other, env);
                }
            default:
                return super.unsignedShiftRight(other, env);
        }
    }


    @Override
    public AviatorObject innerSub(final Map<String, Object> env, final SFAviatorNumber other) {
        switch (other.getAviatorType()) {
            case BigInt:
                return AviatorBigInt.valueOf(toBigInt().subtract(other.toBigInt()));
            case Long:
                return SFAviatorLong.valueOf(this.longValue - other.longValue());
//            case Decimal:
            default:
                return AviatorDecimal.valueOf(toDecimal().subtract(other.toDecimal()));
        }
    }


    @Override
    public AviatorType getAviatorType() {
        return AviatorType.Long;
    }
}
