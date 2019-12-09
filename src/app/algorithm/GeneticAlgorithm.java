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
//                System.out.println(branches.toString());
//                System.out.println(branch.toString());
                // GENERATE POPULATION
                population.clear();
                generatePopulation();
//                System.out.println(population.toString());
                // TEST WITH ALL CHROMOSOME IN POPULATION
                int z = 0;
                while (selection(branch) == 0 && z <= 2) {
//                    System.out.println(population.toString());
                    crossover();
                    mutate();
                    selection(branch);
                    z++;
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

    private static void clearTrace(Class<?> clazz) throws NoSuchFieldException, IllegalAccessException {
        Field f = clazz.getDeclaredField("trace");
        f.setAccessible(true);
        f.set(null, new HashSet<>());
    }

    private static void generatePopulation() {
        int i = 0;
        for (int j = 0; j < populationSize; j++) {
            simpleChromosome = new ArrayList<Object>();
            for (MethodSignature m : Reader.methods.get(Reader.classUnderTest)) {
//                System.out.println(m.getName());
                methodName = m.getName().toString();
                for (Object p : m.getParameters()) {
                    generateValueForChromosome(p.toString(), i);
                    i++;
                }
                population.add(j, simpleChromosome);
                i = 0;
            }
        }
    }

    private static int selection(Branch branch) {
        try {
            int testTimes = 0;
            for (int z = 0; z < populationSize; z++) {
                StringBuilder trace = new StringBuilder();
                List<Object> chromosomeX = (List<Object>) population.get(z);
                Class<?> testClass = Class.forName(Reader.classUnderTest);
                Method m = testClass.getMethod(methodName, double.class, double.class, double.class);
                m.invoke(null, chromosomeX.get(0), chromosomeX.get(1), chromosomeX.get(2));
                Method n = testClass.getMethod("getTrace");
                java.util.Set set = (Set) n.invoke(null);
                for (Object o : set) {
                    trace.append(o).append("-");
                }
                if (trace.toString().equals(branch.toString())) {
//                    System.out.println(set);
//                    System.out.println(trace);
                    chromosomeX.add(3, 1);
                    pathAlreadyHasTestCase = trace.toString();
                    String resultString = "Input value for path " + branch.toString() + " are: a = " + chromosomeX.get(0) + "; b = " + chromosomeX.get(1) + "; c = " + chromosomeX.get(2);
                    result.add(resultString);
                    return 1;
                }
                clearTrace(testClass);
            }
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | NoSuchFieldException | ClassNotFoundException e) {
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
            }else{
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
}
