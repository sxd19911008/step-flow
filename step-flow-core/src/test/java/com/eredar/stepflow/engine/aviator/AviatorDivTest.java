package com.eredar.stepflow.engine.aviator;

import com.eredar.stepflow.engine.aviator.number.OraDecimal;
import com.eredar.stepflow.engine.impl.AviatorBusinessExpressionEngine;
import com.eredar.stepflow.testUtils.HashMapBuilder;
import com.googlecode.aviator.exception.ExpressionRuntimeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Aviator 除法运算单元测试，对应 {@code OperatorType.DIV}（即 {@code /} 号）。
 *
 * <p>测试策略：
 * <ul>
 *   <li>每个 {@code testXxx} 方法固定 {@code a} 的类型，{@code b} 依次覆盖 type.txt 中的全部 8 种类型</li>
 *   <li>数值型之间遵循类型提升规则：OraDecimal > BigDecimal > Double > BigInteger > Long ≥ Integer</li>
 *   <li>Long / Long 为整数除法（截断余数）；只要一方为浮点/小数类型，结果提升为 OraDecimal</li>
 *   <li>String 或 Instant 参与除法时触发 {@link ExpressionRuntimeException}</li>
 * </ul>
 */
@DisplayName("Aviator 除法测试")
public class AviatorDivTest {

    private final AviatorBusinessExpressionEngine aviator = new AviatorBusinessExpressionEngine(null);

    // ========================= Long =========================

