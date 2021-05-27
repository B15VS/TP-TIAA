package model.data;

public class Score {
    private long seed;
    private long distance;

    public Score(long seed, int distance) {
        this.seed = seed;
        this.distance = distance;
    }

    public long getSeed() {
        return seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }
}
