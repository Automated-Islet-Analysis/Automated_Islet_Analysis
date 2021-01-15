/**
 * ImagePanel used for display of the analysed islet with its regions of interest shown with numbers.
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
    private JLabel text;

    private int numROI;
    private int cellSize;
    private VideoProcessor videoProcessor;

    // Constructor
    public ROIs(){
        super(15,145);

        // Create elements
        addROI = new JButton("Add a region of interest");
        addROI.setName("addROI");
        addROI.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add cells manually
                ManualROISelection addROI = new ManualROISelection(videoProcessor.getRoiImage().getBufferedImage(),numROI,cellSize);
                addROI.run();
                // Update VideoProcessor after adding cells
                videoProcessor = Home.getVideoProcessor();
            }
        });
    }

    @Override
    // Display elements on frame
    public void updatePanel(){
        // Get image from video
        videoProcessor = Home.getVideoProcessor();
        numROI = videoProcessor.getVideo().getCells().size();
        image = videoProcessor.getRoiImage().getBufferedImage();

        if(! (image ==null))
            image = resizeImage(image, Home.getInterframe());

        cellSize = videoProcessor.getCellSize();

        // Create label to display number of ROIs
        text = new JLabel("Number of regions of interest: " + numROI);
        Font font =new Font(text.getFont().getFontName(),Font.PLAIN,15);
        text.setFont(font);
        addROI.setFont(font);

        // Create image label to display image
        imgIcon = new ImageIcon(image);
        imgDisp = new JLabel(imgIcon);

        removeAll();

        // Add components to the panel and center-align them
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        imgDisp.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(imgDisp);
        setSize(imgDisp.getWidth()+15,imgDisp.getHeight()+145);
        add(Box.createVerticalStrut(10));

        text.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(text);
        add(Box.createVerticalStrut(10));
        addROI.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(addROI);
    }
}