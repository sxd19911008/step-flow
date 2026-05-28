package io.github.kentasun.stepflow.sfl.constants;

/**
 * SFL 词法单元类型枚举。
 */
public enum SflTokenType {

    /** 标点符号：括号、逗号、点号、等号及输入结束标记等 */
    SYMBOL,

    /** 语言保留关键字：SEQ、STEP、param、TRUE 等 */
    KEYWORD,

    /** 用户自定义字面量：stepCode、flowCode、映射键值中的表达式路径等 */
    LITERAL
}
