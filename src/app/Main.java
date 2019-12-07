package app;

import app.algorithm.PSO;
import app.path.Branch;
import app.path.PathGenerator;
import app.path.PathReader;
import app.signature.Reader;
import it.itc.etoc.MethodSignature;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.io.*;

public class Main {
    public static String classUnderTest;

    public static void main(String[] args) {
        String target = "CheckTriangle";

        String[] filePath = new String[2];
        filePath[0] = "-d=./out";
        filePath[1] = "src/app/" + target + ".oj";

        PathGenerator pathGenerator = new PathGenerator();
        pathGenerator.generate(filePath);
        editSignatureFile(target + ".sign");
        Reader.readSignatures(target + ".sign");

        System.out.println(Reader.classUnderTest);

        for (MethodSignature m: Reader.methods.get(Reader.classUnderTest)) {
            System.out.println(m.getName());
            for (Object p: m.getParameters()) {
                System.out.println(p);
            }
        }

//        PathReader pathReader = new PathReader();
//        ArrayList<Branch> branches = pathReader.read(target + ".path");
//        CheckTriangle.check(1.1, 1.2, 1.3);
//        java.util.Set set = CheckTriangle.getTrace();
//        System.out.println(set);

//        PSO pso = new PSO(target + ".sign");
    }

    private static void editSignatureFile(String fileName) {
        try {
            File file = new File(fileName);
            String content = new String(Files.readAllBytes(Paths.get(fileName)));

            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            PrintWriter printWriter = new PrintWriter(file);
            printWriter.print("");
//            bufferedWriter.write("java.lang.Double.Double(double)\n");
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
