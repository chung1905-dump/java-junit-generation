package app.algorithm;

import app.path.Branch;
import app.signature.Reader;
import it.itc.etoc.ChromosomeFormer;
import it.itc.etoc.MethodSignature;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;


public class GeneticAlgorithm {
    private ChromosomeFormer chromosomeFormer;
    private static List targets = new LinkedList();
    private static Random randomGenerator = new Random();
    private static List<Object> simpleChromosome;
    private static List<Object> population = new ArrayList<Object>();
    private static List<Object> populationTemp;
    private static List<Object> simpleChromosomeTemp = new ArrayList<Object>();
    private static List<Object> traces = new ArrayList<Object>();
    private static List<Object> result = new ArrayList<Object>();
    private static String methodName = "";
    private static String pathAlreadyHasTestCase = "";
    private static int populationSize = 10;

    public GeneticAlgorithm(String signFile, ArrayList<Branch> branches) {
        Reader.readSignatures(signFile);

        branches.forEach((branch -> {
            if (branch.toString().length() >= 4 && !branch.toString().equals(pathAlreadyHasTestCase)) {
                for (MethodSignature methodSignature : Reader.methods.get(Reader.classUnderTest)) {
                    // GENERATE POPULATION
                    population.clear();
                    generatePopulation(methodSignature);
                    // TEST WITH ALL CHROMOSOME IN POPULATION
                    int z = 0;
                    while (selection(branch, methodSignature) == 0 && z <= 2) {
                        crossover();
                        mutate();
                        selection(branch, methodSignature);
                        z++;
                    }
                }
            }
        }));
        for (int j = 0; j < result.size(); j++) {
            System.out.println(result.get(j));
        }
    }


    private static void generateValueForChromosome(String varType, int i) {
        int min = 1;
        int max = 100;
        Random r = new Random();

        if (varType.equals("double") || varType.equals("float")) {
            double random = min + r.nextDouble() * (max - min);
            simpleChromosome.add(i, random);
        } else if (varType.equals("int")) {
            int random = min + r.nextInt() * (max - min);
            simpleChromosome.add(i, random);
        } else if (varType.equals("boolean")) {
            boolean random = r.nextBoolean();
            simpleChromosome.add(i, random);
        }
    }

    private static void clearTrace(Class<?> testClass) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        testClass.getMethod("newTrace").invoke(null);
    }

    private static void generatePopulation(MethodSignature methodSignature) {
        int i = 0;
        for (int j = 0; j < populationSize; j++) {
            simpleChromosome = new ArrayList<Object>();
            methodName = methodSignature.getName();
            for (Object p : methodSignature.getParameters()) {
                generateValueForChromosome(p.toString(), i);
                i++;
            }
            population.add(j, simpleChromosome);
            i = 0;
        }
    }

    private static int selection(Branch branch, MethodSignature methodSignature) {
        try {
            int testTimes = 0;
            for (int z = 0; z < populationSize; z++) {
                StringBuilder trace = new StringBuilder();
                List<Object> chromosomeX = (List<Object>) population.get(z);
                Class<?> testClass = Class.forName(Reader.classUnderTest);
                Method m = testClass.getMethod(methodName, stringToClass(methodSignature.getParameters().toArray()));
                System.out.println(chromosomeX.toString());
                m.invoke(null, chromosomeX.toArray());
                Method n = testClass.getMethod("getTrace");
                java.util.Set set = (Set) n.invoke(null);
                for (Object o : set) {
                    trace.append(o).append("-");
                }
                    System.out.println(trace.toString());
                    System.out.println(branch.toString());

                if (trace.toString().equals(branch.toString())) {
//                    chromosomeX.add(3, 1);

                    pathAlreadyHasTestCase = trace.toString();
                    String resultString = "Input value for path " + branch.toString() + " are: " + chromosomeX.toString();
                    result.add(resultString);
                    return 1;
                }
                clearTrace(testClass);
            }
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static void crossover() {
        Random ran = new Random();
        for (int i = 0; i < populationSize / 2; i += 2) {
            if (i >= populationSize / 2) {
                i = (populationSize / 2) - 1;
            }
            int j = i + 1;
            if (j >= populationSize / 2) {
                j = 0;
            }
            int x = ran.nextInt(3);
            int y = ran.nextInt(3);
            List<Object> simpleChromosomeX = (List<Object>) population.get(i);
            List<Object> simpleChromosomeY = (List<Object>) population.get(j);
            if (j != 0) {
                double temp = (double) simpleChromosomeX.get(x);
                simpleChromosomeX.set(x, simpleChromosomeY.get(y));
                simpleChromosomeY.set(y, temp);
            } else {
                simpleChromosomeX.set(x, simpleChromosomeY.get(y));
            }
        }
    }

    private static void mutate() {
        Random ran = new Random();
        int min = 1;
        int max = 100;
        for (int i = populationSize / 2; i < populationSize; i++) {
            int x = ran.nextInt(3);
            double randomValue = min + ran.nextDouble() * (max - min);
            List<Object> simpleChromosome = (List<Object>) population.get(i);
            simpleChromosome.set(x, randomValue);
        }
    }

    private static Class<?>[] stringToClass(Object[] strings) {
        Class<?>[] classes = new Class[strings.length];
        for (int i = 0; i < strings.length; i++) {
            Class<?> c;
            switch (strings[i].toString()) {
                case "double":
                    c = double.class;
                    break;
                case "int":
                    c = int.class;
                    break;
                case "boolean":
                    c = boolean.class;
                    break;
                default:
                    // Non-support type
                    c = null;
            }
            classes[i] = c;
        }

        return classes;
    }

}
