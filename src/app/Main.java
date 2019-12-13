package app;

import app.algorithm.GeneticAlgorithm;
import app.path.Branch;
import app.path.PathGenerator;
import app.path.PathReader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
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
//        CheckTriangle.check(2, 3, 4);
//        java.util.Set set = CheckTriangle.getTrace();
//        System.out.println(set);


        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(target + ".sign", branches);
//        readTargetFile(target);
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

    private static void readTargetFile(String target) {
        String filePath =target + ".tgt";
        try {
            String s;
            Pattern p = Pattern.compile("([^\\s]+)\\s*:\\s*(.*)");
            BufferedReader in = new BufferedReader(new FileReader(filePath));
            while ((s = in.readLine()) != null) {
                Matcher m = p.matcher(s);
                if (!m.find()) continue;
                String method = m.group(1);
//                MethodTarget tgt = new MethodTarget(method);
                String[] branches = m.group(2).split(",");
                for (int i = 0; i < branches.length; i++) {
                    int n = Integer.parseInt(branches[i].trim());
//                    tgt.addBranch(n);
                }
//                targets.add(tgt);
            }
        } catch (NumberFormatException e) {
            System.err.println("Wrong format file: " + filePath);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("IO error: " + filePath);
            System.exit(1);
        }
    }
}
