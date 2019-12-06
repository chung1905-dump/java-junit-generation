package app.algorithm;

import it.itc.etoc.Chromosome;
import it.itc.etoc.ChromosomeFormer;

import java.util.ArrayList;
import java.util.List;

public class GeneticAlgorithm implements AlgorithmInterface {
    private ChromosomeFormer chromFormer;

    public GeneticAlgorithm(String signFile) {
        initPopulation(signFile);
    }

    private void initPopulation(String signFile) {
        chromFormer = new ChromosomeFormer();
        chromFormer.readSignatures(signFile);
        chromFormer.buildNewChromosome();
        Chromosome chromosome = chromFormer.getChromosome();
        List actions = chromosome.getActions();
        System.out.println(chromosome);
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
