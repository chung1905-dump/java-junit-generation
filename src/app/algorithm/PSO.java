package app.algorithm;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import app.algorithm.pso.Particle;
import app.algorithm.pso.Swarm;
import app.path.Branch;
import app.signature.Reader;
import app.signature.MethodSignature;

public class PSO {
    private Swarm<Object> swarm;

    public Swarm<Object> initSwarm(MethodSignature methodSignature) {
        swarm = new Swarm<>();
        for (int i = 0; i < swarm.size; i++) {
            Particle<Object> particle = new Particle<>();
            particle.setPosition(randomFromParams(methodSignature));
            particle.setVelocity(randomFromParams(methodSignature, 0.5));

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
        double c1 = 0.1, c2 = 0.1; // Learning factors
        List<Object> newVelocity = new ArrayList<>();
        List<Object> currentV = p.getVelocity();
        List<Object> pBest = p.getpBest();
        List<Object> present = p.getPosition();

        for (int i = 0; i < currentV.size(); i++) {
            Double rand1 = (Double) _random("double", 0, 1);
            Double rand2 = (Double) _random("double", 0, 1);
            double v = Double.parseDouble(currentV.get(i).toString());
            double pB = Double.parseDouble(pBest.get(i).toString());
            double gB = Double.parseDouble(gBest.get(i).toString());
            double pr = Double.parseDouble(present.get(i).toString());
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
            double v = Double.parseDouble(currentV.get(i).toString());
            double pr = Double.parseDouble(present.get(i).toString());
            double newSmallP = pr + v;
            if (present.get(i).getClass().getSimpleName().equals("Integer")) {
                newPostition.add((int) newSmallP);
            } else {
                newPostition.add(newSmallP);
            }
        }

        p.setPosition(newPostition);
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

    private List<Object> randomFromParams(MethodSignature methodSignature, double ratio) {
        List<Object> ret = new ArrayList<>();
        List parameters = methodSignature.getParameters();
        for (int i = 0; i < parameters.size(); i++) {
            int min = -20000, max = 20000;
            Object p = parameters.get(i);
            Map<String, Object> conditions = methodSignature.getParamCondition(i);
            if (conditions != null) {
                if (!conditions.get("min").equals("")) {
                    min = Integer.parseInt((String) conditions.get("min"));
                }
                if (!conditions.get("max").equals("")) {
                    max = Integer.parseInt((String) conditions.get("max"));
                }
//                System.out.println("Param " + i + " from " + min + " to " + max);
            }
            ret.add(_random(p.toString(), (int) (min * ratio), (int) (max * ratio)));
        }
        return ret;
    }

    private List<Object> randomFromParams(MethodSignature methodSignature) {
        return randomFromParams(methodSignature, 1);
    }

    private Object _random(String varType, int min, int max) {
        Random r = new Random();
        if (varType.equals("double") || varType.equals("float")) {
            return min + r.nextDouble() * (max - min);
        } else if (varType.equals("int")) {
            return (int) (min + r.nextDouble() * (max - min));
        } else if (varType.equals("boolean")) {
            return r.nextBoolean();
        } else if (varType.equals("int[]")) {
            int l = Integer.parseInt(Objects.requireNonNull(_random("int", 1, 10)).toString());
            int[] integers = new int[l];
            for (int i = 0; i < l; i++) {
                integers[i] = Integer.parseInt(Objects.requireNonNull(_random("int", min, max)).toString());
            }
            return integers;
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
