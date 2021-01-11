package UI.DataTab;

import UI.Controller;
import UI.HomeTab.Home;
import UI.Panel.ImagePanel;
import videoprocessing.VideoProcessor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class ROIs extends ImagePanel {
    private JButton addROI;
    private JPanel subPanel;
    private JLabel text, image;
    private ImageIcon imgIcon;

    private int numROI;
    private BufferedImage imageROI;
    private int cellSize;
    private VideoProcessor videoProcessor;

    public ROIs(){
        // Create elements
        addROI = new JButton("Add ROIs");
        addROI.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ManualROISelection select = new ManualROISelection(videoProcessor.getRoiImage().getBufferedImage(),numROI,cellSize);
                select.run();
                videoProcessor = Home.getVideoProcessor();
            }
        });
        subPanel = new JPanel();
    }

    public void updatePanel(){
        videoProcessor = Home.getVideoProcessor();

        numROI = videoProcessor.getVideo().getCells().size();
        imageROI = videoProcessor.getRoiImage().getBufferedImage();


        if(! (imageROI==null))
            imageROI = resizeImage(imageROI,15,130);

        cellSize = videoProcessor.getCellSize();
        text = new JLabel("Number of ROIs: " + numROI);
        Font font =new Font(text.getFont().getFontName(),Font.PLAIN,15);
        text.setFont(font);
        addROI.setFont(font);
        imgIcon = new ImageIcon(imageROI);
        image = new JLabel(imgIcon);
        removeAll();
        subPanel.removeAll();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        // Add JFrame that hold image
        add(image,BorderLayout.PAGE_START);
        setSize(imageROI.getWidth()+15,imageROI.getHeight()+130);
        // Create JPanel to hold buttons in gridlayout
        add(text);
        add(addROI);
    }
}