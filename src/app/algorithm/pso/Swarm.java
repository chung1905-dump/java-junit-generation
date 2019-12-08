package app.algorithm.pso;

import java.util.*;

public class Swarm<E> {
    public int currentGeneration = 0;
    public int maxGeneration = 10;
    public int size = 10;
    private double highestScore = -1;
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

    public List<E> getgBest() {
        return gBest;
    }

    public void setgBest(List<E> gBest) {
        this.gBest = gBest;
    }

    public double getHighestScore() {
        return highestScore;
    }

    public void setHighestScore(double highestScore) {
        this.highestScore = highestScore;
    }
}
