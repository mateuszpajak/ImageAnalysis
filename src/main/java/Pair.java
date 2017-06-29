public class Pair {

    private KeyPoint keyPointA;
    private KeyPoint keyPointB;
    private double distance;

    public Pair(KeyPoint keyPointA, KeyPoint keyPointB) {
        this.keyPointA = keyPointA;
        this.keyPointB = keyPointB;
    }

    public KeyPoint getKeyPointA() {
        return keyPointA;
    }

    public Pair setKeyPointA(KeyPoint keyPointA) {
        this.keyPointA = keyPointA;
        return this;
    }

    public KeyPoint getKeyPointB() {
        return keyPointB;
    }

    public Pair setKeyPointB(KeyPoint keyPointB) {
        this.keyPointB = keyPointB;
        return this;
    }

    public double getDistance() {
        return distance;
    }

    public Pair setDistance(double distance) {
        this.distance = distance;
        return this;
    }
}
