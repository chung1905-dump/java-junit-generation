package app.algorithm.pso;

import java.util.*;

public class Swarm<E> {
    public int size = 10;
    private List<Particle<E>> particles;
    private List<E> gBest;

    public void add(Particle<E> particle) {
        if (particles == null) {
            particles = new ArrayList<>();
        }
        particles.add(particle);
    }

    public List<Particle<E>> getParticles() {
        return particles;
    }
}
