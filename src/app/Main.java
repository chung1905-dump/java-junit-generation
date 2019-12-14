package app;

import app.algorithm.ChromosomeX;
import app.algorithm.GeneticAlgorithm;
import app.path.Branch;
import app.path.PathGenerator;
import app.path.PathReader;
import app.signature.TgtReader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static String classUnderTest;

    public static void main(String[] args) throws IOException {
        String target = "CheckTriangle";
        // CREATE EMPTY OJ FILE
        File ojFile = new File("src/app/" + target + ".oj");
        ojFile.createNewFile();
        PrintWriter printWriter = new PrintWriter(ojFile);
        printWriter.println("");

        generateOjFile(target);
        String[] filePath = new String[2];
        filePath[0] = "-d=./out";
        filePath[1] = "src/app/" + target + ".oj";

        PathGenerator pathGenerator = new PathGenerator();
        pathGenerator.generate(filePath);
        editSignatureFile(target + ".sign", target);


        PathReader pathReader = new PathReader();
        ArrayList<Branch> branches = pathReader.read(target + ".path");


        Result algorithmResults = new Result();
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(target + ".sign", branches, target + ".tgt", algorithmResults);

        // CREATE TEST FILE
        generateTestFile(target, algorithmResults);
    }

    private static void editSignatureFile(String fileName, String target) {
        try {
            File file = new File(fileName);
            String content = new String(Files.readAllBytes(Paths.get(fileName)));
            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            PrintWriter printWriter = new PrintWriter(file);
            printWriter.print("");
//            bufferedWriter.write("java.lang.Double.Double(double)\n");
            bufferedWriter.write("app." + target + "." + target + "()\n");
            bufferedWriter.write(content);
            bufferedWriter.write("#");
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void generateOjFile(String target) throws IOException {

        int needAddImport = 0;

        String filePath = "src/app/" + target + ".java";
//        File javaFile = new File(filePath);
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                FileWriter fileWriter = new FileWriter("src/app/" + target + ".oj", true);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

                if (line.contains("class " + target)) {
                    if (line.substring(line.length() - 1).equals("{")) {
                        line = line.substring(0, line.length() - 1) + "instantiates BranchInstrumentor {";
                    } else {
                        line = line + " instantiates BranchInstrumentor";
                    }
                }
                if (line.contains(";") && needAddImport != 2) {
                    needAddImport = 1;
                }

                bufferedWriter.write(line + "\n");

                if (needAddImport == 1) {
                    bufferedWriter.write("import it.itc.etoc.*;\n");
                    needAddImport = 2;
                }
                bufferedWriter.close();
            }
        }
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
        for (int i = 0; i < algorithmResults.getPath().size(); i++) {
            ArrayList<Object> paramValues = (ArrayList<Object>) algorithmResults.getParamValues().get(i);
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
