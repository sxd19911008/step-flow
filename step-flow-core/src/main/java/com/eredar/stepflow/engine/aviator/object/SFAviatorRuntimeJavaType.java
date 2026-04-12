package com.eredar.stepflow.engine.aviator.object;

import com.googlecode.aviator.runtime.type.AviatorNil;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.utils.Reflector;
import com.googlecode.aviator.utils.VarNameGenerator;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.concurrent.Callable;


/**
 * Aviator runtime java type,used by when generate runtime result.
 */
public class SFAviatorRuntimeJavaType extends SFAviatorJavaType {

    private static final long serialVersionUID = 3498187912164169051L;

    public static final ThreadLocal<VarNameGenerator> TEMP_VAR_GEN = ThreadLocal.withInitial(VarNameGenerator::new);

    protected Object object;
    @Setter
    @Getter
    protected Callable<Object> callable;

    public static AviatorObject valueOf(final Object object) {
        if (object == null) {
            return AviatorNil.NIL;
        }
        if (object instanceof AviatorObject) {
            return (AviatorObject) object;
        }
        return new SFAviatorRuntimeJavaType(object);
    }

    /**
     * please use {@link SFAviatorRuntimeJavaType#valueOf(Object)} instead.
     */
    @Deprecated
    public SFAviatorRuntimeJavaType(final Object object) {
        super(null);
        this.object = object;
    }

    public static String genName() {
        return TEMP_VAR_GEN.get().gen();
    }

    @Override
    public String getName() {
        if (this.name == null) {
            this.name = genName();
        }
        return this.name;
    }

    @Override
    public Object getValue(final Map<String, Object> env) {
        if (this.callable != null) {
            try {
                return this.callable.call();
            } catch (Exception e) {
                throw Reflector.sneakyThrow(e);
            }
        }
        return this.object;
    }
}
