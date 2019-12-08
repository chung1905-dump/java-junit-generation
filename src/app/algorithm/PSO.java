package app.algorithm;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import app.algorithm.pso.Particle;
import app.algorithm.pso.Swarm;
import app.path.Branch;
import app.signature.Reader;
import it.itc.etoc.MethodSignature;

public class PSO {
    private Swarm<Object> swarm;

    public Swarm<Object> initSwarm(MethodSignature methodSignature) {
        swarm = new Swarm<>();
        System.out.println(methodSignature.getName());
        for (int i = 0; i < swarm.size; i++) {
            Particle<Object> particle = new Particle<>();
            particle.setPosition(randomFromParams(methodSignature));
            particle.setVelocity(randomFromParams(methodSignature));

            swarm.add(particle);
        }

        return swarm;
    }

    /**
     * @param branch (Target branch)
     * @param method (Method to execute)
     */
    public void calculateFitness(Branch branch, MethodSignature method) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        for (Particle<Object> p : getSwarm().getParticles()) {
            // Run
            Set<Integer> traces = TestExecutor.run(Class.forName(Reader.classUnderTest), method, p);
            // Fitness
            double point = _fitness(branch, traces);
//            if (point > p.getHighestScore()){
//                p.setHighestScore(Math.max(point, p.getHighestScore()));
//                p.setpBest(p.getPosition());
//            }

            logInfo(p, branch, traces, point);
        }
    }

    private void logInfo(Particle<Object> p, Branch b, Set<Integer> t, double point) {
        System.out.println("Input: " + p.getPosition());
        System.out.println("Branch: " + b);
        System.out.println("Traces: " + t);
        System.out.println("Point: " + (int) (point * 100) + "/100");
        System.out.println("");
    }

    private double _fitness(Branch b, Set<Integer> traces) {
        int origSize = b.getNodes().size();
        Set<Integer> branchSet = b.toSet();
        branchSet.removeAll(traces);

        return (double) (origSize - branchSet.size()) / origSize;
    }


    private List<Object> randomFromParams(MethodSignature methodSignature) {
        List<Object> ret = new ArrayList<>();
        for (Object p : methodSignature.getParameters()) {
            ret.add(_random(p.toString()));
        }
        return ret;
    }

    private Object _random(String varType) {
        int min = 1;
        int max = 100;
        Random r = new Random();

        if (varType.equals("double") || varType.equals("float")) {
            return min + r.nextDouble() * (max - min);
        } else if (varType.equals("int")) {
            return min + r.nextInt() * (max - min);
        } else if (varType.equals("boolean")) {
            return r.nextBoolean();
        } else {
            // Non-support type
            return null;
        }
    }

    public Swarm<Object> getSwarm() {
        return swarm;
    }

    public void setSwarm(Swarm<Object> swarm) {
        this.swarm = swarm;
    }
}
