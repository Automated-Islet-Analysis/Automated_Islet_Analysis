/**
 * ImagePanel used for display of the analysed islet with its region of interest shown with numbers.
 *
 * @author Team Automated analysis of "islet in eye", Bioengineering department, Imperial College London
 *
 * Last modified: 11/01/2021
 */

package UI.DataTab;

import UI.Controller;
import UI.HomeTab.Home;
import UI.Panel.ImagePanel;
import videoprocessing.VideoProcessor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ROIs extends ImagePanel {
    private JButton addROI;
    private JPanel subPanel;
    private JLabel text;

    private int numROI;
    private int cellSize;
    private VideoProcessor videoProcessor;

    public ROIs(){
        super();
        // Create elements
        addROI = new JButton("Add ROIs");
        addROI.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add cells manually
                ManualROISelection select = new ManualROISelection(videoProcessor.getRoiImage().getBufferedImage(),numROI,cellSize);
                select.run();
                // Update VideoProcessor after adding cells
                videoProcessor = Home.getVideoProcessor();
            }
        });
    }

    @Override
    public void updatePanel(){
        videoProcessor = Home.getVideoProcessor();
        numROI = videoProcessor.getVideo().getCells().size();
        image = videoProcessor.getRoiImage().getBufferedImage();

        if(! (image ==null))
            image = resizeImage(image,15,130, Home.getInterframe());

        cellSize = videoProcessor.getCellSize();

        text = new JLabel("Number of ROIs: " + numROI);
        Font font =new Font(text.getFont().getFontName(),Font.PLAIN,15);
        text.setFont(font);
        addROI.setFont(font);

        imgIcon = new ImageIcon(image);
        imgDisp = new JLabel(imgIcon);

        removeAll();

        // Add components to the panel
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//        image.setAlignmentX(Component.CENTER_ALIGNMENT);
//        add(image);
//        setSize(imageROI.getWidth()+15,imageROI.getHeight()+130);
        add(Box.createVerticalStrut(10));
        text.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Add JFrame that hold imageDisp
        add(imgDisp,BorderLayout.PAGE_START);
        setSize(image.getWidth()+15, image.getHeight()+130);
        // Create JPanel to hold buttons in gridlayout
        add(text);
        add(Box.createVerticalStrut(10));
        addROI.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(addROI);
    }
}