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

    public String filePath= UploadListener.getFile();


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


        //Call the video processor
        VideoProcessor videoProcessor= new VideoProcessor(new Video(filePath));
        videoProcessor.process(10,true,true,true);
        Controller.setVideoProcessor(videoProcessor);



        // Apply processing on the video
        videoProcessor.process(10,true,true,true);
        videoProcessor.analyseCells();
        videoProcessor.saveSummary();
    }

}
