package io.github.kentasun.stepflow.sfl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * {@link SflParser#parse(String)} 失败路径契约测试。
 */
class SflParserParseExceptionTest {

    /**
     * 单条失败用例：非法输入 + 期望的异常类型与消息。
     */
    private static final class ParseFailureCase {

        /** 用例标识，仅用于参数化测试展示名 */
        private final String id;
        /** 传入 {@link SflParser#parse} 的 SFL 文本 */
        private final String sflInput;
        /** 期望抛出的异常类型 */
        private final Class<? extends Throwable> expectedType;
        /** 期望的 {@link Throwable#getMessage()} 全文 */
        private final String expectedMessage;

        ParseFailureCase(
                String id,
                String sflInput,
                Class<? extends Throwable> expectedType,
                String expectedMessage) {
            this.id = id;
            this.sflInput = sflInput;
            this.expectedType = expectedType;
            this.expectedMessage = expectedMessage;
        }

        @Override
        public String toString() {
            return id;
        }
    }

    /** 词法阶段：字符串末尾反斜杠后无字符，触发「转义不完整」 */
    private static final String LEXER_ESCAPE_INCOMPLETE_EOF = "IF(AVIATOR(\"x" + '\\';

    static Stream<ParseFailureCase> parseFailureCases() {
        return Stream.of(
                // ----- parse 入口：空文本 -----
                c("empty_blank", "   ", "SFL 不能为空"),

                // ----- SflLexer：非法字符与双引号字符串 -----
                c("lexer_bad_char", "@STEP(a)", "无法识别的字符: '@'，位置: 第 1 行第 1 列（偏移 0）"),
                c("lexer_unclosed_string", "IF(AVIATOR(\"unclosed))THEN(STEP(a))ENDIF",
                        "字符串缺少结束双引号，位置: 第 1 行第 12 列（偏移 11）"),
                c("lexer_escape_incomplete_eof", LEXER_ESCAPE_INCOMPLETE_EOF,
                        "字符串转义不完整，位置: 第 1 行第 14 列（偏移 13）"),
                c("lexer_escape_invalid", "IF(AVIATOR(\"x\\a\"))THEN(STEP(a))ENDIF",
                        "字符串内仅支持转义双引号[\\\"]，实际为[\\a]，位置: 第 1 行第 14 列（偏移 13）"),
                c("lexer_escape_wrong_char", "IF(AVIATOR(\"x\\))THEN(STEP(a))ENDIF",
                        "字符串内仅支持转义双引号[\\\"]，实际为[\\)]，位置: 第 1 行第 14 列（偏移 13）"),

                // ----- parse 收尾：根后残留记号 -----
                c("trailing_content", "STEP(a)x",
                        "期望 SYMBOL []，实际为 LITERAL [x]，位置: 第 1 行第 8 列（偏移 7）"),

                // ----- keywordToFlow：根须为 flow 关键字 -----
                c("root_not_keyword", "(STEP(a))",
                        "期望 KEYWORD，实际为 SYMBOL [(]，位置: 第 1 行第 1 列（偏移 0）"),
                c("unknown_keyword", "THEN(STEP(a))",
                        "未知的关键字[THEN]，位置: 第 1 行第 1 列（偏移 0）"),

                // ----- parseFlowList：SEQ / PARALLEL 子列表约束 -----
                c("seq_empty_list", "SEQ()", "参数列表不能为空，位置: 第 1 行第 5 列（偏移 4）"),
                c("seq_trailing_comma", "SEQ(STEP(a),)", "参数列表末尾不允许有多余逗号，位置: 第 1 行第 13 列（偏移 12）"),
                c("parallel_empty_list", "PARALLEL()", "参数列表不能为空，位置: 第 1 行第 10 列（偏移 9）"),
                c("parallel_trailing_comma", "PARALLEL(STEP(a),)",
                        "参数列表末尾不允许有多余逗号，位置: 第 1 行第 18 列（偏移 17）"),
                c("seq_missing_rparen", "SEQ(STEP(a)",
                        "期望 SYMBOL [)]，实际为 SYMBOL，位置: 第 1 行第 12 列（偏移 11）"),

                // ----- StepFlowNodeBuilder -----
                c("step_no_paren", "STEP", "期望 SYMBOL [(]，实际为 SYMBOL，位置: 第 1 行第 5 列（偏移 4）"),
                c("step_missing_rparen", "STEP(a", "期望 SYMBOL [)]，实际为 SYMBOL，位置: 第 1 行第 7 列（偏移 6）"),
                c("step_empty_code", "STEP()", "期望 LITERAL，实际为 SYMBOL [)]，位置: 第 1 行第 6 列（偏移 5）"),
                c("step_dup_param", "STEP(a).PARAM(x=y).PARAM(z=w)",
                        "STEP 不允许重复声明 .PARAM(...)，位置: 第 1 行第 20 列（偏移 19）"),
                c("step_dup_result", "STEP(a).result(x=y).result(z=w)",
                        "STEP 不允许重复声明 .result(...)，位置: 第 1 行第 21 列（偏移 20）"),
                c("step_unknown_suffix", "STEP(a).THEN(x=y)",
                        "STEP 后缀未知 [THEN]，仅支持 PARAM / result，位置: 第 1 行第 9 列（偏移 8）"),
                c("step_param_trailing_comma", "STEP(a).PARAM(x=y,)",
                        "PARAM 映射列表末尾不允许有多余逗号，位置: 第 1 行第 19 列（偏移 18）"),
                c("step_param_dup_key", "STEP(a).PARAM(x=y,x=z)", "PARAM 映射键重复: x，位置: 第 1 行第 19 列（偏移 18）"),
                c("step_result_trailing_comma", "STEP(a).result(x=y,)",
                        "result 映射列表末尾不允许有多余逗号，位置: 第 1 行第 20 列（偏移 19）"),
                c("step_result_dup_key", "STEP(a).result(x=y,x=z)", "result 映射键重复: x，位置: 第 1 行第 20 列（偏移 19）"),

                // ----- SubFlowFlowNodeBuilder -----
                c("subflow_not_literal", "SUB_FLOW(STEP)",
                        "期望 LITERAL，实际为 KEYWORD [STEP]，位置: 第 1 行第 10 列（偏移 9）"),
                c("subflow_empty_code", "SUB_FLOW()",
                        "期望 LITERAL，实际为 SYMBOL [)]，位置: 第 1 行第 10 列（偏移 9）"),

                // ----- IfFlowNodeBuilder -----
                c("if_missing_endif", "IF(STEP(a))THEN(STEP(b))",
                        "期望 KEYWORD [ENDIF]，实际为 SYMBOL，位置: 第 1 行第 25 列（偏移 24）"),
                c("if_invalid_condition_seq", "IF(SEQ(STEP(a)))THEN(STEP(b))ENDIF",
                        "IF 的条件必须是 STEP(...) 或 TYPE(\"expression\")，位置: 第 1 行第 1 列（偏移 0）"),
                c("if_invalid_condition_reserved_kw", "IF(THEN)THEN(STEP(a))ENDIF",
                        "IF 的条件必须是 STEP(...) 或 TYPE(\"expression\")，位置: 第 1 行第 1 列（偏移 0）"),
                c("if_missing_then", "IF(STEP(a))STEP(b)ENDIF",
                        "期望 KEYWORD [THEN]，实际为 KEYWORD [STEP]，位置: 第 1 行第 12 列（偏移 11）"),
                c("if_inline_not_quoted", "IF(AVIATOR(expr))THEN(STEP(a))ENDIF",
                        "期望 QUOTED_STRING，实际为 LITERAL [expr]，位置: 第 1 行第 12 列（偏移 11）"),
                c("if_missing_lparen_after_kw", "IF STEP(a))THEN(STEP(b))ENDIF",
                        "期望 SYMBOL [(]，实际为 KEYWORD [STEP]，位置: 第 1 行第 4 列（偏移 3）"),
                c("if_missing_rparen_condition", "IF(STEP(a)THEN(STEP(b))ENDIF",
                        "期望 SYMBOL [)]，实际为 KEYWORD [THEN]，位置: 第 1 行第 11 列（偏移 10）"),
                c("if_then_empty_seq", "IF(STEP(a))THEN(SEQ())ENDIF", "参数列表不能为空，位置: 第 1 行第 21 列（偏移 20）"),
                c("if_elsif_missing_then", "IF(STEP(a))THEN(STEP(b))ELSIF(STEP(c))ENDIF",
                        "期望 KEYWORD [THEN]，实际为 KEYWORD [ENDIF]，位置: 第 1 行第 39 列（偏移 38）"),
                // THEN(...) 内仅允许单个子 flow，逗号导致期望右括号时遇到逗号
                c("if_then_unexpected_comma", "IF(STEP(a))THEN(STEP(b),)ENDIF",
                        "期望 SYMBOL [)]，实际为 SYMBOL [,]，位置: 第 1 行第 24 列（偏移 23）"),
                // THEN 内嵌 SEQ 时走 parseFlowList，触发列表尾随逗号校验
                c("if_then_seq_trailing_comma", "IF(STEP(a))THEN(SEQ(STEP(b),))ENDIF",
                        "参数列表末尾不允许有多余逗号，位置: 第 1 行第 29 列（偏移 28）")
        );
    }

    /** 简化构造：默认期望 {@link SflException} */
    private static ParseFailureCase c(String id, String sflInput, String expectedMessage) {
        return new ParseFailureCase(id, sflInput, SflException.class, expectedMessage);
    }

    /**
     * {@code null} 无法放入参数化 {@link MethodSource} 的输入流，单独断言
     * {@link SflParser#parse(String)} 入口处的空值校验。
     */
    @Test
    void parse_nullInput_shouldThrowEmptyMessage() {
        SflException ex = assertThrows(SflException.class, () -> SflParser.parse(null));
        assertEquals(SflException.class, ex.getClass());
        assertEquals("SFL 不能为空", ex.getMessage());
    }

    /**
     * 对每条非法 SFL 用例：调用 {@link SflParser#parse}，断言异常类型与消息全文
     * 与事先运行采集结果一致（消息中含行/列/偏移的用例，输入串须固定不变）。
     */
    @ParameterizedTest(name = "{0}")
    @MethodSource("parseFailureCases")
    void parse_invalidSfl_shouldThrowDocumentedError(ParseFailureCase failureCase) {
        Throwable thrown = assertThrows(
                failureCase.expectedType,
                () -> SflParser.parse(failureCase.sflInput)
        );
        assertEquals(
                failureCase.expectedMessage,
                thrown.getMessage(),
                () -> "用例 [" + failureCase.id + "] 异常消息与采集时不一致"
        );
    }
}
