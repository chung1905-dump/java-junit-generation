package app;

import app.algorithm.PSO;
import app.algorithm.TestExecutor;
import app.algorithm.pso.Particle;
import app.path.Branch;
import app.path.PathGenerator;
import app.path.PathReader;
import app.signature.Reader;
import it.itc.etoc.MethodSignature;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.*;
import java.util.ArrayList;
import java.util.Set;

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

        System.out.println(Reader.classUnderTest);

        PathReader pathReader = new PathReader();
        ArrayList<Branch> branches = pathReader.read(target + ".path");

        for (Branch b : branches) {
            for (MethodSignature m : Reader.methods.get(Reader.classUnderTest)) {
                _processOnBranch(b, m);
            }
        }
    }

    private static void _processOnBranch(Branch b, MethodSignature m) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // TODO: quick check if method m contains target branch (file .tgt)
        PSO pso = new PSO();
        pso.initSwarm(m);
        for (Particle<?> p : pso.getSwarm().getParticles()) {
            Class<?> clazz = Class.forName(Reader.classUnderTest);
            System.out.println("Input: " + p.getPosition());
            Set<Integer> traces = TestExecutor.run(clazz, m, p);
            System.out.println("Branch: " + b);
            System.out.println("Traces: " + traces);
            double point = _fitness(b, traces);
            System.out.println("Point: " + (int) (point * 100) + "/100");
            System.out.println("");
        }
    }

    private static double _fitness(Branch b, Set<Integer> traces) {
        int origSize = b.getNodes().size();
        Set<Integer> branchSet = b.toSet();
        branchSet.removeAll(traces);

        return (double) (origSize - branchSet.size()) / origSize;
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
