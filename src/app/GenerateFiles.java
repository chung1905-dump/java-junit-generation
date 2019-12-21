package app;

import app.path.PathGenerator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.*;

public class GenerateFiles {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            throw new IllegalArgumentException("Require one argument. E.g: java app.Main CheckTriangle");
        }
        String target = args[0];

        String ojFilePath = generateOjFile(target);

        String[] filePath = new String[2];
        filePath[0] = "-d=./out";
        filePath[1] = ojFilePath;

        PathGenerator pathGenerator = new PathGenerator();
        pathGenerator.generate(filePath);
        editSignatureFile(target + ".sign", target);
    }

    private static String generateOjFile(String target) throws IOException {
        File ojFile = new File(target + ".oj");
        if (ojFile.exists() && !ojFile.delete()) {
            throw new IOException("Failed to delete OJ file.");
        }
        if (!ojFile.createNewFile()) {
            throw new IOException("Failed to create OJ file.");
        }
        int needAddImport = 0;

        final String filePath = "src/app/" + target + ".java";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                FileWriter fileWriter = new FileWriter(ojFile.getPath(), true);
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
                if (!line.contains("@")) {
                    bufferedWriter.write(line + "\n");
                }

                if (needAddImport == 1) {
                    bufferedWriter.write("import it.itc.etoc.*;\n");
                    needAddImport = 2;
                }
                bufferedWriter.close();
            }
        }

        return ojFile.getPath();
    }

    private static void editSignatureFile(String fileName, String target) {
        try {
            File file = new File(fileName);
            String content = new String(Files.readAllBytes(Paths.get(fileName)));

            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            PrintWriter printWriter = new PrintWriter(file);
            printWriter.print("");
            bufferedWriter.write("app." + target + "." + target + "()\n");
            bufferedWriter.write(content);
            bufferedWriter.write("#");
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
