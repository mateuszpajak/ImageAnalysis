import java.util.ArrayList;
import java.util.Arrays;

public class KeyPoint {

    private int[] features = new int[128];
    private double coordinateX;
    private double coordinateY;
    private ArrayList<KeyPoint> neighborhood;
    private KeyPoint nearestNeighbor;

    public KeyPoint() {
        neighborhood = new ArrayList<>();
    }

    public int[] getFeatures() {
        return features;
    }

    public KeyPoint setFeatures(int[] features) {
        this.features = features;
        return this;
    }

    public double getCoordinateX() {
        return coordinateX;
    }

    public KeyPoint setCoordinateX(double coordinateX) {
        this.coordinateX = coordinateX;
        return this;
    }

    public double getCoordinateY() {
        return coordinateY;
    }

    public KeyPoint setCoordinateY(double coordinateY) {
        this.coordinateY = coordinateY;
        return this;
    }

    public ArrayList<KeyPoint> getNeighborhood() {
        return neighborhood;
    }

    public KeyPoint setNeighborhood(ArrayList<KeyPoint> neighborhood) {
        this.neighborhood = neighborhood;
        return this;
    }

    public KeyPoint getNearestNeighbor() {
        return nearestNeighbor;
    }

    public KeyPoint setNearestNeighbor(KeyPoint nearestNeighbor) {
        this.nearestNeighbor = nearestNeighbor;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KeyPoint keyPoint = (KeyPoint) o;

        if (Double.compare(keyPoint.coordinateX, coordinateX) != 0) return false;
        if (Double.compare(keyPoint.coordinateY, coordinateY) != 0) return false;
        if (!Arrays.equals(features, keyPoint.features)) return false;
        if (neighborhood != null ? !neighborhood.equals(keyPoint.neighborhood) : keyPoint.neighborhood != null)
            return false;
        return nearestNeighbor != null ? nearestNeighbor.equals(keyPoint.nearestNeighbor) : keyPoint.nearestNeighbor == null;
    }
}
