package app.signature;

//import it.itc.etoc.MethodSignature;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Reader {
    public static Map<String, List<MethodSignature>> constructors = new HashMap<>();
    public static Map<String, List<MethodSignature>> methods = new HashMap<>();
    public static String classUnderTest;
    public static Map<String, List<String>> concreteTypes = new HashMap<>();

    public static void readSignatures(String fileName) {
        try {
            Set<String> usedClassNames = new HashSet<>();
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
                    List<String> params = new LinkedList<>();
                    List<String[]> paramsConditions = new LinkedList<>();
                    for (int i = 0; i < paramNames.length; i++) {
                        String paramType = paramNames[i];
                        String[] conditions = new String[0];
                        if (paramNames[i].contains("{")) {
                            paramType = paramNames[i].substring(0, paramNames[i].lastIndexOf("{"));
                            conditions = paramNames[i].substring(paramNames[i].indexOf("{")+1,paramNames[i].indexOf("}")).split(";");
                        }
                        paramsConditions.add(conditions);
                        params.add(paramType);
                        String usedClass = paramType;
                        if (paramNames[i].indexOf("[") != -1)
                            usedClass = paramNames[i].substring(0,
                                    paramNames[i].indexOf("["));
                        if (!isPrimitiveType(paramType))
                            usedClassNames.add(usedClass);
                    }
                    String simpleClassName =
                            className.substring(className.lastIndexOf(".") + 1);
                    if (simpleClassName.equals(methodName)) {
                        MethodSignature methodSign = new MethodSignature(className,
                                params);
                        addConstructor(methodSign);
                    } else {
                        MethodSignature methodSign = new MethodSignature(methodName,
                                params);
                        for (int i = 0; i < paramsConditions.size(); i++) {
                            methodSign.addParamCondition(i,"max",paramsConditions.get(i)[0]);
                            methodSign.addParamCondition(i,"min",paramsConditions.get(i)[1]);
                        }
                        addMethod(className, methodSign);
                        usedClassNames.add(className);
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
                    addConcreteType(typeName, className);
                }
            }
            in.close();
            checkConstructorsAvailable(usedClassNames);
        } catch (IOException e) {
            System.err.println("IO error: " + fileName);
            System.exit(1);
        }
    }

    private static boolean isPrimitiveType(String type) {
        if (type.indexOf("[") != -1)
            type = type.substring(0, type.indexOf("["));
        return type.equals("int") || type.equals("long") ||
                type.equals("short") || type.equals("char") ||
                type.equals("byte") || type.equals("String") ||
                type.equals("boolean") || type.equals("float") ||
                type.equals("double");
    }

    private static void addConstructor(MethodSignature sign) {
        String className = sign.getName();
        if (constructors.get(className) == null)
            constructors.put(className, new LinkedList<MethodSignature>());
        List<MethodSignature> constr = constructors.get(className);
        constr.add(sign);
    }

    private static void addMethod(String className, MethodSignature sign) {
        if (methods.get(className) == null)
            methods.put(className, new LinkedList<MethodSignature>());
        List<MethodSignature> meth = methods.get(className);
        meth.add(sign);
    }

    private static void addConcreteType(String abstractType, String concreteType) {
        if (concreteTypes.get(abstractType) == null)
            concreteTypes.put(abstractType, new LinkedList<String>());
        List<String> types = concreteTypes.get(abstractType);
        types.add(concreteType);
    }

    private static void checkConstructorsAvailable(Set<String> usedClasses) {
        boolean error = false;
        String cl = "";
        Iterator<String> k = concreteTypes.keySet().iterator();
        while (!error && k.hasNext()) {
            String absType = (String) k.next();
            List<String> types = concreteTypes.get(absType);
            Iterator<String> j = types.iterator();
            while (!error && j.hasNext()) {
                cl = (String) j.next();
                if (!constructors.containsKey(cl))
                    error = true;
            }
        }
        Iterator<String> i = usedClasses.iterator();
        while (!error && i.hasNext()) {
            cl = (String) i.next();
            if (!constructors.containsKey(cl) && !concreteTypes.containsKey(cl))
                error = true;
        }
        if (error) {
            System.err.println("Missing constructor for class: " + cl);
            System.exit(1);
        }
    }
}

