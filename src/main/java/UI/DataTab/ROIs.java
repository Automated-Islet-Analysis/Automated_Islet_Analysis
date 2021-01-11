package UI.DataTab;

import UI.Controller;
import UI.Panel.ImagePanel;
import videoprocessing.VideoProcessor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ROIs extends ImagePanel {
    private JButton addROI;
<<<<<<< HEAD
    private JLabel text, image;
    private ImageIcon imgIcon;
=======
    private JPanel subPanel;
    private JLabel text;
>>>>>>> 41aec54c48d8f4a439de9a060d43c840e387c78d

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
                videoProcessor = Controller.getVideoProcessor();
            }
        });
    }

    @Override
    public void updatePanel(){
        videoProcessor = Controller.getVideoProcessor();
        numROI = videoProcessor.getVideo().getCells().size();
        image = videoProcessor.getRoiImage().getBufferedImage();

        if(! (image ==null))
            image = resizeImage(image,15,130, Controller.getInterframe());

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
<<<<<<< HEAD
        image.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(image);
        setSize(imageROI.getWidth()+15,imageROI.getHeight()+130);
        add(Box.createVerticalStrut(10));
        text.setAlignmentX(Component.CENTER_ALIGNMENT);
=======
        // Add JFrame that hold imageDisp
        add(imgDisp,BorderLayout.PAGE_START);
        setSize(image.getWidth()+15, image.getHeight()+130);
        // Create JPanel to hold buttons in gridlayout
>>>>>>> 41aec54c48d8f4a439de9a060d43c840e387c78d
        add(text);
        add(Box.createVerticalStrut(10));
        addROI.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(addROI);
    }
}
