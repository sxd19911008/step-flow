package com.eredar.stepflow.engine.aviator;

import java.time.Instant;

public class CalcUtils {

    /**
     * 计算两个 Instant 之间的天数差 (date2 - date1)
     *
     * @param beginDate 减数 (起始时间)
     * @param endDate 被减数 (结束时间)
     * @return 差值天数 (OraDecimal)
     */
    public static OraDecimal oracleDaysBetween(Instant beginDate, Instant endDate) {
        // 校验参数，为 null 直接报错
        if (beginDate == null || endDate == null) {
            throw new IllegalArgumentException("Date parameters cannot be null");
        }
        // 计算持续时间 (Duration 自动处理了正负逻辑)
        long beginSeconds = beginDate.getEpochSecond();
        long endSeconds = endDate.getEpochSecond();
        // 获取总秒数差
        OraDecimal secondsDiff = OraDecimal.valueOf(endSeconds - beginSeconds);
        // 计算天数
        return secondsDiff.divide(AviatorConstants.SECOND_OF_DAY);
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
