package UI;

import videoprocessing.Video;
import videoprocessing.VideoProcessor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class AnalyseListener implements ActionListener {
    JPanel mainPanel = new JPanel(new GridLayout(3,2));

    JLabel error = new JLabel("Allowed CS error (%)");
    JTextField perError = new JTextField(5);
    JCheckBox checkPlanar = new JCheckBox("Planar motion correction");
    JCheckBox checkDepth = new JCheckBox("Depth motion correction");
    JCheckBox checkROI = new JCheckBox("Find ROIs");

    Object[] options = {"Ok", "Help", "Cancel"};

    @Override

    public void actionPerformed(ActionEvent e) {
        mainPanel.add(error);
        mainPanel.add(perError);
        mainPanel.add(checkPlanar);
        mainPanel.add(checkROI);
        mainPanel.add(checkDepth);


        // Popup.
        JOptionPane.showOptionDialog(null, mainPanel, "Customise analysis",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                options, null);


        // Convert the inputs to int or booleans to be used by function videoProcessor
        int errorAllowed= Integer.parseInt(String.valueOf(perError));
        boolean planarSelected= checkPlanar.isSelected();
        boolean ROISelected= checkROI.isSelected();
        boolean depthSelected= checkDepth.isSelected();

        //get the path of the video
        String filePath= UploadListener.getFilePath();


        //VideoProcessor videoProcessor=Main.getVideoProcessor();


        //Create a videoprocessor object and initiate it with the path:
        VideoProcessor videoProcessor = new VideoProcessor(new Video(filePath));
        videoProcessor.process(errorAllowed,planarSelected, ROISelected, depthSelected);

        //Set the video processor with the values given by user:
        Controller.setVideoProcessor(videoProcessor);

    }
}
