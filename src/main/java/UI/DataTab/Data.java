package UI.DataTab;
import UI.Controller;
import videoprocessing.VideoProcessor;

import javax.swing.*;
import java.awt.*;

public class Data extends JPanel{
    JButton measureBtn;
    JPanel panDisc, panROI, panInt;
    JLabel titleDisc, titleROI, titleInt;
    JLabel bodyDisc1, bodyDisc2, bodyROI1, bodyROI2, bodyInt;;

    public Data(){

        //Get the video processor and set it with the analyze cell function
        VideoProcessor videoProcessor;
        videoProcessor=Controller.getVideoProcessor();
        videoProcessor.analyseCells();
        Controller.setVideoProcessor(videoProcessor);




        // Create button
        measureBtn = new JButton("Measure intensity");

        // Create panels
        panDisc = new JPanel(new GridLayout(3,1));
        panROI = new JPanel(new GridLayout(3,1));
        panInt = new JPanel(new GridLayout(3,1));

        // Create title labels
        titleDisc = new JLabel("Discarded frames");
        titleDisc.setFont(new Font(titleDisc.getFont().getName(), Font.BOLD, 20));
        titleROI = new JLabel("Number of ROIs");
        titleROI.setFont(new Font(titleROI.getFont().getName(), Font.BOLD, 20));
        titleInt = new JLabel("Intensity");
        titleInt.setFont(new Font(titleInt.getFont().getName(), Font.BOLD, 20));

        // Create body labels
        bodyDisc1 = new JLabel("Used percentage cross sectional error of: ...");
        bodyDisc2 = new JLabel("The program has discarded ... frames");
        bodyROI1 = new JLabel("There are ... regions of interest in the uploaded video: ");
        bodyROI2 = new JLabel( "... found by the program and ... added by the user");
        bodyInt = new JLabel("The average intensity of each ROI is:");

        // Add elements to sub panels
        panDisc.add(titleDisc);
        panDisc.add(bodyDisc1);
        panDisc.add(bodyDisc2);

        panROI.add(titleROI);
        panROI.add(bodyROI1);
        panROI.add(bodyROI2);

        panInt.add(titleInt);
        panInt.add(bodyInt);

        // Add elements to main JPanel
        setLayout(new FlowLayout(FlowLayout.LEFT, 20, 30));
        add(measureBtn);
        add(Box.createHorizontalStrut(400));
        add(panDisc);
        add(Box.createHorizontalStrut(400));
        add(panROI);
        add(Box.createHorizontalStrut(400));
        add(panInt);

    }
}
