package app.algorithm;

import java.util.ArrayList;

import it.itc.etoc.CustomPopulation;

public class PSO implements AlgorithmInterface {
    public PSO(String signFile) {
        initPopulation(signFile);
    }

    private void initPopulation(String signFile) {
        CustomPopulation.setChromosomeFormer(signFile);
        CustomPopulation population = CustomPopulation.generateRandomPopulation();
        System.out.println(population);
//        chromFormer = new ChromosomeFormer();
//        chromFormer.readSignatures(signFile);
//        chromFormer.buildNewChromosome();
//        Chromosome chromosome = chromFormer.getChromosome();
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
