package DataTab;




//import com.sun.media.controls.VFlowLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ROIs extends JPanel {
    JButton addROI;
    JPanel subPanel,addROIPanel,textPanel;
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
//        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 50));
        //add(image);
        setLayout(new BorderLayout());
        add(Box.createVerticalStrut(500),BorderLayout.NORTH); // Spacing
        addROIPanel=new JPanel();
        addROIPanel.add(addROI);
        textPanel=new JPanel();
        textPanel.add(text);

        subPanel.add(textPanel);
        subPanel.add(addROIPanel);

        addROI.setName("addROIButton");

        add(subPanel,BorderLayout.CENTER);
    }
}