    /**
     * a 固定为 Long，b 依次为 Long / Integer / BigInteger / Double / BigDecimal / OraDecimal / String / Instant
     */
    static Stream<Arguments> testLongProvider() {
        return Stream.of(
                // a(Long) / b(Long) → Long（整数除法，截断余数）
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", 10L).put("b", 2L).build(),
                        5L
                ),
                // a(Long) / b(Integer) → Long（Aviator 内部将 Integer 提升为 Long）
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", 10L).put("b", 2).build(),
                        5L
                ),
                // a(Long) / b(BigInteger) → BigInteger（BigInteger 优先级高于 Long）
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", 10L).put("b", new BigInteger("2")).build(),
                        new BigInteger("5")
                ),
                // a(Long) / b(Double) → OraDecimal（Double 优先级高于 Long）
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", 10L).put("b", 0.4836065573770491803278688524590163934426).build(),
                        new OraDecimal("20.67796610169491612341281241022698281464")
                ),
                // a(Long) / b(BigDecimal) → OraDecimal
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", 10L).put("b", new BigDecimal("4")).build(),
                        new OraDecimal("2.5")
                ),
                // a(Long) / b(OraDecimal) → OraDecimal（最高优先级）
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", 10L).put("b", new OraDecimal("4")).build(),
                        new OraDecimal("2.5")
                ),
                // a(Long) / b(String) → 抛出异常
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", 10L).put("b", "2").build(),
                        ExpressionRuntimeException.class
                ),
                // a(Long) / b(Instant) → 抛出异常
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", 10L).put("b", Instant.parse("2024-01-01T00:00:02Z")).build(),
                        ExpressionRuntimeException.class
                )
        );
    }

    @DisplayName("Long")
    @ParameterizedTest(name = "【{index}】{0}: vars={1}")
    @MethodSource("testLongProvider")
    public void testLong(String expression, Map<String, Object> vars, Object excepted) {
        if (excepted instanceof Class) {
            @SuppressWarnings("unchecked")
            Class<? extends Throwable> exceptionClass = (Class<? extends Throwable>) excepted;
            Assertions.assertThrows(exceptionClass, () -> aviator.execute(expression, vars));
        } else {
            Object actual = aviator.execute(expression, vars);
            Assertions.assertEquals(excepted, actual);
        }
    }

    // ========================= Integer =========================

    /**
     * a 固定为 Integer，b 依次为全部 8 种类型
     */
    static Stream<Arguments> testIntegerProvider() {
        return Stream.of(
                // a(Integer) / b(Long) → Long
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", 10).put("b", 2L).build(),
                        5L
                ),
                // a(Integer) / b(Integer) → Long（Aviator 将两个 Integer 统一存储为 Long）
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", 10).put("b", 2).build(),
                        5L
                ),
                // a(Integer) / b(BigInteger) → BigInteger
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", 10).put("b", new BigInteger("2")).build(),
                        new BigInteger("5")
                ),
                // a(Integer) / b(Double) → OraDecimal
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", 10).put("b", 0.4836065573770491803278688524590163934426).build(),
                        new OraDecimal("20.67796610169491612341281241022698281464")
                ),
                // a(Integer) / b(BigDecimal) → OraDecimal
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", 10).put("b", new BigDecimal("4")).build(),
                        new OraDecimal("2.5")
                ),
                // a(Integer) / b(OraDecimal) → OraDecimal
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", 10).put("b", new OraDecimal("4")).build(),
                        new OraDecimal("2.5")
                ),
                // a(Integer) / b(String) → 抛出异常
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", 10).put("b", "2").build(),
                        ExpressionRuntimeException.class
                ),
                // a(Integer) / b(Instant) → 抛出异常
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", 10).put("b", Instant.parse("2024-01-01T00:00:02Z")).build(),
                        ExpressionRuntimeException.class
                )
        );
    }

    @DisplayName("Integer")
    @ParameterizedTest(name = "【{index}】{0}: vars={1}")
    @MethodSource("testIntegerProvider")
    public void testInteger(String expression, Map<String, Object> vars, Object excepted) {
        if (excepted instanceof Class) {
            @SuppressWarnings("unchecked")
            Class<? extends Throwable> exceptionClass = (Class<? extends Throwable>) excepted;
            Assertions.assertThrows(exceptionClass, () -> aviator.execute(expression, vars));
        } else {
            Object actual = aviator.execute(expression, vars);
            Assertions.assertEquals(excepted, actual);
        }
    }

    // ========================= BigInteger =========================

    /**
     * a 固定为 BigInteger，b 依次为全部 8 种类型
     */
    static Stream<Arguments> testBigIntegerProvider() {
        return Stream.of(
                // a(BigInteger) / b(Long) → BigInteger
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", new BigInteger("10")).put("b", 2L).build(),
                        new BigInteger("5")
                ),
                // a(BigInteger) / b(Integer) → BigInteger
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", new BigInteger("10")).put("b", 2).build(),
                        new BigInteger("5")
                ),
                // a(BigInteger) / b(BigInteger) → BigInteger
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", new BigInteger("10")).put("b", new BigInteger("2")).build(),
                        new BigInteger("5")
                ),
                // a(BigInteger) / b(Double) → OraDecimal（Double 优先级高于 BigInteger）
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", new BigInteger("10")).put("b", 0.4836065573770491803278688524590163934426).build(),
                        new OraDecimal("20.67796610169491612341281241022698281464")
                ),
                // a(BigInteger) / b(BigDecimal) → OraDecimal
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", new BigInteger("10")).put("b", new BigDecimal("4")).build(),
                        new OraDecimal("2.5")
                ),
                // a(BigInteger) / b(OraDecimal) → OraDecimal
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", new BigInteger("10")).put("b", new OraDecimal("4")).build(),
                        new OraDecimal("2.5")
                ),
                // a(BigInteger) / b(String) → 抛出异常
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", new BigInteger("10")).put("b", "2").build(),
                        ExpressionRuntimeException.class
                ),
                // a(BigInteger) / b(Instant) → 抛出异常
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", new BigInteger("10")).put("b", Instant.parse("2024-01-01T00:00:02Z")).build(),
                        ExpressionRuntimeException.class
                )
        );
    }

    @DisplayName("BigInteger")
    @ParameterizedTest(name = "【{index}】{0}: vars={1}")
    @MethodSource("testBigIntegerProvider")
    public void testBigInteger(String expression, Map<String, Object> vars, Object excepted) {
        if (excepted instanceof Class) {
            @SuppressWarnings("unchecked")
            Class<? extends Throwable> exceptionClass = (Class<? extends Throwable>) excepted;
            Assertions.assertThrows(exceptionClass, () -> aviator.execute(expression, vars));
        } else {
            Object actual = aviator.execute(expression, vars);
            Assertions.assertEquals(excepted, actual);
        }
    }

    // ========================= Double =========================

    /**
     * a 固定为 Double，b 依次为全部 8 种类型
     */
    static Stream<Arguments> testDoubleProvider() {
        return Stream.of(
                // a(Double) / b(Long) → OraDecimal
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", 10.0d).put("b", 4L).build(),
                        new OraDecimal("2.5")
                ),
                // a(Double) / b(Integer) → OraDecimal
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", 10.0d).put("b", 4).build(),
                        new OraDecimal("2.5")
                ),
                // a(Double) / b(BigInteger) → OraDecimal
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", 10.0d).put("b", new BigInteger("4")).build(),
                        new OraDecimal("2.5")
                ),
                // a(Double) / b(Double) → OraDecimal
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", 10.0d).put("b", 0.4836065573770491803278688524590163934426).build(),
                        new OraDecimal("20.67796610169491612341281241022698281464")
                ),
                // a(Double) / b(BigDecimal) → OraDecimal（BigDecimal 优先级高于 Double）
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", 10.0d).put("b", new BigDecimal("4")).build(),
                        new OraDecimal("2.5")
                ),
                // a(Double) / b(OraDecimal) → OraDecimal
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", 10.0d).put("b", new OraDecimal("4")).build(),
                        new OraDecimal("2.5")
                ),
                // a(Double) / b(String) → 抛出异常
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", 10.0d).put("b", "4").build(),
                        ExpressionRuntimeException.class
                ),
                // a(Double) / b(Instant) → 抛出异常
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", 10.0d).put("b", Instant.parse("2024-01-01T00:00:02Z")).build(),
                        ExpressionRuntimeException.class
                )
        );
    }

    @DisplayName("Double")
    @ParameterizedTest(name = "【{index}】{0}: vars={1}")
    @MethodSource("testDoubleProvider")
    public void testDouble(String expression, Map<String, Object> vars, Object excepted) {
        if (excepted instanceof Class) {
            @SuppressWarnings("unchecked")
            Class<? extends Throwable> exceptionClass = (Class<? extends Throwable>) excepted;
            Assertions.assertThrows(exceptionClass, () -> aviator.execute(expression, vars));
        } else {
            Object actual = aviator.execute(expression, vars);
            Assertions.assertEquals(excepted, actual);
        }
    }

    // ========================= BigDecimal =========================

    /**
     * a 固定为 BigDecimal，b 依次为全部 8 种类型
     */
    static Stream<Arguments> testBigDecimalProvider() {
        return Stream.of(
                // a(BigDecimal) / b(Long) → OraDecimal
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", new BigDecimal("10")).put("b", 2L).build(),
                        new OraDecimal("5")
                ),
                // a(BigDecimal) / b(Integer) → OraDecimal
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", new BigDecimal("10")).put("b", 2).build(),
                        new OraDecimal("5")
                ),
                // a(BigDecimal) / b(BigInteger) → OraDecimal
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", new BigDecimal("10")).put("b", new BigInteger("2")).build(),
                        new OraDecimal("5")
                ),
                // a(BigDecimal) / b(Double) → OraDecimal
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", new BigDecimal("10")).put("b", 0.4836065573770491803278688524590163934426).build(),
                        new OraDecimal("20.67796610169491612341281241022698281464")
                ),
                // a(BigDecimal) / b(BigDecimal) → OraDecimal
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", new BigDecimal("10")).put("b", new BigDecimal("4")).build(),
                        new OraDecimal("2.5")
                ),
                // a(BigDecimal) / b(OraDecimal) → OraDecimal（OraDecimal 优先级最高）
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", new BigDecimal("10")).put("b", new OraDecimal("4")).build(),
                        new OraDecimal("2.5")
                ),
                // a(BigDecimal) / b(String) → 抛出异常
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", new BigDecimal("10")).put("b", "2").build(),
                        ExpressionRuntimeException.class
                ),
                // a(BigDecimal) / b(Instant) → 抛出异常
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", new BigDecimal("10")).put("b", Instant.parse("2024-01-01T00:00:02Z")).build(),
                        ExpressionRuntimeException.class
                )
        );
    }

    @DisplayName("BigDecimal")
    @ParameterizedTest(name = "【{index}】{0}: vars={1}")
    @MethodSource("testBigDecimalProvider")
    public void testBigDecimal(String expression, Map<String, Object> vars, Object excepted) {
        if (excepted instanceof Class) {
            @SuppressWarnings("unchecked")
            Class<? extends Throwable> exceptionClass = (Class<? extends Throwable>) excepted;
            Assertions.assertThrows(exceptionClass, () -> aviator.execute(expression, vars));
        } else {
            Object actual = aviator.execute(expression, vars);
            Assertions.assertEquals(excepted, actual);
        }
    }

    // ========================= OraDecimal =========================

    /**
     * a 固定为 OraDecimal（最高精度类型），b 依次为全部 8 种类型
     */
    static Stream<Arguments> testOraDecimalProvider() {
        return Stream.of(
                // a(OraDecimal) / b(Long) → OraDecimal
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", new OraDecimal("10")).put("b", 2L).build(),
                        new OraDecimal("5")
                ),
                // a(OraDecimal) / b(Integer) → OraDecimal
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", new OraDecimal("10")).put("b", 2).build(),
                        new OraDecimal("5")
                ),
                // a(OraDecimal) / b(BigInteger) → OraDecimal
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", new OraDecimal("10")).put("b", new BigInteger("2")).build(),
                        new OraDecimal("5")
                ),
                // a(OraDecimal) / b(Double) → OraDecimal
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", new OraDecimal("10")).put("b", 0.4836065573770491803278688524590163934426).build(),
                        new OraDecimal("20.67796610169491612341281241022698281464")
                ),
                // a(OraDecimal) / b(BigDecimal) → OraDecimal
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", new OraDecimal("10")).put("b", new BigDecimal("4")).build(),
                        new OraDecimal("2.5")
                ),
                // a(OraDecimal) / b(OraDecimal) → OraDecimal
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", new OraDecimal("10")).put("b", new OraDecimal("4")).build(),
                        new OraDecimal("2.5")
                ),
                // a(OraDecimal) / b(String) → 抛出异常
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", new OraDecimal("10")).put("b", "2").build(),
                        ExpressionRuntimeException.class
                ),
                // a(OraDecimal) / b(Instant) → 抛出异常
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", new OraDecimal("10")).put("b", Instant.parse("2024-01-01T00:00:02Z")).build(),
                        ExpressionRuntimeException.class
                )
        );
    }

    @DisplayName("OraDecimal")
    @ParameterizedTest(name = "【{index}】{0}: vars={1}")
    @MethodSource("testOraDecimalProvider")
    public void testOraDecimal(String expression, Map<String, Object> vars, Object excepted) {
        if (excepted instanceof Class) {
            @SuppressWarnings("unchecked")
            Class<? extends Throwable> exceptionClass = (Class<? extends Throwable>) excepted;
            Assertions.assertThrows(exceptionClass, () -> aviator.execute(expression, vars));
        } else {
            Object actual = aviator.execute(expression, vars);
            Assertions.assertEquals(excepted, actual);
        }
    }

    // ========================= String =========================

    /**
     * a 固定为 String，b 依次为全部 8 种类型。
     * Aviator 中 String 不支持 {@code /} 运算，任意组合均抛出 {@link ExpressionRuntimeException}。
     */
    static Stream<Arguments> testStringProvider() {
        return Stream.of(
                // a(String) / b(Long) → 抛出异常
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", "10").put("b", 2L).build(),
                        ExpressionRuntimeException.class
                ),
                // a(String) / b(Integer) → 抛出异常
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", "10").put("b", 2).build(),
                        ExpressionRuntimeException.class
                ),
                // a(String) / b(BigInteger) → 抛出异常
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", "10").put("b", new BigInteger("2")).build(),
                        ExpressionRuntimeException.class
                ),
                // a(String) / b(Double) → 抛出异常
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", "10").put("b", 0.4836065573770491803278688524590163934426).build(),
                        ExpressionRuntimeException.class
                ),
                // a(String) / b(BigDecimal) → 抛出异常
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", "10").put("b", new BigDecimal("4")).build(),
                        ExpressionRuntimeException.class
                ),
                // a(String) / b(OraDecimal) → 抛出异常
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", "10").put("b", new OraDecimal("4")).build(),
                        ExpressionRuntimeException.class
                ),
                // a(String) / b(String) → 抛出异常
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", "10").put("b", "2").build(),
                        ExpressionRuntimeException.class
                ),
                // a(String) / b(Instant) → 抛出异常
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", "10").put("b", Instant.parse("2024-01-01T00:00:02Z")).build(),
                        ExpressionRuntimeException.class
                )
        );
    }

    @DisplayName("String")
    @ParameterizedTest(name = "【{index}】{0}: vars={1}")
    @MethodSource("testStringProvider")
    public void testString(String expression, Map<String, Object> vars, Object excepted) {
        if (excepted instanceof Class) {
            @SuppressWarnings("unchecked")
            Class<? extends Throwable> exceptionClass = (Class<? extends Throwable>) excepted;
            Assertions.assertThrows(exceptionClass, () -> aviator.execute(expression, vars));
        } else {
            Object actual = aviator.execute(expression, vars);
            Assertions.assertEquals(excepted, actual);
        }
    }

    // ========================= Instant =========================

    /**
     * a 固定为 Instant，b 依次为全部 8 种类型。
     * Instant 无法参与任何算术运算，{@code /} 均触发 {@link ExpressionRuntimeException}。
     */
    static Stream<Arguments> testInstantProvider() {
        Instant aInstant = Instant.parse("2024-01-01T00:00:10Z");
        Instant bInstant = Instant.parse("2024-01-01T00:00:02Z");
        return Stream.of(
                // a(Instant) / b(Long) → 抛出异常
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", aInstant).put("b", 2L).build(),
                        ExpressionRuntimeException.class
                ),
                // a(Instant) / b(Integer) → 抛出异常
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", aInstant).put("b", 2).build(),
                        ExpressionRuntimeException.class
                ),
                // a(Instant) / b(BigInteger) → 抛出异常
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", aInstant).put("b", new BigInteger("2")).build(),
                        ExpressionRuntimeException.class
                ),
                // a(Instant) / b(Double) → 抛出异常
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", aInstant).put("b", 0.4836065573770491803278688524590163934426).build(),
                        ExpressionRuntimeException.class
                ),
                // a(Instant) / b(BigDecimal) → 抛出异常
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", aInstant).put("b", new BigDecimal("4")).build(),
                        ExpressionRuntimeException.class
                ),
                // a(Instant) / b(OraDecimal) → 抛出异常
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", aInstant).put("b", new OraDecimal("4")).build(),
                        ExpressionRuntimeException.class
                ),
                // a(Instant) / b(String) → 抛出异常
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", aInstant).put("b", "2").build(),
                        ExpressionRuntimeException.class
                ),
                // a(Instant) / b(Instant) → 抛出异常
                Arguments.of(
                        "a / b",
                        HashMapBuilder.<String, Object>builder().put("a", aInstant).put("b", bInstant).build(),
                        ExpressionRuntimeException.class
                )
        );
    }

    @DisplayName("Instant")
    @ParameterizedTest(name = "【{index}】{0}: vars={1}")
    @MethodSource("testInstantProvider")
    public void testInstant(String expression, Map<String, Object> vars, Object excepted) {
        if (excepted instanceof Class) {
            @SuppressWarnings("unchecked")
            Class<? extends Throwable> exceptionClass = (Class<? extends Throwable>) excepted;
            Assertions.assertThrows(exceptionClass, () -> aviator.execute(expression, vars));
        } else {
            Object actual = aviator.execute(expression, vars);
            Assertions.assertEquals(excepted, actual);
        }
    }
}
