package app;

import app.algorithm.PSO;
import app.algorithm.TestExecutor;
import app.algorithm.pso.Particle;
import app.path.PathGenerator;
import app.signature.Reader;
import it.itc.etoc.MethodSignature;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.*;
import java.util.Set;

public class Main {
    public static String classUnderTest;

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        String target = "CheckTriangle";

        String[] filePath = new String[2];
        filePath[0] = "-d=./out";
        filePath[1] = "src/app/" + target + ".oj";

        PathGenerator pathGenerator = new PathGenerator();
        pathGenerator.generate(filePath);
        editSignatureFile(target + ".sign");
        Reader.readSignatures(target + ".sign");

        System.out.println(Reader.classUnderTest);

        for (MethodSignature m : Reader.methods.get(Reader.classUnderTest)) {
            PSO pso = new PSO();
            pso.initSwarm(m);
            for (Particle<?> p : pso.getSwarm().getParticles()) {
                Class<?> clazz = Class.forName(Reader.classUnderTest);
                System.out.println("Input: " + p.getPosition());
                Set traces = TestExecutor.run(clazz, m, p);
                System.out.println("Traces: " + traces);
            }
//            System.out.println(m.getName());
//            for (Object p: m.getParameters()) {
//                System.out.println(p);
//            }
        }

//        PathReader pathReader = new PathReader();
//        ArrayList<Branch> branches = pathReader.read(target + ".path");
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
