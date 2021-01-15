/**
 * Panel that summarizes the results and give the possibility to measure the mean intensity of the ROIs.
 *
 * @author Team Automated analysis of "islet in eye", Bioengineering department, Imperial College London
 *
 * Last modified: 11/01/2021
 */


package UI.DataTab;

import UI.Controller;
import UI.HomeTab.Home;
import videoprocessing.Video;
import videoprocessing.VideoProcessor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Results extends JPanel{
    JButton measureBtn;
    JPanel panDisc, panROI, panInt, btnPan;
    JLabel titleDisc, titleROI, titleInt;
    JLabel bodyDisc1, bodyDisc2, bodyROI1, bodyInt;

    VideoProcessor videoProcessor;

    private boolean meanIntensityMeasured;

    // Constructor
    public Results(boolean meanIntensityMeasured){
        this.meanIntensityMeasured = meanIntensityMeasured;
        // Create button
        measureBtn = new JButton("Measure intensity");
        measureBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                videoProcessor= Home.getVideoProcessor();
                videoProcessor.analyseCells();
                Home.setVideoProcessor(videoProcessor);
                Home.setMeanIntensityMeasured(true);
                Home.setDisplay();
            }
        });

        // Create panels
        panDisc = new JPanel(new GridLayout(3,1));
        panROI = new JPanel(new GridLayout(2,1));
        panInt = new JPanel(new GridLayout(3,1));
        btnPan = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Create title labels
        titleDisc = new JLabel("Discarded frames");
        titleDisc.setFont(new Font(titleDisc.getFont().getName(), Font.BOLD, 24));
        titleROI = new JLabel("Number of ROIs");
        titleROI.setFont(new Font(titleROI.getFont().getName(), Font.BOLD, 24));
        titleInt = new JLabel("Intensity");
        titleInt.setFont(new Font(titleInt.getFont().getName(), Font.BOLD, 24));

        // Create layout
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    }

    // Get data from analysed videos and display it on the screen
    public void showResults(){
        // Remove all components from the frame
        removeAll();
        panDisc.removeAll();
        panROI.removeAll();
        panInt.removeAll();

        // Get the processed video
        videoProcessor = Home.getVideoProcessor();
        Video video = videoProcessor.getVideo();

        // Create body labels
        bodyDisc1 = new JLabel("    The program identified "+video.getIdxFramesInFocus().size()+" frames without depth motion.");
        bodyDisc2 = new JLabel("    Used cross sectional error of: "+videoProcessor.getThresholdArea()+"%");
        bodyROI1 = new JLabel("    There are "+video.getCells().size()+" regions of interest in the uploaded video.");
        if(meanIntensityMeasured==false)
            bodyInt = new JLabel("    Mean intensities have not been measured yet!");
        else
            bodyInt = new JLabel("    Mean intensities have been measured and can be saved!");

        // Style labels
        Font fontBody = new Font(bodyDisc1.getFont().getFontName(),Font.PLAIN,15);
        bodyDisc1.setFont(fontBody);
        bodyDisc2.setFont(fontBody);
        bodyROI1.setFont(fontBody);
        bodyInt.setFont(fontBody);
        measureBtn.setFont(fontBody);

        // Add elements to sub-panels
        panDisc.add(titleDisc);
        panDisc.add(bodyDisc1);
        panDisc.add(bodyDisc2);

        panROI.add(titleROI);
        panROI.add(bodyROI1);

        panInt.add(titleInt);
        panInt.add(bodyInt);
        measureBtn.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        btnPan.add(measureBtn);
        panInt.add(btnPan);

        // Add sub-panels to main panel
        add(panDisc,BorderLayout.CENTER);
        add(Box.createHorizontalStrut(400));
        add(panROI,BorderLayout.CENTER);
        add(Box.createHorizontalStrut(400));
        add(panInt,BorderLayout.CENTER);
    }
}
