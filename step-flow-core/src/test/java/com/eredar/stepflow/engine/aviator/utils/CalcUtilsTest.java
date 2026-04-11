package com.eredar.stepflow.engine.aviator.utils;

import com.eredar.stepflow.engine.aviator.number.OraDecimal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Instant;
import java.util.stream.Stream;

/**
 * {@link CalcUtils} 单元测试类
 */
public class CalcUtilsTest {

    // -------------------------------------------------------------------------
    // oracleDaysBetween
    // -------------------------------------------------------------------------

    /**
     * oracleDaysBetween 场景数据：caseId 为可读说明，便于参数化测试报告展示。
     */
    static Stream<Arguments> testOracleDaysBetweenProvider() {
        return Stream.of(
                Arguments.of(
                        "结果为正，跨年多日",
                        Instant.parse("2023-03-11T10:43:26Z"),
                        Instant.parse("2025-10-20T22:11:17Z"),
                        new OraDecimal("954.477673611111111111111111111111111111")
                ),
                Arguments.of( // 该场景违反正常的精度逻辑，强行保留40位小数
                        "结果为正，1秒",
                        Instant.parse("2025-10-20T23:59:59Z"),
                        Instant.parse("2025-10-21T00:00:00Z"),
                        new OraDecimal("0.0000115740740740740740740740740740740741")
                ),
                Arguments.of(
                        "结果为负，与正序对称取负",
                        Instant.parse("2025-10-20T22:11:17Z"),
                        Instant.parse("2023-03-11T10:43:26Z"),
                        new OraDecimal("-954.477673611111111111111111111111111111")
                ),
                Arguments.of( // 该场景违反正常的精度逻辑，强行保留40位小数
                        "结果为负，1秒",
                        Instant.parse("2025-10-21T00:00:00Z"),
                        Instant.parse("2025-10-20T23:59:59Z"),
                        new OraDecimal("-0.0000115740740740740740740740740740740741")
                )
        );
    }

    @DisplayName("oracleDaysBetween 方法测试")
    @ParameterizedTest(name = "【{index}】{0}: date1={1}, date2={2}")
    @MethodSource("testOracleDaysBetweenProvider")
    void testOracleDaysBetween(String caseId, Instant date1, Instant date2, OraDecimal expected) {
        // 与 UtilsTest 一致：直接比较 OraDecimal，避免字符串手写与数值语义脱节
        OraDecimal actual = CalcUtils.oracleDaysBetween(date1, date2);
        Assertions.assertEquals(expected, actual);
    }
}
