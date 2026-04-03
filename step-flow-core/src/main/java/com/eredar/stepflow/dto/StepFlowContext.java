package com.eredar.stepflow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 表达式引擎计算公式需要的入参实体类
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class StepFlowContext {

    // 参数 map
    private Map<String, Object> contextMap;

    public void putAll(Map<String, Object> map) {
        contextMap.putAll(map);
    }

    public void put(String key, Object value) {
        contextMap.put(key, value);
    }

    public Object get(String key) {
        return contextMap.get(key);
    }
}
