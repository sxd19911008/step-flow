package io.github.kentasun.stepflow.sfl;

import io.github.kentasun.stepflow.sfl.constants.SflTokenType;
import io.github.kentasun.stepflow.sfl.constants.SlfKeyWords;

/**
 * SFL 词法分析器：将编排文本切分为 {@link SflToken} 流。
 * <p>空白符在记号边界处跳过，不参与 Token 产出，从而允许在关键字与括号间自由换行，降低存储时的格式化约束。</p>
 */
public class SflLexer {

    private final String text;
    private int pos;
    private SflToken nextToken;
    // TODO 记录 行数、列数，用于打印报错日志

    /**
     * 绑定完整 SFL 源文本并预读第一个记号。
     *
     * @param sflText slf 字符串
     */
    public SflLexer(String sflText) {
        this.text = sflText;
        this.pos = 0;
        this.nextToken = this.nextToken();
    }

    /**
     * 查看当前记号但不前进游标，供语法分析做分支判断。
     *
     * @return 下一个待消费的记号
     */
    public SflToken peek() {
        return this.nextToken;
    }

    /**
     * 取出当前记号并将前瞻推进到后继记号。
     *
     * @return 本次消费掉的记号
     */
    public SflToken consume() {
        SflToken current = this.nextToken;
        this.nextToken = this.nextToken();
        return current;
    }

    /**
     * 从当前 {@link #pos} 扫描并生成下一个记号。
     * <p>
     * 遇非法字符立即失败，避免将脏数据传入语法层产生误导性「期望 RPAREN」类消息。
     * </p>
     *
     * @return 新记号，输入耗尽时返回 type={@link SflTokenType#SYMBOL}、text 为空串的 EOF 记号
     */
    private SflToken nextToken() {
        this.skipWhitespace();
        if (this.pos >= this.text.length()) {
            return new SflToken(SflTokenType.SYMBOL, SlfKeyWords.EOF_TEXT, this.pos);
        }
        char c = this.text.charAt(this.pos);
        int start = this.pos;
        switch (c) {
            case SlfKeyWords.CHAR_LPAREN:
                this.pos++;
                return symbolToken(SlfKeyWords.LPAREN, start);
            case SlfKeyWords.CHAR_RPAREN:
                this.pos++;
                return symbolToken(SlfKeyWords.RPAREN, start);
            case SlfKeyWords.CHAR_COMMA:
                this.pos++;
                return symbolToken(SlfKeyWords.COMMA, start);
            case SlfKeyWords.CHAR_DOT:
                this.pos++;
                return symbolToken(SlfKeyWords.DOT, start);
            case SlfKeyWords.CHAR_EQ:
                this.pos++;
                return symbolToken(SlfKeyWords.EQ, start);
            case SlfKeyWords.CHAR_DOUBLE_QUOTE:
                return this.readQuotedString(start);
            default:
                if (isIdentStart(c)) {
                    return this.readWord(start);
                }
                throw new SflException(String.format("无法识别的字符: '%s'，位置: %s", c, start));
        }
    }

    /**
     * 构造符号类记号。
     */
    private static SflToken symbolToken(String symbolText, int position) {
        return new SflToken(SflTokenType.SYMBOL, symbolText, position);
    }

    /**
     * 读取标识符体：字母或下划线开头，后续允许字母、数字、下划线及点号。
     * <p>
     * 点号纳入标识符体是为了将 {@code dto.num1} 作为单一 {@link SflTokenType#LITERAL} 交给
     * 语法层，映射值侧无需再拆路径表达式，与引擎按字符串键解析上下文的行为一致。
     * </p>
     * <p>
     * 若文本命中 {@link SlfKeyWords#isKeywordText(String)} 则产出 {@link SflTokenType#KEYWORD}，
     * 否则产出 {@link SflTokenType#LITERAL}。
     * </p>
     *
     * @param start 标识符在源文本中的起始下标
     * @return KEYWORD 或 LITERAL 记号
     */
    private SflToken readWord(int start) {
        do {
            this.pos++;
        } while (this.pos < this.text.length() && isIdentPart(this.text.charAt(this.pos)));
        String word = this.text.substring(start, this.pos);
        SflTokenType type = SlfKeyWords.isKeywordText(word)
                ? SflTokenType.KEYWORD
                : SflTokenType.LITERAL;
        return new SflToken(type, word, start);
    }

    /**
     * 读取双引号字符串：{@code "..."}，内部双引号须写作 {@code \"}，其它字符按字面保留。
     * <p>
     * 产出 {@link SflTokenType#QUOTED_STRING}，{@link SflToken#getText()} 为去掉转义后的正文。
     * </p>
     *
     * @param start 起始双引号在源文本中的下标
     * @return 字符串记号
     */
    private SflToken readQuotedString(int start) {
        this.pos++; // 跳过起始 "
        StringBuilder sb = new StringBuilder();
        while (this.pos < this.text.length()) {
            char c = this.text.charAt(this.pos);
            if (c == SlfKeyWords.CHAR_BACKSLASH) {
                this.pos++;
                if (this.pos >= this.text.length()) {
                    throw new SflException(String.format("字符串转义不完整，位置: %s", this.pos - 1));
                }
                char escaped = this.text.charAt(this.pos);
                if (escaped != SlfKeyWords.CHAR_DOUBLE_QUOTE) {
                    throw new SflException(String.format(
                            "字符串内仅支持转义双引号（\\\"），实际为 '\\%s'，位置: %s",
                            escaped,
                            this.pos - 1
                    ));
                }
                sb.append(SlfKeyWords.CHAR_DOUBLE_QUOTE);
                this.pos++;
                continue;
            }
            if (c == SlfKeyWords.CHAR_DOUBLE_QUOTE) {
                this.pos++; // 跳过结束 "
                return new SflToken(SflTokenType.QUOTED_STRING, sb.toString(), start);
            }
            sb.append(c);
            this.pos++;
        }
        throw new SflException(String.format("字符串缺少结束双引号，位置: %s", start));
    }

    /**
     * 跳过空格、制表符及换行，不生成空白类 Token。
     */
    private void skipWhitespace() {
        while (this.pos < this.text.length()) {
            char c = this.text.charAt(this.pos);
            if (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
                this.pos++;
            } else {
                break;
            }
        }
    }

    private static boolean isIdentStart(char c) {
        return Character.isLetter(c) || c == '_';
    }

    private static boolean isIdentPart(char c) {
        return Character.isLetterOrDigit(c) || c == '_' || c == '.';
    }
}
