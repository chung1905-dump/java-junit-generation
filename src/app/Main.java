package app;

import app.algorithm.PSO;
import app.algorithm.pso.Swarm;
import app.path.Branch;
import app.path.PathGenerator;
import app.path.PathReader;
import app.signature.Reader;
import it.itc.etoc.MethodSignature;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws Exception {
        String target = "CheckTriangle";

        String[] filePath = new String[2];
        filePath[0] = "-d=./out";
        filePath[1] = "src/app/" + target + ".oj";

        PathGenerator pathGenerator = new PathGenerator();
        pathGenerator.generate(filePath);
        editSignatureFile(target + ".sign");
        Reader.readSignatures(target + ".sign");

        PathReader pathReader = new PathReader();
        ArrayList<Branch> branches = pathReader.read(target + ".path");

        for (Branch b : branches) {
            for (MethodSignature m : Reader.methods.get(Reader.classUnderTest)) {
                System.out.println("Branch: " + b);
                // TODO: quick check if method m contains target branch (file .tgt)
                PSO pso = new PSO();
                Swarm<?> s = pso.initSwarm(m);
                while (s.currentGeneration < s.maxGeneration && s.getHighestScore() < 1) {
                    pso.calculateFitness(b, m);
                    pso.updateSwarm();
                    s.currentGeneration++;
                }
                System.out.println("Highscore: " + s.getHighestScore());
                System.out.println("Best particle: " + s.getgBest());
                System.out.println("");
            }
        }
    }

    private static void editSignatureFile(String fileName) {
        try {
            File file = new File(fileName);
            String content = new String(Files.readAllBytes(Paths.get(fileName)));

            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            PrintWriter printWriter = new PrintWriter(file);
            printWriter.print("");
            bufferedWriter.write("app.CheckTriangle.CheckTriangle()\n");
            bufferedWriter.write(content);
            bufferedWriter.write("#");
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
