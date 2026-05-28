package io.github.kentasun.stepflow.sfl.flowbuilder.impl;

import io.github.kentasun.stepflow.flow.constants.FlowContentType;
import io.github.kentasun.stepflow.flow.dto.node.FlowNode;
import io.github.kentasun.stepflow.flow.dto.node.IfBranch;
import io.github.kentasun.stepflow.flow.dto.node.IfElseFlowNode;
import io.github.kentasun.stepflow.flow.dto.node.StepFlowNode;
import io.github.kentasun.stepflow.sfl.SflException;
import io.github.kentasun.stepflow.sfl.SflParser;
import io.github.kentasun.stepflow.sfl.SflToken;
import io.github.kentasun.stepflow.sfl.constants.SlfKeyWords;
import io.github.kentasun.stepflow.sfl.flowbuilder.FlowNodeBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * IF 关键字解析策略
 * <p>{@code IF(条件) THEN(...) [ELSIF(条件) THEN(...)]* [ELSE(...)] ENDIF}，
 * 并构造 {@link IfElseFlowNode}。</p>
 * <p>
 * 语义约束：
 * <ul>
 *   <li>IF / ELSIF 括号内条件为 {@link StepFlowNode}（{@code STEP(...)}）
 *       或内联表达式 {@code TYPE("expression")}，TYPE 为 StepContentType。</li>
 *   <li>表达式正文须用英文双引号包裹，内部双引号写作 {@code \"}。</li>
 *   <li>每个 IF 或 ELSIF 后必须有且仅有一个 {@code THEN(...)}。</li>
 *   <li>{@code ELSE(...)} 至多一次，位于所有 ELSIF 段之后、ENDIF 之前。</li>
 *   <li>{@code ENDIF} 必填，结束当前 IF 块。</li>
 * </ul>
 * </p>
 */
public class IfFlowNodeBuilder implements FlowNodeBuilder {

    @Override
    public FlowNode parse(SflParser parser, int keywordPos) {
        List<IfBranch> branches = new ArrayList<>();

        // 首段：IF(条件) THEN(体)
        branches.add(this.parseIfConditionThenPair(parser, keywordPos));

        // 零个或多个 ELSIF(条件) THEN(体)
        while (parser.tryConsumeKeyword(SlfKeyWords.ELSIF)) {
            branches.add(this.parseElsifConditionThen(parser));
        }

        // 可选 ELSE(体)
        FlowNode elseFlowNode = null;
        if (parser.tryConsumeKeyword(SlfKeyWords.ELSE)) {
            elseFlowNode = this.parseParenWrappedFlow(parser);
        }

        parser.consumeKeyword(SlfKeyWords.ENDIF);

        return new IfElseFlowNode(FlowContentType.IF_ELSE, branches, elseFlowNode);
    }

    /**
     * 解析 {@code IF(条件)} 后紧跟的 {@code THEN(体)}，返回一条分支。
     */
    private IfBranch parseIfConditionThenPair(SflParser parser, int keywordPos) {
        parser.consumeSymbol(SlfKeyWords.LPAREN_TEXT);
        ConditionParts conditionParts = this.parseCondition(parser, SlfKeyWords.IF, keywordPos);
        parser.consumeSymbol(SlfKeyWords.RPAREN_TEXT);
        FlowNode thenFlowNode = this.parseThenBlock(parser);
        return conditionParts.toIfBranch(thenFlowNode);
    }

    /**
     * 解析 {@code ELSIF(条件)} 后紧跟的 {@code THEN(体)}（ELSIF 关键字已由调用方消费）。
     */
    private IfBranch parseElsifConditionThen(SflParser parser) {
        parser.consumeSymbol(SlfKeyWords.LPAREN_TEXT);
        SflToken elsifToken = parser.peek();
        ConditionParts conditionParts = this.parseCondition(parser, SlfKeyWords.ELSIF, elsifToken.getPosition());
        parser.consumeSymbol(SlfKeyWords.RPAREN_TEXT);
        FlowNode thenFlowNode = this.parseThenBlock(parser);
        return conditionParts.toIfBranch(thenFlowNode);
    }

    /**
     * 解析 {@code THEN(...)}：消费 THEN 关键字与括号内子 flow。
     */
    private FlowNode parseThenBlock(SflParser parser) {
        parser.consumeKeyword(
                SlfKeyWords.THEN,
                String.format("IF 块在条件之后缺少 %s(...)，位置: %s",
                        SlfKeyWords.THEN,
                        parser.peek().getPosition())
        );
        return this.parseParenWrappedFlow(parser);
    }

    /**
     * 解析 {@code 关键字(...)} 括号内的单个子 flow（用于 THEN / ELSE）。
     */
    private FlowNode parseParenWrappedFlow(SflParser parser) {
        parser.consumeSymbol(SlfKeyWords.LPAREN_TEXT);
        FlowNode flowNode = parser.keywordToFlow();
        parser.consumeSymbol(SlfKeyWords.RPAREN_TEXT);
        return flowNode;
    }

    /**
     * 解析 IF / ELSIF 括号内的条件：{@code STEP(...)} 或 {@code TYPE("expression")}。
     */
    private ConditionParts parseCondition(SflParser parser, String blockKeyword, int position) {
        if (parser.nextTokenIsKeyword(SlfKeyWords.STEP)) {
            FlowNode conditionNode = parser.keywordToFlow();
            if (!(conditionNode instanceof StepFlowNode)) {
                throw new SflException(
                        blockKeyword + " 的条件必须是 " + SlfKeyWords.STEP + "(...)，实际为 ["
                                + conditionNode.getType() + "]，位置: " + position);
            }
            return ConditionParts.step((StepFlowNode) conditionNode);
        } else if (parser.nextTokenIsLiteral()) {
            return this.parseInlineExpressionCondition(parser, blockKeyword, position);
        }
        throw new SflException(
                blockKeyword + " 的条件必须是 " + SlfKeyWords.STEP
                        + "(...) 或 TYPE(\"expression\")，位置: " + position);
    }

    /**
     * 解析 {@code TYPE("expression")} 形式的内联表达式条件。
     */
    private ConditionParts parseInlineExpressionCondition(SflParser parser, String blockKeyword, int position) {
        String expressionType = parser.consumeLiteral().getText();
        parser.consumeSymbol(
                SlfKeyWords.LPAREN_TEXT,
                String.format(
                        "%s 的内联表达式 %s 后缺少 '('，位置: %s",
                        blockKeyword,
                        expressionType,
                        position
                )
        );
        SflToken expressionToken = parser.consumeQuotedString(String.format(
                "%s 的内联表达式 %s 的参数须为双引号字符串，位置: %s",
                blockKeyword,
                expressionType,
                parser.peek().getPosition()
        ));
        String expression = expressionToken.getText();
        parser.consumeSymbol(
                SlfKeyWords.RPAREN_TEXT,
                String.format(
                        "%s 的内联表达式 %s 缺少 ')'，位置: %s",
                        blockKeyword,
                        expressionType,
                        parser.peek().getPosition()
                )
        );
        return ConditionParts.expression(expressionType, expression);
    }

    /**
     * IF / ELSIF 条件解析中间结果，便于在消费 THEN 体后再组装 {@link IfBranch}。
     */
    private static final class ConditionParts {

        /** STEP 条件节点；与 expression 字段互斥 */
        private final StepFlowNode stepCondition;
        /** 内联表达式类型（StepContentType） */
        private final String expressionType;
        /** 内联表达式正文 */
        private final String expression;

        private ConditionParts(StepFlowNode stepCondition, String expressionType, String expression) {
            this.stepCondition = stepCondition;
            this.expressionType = expressionType;
            this.expression = expression;
        }

        static ConditionParts step(StepFlowNode stepCondition) {
            return new ConditionParts(stepCondition, null, null);
        }

        static ConditionParts expression(String expressionType, String expression) {
            return new ConditionParts(null, expressionType, expression);
        }

        IfBranch toIfBranch(FlowNode thenFlowNode) {
            if (stepCondition != null) {
                return new IfBranch(stepCondition, thenFlowNode);
            }
            return new IfBranch(expressionType, expression, thenFlowNode);
        }
    }
}
