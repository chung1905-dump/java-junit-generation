package app;

import app.path.PathGenerator;
import app.path.PathReader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    public static String classUnderTest;

    public static void main(String[] args) {
        String[] filePath = new String[2];
        filePath[0] = "-d=./out";
        filePath[1] = "src/app/CheckTriangle.oj";

        PathGenerator pathGenerator = new PathGenerator();
        pathGenerator.generate(filePath);

        PathReader pathReader = new PathReader();
        ArrayList<String> paths = new ArrayList<String>();
        String pathFile = "/home/ducanh/IdeaProjects/test-generation/CheckTriangle.path";
        try {
            File fileContainsPath = new File(pathFile);
            Scanner myReader = new Scanner(fileContainsPath);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                paths.add(data);
//                System.out.println(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        editSignatureFile("/home/ducanh/IdeaProjects/test-generation/CheckTriangle.sign");
        readSignature("/home/ducanh/IdeaProjects/test-generation/CheckTriangle.sign");

    }

    private static void readSignature(String fileName) {
        try {
            Set usedClassNames = new HashSet();
            String s, r = "";
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            while ((s = in.readLine()) != null && !s.equals("#")) {
                s = s.replaceAll("\\s+", "");
                if (s.length() > 0) {
                    String s1 = s.substring(0, s.indexOf("("));
                    String className = s1.substring(0, s1.lastIndexOf("."));
                    String methodName = s1.substring(s1.lastIndexOf(".") + 1);
                    String[] paramNames = s.substring(s.indexOf("(") + 1, s.indexOf(")")).split(",");
                    if (paramNames.length == 1 && paramNames[0].equals(""))
                        paramNames = new String[0];
                    List params = new LinkedList();
                    for (int i = 0; i < paramNames.length; i++) {
                        params.add(paramNames[i]);
                        String usedClass = paramNames[i];
                        if (paramNames[i].contains("["))
                            usedClass = paramNames[i].substring(0,
                                    paramNames[i].indexOf("["));
                        if (!isPrimitiveType(paramNames[i]))
                            usedClassNames.add(usedClass);
                    }
                    String simpleClassName = className.substring(className.lastIndexOf(".") + 1);
                    if (simpleClassName.equals(methodName)) {
//                        MethodSignature methodSign = new MethodSignature(className, params);
//                        addConstructor(methodSign);
                    } else {
//                        MethodSignature methodSign = new MethodSignature(methodName, params);
//                        addMethod(className, methodSign);
//                        usedClassNames.add(className);
                    }
                    r = s;
                }
            }
            String r1 = r.substring(0, r.indexOf("("));
            classUnderTest = r1.substring(0, r1.lastIndexOf("."));
            while ((s = in.readLine()) != null) {
                if (s.length() > 0) {
                    String className = s.substring(0, s.indexOf(" as ")).trim();
                    String typeName = s.substring(s.indexOf(" as ") + 4).trim();
//                    addConcreteType(typeName, className);
                }
            }
            in.close();
//            checkConstructorsAvailable(usedClassNames);
        } catch (IOException e) {
            System.err.println("IO error: " + fileName);
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static boolean isPrimitiveType(String type) {
        if (type.contains("["))
            type = type.substring(0, type.indexOf("["));
        return type.equals("int") || type.equals("long") ||
                type.equals("short") || type.equals("char") ||
                type.equals("byte") || type.equals("String") ||
                type.equals("boolean") || type.equals("float") ||
                type.equals("double");
    }

    private static void editSignatureFile(String fileName) {
        try {
            File file = new File(fileName);
            String content = new String(Files.readAllBytes(Paths.get(fileName)));

            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            PrintWriter printWriter = new PrintWriter(file);
            printWriter.print("");
            bufferedWriter.write("java.lang.Integer.Integer(int)\n");
            bufferedWriter.write(content);
            bufferedWriter.write("#\n");
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
