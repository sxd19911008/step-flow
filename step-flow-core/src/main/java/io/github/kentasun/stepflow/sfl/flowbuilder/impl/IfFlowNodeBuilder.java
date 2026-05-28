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
 * IF 关键字解析策略：解析 PL/SQL 风格块
 * {@code IF(条件) THEN(...) [ELSIF(条件) THEN(...)]* [ELSE(...)] ENDIF}，
 * 并构造 {@link IfElseFlowNode}。
 * <p>
 * 语义约束：
 * <ul>
 *   <li>IF / ELSIF 括号内条件必须是 {@link StepFlowNode}。</li>
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
        branches.add(parseConditionThenPair(parser, keywordPos));

        // 零个或多个 ELSIF(条件) THEN(体)
        while (parser.nextTokenIsKeyword(SlfKeyWords.ELSIF)) {
            parser.consumeKeyword(SlfKeyWords.ELSIF);
            branches.add(parseElsifConditionThen(parser));
        }

        // 可选 ELSE(体)
        FlowNode elseFlowNode = null;
        if (parser.nextTokenIsKeyword(SlfKeyWords.ELSE)) {
            parser.consumeKeyword(SlfKeyWords.ELSE);
            elseFlowNode = parseParenWrappedFlow(parser);
        }

        parser.consumeKeyword(SlfKeyWords.ENDIF);

        return new IfElseFlowNode(FlowContentType.IF_ELSE, branches, elseFlowNode);
    }

    /**
     * 解析 {@code IF(条件)} 后紧跟的 {@code THEN(体)}，返回一条分支。
     */
    private IfBranch parseConditionThenPair(SflParser parser, int keywordPos) {
        parser.consumeSymbol(SlfKeyWords.LPAREN_TEXT);
        StepFlowNode condition = parseConditionStep(parser, SlfKeyWords.IF, keywordPos);
        parser.consumeSymbol(SlfKeyWords.RPAREN_TEXT);
        FlowNode thenFlowNode = parseThenBlock(parser);
        return new IfBranch(condition, thenFlowNode);
    }

    /**
     * 解析 {@code ELSIF(条件)} 后紧跟的 {@code THEN(体)}（ELSIF 关键字已由调用方消费）。
     */
    private IfBranch parseElsifConditionThen(SflParser parser) {
        parser.consumeSymbol(SlfKeyWords.LPAREN_TEXT);
        SflToken elsifToken = parser.peek();
        StepFlowNode condition = parseConditionStep(parser, SlfKeyWords.ELSIF, elsifToken.getPosition());
        parser.consumeSymbol(SlfKeyWords.RPAREN_TEXT);
        FlowNode thenFlowNode = parseThenBlock(parser);
        return new IfBranch(condition, thenFlowNode);
    }

    /**
     * 解析 {@code THEN(...)}：消费 THEN 关键字与括号内子 flow。
     */
    private FlowNode parseThenBlock(SflParser parser) {
        if (!parser.nextTokenIsKeyword(SlfKeyWords.THEN)) {
            throw new SflException(
                    "IF 块在条件之后缺少 " + SlfKeyWords.THEN + "(...)，位置: "
                            + parser.peek().getPosition());
        }
        parser.consumeKeyword(SlfKeyWords.THEN);
        return parseParenWrappedFlow(parser);
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
     * 解析 IF / ELSIF 括号内的条件，必须为 STEP 节点。
     */
    private StepFlowNode parseConditionStep(SflParser parser, String blockKeyword, int position) {
        FlowNode conditionNode = parser.keywordToFlow();
        if (!(conditionNode instanceof StepFlowNode)) {
            throw new SflException(
                    blockKeyword + " 的条件必须是 " + SlfKeyWords.STEP + "(...)，实际为 ["
                            + conditionNode.getType() + "]，位置: " + position);
        }
        return (StepFlowNode) conditionNode;
    }
}
