/**
 * Class that deals with the action performed when the analyze button is pressed
 *
 * @author Team Automated analysis of "islet in eye", Bioengineering department, Imperial College London
 *
 * Last modified: 11/01/2021
 */

package UI.HomeTab;

import UI.Controller;
import videoprocessing.Video;
import videoprocessing.VideoProcessor;
import videoprocessing.VideoProcessorError;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProcessListener implements ActionListener {
    JPanel mainPanel = new JPanel(new GridLayout(3,2));

    // Components to allow personalization of the analysis
    JLabel error = new JLabel("Allowed change in cross section area(%)  ");
    JTextField perError = new JTextField("10",5); // find a way of getting jtext field text
    JCheckBox checkPlanar = new JCheckBox("Planar motion correction");
    JCheckBox checkDepth = new JCheckBox("Depth motion correction");
    JCheckBox checkROI = new JCheckBox("Find regions of interest");

    // Options for pop-up
    Object[] options = {"Ok", "Help", "Cancel"};

    // Keep track of selected parameters
    static double errorAllowed;
    static boolean planarSelected;
    static boolean ROISelected;
    static boolean depthSelected;

    @Override
    // Add all components to the pop-up dialog panel
    public void actionPerformed(ActionEvent e) {
        mainPanel.add(error);
        mainPanel.add(perError);
        mainPanel.add(checkPlanar);
        mainPanel.add(checkROI);
        mainPanel.add(checkDepth);

        error.setName("errorLabel");
        perError.setName("perError");
        checkPlanar.setName("checkPlanar");
        checkROI.setName("checkROI");
        checkDepth.setName("checkDepth");

        pop_up();
    }

    // Set up the pop-up dialog
    private void pop_up(){
        // Call pop-up
        int input = JOptionPane.showOptionDialog(Home.getInterframe(), mainPanel, "Customise analysis",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                options, null);

        if(input==0){ // Ok option button
            // Convert the inputs to int or booleans to be used by function videoProcessor
            try{
                errorAllowed = Integer.parseInt(perError.getText());
                planarSelected= checkPlanar.isSelected();
                ROISelected= checkROI.isSelected();
                depthSelected= checkDepth.isSelected();

                process();
                Home.setAnalysedImg(true); // Allow clicking of "Data" and "Save" tabs

            } catch (NumberFormatException ex){
                // Catch exception for non-integer input
                JOptionPane.showMessageDialog(Home.getInterframe(), "Please enter an integer");
            }

        }else if(input==1){ // Help option button
            System.out.println(input);
            JOptionPane.showMessageDialog(Home.getInterframe(), "Choose the change in cross-sectional area allowed of the islet (suggested value: 10%).\n" +
                    "This is a measure of motion in the depth direction and frames with a cross-sectional area outside of this range will be discarded. " +
                    "Additionally, choose if you want to perform the different motion corrections and the automatic search of ROIs");
            pop_up();
        }else if(input==2){ // Close option button
            // Nothing to do, just close pop-up
        }
    }

    //
    private void process(){
        // Create components of message to user
        final JDialog dialog = new JDialog();
        JPanel panel = new JPanel(new FlowLayout());
        JLabel text = new JLabel();
        text.setText("The video is being processed, please wait!");
        Font font = text.getFont();
        text.setFont(new Font(font.getFontName(),Font.PLAIN,20));

        final VideoProcessorError[] videoProcessorError = new VideoProcessorError[1];

        SwingWorker<Void, Void> worker = new SwingWorker<Void,Void>() {
            @Override
            // Call video processor and analyse taking into account parameters inputed in pop-up
            protected Void doInBackground() throws InterruptedException{
                try {
                    System.out.println("Processing: " + Uploaded.getFilePath());
                    VideoProcessor videoProcessor = new VideoProcessor(new Video(Uploaded.getFilePath()));
                    videoProcessorError[0] = videoProcessor.process((int) ProcessListener.errorAllowed, ProcessListener.planarSelected, ProcessListener.depthSelected, ProcessListener.ROISelected);
                    Home.setVideoProcessor(videoProcessor);
                }catch (OutOfMemoryError e){
                    videoProcessorError[0] = VideoProcessorError.VIDEO_PROCESSOR_OUT_OF_MEMORY_ERROR;
                }
                return null;
            }

            @Override
            // Notify user that processing is done
            protected void done() {
                dialog.setVisible(false);
                if(videoProcessorError[0]==VideoProcessorError.VIDEO_PROCESSOR_SUCCESS){
                    System.out.println("Processing done");
                    JOptionPane.showMessageDialog(Home.getInterframe(),"The processing is finished.\n" +
                            " You can find the different results of the processing in the data tab.\n These results can be saved in the save tab.");
                }else if (videoProcessorError[0]==VideoProcessorError.VIDEO_PROCESSOR_NOT_VIDEO_ERROR) {
                    System.out.println(videoProcessorError[0]);
                    JOptionPane.showOptionDialog(Home.getInterframe(), "ERROR, the processing failed because the video is either corrupt or the video has only one frame!",
                            "Warning",
                            JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
                }else if(videoProcessorError[0]==VideoProcessorError.VIDEO_PROCESSOR_OUT_OF_BOUNDS_ERROR){
                    System.out.println(videoProcessorError[0]);
                    JOptionPane.showOptionDialog(Home.getInterframe(), "ERROR, the processing failed because the Islet is too close too the edge of the frame!",
                            "Warning",
                            JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
                }else if(videoProcessorError[0]==VideoProcessorError.VIDEO_PROCESSOR_TEMP_ERROR){
                    System.out.println(videoProcessorError[0]);
                    JOptionPane.showOptionDialog(Home.getInterframe(), "ERROR, the processing failed because temporary files could not be created. " +
                                    "Change position of directory or try to delete temp.tiff and temp.nii files if present in directory!",
                            "Warning",
                            JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
                }else if(videoProcessorError[0]==VideoProcessorError.VIDEO_PROCESSOR_OUT_OF_MEMORY_ERROR){
                    System.out.println(videoProcessorError[0]);
                    JOptionPane.showOptionDialog(Home.getInterframe(), "ERROR, the processing failed because the momory allocated to this app is too small. Analyse a shorter video or change memory allocated!",
                            "Warning",
                            JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
                }
            }
        };

        // Style dialog
        dialog.setUndecorated(true);
        panel.add(text);
        dialog.add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(Home.getInterframe());

        // Start the swing worker
        worker.execute();
        dialog.setVisible(true);
    }
}
