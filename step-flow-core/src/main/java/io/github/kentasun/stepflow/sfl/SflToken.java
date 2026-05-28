package io.github.kentasun.stepflow.sfl;

import io.github.kentasun.stepflow.sfl.constants.SflTokenType;

/**
 * 词法分析产出的不可变记号。
 * <p>
 * {@link #getType()} 仅表示语法角色（符号 / 关键字 / 用户字面量），
 * 具体符号或关键字字面量由 {@link #getText()} 承载。
 * 判断是否为某个特定符号或关键字，须同时校验 type 与 text，
 * 见 {@link #matches(SflTokenType, String)}。
 * </p>
 * <p>
 * {@link #getPosition()} 为记号在原始 SFL 串中的起始下标，
 * 用于构造带偏移的 {@link SflException}。
 * </p>
 */
public class SflToken {

    private final SflTokenType type;
    private final String text;
    private final int position;

    /**
     * 构造词法记号。
     *
     * @param type     语法角色，不可为 null
     * @param text     记号文本，不可为 null（EOF 使用空串）
     * @param position 源文本起始偏移
     */
    SflToken(SflTokenType type, String text, int position) {
        this.type = type;
        this.text = text;
        this.position = position;
    }

    public SflTokenType getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public int getPosition() {
        return position;
    }

    /**
     * 同时校验 type 与 text 是否与期望一致。
     * <p>
     * 符号、关键字等固定字面量的匹配均应通过本方法（或基于它的便捷方法）完成，
     * 避免仅用 type 或仅用 text 做片面判断。
     * </p>
     *
     * @param expectedType 期望的语法角色
     * @param expectedText 期望的文本字面量
     * @return {@code true} 表示 type 与 text 均匹配
     */
    public boolean matches(SflTokenType expectedType, String expectedText) {
        return this.type == expectedType && this.text.equals(expectedText);
    }

    /** @return {@code true} 表示当前记号为 {@link SflTokenType#SYMBOL} 且 text 等于 {@code symbolText} */
    public boolean isSymbol(String symbolText) {
        return matches(SflTokenType.SYMBOL, symbolText);
    }

    /** @return {@code true} 表示当前记号为 {@link SflTokenType#KEYWORD} 且 text 等于 {@code keywordText} */
    public boolean isKeyword(String keywordText) {
        return matches(SflTokenType.KEYWORD, keywordText);
    }

    /** @return {@code true} 表示当前记号为 {@link SflTokenType#LITERAL}（不校验具体文本） */
    public boolean isLiteral() {
        return this.type == SflTokenType.LITERAL;
    }
}
