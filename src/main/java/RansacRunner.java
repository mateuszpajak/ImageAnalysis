import Jama.Matrix;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class RansacRunner {

    public static void main(String[] args) {

        Image imgA = FileReader.readImage("drawable/pic4a.png");
        Image imgB = FileReader.readImage("drawable/pic4b.png");
        File siftA = FileReader.readSift("drawable/pic4a.png.haraff.sift");
        File siftB = FileReader.readSift("drawable/pic4b.png.haraff.sift");

        ImageProperties imagePropertiesA = new ImageProperties(imgA, siftA);
        ImageProperties imagePropertiesB = new ImageProperties(imgB, siftB);

        NeighborhoodCompatibility.setNearestNeighbor(imagePropertiesA, imagePropertiesB);
        NeighborhoodCompatibility.setNearestNeighbor(imagePropertiesB, imagePropertiesA);

        ArrayList<Pair> pairs = NeighborhoodCompatibility.getPairNeighbor(imagePropertiesA);

        Ransac ransac = new Ransac(100000, 20, 10, 310, pairs);
        Matrix matrix = ransac.ransacAlgorithm(true);

        ArrayList<Pair> best = ransac.getMatchingPairs(matrix);
        System.out.println("Number of pairs: " + pairs.size());

        //EventQueue.invokeLater(() -> new Frame(imagePropertiesA, imagePropertiesB, pairs, best, true));
        EventQueue.invokeLater(() -> new Frame(imagePropertiesA, imagePropertiesB, best, null, false));
    }
}
