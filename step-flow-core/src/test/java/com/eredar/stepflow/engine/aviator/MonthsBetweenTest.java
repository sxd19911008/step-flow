package com.eredar.stepflow.engine.aviator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("monthsBetween 方法测试")
public class MonthsBetweenTest {

    static Stream<Arguments> testFractionalMonthsBySecondsProvider() {
        return Stream.of(
                Arguments.of(
                        "不同月份，同一天",
                        Instant.parse("2023-01-15T15:47:39Z"),
                        Instant.parse("2023-03-15T01:14:22Z"),
                        new OraDecimal("2")
                ),
                Arguments.of(
                        "均为月末",
                        Instant.parse("2023-01-31T15:47:39Z"),
                        Instant.parse("2023-02-28T01:14:22Z"),
                        new OraDecimal("1")
                ),
                Arguments.of(
                        "同一天",
                        Instant.parse("2023-01-31T01:14:22Z"),
                        Instant.parse("2023-01-31T15:47:39Z"),
                        new OraDecimal("0")
                ),
                Arguments.of(
                        "小数",
                        Instant.parse("2024-02-23T11:02:39Z"),
                        Instant.parse("2024-02-29T15:50:39Z"),
                        new OraDecimal("0.2")
                ),
                Arguments.of(
                        "含0小数",
                        Instant.parse("2024-02-28T14:50:39Z"),
                        Instant.parse("2024-02-29T15:11:53Z"),
                        new OraDecimal("0.0327337216248506571087216248506571087216")
                ),
                Arguments.of(
                        "1秒是零点几月",
                        Instant.parse("2024-02-28T23:59:59Z"),
                        Instant.parse("2024-02-29T00:00:00Z"),
                        new OraDecimal("0.0000003733572281959378733572281959378733572282")
                ),
                Arguments.of(
                        "3位整数+小数",
                        Instant.parse("2013-01-31T23:24:39Z"),
                        Instant.parse("2033-10-28T01:14:11Z"),
                        new OraDecimal("248.873421445639187574671445639187574671")
                ),
                Arguments.of(
                        "整数+1秒",
                        Instant.parse("2013-01-31T23:59:59Z"),
                        Instant.parse("2033-10-01T00:00:00Z"),
                        new OraDecimal("248.000000373357228195937873357228195938")
                )

        );
    }

    @ParameterizedTest(name = "【{index}】{0}: beginDate={1}, endDate={2}")
    @MethodSource("testFractionalMonthsBySecondsProvider")
    void testFractionalMonthsBySeconds(String caseId, Instant beginDate, Instant endDate, OraDecimal expected) {
        OraDecimal actual = Utils.monthsBetween(beginDate, endDate);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("测试异常情况")
    void testExceptions() {
        Instant now = Instant.now();
        assertThrows(IllegalArgumentException.class, () -> Utils.monthsBetween(null, now));
        assertThrows(IllegalArgumentException.class, () -> Utils.monthsBetween(now.plus(1, ChronoUnit.DAYS), now));
    }
}
