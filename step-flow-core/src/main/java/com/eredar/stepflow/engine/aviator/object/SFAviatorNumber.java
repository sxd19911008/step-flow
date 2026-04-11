package com.eredar.stepflow.engine.aviator.object;

import com.eredar.stepflow.engine.aviator.number.OraDecimal;
import com.googlecode.aviator.exception.CompareNotSupportedException;
import com.googlecode.aviator.exception.ExpressionRuntimeException;
import com.googlecode.aviator.runtime.type.*;
import com.googlecode.aviator.utils.TypeUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

public abstract class SFAviatorNumber extends AviatorObject {

    private static final long serialVersionUID = -5301860468644344555L;
    /**
     * Number union
     */
    // Only for bigint/decimal
    protected Number number;
    // Only valid for AviatorLong
    protected long longValue;

    public SFAviatorNumber(final long longValue) {
        super();
        this.longValue = longValue;
    }

    public SFAviatorNumber(final Number number) {
        super();
        if (number instanceof Double || number instanceof Float) {
            this.number = new OraDecimal(String.valueOf(number));
        } else {
            this.number = number;
        }
    }

    @Override
    public Object getValue(final Map<String, Object> env) {
        return this.number;
    }


    public static SFAviatorNumber valueOf(final Object value) {
        if (TypeUtils.isLong(value)) {
            return SFAviatorLong.valueOf(((Number) value).longValue());
        } else if (TypeUtils.isDouble(value)) {
            return new SFAviatorDecimal(new OraDecimal(String.valueOf(value)));
        } else if (TypeUtils.isBigInt(value)) {
            return SFAviatorBigInt.valueOf((BigInteger) value);
        } else if (TypeUtils.isDecimal(value)) {
            return SFAviatorDecimal.valueOf(new OraDecimal((BigDecimal) value));
        } else if (value instanceof OraDecimal) {
            return SFAviatorDecimal.valueOf((OraDecimal) value);
        } else {
            throw new ClassCastException("Could not cast " + value.getClass().getName() + " to Number");
        }

    }

    @Override
    public AviatorObject add(final AviatorObject other, final Map<String, Object> env) {
        switch (other.getAviatorType()) {
            case String:
                return new AviatorString(getValue(env).toString() + ((AviatorString) other).getLexeme(env));
            case BigInt:
            case Decimal:
            case Long:
            case Double:
                return innerAdd(env, toSFAviatorNumber(other, env));
            case JavaType:
                final Object otherValue = other.getValue(env);
                if (otherValue instanceof Number) {
                    return innerAdd(env, SFAviatorNumber.valueOf(otherValue));
                } else if (TypeUtils.isString(otherValue)) {
                    return new AviatorString(getValue(env).toString() + otherValue);
                } else {
                    return super.add(other, env);
                }
            default:
                return super.add(other, env);
        }

    }


    @Override
    public AviatorObject sub(final AviatorObject other, final Map<String, Object> env) {
        switch (other.getAviatorType()) {
            case BigInt:
            case Decimal:
            case Long:
            case Double:
                return innerSub(env, toSFAviatorNumber(other, env));
            case JavaType:
                final Object otherValue = other.getValue(env);
                if (otherValue instanceof Number) {
                    return innerSub(env, SFAviatorNumber.valueOf(otherValue));
                } else {
                    return super.sub(other, env);
                }
            default:
                return super.sub(other, env);
        }

    }


    @Override
    public AviatorObject mod(final AviatorObject other, final Map<String, Object> env) {
        switch (other.getAviatorType()) {
            case BigInt:
            case Decimal:
            case Long:
            case Double:
                return innerMod(env, toSFAviatorNumber(other, env));
            case JavaType:
                final Object otherValue = other.getValue(env);
                if (otherValue instanceof Number) {
                    return innerMod(env, SFAviatorNumber.valueOf(otherValue));
                } else {
                    return super.mod(other, env);
                }
            default:
                return super.mod(other, env);
        }
    }


