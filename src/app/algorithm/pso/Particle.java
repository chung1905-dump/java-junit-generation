package app.algorithm.pso;

import java.util.List;

public class Particle<E> {
    private List<E> position;
    private List<E> velocity;

    private List<E> pBest;

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
}
