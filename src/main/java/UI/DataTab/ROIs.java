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
                videoProcessor = Controller.getVideoProcessor();
            }
        });
        subPanel = new JPanel();
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
        subPanel.removeAll();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        // Add JFrame that hold imageDisp
        add(imgDisp,BorderLayout.PAGE_START);
        setSize(image.getWidth()+15, image.getHeight()+130);
        // Create JPanel to hold buttons in gridlayout
        add(text);
        add(addROI);
    }
}
