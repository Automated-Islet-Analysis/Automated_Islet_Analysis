package UI.DataTab;
import UI.Controller;
import UI.UserInterface;
import videoprocessing.Video;
import videoprocessing.VideoProcessor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Results extends JPanel{
    JButton measureBtn;
    JPanel panDisc, panROI, panInt;
    JLabel titleDisc, titleROI, titleInt;
    JLabel bodyDisc1, bodyDisc2, bodyROI1, bodyInt;

    VideoProcessor videoProcessor;

    private boolean meanIntensityMeasured;

    public Results(boolean meanIntensityMeasured){
        this.meanIntensityMeasured = meanIntensityMeasured;
        // Create button
        measureBtn = new JButton("Measure intensity");
        measureBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                videoProcessor= UserInterface.getVideoProcessor();
                videoProcessor.analyseCells();
                UserInterface.setVideoProcessor(videoProcessor);
                Controller.meanIntensityMeasured=true;
                Controller.setDisplay();
            }
        });

        // Create panels
        panDisc = new JPanel(new GridLayout(3,1));
        panROI = new JPanel(new GridLayout(2,1));
        panInt = new JPanel(new GridLayout(3,1));

        // Create title labels
        titleDisc = new JLabel("Discarded frames");
        titleDisc.setFont(new Font(titleDisc.getFont().getName(), Font.BOLD, 24));
        titleROI = new JLabel("Number of ROIs");
        titleROI.setFont(new Font(titleROI.getFont().getName(), Font.BOLD, 24));
        titleInt = new JLabel("Intensity");
        titleInt.setFont(new Font(titleInt.getFont().getName(), Font.BOLD, 24));

        // Create layout
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

//        if (meanIntensityMeasured == false){
//            helpText1 = new JLabel("Before analysis, make sure all desired ROIs have been selected.");
//            helpText2 = new JLabel("To do so, go to Data->ROIs.");
//            helpText1.setFont(new Font(helpText1.getFont().getFontName(),Font.PLAIN,15));
//            helpText2.setFont(new Font(helpText2.getFont().getFontName(),Font.PLAIN,15));
//            add(helpText1);
//            add(helpText2);
//            add(Box.createHorizontalStrut(400));
//            add(measureBtn);
//        }
    }

    public void showResults(){
        removeAll();
        panDisc.removeAll();
        panROI.removeAll();
        panInt.removeAll();

        videoProcessor = UserInterface.getVideoProcessor();
        Video video = videoProcessor.getVideo();

        // Create body labels
        bodyDisc2 = new JLabel("    Used percentage cross sectional error of: "+videoProcessor.getThresholdArea());
        bodyDisc1 = new JLabel("    The program identified "+video.getIdxFramesInFocus().size()+" frames without depth motion.");
        bodyROI1 = new JLabel("    There are "+video.getCells().size()+" regions of interest in the uploaded video.");
        if(meanIntensityMeasured==false)
            bodyInt = new JLabel("    Mean intensities are not measured yet!");
        else
            bodyInt = new JLabel("    Mean intensities were measured and can be saved!");

        Font fontBody = new Font(bodyDisc1.getFont().getFontName(),Font.PLAIN,20);
        bodyDisc1.setFont(fontBody);
        bodyDisc2.setFont(fontBody);
        bodyROI1.setFont(fontBody);
        bodyInt.setFont(fontBody);
        measureBtn.setFont(fontBody);

        // Add elements to sub panels
        panDisc.add(titleDisc);
        panDisc.add(bodyDisc1);
        panDisc.add(bodyDisc2);

        panROI.add(titleROI);
        panROI.add(bodyROI1);

        panInt.add(titleInt);
        panInt.add(bodyInt);
        panInt.add(measureBtn);

        add(Box.createHorizontalStrut(400));
        add(panDisc,BorderLayout.CENTER);
        add(Box.createHorizontalStrut(400));
        add(panROI,BorderLayout.CENTER);
        add(Box.createHorizontalStrut(400));
        add(panInt,BorderLayout.CENTER);
    }
}
