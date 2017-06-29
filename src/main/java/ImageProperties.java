import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class ImageProperties {

    private Image image;
    private int resolutionX;
    private int resolutionY;
    private ArrayList<KeyPoint> keyPoints;

    public ImageProperties(Image image, File siftFile) {
        this.image = image;
        this.resolutionX = image.getWidth(null);
        this.resolutionY = image.getHeight(null);
        this.keyPoints = readFile(siftFile);
    }

    private ArrayList<KeyPoint> readFile(File siftFile) {
        ArrayList<KeyPoint> keyPoints = new ArrayList<KeyPoint>();

        try {
            Scanner sc = new Scanner(siftFile);
            sc.nextLine();
            sc.nextLine();

            while (sc.hasNextLine()) {
                KeyPoint keyPoint = new KeyPoint();

                String line = sc.nextLine();
                String[] splitLine = line.split(" ");
                keyPoint.setCoordinateX(Double.parseDouble(splitLine[0]));
                keyPoint.setCoordinateY(Double.parseDouble(splitLine[1]));

                int[] features = new int[128];
                int j = 0;
                for (int i = 5; i < splitLine.length; i++) {
                    features[j++] = Integer.parseInt(splitLine[i]);
                }
                keyPoint.setFeatures(features);
                keyPoints.add(keyPoint);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return keyPoints;
    }

    public Image getImage() {
        return image;
    }

    public ImageProperties setImage(Image image) {
        this.image = image;
        return this;
    }

    public int getResolutionX() {
        return resolutionX;
    }

    public ImageProperties setResolutionX(int resolutionX) {
        this.resolutionX = resolutionX;
        return this;
    }

    public int getResolutionY() {
        return resolutionY;
    }

    public ImageProperties setResolutionY(int resolutionY) {
        this.resolutionY = resolutionY;
        return this;
    }

    public ArrayList<KeyPoint> getKeyPoints() {
        return keyPoints;
    }

    public ImageProperties setKeyPoints(ArrayList<KeyPoint> keyPoints) {
        this.keyPoints = keyPoints;
        return this;
    }
}
