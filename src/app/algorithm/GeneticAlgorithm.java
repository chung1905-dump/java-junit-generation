package app.algorithm;

import app.path.Branch;
import app.signature.Reader;
import it.itc.etoc.ChromosomeFormer;
import it.itc.etoc.MethodSignature;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;


public class GeneticAlgorithm {
    private ChromosomeFormer chromosomeFormer;
    private static ChromosomeX simpleChromosome;
    private static List<Object> population = new ArrayList<Object>();
    private static List<Object> traces = new ArrayList<Object>();
    private static List<Object> result = new ArrayList<Object>();
    private static String methodName = "";
    private static String pathAlreadyHasTestCase = "";
    private static int populationSize = 300;
    private static int maxPoint = 0;

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
                    while (selection(branch, methodSignature, z) == 0 && z < 100) {
                        sortPopulation();
                        crossover();
                        mutate();
                        selection(branch, methodSignature, z);
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
            simpleChromosome.setChromosome(i, random);
        } else if (varType.equals("int")) {
            int random = min + r.nextInt() * (max - min);
            simpleChromosome.setChromosome(i, random);
        } else if (varType.equals("boolean")) {
            boolean random = r.nextBoolean();
            simpleChromosome.setChromosome(i, random);
        }
    }

    private static void clearTrace(Class<?> testClass) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        testClass.getMethod("newTrace").invoke(null);
    }

    private static void generatePopulation(MethodSignature methodSignature) {
        int i = 0;
        for (int j = 0; j < populationSize; j++) {
            simpleChromosome = new ChromosomeX();
            methodName = methodSignature.getName();
            for (Object p : methodSignature.getParameters()) {
                generateValueForChromosome(p.toString(), i);
                i++;
            }
            population.add(j, simpleChromosome);
            i = 0;
        }
    }

    private static int selection(Branch branch, MethodSignature methodSignature, int difficulty) {
        try {
            int point = 0;
            for (int z = 0; z < populationSize; z++) {
                StringBuilder trace = new StringBuilder();
                ChromosomeX chromosomeX = (ChromosomeX) population.get(z);
                Class<?> testClass = Class.forName(Reader.classUnderTest);
                Method m = testClass.getMethod(methodName, stringToClass(methodSignature.getParameters().toArray()));
                m.invoke(null, chromosomeX.getChromoSome().toArray());
                Method n = testClass.getMethod("getTrace");
                java.util.Set set = (Set) n.invoke(null);
                for (Object o : set) {
                    trace.append(o).append("-");
                }
                computeFitnes(trace, branch, chromosomeX);
                int chromosomePoint = (int) chromosomeX.getChromosomePoint();
                if (chromosomePoint > point) {
                    point = chromosomePoint;
                }
                clearTrace(testClass);
            }
            for (Object o : population) {
                ChromosomeX chromosome = (ChromosomeX) o;
                int chromosomePoint = chromosome.getChromosomePoint();
                if (chromosomePoint != 0 && chromosomePoint == point && chromosomePoint == maxPoint && !pathAlreadyHasTestCase.equals(branch.toString())) {
                    pathAlreadyHasTestCase = branch.toString();
                    difficulty = difficulty + 1;
                    String resultString = "Input value for path " + branch.toString() + " are: " + chromosome.getChromoSome().toString() + "---"
                            + "Dificulty: " + difficulty;
                    result.add(resultString);
                    return 1;
                }
            }
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static void crossover() {
//        System.out.println("crossover");
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
            ChromosomeX simpleChromosomeX = (ChromosomeX) population.get(i);
            ChromosomeX simpleChromosomeY = (ChromosomeX) population.get(j);
            if (j != 0) {
                double temp = (double) simpleChromosomeX.getSpecificValue(x);
                simpleChromosomeX.fixChromosome(x, simpleChromosomeY.getSpecificValue(y));
                simpleChromosomeY.fixChromosome(y, temp);
            } else {
                simpleChromosomeX.fixChromosome(x, simpleChromosomeY.getSpecificValue(y));
            }
        }
    }

    private static void mutate() {
//        System.out.println("mutate");
        Random ran = new Random();
        int min = 1;
        int max = 100;
        for (int i = populationSize / 2; i < populationSize; i++) {
            int x = ran.nextInt(3);
            double randomValue = min + ran.nextDouble() * (max - min);
            ChromosomeX simpleChromosome = (ChromosomeX) population.get(i);
            simpleChromosome.fixChromosome(x, randomValue);
        }
    }

    private static void computeFitnes(StringBuilder trace, Branch branch, ChromosomeX chromosomeX) {
        int point = 0;
        String[] traceArray = trace.toString().split("-");
        String[] branchArray = branch.toString().split("-");
        maxPoint = branchArray.length;
        for (int i = 0; i < branchArray.length; i++) {
            for (int j = 0; j < traceArray.length; j++) {
                if (traceArray[j].equals(branchArray[i])) {
                    point += 1;
                }
            }
        }
        chromosomeX.setChromosomePoint(point);
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

    private static void sortPopulation() {
        for (int i = 0; i < populationSize - 1; i++) {
            for (int j = i + 1; j < populationSize; j++) {
                ChromosomeX chromosome1 = (ChromosomeX) population.get(i);
                ChromosomeX chromosome2 = (ChromosomeX) population.get(j);
                int chromosome1Point = chromosome1.getChromosomePoint();
                int chromosome2Point = chromosome2.getChromosomePoint();
                if (chromosome2Point >= chromosome1Point) {
                    population.set(i, chromosome2);
                    population.set(j, chromosome1);
                }
            }
        }
    }

}
