import javax.swing.*;
import java.util.ArrayList;

public class Frame extends JFrame {
    public Frame(ImageProperties imageA, ImageProperties imageB, ArrayList<Pair> pairs, ArrayList<Pair> ransac, boolean isRansac) {
        super("Image Analysis");

        JPanel panel = new Panel(imageA, imageB, pairs, ransac, isRansac);
        JScrollPane scrollPane = new JScrollPane(panel,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        this.add(scrollPane);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }
}
