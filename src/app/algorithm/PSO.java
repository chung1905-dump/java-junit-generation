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
            // Assign point
            if (point > p.getHighestScore()) {
                p.setHighestScore(Math.max(point, p.getHighestScore()));
                p.setpBest(p.getPosition());
                if (point > swarm.getHighestScore()) {
                    swarm.setHighestScore(point);
                    swarm.setgBest(p.getPosition());
                }
            }

//            __logInfo(p, branch, traces, point);
        }
    }

    public void updateSwarm() {
        for (Particle<Object> p : getSwarm().getParticles()) {
            _updateVelocity(p, getSwarm().getgBest());
            _updatePosition(p);
        }
    }

    private void _updateVelocity(Particle<Object> p, List<Object> gBest) {
        int c1 = 2, c2 = 2; // Learning factors
        List<Object> newVelocity = new ArrayList<>();
        List<Object> currentV = p.getVelocity();
        List<Object> pBest = p.getpBest();
        List<Object> present = p.getPosition();

        for (int i = 0; i < currentV.size(); i++) {
            Double rand1 = (Double) _random("double", 0, 1);
            Double rand2 = (Double) _random("double", 0, 1);
            Double v = (Double) currentV.get(i);
            Double pB = (Double) pBest.get(i);
            Double gB = (Double) gBest.get(i);
            Double pr = (Double) present.get(i);
            Double newSmallV = v + c1 * rand1 * (pB - pr) + c2 * rand2 * (gB - pr);
            newVelocity.add(newSmallV);
        }

        p.setVelocity(newVelocity);
    }

    private void _updatePosition(Particle<Object> p) {
        List<Object> newPostition = new ArrayList<>();
        List<Object> currentV = p.getVelocity();
        List<Object> present = p.getPosition();

        for (int i = 0; i < currentV.size(); i++) {
            Double v = (Double) currentV.get(i);
            Double pr = (Double) present.get(i);
            Double newSmallP = pr + v;
            newPostition.add(newSmallP);
        }

        p.setVelocity(newPostition);
    }

    private void __logInfo(Particle<Object> p, Branch b, Set<Integer> t, double point) {
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
        return _random(varType, 1, 100);
    }

    private Object _random(String varType, int min, int max) {
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
