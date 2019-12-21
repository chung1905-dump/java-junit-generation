package app;

import app.algorithm.PSO;
import app.algorithm.pso.Swarm;
import app.path.Branch;
import app.path.PathReader;
import app.signature.Reader;
import app.signature.tgt.TgtReader;
import app.signature.MethodSignature;

import java.io.File;
import java.io.IOException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            throw new IllegalArgumentException("Require one argument. E.g: java app.Main CheckTriangle");
        }
        String target = args[0];

        Reader.readSignatures(target + ".sign");

        PathReader pathReader = new PathReader();
        ArrayList<Branch> branches = pathReader.read(target + ".path");

        Map<Integer, Set<Integer>> branchesFromTgt = TgtReader.readTargetFile(target + ".tgt");
        Result result = new Result();

        for (Branch b : branches) {
            for (MethodSignature m : Reader.methods.get(Reader.classUnderTest)) {
                int methodHash = TgtReader.hashMethodSignature(m.getName(), m.getParameters().toArray());
                Set<Integer> bSet = branchesFromTgt.get(methodHash);
                if (!bSet.containsAll(b.toSet())) {
                    continue;
                }

                System.out.println("Branch: " + b);
                PSO pso = new PSO();
                Swarm<?> s = pso.initSwarm(m);

                pso.calculateFitness(b, m);
                System.out.println("Highscore: " + s.getHighestScore() + " at K" + s.currentGeneration);
                System.out.println("Best particle: " + s.getgBest());
                System.out.println("");

                while (s.currentGeneration < s.maxGeneration && s.getHighestScore() < 1) {
                    pso.calculateFitness(b, m);
                    pso.updateSwarm();
                    s.currentGeneration++;
//                    System.out.println("Best particle: " + s.getgBest());
                }

                if (s.currentGeneration != 0) {
                    System.out.println("Highscore: " + s.getHighestScore() + " at K" + s.currentGeneration);
                    System.out.println("Best particle: " + s.getgBest());
                    System.out.println("");
                }

                result.addMethodName(m.getName());
                result.addParamValues((List<Object>) s.getgBest());
            }
        }

        generateTestFile(target, result);
    }

    private static void generateTestFile(String target, Result algorithmResults) throws IOException {
        String filePath = "src/app/" + target + "Test.java";
        File testFile = new File(filePath);
        testFile.createNewFile();
        PrintWriter printWriter = new PrintWriter(testFile);
        printWriter.println("");

        FileWriter fileWriter = new FileWriter(filePath, true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write("package app;\n");
        bufferedWriter.write("\n");
        bufferedWriter.write("import junit.framework.*;\n");
        bufferedWriter.write("public class " + target + "Test extends TestCase {\n");
        for (int i = 0; i < algorithmResults.getParamValues().size(); i++) {
            List<Object> paramValues = (List<Object>) algorithmResults.getParamValues().get(i);
            int noTestCase = i + 1;
            bufferedWriter.write("  public void testCase" + noTestCase + "() {\n");
            bufferedWriter.write("   " + target + " x1 = new " + target + "();\n");
            int varIndex = 2;
            StringBuilder paramsToPassToMethod = new StringBuilder("");
            for (int j = 0; j < paramValues.size(); j++) {
                Object paramValue = paramValues.get(j);
                String varType = paramValue.getClass().getSimpleName();
                bufferedWriter.write("   java.lang." + varType + " x" + varIndex + " = new java.lang." + varType + "(" + paramValue + ");\n");
                if (j == paramValues.size() - 1) {
                    paramsToPassToMethod.append("x").append(varIndex);
                } else {
                    paramsToPassToMethod.append("x").append(varIndex).append(",");
                }
                varIndex++;
            }
            String methodName = (String) algorithmResults.getMethodName().get(i);
            bufferedWriter.write("   x1." + methodName + "(" + paramsToPassToMethod + ");\n");
            bufferedWriter.write("  }\n");
            bufferedWriter.write("\n");
        }
        bufferedWriter.write("  public static void main (String[] args) {\n");
        bufferedWriter.write("    junit.textui.TestRunner.run(" + target + "Test.class);\n");
        bufferedWriter.write("  }\n");
        bufferedWriter.write("}");
        bufferedWriter.close();
    }
}
