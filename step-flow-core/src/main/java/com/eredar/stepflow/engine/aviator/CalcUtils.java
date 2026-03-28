package com.eredar.stepflow.engine.aviator;

import java.time.Duration;
import java.time.LocalDateTime;

public class CalcUtils {

    // 1天的秒数
    private static final OraDecimal DAY_SECONDS = new OraDecimal("86400");

    /**
     * 计算两个 LocalDateTime 之间的天数差 (date2 - date1)
     *
     * @param date1 减数 (起始时间)
     * @param date2 被减数 (结束时间)
     * @return 差值天数 (OraDecimal)
     */
    public static OraDecimal oracleDaysBetween(LocalDateTime date1, LocalDateTime date2) {
        // 校验参数，为 null 直接报错
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("Date parameters cannot be null");
        }
        // 计算持续时间 (Duration 自动处理了正负逻辑)
        Duration duration = Duration.between(date1, date2);
        // 获取总秒数差
        OraDecimal secondsDiff = OraDecimal.valueOf(duration.getSeconds());
        // 计算天数
        return secondsDiff.divide(DAY_SECONDS);
    }

    /**
     * 判断是否为支持的整数类型 (Integer 或 Long)
     */
    public static boolean isSupportedInteger(Object obj) {
        return obj instanceof Integer || obj instanceof Long;
    }

    public static boolean isZero(Object v) {
        if (v instanceof OraDecimal) {
            OraDecimal d = (OraDecimal) v;
            return d.compareTo(OraDecimal.ZERO) == 0;
        }
        if (v instanceof Number) {
            Number n = (Number) v;
            return n.doubleValue() == 0;
        }
        return false;
    }
}
