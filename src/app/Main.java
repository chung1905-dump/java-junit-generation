package app;

import app.algorithm.PSO;
import app.algorithm.pso.Swarm;
import app.path.Branch;
import app.path.PathGenerator;
import app.path.PathReader;
import app.signature.Reader;
import app.signature.tgt.TgtReader;
import it.itc.etoc.MethodSignature;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class Main {
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
        Reader.readSignatures(target + ".sign");

        PathReader pathReader = new PathReader();
        ArrayList<Branch> branches = pathReader.read(target + ".path");

        Map<Integer, Set<Integer>> branchesFromTgt = TgtReader.readTargetFile(target + ".tgt");

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
                while (s.currentGeneration < s.maxGeneration && s.getHighestScore() < 1) {
                    pso.calculateFitness(b, m);
                    pso.updateSwarm();
                    s.currentGeneration++;
                }
                System.out.println("Highscore: " + s.getHighestScore() + " at K" + s.currentGeneration);
                System.out.println("Best particle: " + s.getgBest());
                System.out.println("");
            }
        }
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

                bufferedWriter.write(line + "\n");

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
