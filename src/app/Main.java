package app;

import app.algorithm.GeneticAlgorithm;
import app.path.Branch;
import app.path.PathGenerator;
import app.path.PathReader;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import app.signature.Reader;
import it.itc.etoc.MethodSignature;

public class Main {
    public static String classUnderTest;

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {
        String target = "CheckTriangle";
//        String target = "MinMax";

        String[] filePath = new String[2];
        filePath[0] = "-d=./out";
        filePath[1] = "src/app/" + target + ".oj";

        PathGenerator pathGenerator = new PathGenerator();
        pathGenerator.generate(filePath);
        editSignatureFile(target + ".sign");


        PathReader pathReader = new PathReader();
        ArrayList<Branch> branches = pathReader.read(target + ".path");
//        CheckTriangle.check(2, 3, 4);
//        java.util.Set set = CheckTriangle.getTrace();
//        System.out.println(set);



        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(target + ".sign", branches);
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
