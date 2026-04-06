package com.eredar.stepflow.engine.aviator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link Utils} 单元测试类
 */
@DisplayName("Utils 单元测试")
public class UtilsTest {

    @Nested
    @DisplayName("decode 方法测试")
    class DecodeTest {

        @Test
        @DisplayName("测试基本匹配功能")
        void testBasicMatch() {
            /* 匹配第一个 search 项 */
            assertEquals("A", Utils.decode("1", "1", "A", "2", "B", "C"));
            /* 匹配第二个 search 项 */
            assertEquals("B", Utils.decode("2", "1", "A", "2", "B", "C"));
            /* 无匹配项，返回默认值 */
            assertEquals("C", Utils.decode("3", "1", "A", "2", "B", "C"));
            /* 无匹配项且无默认值，返回 null */
            assertNull(Utils.decode("3", "1", "A", "2", "B"));
        }

        @Test
        @DisplayName("测试 Null 值匹配")
        void testNullMatch() {
            /* null 等于 null */
            assertEquals("Result", Utils.decode(null, null, "Result", "Other"));
            /* 表达式为 null，search 不为 null */
            assertEquals("Default", Utils.decode(null, "1", "Result", "Default"));
        }

        @Test
        @DisplayName("测试数值类型增强比较")
        void testNumberComparison() {
            /* Integer 与 Long 比较 */
            assertEquals("Match", Utils.decode(100, 100L, "Match", "No Match"));
            /* Long 与 OraDecimal 比较 */
            assertEquals("Match", Utils.decode(200L, new OraDecimal("200.00"), "Match", "No Match"));
            /* 不同精度的 OraDecimal 比较 */
            assertEquals("Match", Utils.decode(new OraDecimal("3.14"), new OraDecimal("3.1400"), "Match", "No Match"));
        }

        @Test
        @DisplayName("测试异常输入")
        void testInvalidArguments() {
            /* 参数少于 3 个 */
            assertThrows(IllegalArgumentException.class, () -> Utils.decode("1", "2"));
            assertThrows(IllegalArgumentException.class, () -> Utils.decode("1"));
            //noinspection Convert2MethodRef
            assertThrows(IllegalArgumentException.class, () -> Utils.decode());
        }
    }

    @Nested
    @DisplayName("nvl 方法测试")
    class NvlTest {

        @Test
        @DisplayName("测试正常逻辑")
        void testNvl() {
            /* expr1 为 null */
            assertEquals("Default", Utils.nvl(null, "Default"));
            /* expr1 不为 null */
            assertEquals("Value", Utils.nvl("Value", "Default"));
        }
    }

    @Nested
    @DisplayName("yearsBetween 方法测试")
    class YearsBetweenTest {

        @Test
        @DisplayName("测试 L 类型（不满1年舍去）")
        void testTypeL() {
            Instant begin = Instant.parse("2020-01-01T00:00:00Z");
            /* 正好 1 年 */
            assertEquals(1, Utils.yearsBetween(begin, Instant.parse("2021-01-01T00:00:00Z"), "L"));
            /* 不满 1 年 */
            assertEquals(0, Utils.yearsBetween(begin, Instant.parse("2020-12-31T23:59:00Z"), "L"));
            /* 1 年多一点 */
            assertEquals(1, Utils.yearsBetween(begin, Instant.parse("2021-01-01T00:00:01Z"), "L"));
        }

        @Test
        @DisplayName("测试 Y 类型（不满1年算1年）")
        void testTypeY() {
            Instant begin = Instant.parse("2020-01-01T00:00:00Z");
            /* 正好 1 年 */
            assertEquals(1, Utils.yearsBetween(begin, Instant.parse("2021-01-01T00:00:00Z"), "Y"));
            /* 不满 1 年（向上取整） */
            assertEquals(1, Utils.yearsBetween(begin, Instant.parse("2020-01-02T00:00:00Z"), "Y"));
            /* 1 年多一点 */
            assertEquals(2, Utils.yearsBetween(begin, Instant.parse("2021-01-01T00:00:01Z"), "Y"));
        }

        @Test
        @DisplayName("测试异常情况")
        void testExceptions() {
            Instant now = Instant.now();
            /* 日期为空 */
            assertThrows(IllegalArgumentException.class, () -> Utils.yearsBetween(null, now, "L"));
            /* 起始日期晚于结束日期 */
            assertThrows(IllegalArgumentException.class, () -> Utils.yearsBetween(now.plus(1, ChronoUnit.DAYS), now, "L"));
            /* 不支持的类型 */
            assertThrows(IllegalArgumentException.class, () -> Utils.yearsBetween(now, now.plus(10, ChronoUnit.DAYS), "X"));
        }
    }
}

