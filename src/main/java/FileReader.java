import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class FileReader {

    public static Image readImage(String pathname) {
        Image image = null;
        try {
            image = ImageIO.read(new File(pathname));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public static File readSift(String pathname) {
        return new File(pathname);
    }
}
