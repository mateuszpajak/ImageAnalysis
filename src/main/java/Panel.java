import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Panel extends JPanel {

    private ImageProperties imageA;
    private ImageProperties imageB;
    private ArrayList<Pair> pairs;
    private ArrayList<Pair> ransac;
    private boolean isRansac;

    public Panel(ImageProperties imageA, ImageProperties imageB, ArrayList<Pair> pairs, ArrayList<Pair> ransac, boolean isRansac) {
        this.imageA = imageA;
        this.imageB = imageB;
        this.pairs = pairs;
        this.ransac = ransac;
        this.isRansac = isRansac;

        if (imageA.getResolutionY() >= imageB.getResolutionY())
            setPreferredSize(new Dimension(imageA.getResolutionX() + imageB.getResolutionX(), imageA.getResolutionY()));
        else
            setPreferredSize(new Dimension(imageA.getResolutionX() + imageB.getResolutionX(), imageB.getResolutionY()));
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        graphics.drawImage(imageA.getImage(), 0, 0, this);
        graphics.drawImage(imageB.getImage(), imageA.getResolutionX(), 0, this);

        if (isRansac) {
            graphics.setColor(Color.RED);
            imageA.getKeyPoints().forEach(keyPoint ->
                    graphics.fillOval((int) keyPoint.getCoordinateX(), (int) keyPoint.getCoordinateY(), 2, 2)
            );
            imageB.getKeyPoints().forEach(keyPoint ->
                    graphics.fillOval((int) (imageA.getResolutionX() + keyPoint.getCoordinateX()), (int) keyPoint.getCoordinateY(), 2, 2)
            );

            pairs.forEach(pair -> {
                KeyPoint nearest = pair.getKeyPointA().getNearestNeighbor();
                graphics.setColor(Color.YELLOW);
                graphics.drawLine((int) pair.getKeyPointA().getCoordinateX(), (int) pair.getKeyPointA().getCoordinateY(),
                        (int) (imageA.getResolutionX() + nearest.getCoordinateX()), (int) nearest.getCoordinateY());
                graphics.setColor(Color.BLUE);
                graphics.fillOval((int) pair.getKeyPointA().getCoordinateX() - 2, (int) pair.getKeyPointA().getCoordinateY() - 2, 6, 4);
                graphics.fillOval((int) (imageA.getResolutionX() + nearest.getCoordinateX()) - 2, (int) nearest.getCoordinateY() - 2, 6, 4);
            });

            ransac.forEach(pair -> {
                KeyPoint nearest = pair.getKeyPointA().getNearestNeighbor();
                graphics.setColor(Color.GREEN);
                graphics.drawLine((int) pair.getKeyPointA().getCoordinateX(), (int) pair.getKeyPointA().getCoordinateY(),
                        (int) (imageA.getResolutionX() + nearest.getCoordinateX()), (int) nearest.getCoordinateY());
            });
        } else {
            Random random = new Random();
            pairs.forEach(pair -> {
                KeyPoint nearest = pair.getKeyPointA().getNearestNeighbor();
                graphics.setColor(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat()));
                graphics.drawLine((int) pair.getKeyPointA().getCoordinateX(), (int) pair.getKeyPointA().getCoordinateY(),
                        (int) (imageA.getResolutionX() + nearest.getCoordinateX()), (int) nearest.getCoordinateY());
                graphics.fillOval((int) pair.getKeyPointA().getCoordinateX() - 2, (int) pair.getKeyPointA().getCoordinateY() - 2, 6, 4);
                graphics.fillOval((int) (imageA.getResolutionX() + nearest.getCoordinateX()) - 2, (int) nearest.getCoordinateY() - 2, 6, 4);
            });
        }
    }
}