    @Override
    public AviatorObject div(final AviatorObject other, final Map<String, Object> env) {
        switch (other.getAviatorType()) {
            case BigInt:
            case Decimal:
            case Long:
            case Double:
                return innerDiv(env, toSFAviatorNumber(other, env));
            case JavaType:
                final Object otherValue = other.getValue(env);
                if (otherValue instanceof Number) {
                    return innerDiv(env, SFAviatorNumber.valueOf(otherValue));
                } else {
                    return super.div(other, env);
                }
            default:
                return super.div(other, env);
        }

    }


    @Override
    public AviatorObject mult(final AviatorObject other, final Map<String, Object> env) {
        switch (other.getAviatorType()) {
            case BigInt:
            case Decimal:
            case Long:
            case Double:
                return innerMult(env, toSFAviatorNumber(other, env));
            case JavaType:
                final Object otherValue = other.getValue(env);
                if (otherValue instanceof Number) {
                    return innerMult(env, SFAviatorNumber.valueOf(otherValue));
                } else {
                    return super.mult(other, env);
                }
            default:
                return super.mult(other, env);
        }

    }


    @Override
    public int innerCompare(final AviatorObject other, final Map<String, Object> env) {
        switch (other.getAviatorType()) {
            case BigInt:
            case Decimal:
            case Long:
            case Double:
                return innerCompare(env, toSFAviatorNumber(other, env));
            case JavaType:
                final Object otherValue = other.getValue(env);
                if (otherValue == null) {
                    throw new CompareNotSupportedException(
                            "Could not compare " + desc(env) + " with null value " + other.desc(env));
                }
                if (otherValue instanceof Number) {
                    return innerCompare(env, SFAviatorNumber.valueOf(otherValue));
                } else {
                    throw new CompareNotSupportedException(
                            "Could not compare " + desc(env) + " with " + other.desc(env));
                }
            case Nil:
                throw new CompareNotSupportedException(
                        "Could not compare " + desc(env) + " with null value " + other.desc(env));
            default:
                throw new CompareNotSupportedException(
                        "Could not compare " + desc(env) + " with " + other.desc(env));

        }
    }


    public abstract AviatorObject innerSub(Map<String, Object> env, SFAviatorNumber other);


    public abstract AviatorObject innerMult(Map<String, Object> env, SFAviatorNumber other);


    public abstract AviatorObject innerMod(Map<String, Object> env, SFAviatorNumber other);


    public abstract AviatorObject innerDiv(Map<String, Object> env, SFAviatorNumber other);


    public abstract AviatorObject innerAdd(Map<String, Object> env, SFAviatorNumber other);


    public abstract int innerCompare(Map<String, Object> env, SFAviatorNumber other);


    public long longValue() {
        return this.number.longValue();
    }


    public final BigInteger toBigInt() {
        if (TypeUtils.isBigInt(this.number)) {
            return (BigInteger) this.number;
        } else {
            return new BigInteger(String.valueOf(longValue()));
        }
    }


    public final OraDecimal toDecimal() {
        if (this.number instanceof OraDecimal) {
            return (OraDecimal) this.number;
        } else if (this.number instanceof BigDecimal) {
            return new OraDecimal((BigDecimal) this.number);
        } else if (this.number != null) {
            return new OraDecimal(String.valueOf(this.number));
        } else {
            return new OraDecimal(String.valueOf(this.longValue));
        }
    }

    public static SFAviatorNumber toSFAviatorNumber(AviatorObject other, Map<String, Object> env) {
        if (other instanceof SFAviatorNumber) {
            return (SFAviatorNumber) other;
        } else if (other instanceof AviatorNumber) {
            AviatorNumber otherNumber = (AviatorNumber) other;
            switch (other.getAviatorType()) {
                case BigInt:
                    BigInteger bigInt = otherNumber.toBigInt();
                    return SFAviatorBigInt.valueOf(bigInt);
                case Decimal:
                    BigDecimal decimal = otherNumber.toDecimal(env);
                    return SFAviatorDecimal.valueOf(new OraDecimal(decimal));
                case Long:
                    Long l = otherNumber.longValue();
                    return SFAviatorLong.valueOf(l);
                default:
                    double doubleValue = otherNumber.doubleValue();
                    return SFAviatorDecimal.valueOf(OraDecimal.valueOf(doubleValue));
            }
        }

        throw new ExpressionRuntimeException(String.format("Unexpected type %s", other.getClass().getName()));
    }
}
