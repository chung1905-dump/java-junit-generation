package app.signature;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MethodSignature extends it.itc.etoc.MethodSignature {
    private List<Map<String, Object>> paramConditions;

    public MethodSignature(String s, List list) {
        super(s, list);
    }

    public void addParamCondition(int paramNum, String condition, Object value) {
        Map<String, Object> c = new HashMap<>();
        c.put(condition, value);
        paramConditions.set(paramNum, c);
    }

    public List<Map<String, Object>> getParamConditions() {
        return paramConditions;
    }

    public Map<String, Object> getParamCondition(int i) {
        return paramConditions.get(i);
    }

    public void setParamConditions(List<Map<String, Object>> paramConditions) {
        this.paramConditions = paramConditions;
    }
}
