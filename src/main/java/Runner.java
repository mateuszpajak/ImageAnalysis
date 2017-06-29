import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class Runner {

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

        NeighborhoodCompatibility.setNeighborhood(pairs);

        ArrayList<Pair> cohesiveNeighborhood = NeighborhoodCompatibility.getCohesiveNeighborhood(pairs);
        System.out.println("Number of pairs: " + cohesiveNeighborhood.size());

        EventQueue.invokeLater(() -> new Frame(imagePropertiesA, imagePropertiesB, cohesiveNeighborhood, null, false));
    }
}
