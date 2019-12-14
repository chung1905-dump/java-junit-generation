package app;

import app.algorithm.ChromosomeX;

import java.util.ArrayList;
import java.util.List;

public class Result {
    private List<Object> resultPaths = new ArrayList<Object>();
    private List<Object> difficulties = new ArrayList<Object>();
    private List<Object> paramValues = new ArrayList<Object>();
    private List<Object> methodName = new ArrayList<Object>();

    public void setPath(String path) {
        resultPaths.add(path);
    }

    public List<Object> getPath() {
        return resultPaths;
    }

    public void setDifficulties(int pathDiffculty) {
        difficulties.add(pathDiffculty);
    }

    public List<Object> getDifficulty() {
        return difficulties;
    }

    public void setParamValues(List chromosomeX) {
        paramValues.add(chromosomeX);
    }

    public List<Object> getParamValues() {
        return paramValues;
    }

    public void setMethodName(String method) {
        methodName.add(method);
    }
    public List<Object> getMethodName() {
        return methodName;
    }
}
