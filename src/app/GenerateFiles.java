package app;

import app.algorithm.GeneticAlgorithm;
import app.path.Branch;
import app.path.PathGenerator;
import app.path.PathReader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class GenerateFiles {
    public static void main(String[] args) throws IOException {
        String target = "NextDate";
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
//            bufferedWriter.newLine();
//            bufferedWriter.write("conditions");
//            bufferedWriter.newLine();
//            bufferedWriter.write(content);
            bufferedWriter.write("#");
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
}
