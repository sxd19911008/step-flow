package com.eredar.stepflow.utils;

import com.eredar.stepflow.dto.OneOffParams;
import com.eredar.stepflow.exception.StepFlowException;

import java.util.Collection;
import java.util.Map;

public class StepFlowUtils {

    /**
     * 是否是空字符串
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * 字符串是否非空
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * 驼峰命名转下划线大写
     * <p>例：userName -> USER_NAME
     */
    public static String humpToUpper(String str) {
        if (isBlank(str)) return str;
        return str.replaceAll("(\\p{Upper})", "_$1").toUpperCase();
    }

    /**
     * 驼峰命名转下划线大写
     * 例：userName -> user_name
     */
    public static String humpToLower(String str) {
        if (isBlank(str)) return str;
        return str.replaceAll("(\\p{Upper})", "_$1").toLowerCase();
    }

    /**
     * 比较两个字符串是否相等
     * <p>特殊规则：
     * <p>1. 如果两个字符串都为null，视为不相等
     * <p>2. 如果只有一个字符串为null，视为不相等
     * <p>3. 都不为null时，使用String.equals进行比对
     *
     * @param str1 字符串1
     * @param str2 字符串2
     * @return true:相等; false:不相等
     */
    public static boolean equals(String str1, String str2) {
        if (str1 == null) return false;
        return str1.equals(str2);
    }

    public static boolean notEquals(String str1, String str2) {
        return !equals(str1, str2);
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static Map<String, String> getParamNameMap(OneOffParams oneOffParams) {
        if (oneOffParams == null) return null;
        return oneOffParams.getParamNameMap();
    }

    /**
     * 从 map 中获取指定类型的参数
     *
     * @param key 参数 key
     * @param map 参数集合
     * @param clazz 参数期望类型
     * @return 参数对象
     */
    public static <T> T getValByMap(String key, Map<String, Object> map, Class<T> clazz) {
        return getValByMap(key, map, clazz, false);
    }

    /**
     * 从 map 中获取指定类型的参数
     *
     * @param key 参数 key
     * @param map 参数集合
     * @param clazz 参数期望类型
     * @param isCheckNull 为 null 是否报错：true-报错；false-返回null。默认false
     * @return 参数对象
     */
    public static <T> T getValByMap(String key, Map<String, Object> map, Class<T> clazz, boolean isCheckNull) {
        Object o = map.get(key);
        if (o == null) {
            if (isCheckNull) {
                throw new StepFlowException(String.format("key【%s】对应的参数为null", key));
            }
            return null;
        }
        if (clazz.isInstance(o)) {
            return clazz.cast(o);
        } else {
            throw new StepFlowException(
                    String.format(
                            "类型错误，key=%s; 期待类型=%s; 实际类型=%s",
                            key,
                            clazz.getName(),
                            o.getClass().getName()
                    )
            );
        }
    }

    /**
     * 默认值工具
     */
    public static <T> T defaultIfNull(final T object, final T defaultValue) {
        return object != null ? object : defaultValue;
    }
}
