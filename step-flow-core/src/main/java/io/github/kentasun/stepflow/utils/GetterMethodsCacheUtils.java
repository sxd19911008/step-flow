package io.github.kentasun.stepflow.utils;

import io.github.kentasun.stepflow.cache.StepFlowSoftCache;
import io.github.kentasun.stepflow.exception.StepFlowException;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Objects;
import java.util.Optional;

/**
 * Get 方法对象 缓存工具类
 *
 * @author kenta-sun
 */
public class GetterMethodsCacheUtils {

    /**
     * 方法句柄单级缓存：{@code (Class, propertyName, paramCount)} → {@link Optional}{@code <MethodHandle>}。
     */
    private static final StepFlowSoftCache<MethodLookupKey, Optional<MethodHandle>> HANDLE_CACHE =
            new StepFlowSoftCache<>(GetterMethodsCacheUtils::computeHandle);

    public static Optional<MethodHandle> getMethodHandleOptional(Class<?> clazz, String propertyName) {
        return HANDLE_CACHE.getOrPut(new MethodLookupKey(clazz, propertyName));
    }

    private static Optional<MethodHandle> computeHandle(MethodLookupKey key) {
        Class<?> clazz = key.getClazz();
        String propertyName = key.getPropertyName();
        if (StepFlowUtils.isBlank(propertyName)) {
            throw new StepFlowException(String.format(
                    "propertyName can not be blank in class[%s]",
                    clazz.getName()
            ));
        }

        Method[] methodList = clazz.getMethods();
        if (StepFlowUtils.isEmpty(methodList)) {
            // 没有方法，直接返回空 Optional
            return Optional.empty();
        }
        for (Method method : methodList) {
            int modifiers = method.getModifiers();
            if (!Modifier.isStatic(modifiers)
                    && Modifier.isPublic(modifiers)
                    && method.getParameterTypes().length == 0) {
                if (genGetterName("get", propertyName).equals(method.getName())
                        || genGetterName("is", propertyName).equals(method.getName())) {
                    MethodHandle methodHandle = buildMethodHandle(method);
                    return Optional.of(methodHandle);
                } else {
                    if (propertyName.startsWith("is") && propertyName.equals(method.getName())) {
                        MethodHandle methodHandle = buildMethodHandle(method);
                        return Optional.of(methodHandle);
                    }
                }
            }
        }

        // 未找到任何匹配方法，返回空 Optional，防止后续重复扫描
        return Optional.empty();
    }

    private static MethodHandle buildMethodHandle(Method method) {
        method.setAccessible(true);
        try {
            return MethodHandles.lookup().unreflect(method);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static String genGetterName(String prefix, String name) {
        if (name == null) {
            return prefix;
        }
        if (name.length() > 1 && Character.isUpperCase(name.charAt(1))) {
            return prefix + name;
        }
        return prefix + name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    /**
     * 方法查询三元复合键：{@code (Class, propertyName, paramCount)}。
     */
    private static final class MethodLookupKey {

        /**
         * 目标类。
         */
        final Class<?> clazz;

        /**
         * 目标方法名。
         */
        final String propertyName;

        MethodLookupKey(Class<?> clazz, String propertyName) {
            this.clazz = clazz;
            this.propertyName = propertyName;
        }

        public Class<?> getClazz() {
            return clazz;
        }

        public String getPropertyName() {
            return propertyName;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof MethodLookupKey)) {
                return false;
            }
            MethodLookupKey other = (MethodLookupKey) obj;
            return Objects.equals(clazz, other.clazz)
                    && StepFlowUtils.equals(propertyName, other.propertyName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(clazz, propertyName);
        }

        @Override
        public String toString() {
            return "MethodLookupKey{" +
                    "clazz=" + clazz +
                    ", propertyName='" + propertyName + '\'' +
                    '}';
        }
    }
}
