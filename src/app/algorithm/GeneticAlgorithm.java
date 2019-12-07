package app.algorithm;

import it.itc.etoc.Chromosome;
import it.itc.etoc.ChromosomeFormer;
import it.itc.etoc.CustomPopulation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeneticAlgorithm implements AlgorithmInterface {
    private ChromosomeFormer chromosomeFormer;
    private static List targets = new LinkedList();
    private static Random randomGenerator = new Random();


    public GeneticAlgorithm(String signFile) {
//        initPopulation(signFile);
        initPopulation2(signFile);
//        readTarget("BinaryTree.tgt");
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
        System.out.println(population);

    }

    public void readTarget(String targetFile) {
        try {
            String s;
            Pattern p = Pattern.compile("([^\\s]+)\\s*:\\s*(.*)");
            BufferedReader in = new BufferedReader(new FileReader(targetFile));
            while ((s = in.readLine()) != null) {
                Matcher m = p.matcher(s);
                if (!m.find()) continue;
                String method = m.group(1);
//                MethodTarget tgt = new MethodTarget(method);
                String[] branches = m.group(2).split(",");
                for (int i = 0; i < branches.length; i++) {
                    int n = Integer.parseInt(branches[i].trim());
//                    tgt.addBranch(n);
                }
//                targets.add(tgt);
            }
        } catch (NumberFormatException e) {
            System.err.println("Wrong format file: " + targetFile);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("IO error: " + targetFile);
            System.exit(1);
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
