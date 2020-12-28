package UI.DataTab;




//import com.sun.media.controls.VFlowLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ROIs extends JPanel {
    JButton addROI;
    JPanel subPanel;
    JLabel text, image;
    ImageIcon imgIcon;

    public ROIs(){
        // Create elements
        addROI = new JButton("Add ROIs");
        addROI.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                int numROI = number of already present ROI on image
//                BufferedImage image =  ImageIO.read(path to image);
//                ManualROISelection select = new ManualROISelection(image,numROI);
//                select.run();
            }
        });
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
