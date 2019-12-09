package app.algorithm;

import app.CheckTriangle;
import app.path.Branch;
import app.signature.Reader;
import it.itc.etoc.Chromosome;
import it.itc.etoc.ChromosomeFormer;
import it.itc.etoc.CustomPopulation;
import it.itc.etoc.MethodSignature;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public GeneticAlgorithm(String signFile, ArrayList<Branch> branches) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        Reader.readSignatures(signFile);

        branches.forEach((branch -> {
            if (branch.toString().length() >= 4 && !branch.toString().equals(pathAlreadyHasTestCase)) {
                System.out.println(branches.toString());
                System.out.println(branch.toString());
                // GENERATE POPULATION
                population.clear();
                generatePopulation();
                // TEST WITH ALL CHROMOSOME IN POPULATION
//                System.out.println(selection(branch));
//                selection(branch);
                int z = 0;
                while (selection(branch) == 0 && z <= 2) {
                    crossover();
                    mutate();
                    selection(branch);
                    z++;
                }
            }
        }));
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
                    System.out.println(set);
                    System.out.println(trace);
                    chromosomeX.add(3, 1);
                    pathAlreadyHasTestCase = trace.toString();
                    String resultString = "Input value for path " + branch.toString() + " are: a = " + chromosomeX.get(0) + "; b = " + chromosomeX.get(1) + "; c = " + chromosomeX.get(2);
                    result.add(resultString);
                    System.out.println(resultString);
                    return 1;
                } else {
                    chromosomeX.add(3, 0);

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
        for (int i = 0; i < populationSize / 2; i++) {
            int j = i + 1;
            if (j >= populationSize / 2) {
                j = 0;
            }
            int x = ran.nextInt(3);
            int y = ran.nextInt(3);
            List<Object> simpleChromosomeX = (List<Object>) population.get(i);
            List<Object> simpleChromosomeY = (List<Object>) population.get(j);
            simpleChromosomeX.add(x, simpleChromosomeY.get(y));
            simpleChromosomeY.add(y, simpleChromosomeX.get(x));
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
            simpleChromosome.add(x, randomValue);
        }
    }


    private void initPopulation(String signFile) {
        chromosomeFormer = new ChromosomeFormer();
        chromosomeFormer.readSignatures(signFile);
//        chromosomeFormer.readSignatures("BinaryTree.sign");

//        List individs = new LinkedList();
//        for (int j = 0; j < 10; j++) {
//            chromosomeFormer.buildNewChromosome();
//            individs.add(chromosomeFormer.getChromosome());
//            System.out.println(chromosomeFormer.getChromosome().toCode());
//        }

        chromosomeFormer.buildNewChromosome();
        Chromosome chromosome = chromosomeFormer.getChromosome();
        System.out.println(chromosome.toCode());
        System.out.println(chromosome);

//        List actions = chromosome.getActions();

//        for (Object obj : actions) {
//            Action act = (Action) obj;
//            System.out.println(1);
//            System.out.println(act.countPrimitiveTypes());
//        }
    }

    private void initPopulation2(String signFile) {
        CustomPopulation.setChromosomeFormer(signFile);
        CustomPopulation population = CustomPopulation.generateRandomPopulation();
        List ChromosomeList = population.getChromoList();

        System.out.println(ChromosomeList.get(0));
        for (int i = 0; i < ChromosomeList.size(); i++) {

//            String chromosome = renameChromsomeVariables(ChromosomeList.get(i).toString());
            String chromosome = ChromosomeList.get(i).toString();
            String inputDescription = chromosome.substring(0,
                    chromosome.indexOf("@"));
            String inputValues = chromosome.substring(chromosome.indexOf("@") + 1);
            String[] actions = inputDescription.split(":");

            String[] values = inputValues.split(",");
            int n = -1;
//            for (int j = 0; j < actions.length; j++) {
//                if (actions[i].contains("=")) {
//                    String targetObj = actions[i].substring(0,
//                            actions[i].indexOf("="));
//                    int k = Integer.parseInt(targetObj.substring(2));
//                    if (k > n) n = k;
//                }
//            }

        }
    }

    private static String renameChromsomeVariables(String chrom) {
        String inputDescription = chrom.substring(0, chrom.indexOf("@"));
        String[] actions = inputDescription.split(":");
        int n = 0;
        Map mapIndex = new HashMap();
        for (int i = 0; i < actions.length; i++)
            if (actions[i].contains("=")) {
                String targetObj = actions[i].substring(2,
                        actions[i].indexOf("="));
                int k = Integer.parseInt(targetObj);
                mapIndex.put(k, n++);
            }
        Iterator i = mapIndex.keySet().iterator();
        while (i.hasNext()) {
            Integer x = (Integer) i.next();
            int k = x;
            int j = (Integer) mapIndex.get(x);
            if (k == j) continue;
            Pattern p = Pattern.compile("(.*)\\$x" + k + "([\\.=,\\)].*)");
            Matcher m = p.matcher(chrom);
            while (m.find()) {
                chrom = m.group(1) + "$y" + j + m.group(2);
                m = p.matcher(chrom);
            }
        }
        chrom = chrom.replaceAll("\\$y", "\\$x");
        return chrom;
    }
}
