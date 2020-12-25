package DataTab;

import com.sun.media.controls.VFlowLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ROIs extends JPanel {
    JButton addROI;
    JPanel subPanel;
    JLabel text, image;
    ImageIcon imgIcon;

    public ROIs(){
        // Create elements
        addROI = new JButton("Add ROIs");
        text = new JLabel("Number of ROIs:           ");

        // imgIcon = new ImageIcon(path to image)
        // image = new JLabel(imgIcon);
        subPanel = new JPanel(new GridLayout(1,2));

        // Add elements to main panel
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 50));
        //add(image);
        add(Box.createHorizontalStrut(500)); // Spacing
        subPanel.add(text);
        subPanel.add(addROI);
        add(subPanel);
    }
}
