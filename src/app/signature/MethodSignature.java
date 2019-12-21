package app.signature;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MethodSignature extends it.itc.etoc.MethodSignature {
    private Map<Integer, Map<String, Object>> paramConditions = new HashMap<>();

    public MethodSignature(String s, List list) {
        super(s, list);
    }

    public void addParamCondition(int paramNum, String condition, Object value) {
        Map<String, Object> c = paramConditions.get(paramNum);
        if (c == null) {
            c = new HashMap<>();
        }
        c.put(condition, value);
        paramConditions.put(paramNum, c);
    }

    public Map<String, Object> getParamCondition(int i) {
        return paramConditions.get(i);
    }

    public Map<Integer, Map<String, Object>> getParamConditions() {
        return paramConditions;
    }

    public void setParamConditions(Map<Integer, Map<String, Object>> paramConditions) {
        this.paramConditions = paramConditions;
    }
}
