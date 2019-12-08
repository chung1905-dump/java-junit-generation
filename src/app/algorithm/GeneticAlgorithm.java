package app.algorithm;

import app.signature.Reader;
import it.itc.etoc.Chromosome;
import it.itc.etoc.ChromosomeFormer;
import it.itc.etoc.CustomPopulation;
import it.itc.etoc.MethodSignature;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeneticAlgorithm implements AlgorithmInterface {
    private ChromosomeFormer chromosomeFormer;
    private static List targets = new LinkedList();
    private static Random randomGenerator = new Random();
    private static List<Object> simpleChromosome;
    private static List<Object> population = new ArrayList<Object>();


    public GeneticAlgorithm(String signFile) {
        Reader.readSignatures(signFile);
        System.out.println(Reader.classUnderTest);
        int i = 0;
        int populationSize = 10;
        for (int j = 0; j < populationSize; j++) {
            simpleChromosome = new ArrayList<Object>();
            for (MethodSignature m : Reader.methods.get(Reader.classUnderTest)) {
                System.out.println(m.getName());
                for (Object p : m.getParameters()) {
                    generateValueForChromosome(p.toString(), i);
                    i++;
                }
                population.add(j, simpleChromosome);
                i = 0;
            }
        }


        initPopulation(signFile);
        initPopulation2(signFile);
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


    @Override
    public ArrayList getCurrentTestArgs() {
        return null;
    }

    @Override
    public void change() {

    }

    @Override
    public void fitnessCalculate() {

    }
}
