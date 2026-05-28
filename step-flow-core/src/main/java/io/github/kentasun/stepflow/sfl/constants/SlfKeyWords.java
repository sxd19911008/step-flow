package io.github.kentasun.stepflow.sfl.constants;

import io.github.kentasun.stepflow.sfl.SflLexer;
import io.github.kentasun.stepflow.sfl.SflParser;
import io.github.kentasun.stepflow.sfl.flowbuilder.FlowNodeBuilder;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * SFL（Step Flow Language）语法关键字与符号字面量常量集。
 * <p>
 * 取值与源文本中的标识符或符号完全一致，供 {@link SflLexer}、{@link SflParser}
 * 注册表及各 {@link FlowNodeBuilder} 实现统一引用，避免魔法字符串分散在解析逻辑中。
 * </p>
 */
public final class SlfKeyWords {

    private SlfKeyWords() {
        // 工具类，禁止实例化
    }

    // -------------------------------------------------------------------------
    // 符号（char 供词法器单字符扫描，String 供 type+text 匹配）
    // -------------------------------------------------------------------------

    public static final char LPAREN = '(';
    public static final String LPAREN_TEXT = String.valueOf(LPAREN);

    public static final char RPAREN = ')';
    public static final String RPAREN_TEXT = String.valueOf(RPAREN);

    public static final char COMMA = ',';
    public static final String COMMA_TEXT = String.valueOf(COMMA);

    public static final char DOT = '.';
    public static final String DOT_TEXT = String.valueOf(DOT);

    public static final char EQ = '=';
    public static final String EQ_TEXT = String.valueOf(EQ);

    /** 输入结束标记的文本 */
    public static final String EOF_TEXT = "";

    // -------------------------------------------------------------------------
    // 顶层编排关键字（flow 产生式入口）
    // -------------------------------------------------------------------------

    /** 顺序执行容器：{@code SEQ(child, child, ...)} */
    public static final String SEQ = "SEQ";

    /** 并行执行容器：{@code PARALLEL(child, child, ...)} */
    public static final String PARALLEL = "PARALLEL";

    /** 单步执行：{@code STEP(stepCode)[.param(...)][.result(...)]} */
    public static final String STEP = "STEP";

    /** 子流程引用：{@code SUB_FLOW(flowCode)} */
    public static final String SUB_FLOW = "SUB_FLOW";

    /** 条件分支：{@code IF(条件).TRUE(真分支)[.FALSE(假分支)]} */
    public static final String IF = "IF";

    // -------------------------------------------------------------------------
    // IF 后缀分支标识符
    // -------------------------------------------------------------------------

    /** IF 真分支后缀：{@code .TRUE(...)}，必填 */
    public static final String IF_TRUE = "TRUE";

    /** IF 假分支后缀：{@code .FALSE(...)}，可省略 */
    public static final String IF_FALSE = "FALSE";

    // -------------------------------------------------------------------------
    // STEP 后缀标识符
    // -------------------------------------------------------------------------

    /** STEP 入参映射后缀：{@code .param(k=v,...)} */
    public static final String STEP_PARAM = "param";

    /** STEP 出参映射后缀：{@code .result(k=v,...)} */
    public static final String STEP_RESULT = "result";

    // -------------------------------------------------------------------------
    // 关键字集合（供词法器区分 KEYWORD 与 LITERAL）
    // -------------------------------------------------------------------------

    private static final Set<String> KEYWORD_TEXTS;

    static {
        Set<String> set = new HashSet<>();
        set.add(SEQ);
        set.add(PARALLEL);
        set.add(STEP);
        set.add(SUB_FLOW);
        set.add(IF);
        set.add(IF_TRUE);
        set.add(IF_FALSE);
        set.add(STEP_PARAM);
        set.add(STEP_RESULT);
        KEYWORD_TEXTS = Collections.unmodifiableSet(set);
    }

    /**
     * 判断给定文本是否为 SFL 保留关键字。
     *
     * @param text 词法器读出的标识符文本
     * @return {@code true} 表示应产出 {@link SflTokenType#KEYWORD}
     */
    public static boolean isKeywordText(String text) {
        return KEYWORD_TEXTS.contains(text);
    }
}
