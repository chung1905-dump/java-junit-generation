package app;

import java.util.ArrayList;
import java.util.List;

public class Result {
    private List<Object> resultPaths = new ArrayList<>();
    private List<Object> paramValues = new ArrayList<>();
    private List<Object> methodName = new ArrayList<>();

    public void addPath(String path) {
        resultPaths.add(path);
    }

    public List<Object> getPath() {
        return resultPaths;
    }

    public void addParamValues(List<Object> paramValues) {
        this.paramValues.add(paramValues);
    }

    public List<Object> getParamValues() {
        return paramValues;
    }

    public void addMethodName(String method) {
        methodName.add(method);
    }
    public List<Object> getMethodName() {
        return methodName;
    }
}
