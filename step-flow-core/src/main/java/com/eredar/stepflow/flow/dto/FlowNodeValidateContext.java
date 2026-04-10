package com.eredar.stepflow.flow.dto;

import com.eredar.stepflow.step.StepExecutor;
import com.eredar.stepflow.utils.StepFlowUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * FlowNode 递归校验上下文
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class FlowNodeValidateContext {

    private Map<String, List<String>> illegalMsgMap;

    private StepExecutor stepExecutor;

    private Set<String> flowCodeSet;

    /**
     * flowCode 是否不存在
     * @return true-不存在; false-存在
     */
    public boolean flowCodeNotExist(String flowCode) {
        return !flowCodeSet.contains(flowCode);
    }

    /**
     * stepCode 是否不存在
     * @return true-不存在; false-存在
     */
    public boolean stepCodeNotExist(String stepCode) {
        return !stepExecutor.hasStepCode(stepCode);
    }

    /**
     * 保存校验发现的异常信息
     *
     * @param globalFlowCode FlowNode 所在的 Flow 对象的 flowCode
     * @param errMsg 异常信息
     */
    public void saveErrMsg(String globalFlowCode, String errMsg) {
        List<String> list = illegalMsgMap.computeIfAbsent(globalFlowCode, k -> new ArrayList<>());
        list.add(errMsg);
    }

    /**
     * 拼接最终的信息字符串
     */
    public String buildErrMsg() {
        if (StepFlowUtils.isEmpty(illegalMsgMap)) {
            return null;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("========== 以下flow不合法 ==========").append("\n");
            for (Map.Entry<String, List<String>> entry : illegalMsgMap.entrySet()) {
                String flowCode = entry.getKey();
                List<String> msgList = entry.getValue();
                sb.append(flowCode).append(":\n");
                for (String msg : msgList) {
                    sb.append("    ").append(msg).append("\n");
                }
            }
            return sb.toString();
        }
    }
}
