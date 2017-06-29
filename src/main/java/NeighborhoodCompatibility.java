import java.util.*;

public class NeighborhoodCompatibility {

    private static final int neighborhoodSize = 25;
    private static final double cohesion = 0.5;
    private static final double acceptable = cohesion * (double) neighborhoodSize;

    public static void setNearestNeighbor(ImageProperties imageA, ImageProperties imageB) {
        imageA.getKeyPoints().forEach(keyPointA -> {
            ArrayList<Pair> neighborList = new ArrayList<>();
            final double[] distance = {0};
            imageB.getKeyPoints().forEach(keyPointB -> {
                for (int i = 0; i < keyPointA.getFeatures().length; i++) {
                    distance[0] += Math.pow(keyPointA.getFeatures()[i] - keyPointB.getFeatures()[i], 2);
                    //distance[0] += Math.abs(keyPointA.getFeatures()[i] - keyPointB.getFeatures()[i]);
                }
                neighborList.add(new Pair(keyPointA, keyPointB).setDistance(Math.sqrt(distance[0])));
                //neighborList.add(new Pair(keyPointA, keyPointB).setDistance(distance[0]));
                distance[0] = 0;
            });
            neighborList.sort(Comparator.comparing(Pair::getDistance));
            keyPointA.setNearestNeighbor(neighborList.get(0).getKeyPointB());
        });
    }

    public static ArrayList<Pair> getPairNeighbor(ImageProperties imageProperties) {
        ArrayList<Pair> pairs = new ArrayList<>();
        imageProperties.getKeyPoints().forEach(keyPoint -> {
            if (keyPoint.getNearestNeighbor().getNearestNeighbor().equals(keyPoint)) {
                pairs.add(new Pair(keyPoint, keyPoint.getNearestNeighbor()));
            }
        });
        return pairs;
    }

    public static void setNeighborhood(ArrayList<Pair> pairs) {

        ArrayList<KeyPoint> keyPointsA = new ArrayList<>();
        ArrayList<KeyPoint> keyPointsB = new ArrayList<>();

        pairs.forEach(pair -> {
            keyPointsA.add(pair.getKeyPointA());
            keyPointsB.add(pair.getKeyPointB());
        });

        setNeighborhoodHelper(keyPointsA);
        setNeighborhoodHelper(keyPointsB);
    }

    public static void setNeighborhoodHelper(ArrayList<KeyPoint> keyPoints) {

        keyPoints.forEach(keyPoint -> {
            ArrayList<KeyPoint> neighborhood = new ArrayList<>();
            ArrayList<Pair> distanceBetweenPoints = new ArrayList<>();

            keyPoints.forEach(innerKeyPoint -> {
                double distance = getDistance(keyPoint, innerKeyPoint);
                if (distance > 0) {
                    distanceBetweenPoints.add(new Pair(keyPoint, innerKeyPoint).setDistance(distance));
                }
            });

            distanceBetweenPoints.sort(Comparator.comparing(Pair::getDistance));
            for (int i = 0; i < neighborhoodSize; i++) {
                neighborhood.add(distanceBetweenPoints.get(i).getKeyPointB());
            }
            keyPoint.setNeighborhood(neighborhood);
        });
    }

    private static double getDistance(KeyPoint keyPoint, KeyPoint keyPointSnd) {
        return Math.sqrt(Math.pow(keyPoint.getCoordinateX() - keyPointSnd.getCoordinateX(), 2) +
                Math.pow(keyPoint.getCoordinateY() - keyPointSnd.getCoordinateY(), 2));
    }

    public static ArrayList<Pair> getCohesiveNeighborhood(ArrayList<Pair> pairs) {

        ArrayList<Pair> result = new ArrayList<>();
        pairs.forEach(pair -> {
            final int[] counter = {0};
            pair.getKeyPointA().getNeighborhood().forEach(keyPoint -> {
                if (pair.getKeyPointB().getNeighborhood().contains(keyPoint.getNearestNeighbor())) {
                    counter[0]++;
                }
            });

            if (counter[0] >= acceptable) {
                result.add(pair);
            }
        });
        return result;
    }
}
