package app.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import app.algorithm.pso.Particle;
import app.algorithm.pso.Swarm;
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

        System.out.println(varType);
        if (varType.equals("double") || varType.equals("float")) {
            double random = min + r.nextDouble() * (max - min);
            return String.valueOf(random);
        } else if (varType.equals("int")) {
            int random = min + r.nextInt() * (max - min);
            return String.valueOf(random);
        } else if (varType.equals("boolean")) {
            boolean random = r.nextBoolean();
            return String.valueOf(random);
        } else {
            // Non-support type
            return null;
        }
    }
}
