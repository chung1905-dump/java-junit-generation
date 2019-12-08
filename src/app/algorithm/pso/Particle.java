package app.algorithm.pso;

import java.util.List;

public class Particle<E> {
    private List<E> position;
    private List<E> velocity;

    private List<E> pBest;
    private double highestScore = -1;

    public List<E> getPosition() {
        return position;
    }

    public void setPosition(List<E> position) {
        this.position = position;
    }

    public List<E> getVelocity() {
        return velocity;
    }

    public void setVelocity(List<E> velocity) {
        this.velocity = velocity;
    }

    public List<E> getpBest() {
        return pBest;
    }

    public void setpBest(List<E> pBest) {
        this.pBest = pBest;
    }

    public double getHighestScore() {
        return highestScore;
    }

    public void setHighestScore(double highestScore) {
        this.highestScore = highestScore;
    }
}
