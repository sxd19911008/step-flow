package io.github.kentasun.stepflow.flow;

import io.github.kentasun.stepflow.api.dto.StepFlowContext;
import io.github.kentasun.stepflow.dto.ExecutorsContext;
import io.github.kentasun.stepflow.flow.dto.node.FlowNode;
import io.github.kentasun.stepflow.utils.StepFlowUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Flow {

    // 流程标识
    private String flowCode;

    // 流程名称
    private String flowName;

    // 流程类型
    private String flowType;

    // 流程正文
    private FlowNode content;

    // 返回字段列表，多个返回字段配置在这里，否则为空
    private List<String> returnFieldList;

    public Flow(String flowCode, String flowName, String flowType, FlowNode content, List<String> returnFieldList) {
        this.flowCode = flowCode;
        this.flowName = flowName;
        this.flowType = flowType;
        this.content = content;
        this.returnFieldList = returnFieldList;
    }

    public Flow() {
    }

    public static FlowBuilder builder() {
        return new FlowBuilder();
    }

    /**
     * 执行流程
     *
     * @param stepFlowContext  步骤上下文，用于传递
     * @param executorsContext 执行器上下文
     * @return 返回 returnFieldList 指定的参数
     */
    public Map<String, Object> execute(StepFlowContext stepFlowContext, ExecutorsContext executorsContext) {
        /* 执行 flow */
        content.execute(stepFlowContext, executorsContext);

        /* flow 如果配置了返回值，则直接从上下文中获取 */
        if (StepFlowUtils.isNotEmpty(returnFieldList)) {
            Map<String, Object> returnMap = new HashMap<>();
            for (String key : returnFieldList) {
                Object value = stepFlowContext.get(key);
                returnMap.put(key, value);
            }
            return returnMap;
        }

        /* 默认无返回值 */
        return null;
    }

    public String getFlowCode() {
        return this.flowCode;
    }

    public String getFlowName() {
        return this.flowName;
    }

    public String getFlowType() {
        return this.flowType;
    }

    public FlowNode getContent() {
        return this.content;
    }

    public List<String> getReturnFieldList() {
        return this.returnFieldList;
    }

    public void setFlowCode(String flowCode) {
        this.flowCode = flowCode;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    public void setFlowType(String flowType) {
        this.flowType = flowType;
    }

    public void setContent(FlowNode content) {
        this.content = content;
    }

    public void setReturnFieldList(List<String> returnFieldList) {
        this.returnFieldList = returnFieldList;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Flow)) return false;
        final Flow other = (Flow) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$flowCode = this.getFlowCode();
        final Object other$flowCode = other.getFlowCode();
        if (this$flowCode == null ? other$flowCode != null : !this$flowCode.equals(other$flowCode)) return false;
        final Object this$flowName = this.getFlowName();
        final Object other$flowName = other.getFlowName();
        if (this$flowName == null ? other$flowName != null : !this$flowName.equals(other$flowName)) return false;
        final Object this$flowType = this.getFlowType();
        final Object other$flowType = other.getFlowType();
        if (this$flowType == null ? other$flowType != null : !this$flowType.equals(other$flowType)) return false;
        final Object this$content = this.getContent();
        final Object other$content = other.getContent();
        if (this$content == null ? other$content != null : !this$content.equals(other$content)) return false;
        final Object this$returnFieldList = this.getReturnFieldList();
        final Object other$returnFieldList = other.getReturnFieldList();
        if (this$returnFieldList == null ? other$returnFieldList != null : !this$returnFieldList.equals(other$returnFieldList))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Flow;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $flowCode = this.getFlowCode();
        result = result * PRIME + ($flowCode == null ? 43 : $flowCode.hashCode());
        final Object $flowName = this.getFlowName();
        result = result * PRIME + ($flowName == null ? 43 : $flowName.hashCode());
        final Object $flowType = this.getFlowType();
        result = result * PRIME + ($flowType == null ? 43 : $flowType.hashCode());
        final Object $content = this.getContent();
        result = result * PRIME + ($content == null ? 43 : $content.hashCode());
        final Object $returnFieldList = this.getReturnFieldList();
        result = result * PRIME + ($returnFieldList == null ? 43 : $returnFieldList.hashCode());
        return result;
    }

    public String toString() {
        return "Flow(flowCode=" + this.getFlowCode() + ", flowName=" + this.getFlowName() + ", flowType=" + this.getFlowType() + ", content=" + this.getContent() + ", returnFieldList=" + this.getReturnFieldList() + ")";
    }

    public static class FlowBuilder {
        private String flowCode;
        private String flowName;
        private String flowType;
        private FlowNode content;
        private List<String> returnFieldList;

        FlowBuilder() {
        }

        public FlowBuilder flowCode(String flowCode) {
            this.flowCode = flowCode;
            return this;
        }

        public FlowBuilder flowName(String flowName) {
            this.flowName = flowName;
            return this;
        }

        public FlowBuilder flowType(String flowType) {
            this.flowType = flowType;
            return this;
        }

        public FlowBuilder content(FlowNode content) {
            this.content = content;
            return this;
        }

        public FlowBuilder returnFieldList(List<String> returnFieldList) {
            this.returnFieldList = returnFieldList;
            return this;
        }

        public Flow build() {
            return new Flow(this.flowCode, this.flowName, this.flowType, this.content, this.returnFieldList);
        }

        public String toString() {
            return "Flow.FlowBuilder(flowCode=" + this.flowCode + ", flowName=" + this.flowName + ", flowType=" + this.flowType + ", content=" + this.content + ", returnFieldList=" + this.returnFieldList + ")";
        }
    }
}
