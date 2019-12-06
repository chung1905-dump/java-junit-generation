package app.algorithm;

import it.itc.etoc.Chromosome;
import it.itc.etoc.ChromosomeFormer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GeneticAlgorithm implements AlgorithmInterface {
    private ChromosomeFormer chromosomeFormer;

    public GeneticAlgorithm(String signFile) {
        initPopulation(signFile);
    }

    private void initPopulation(String signFile) {
        chromosomeFormer = new ChromosomeFormer();
        chromosomeFormer.readSignatures(signFile);
        List individs = new LinkedList();
        for (int j = 0; j < 10; j++) {
            chromosomeFormer.buildNewChromosome();
            individs.add(chromosomeFormer.getChromosome());
            System.out.println(chromosomeFormer.getChromosome().toCode());
        }
//        chromosomeFormer.buildNewChromosome();
//        Chromosome chromosome = chromosomeFormer.getChromosome();
//        List actions = chromosome.getActions();
//        System.out.println(chromosome);

//        for (Object obj : actions) {
//            Action act = (Action) obj;
//            System.out.println(1);
//            System.out.println(act.countPrimitiveTypes());
//        }
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
