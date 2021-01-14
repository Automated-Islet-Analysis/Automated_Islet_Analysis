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

public class AnalyseListener implements ActionListener {
    JPanel mainPanel = new JPanel(new GridLayout(3,2));

    JLabel error = new JLabel("Allowed CS error (%)");
    JTextField perError = new JTextField("10",5); // find a way of getting jtext field text
    JCheckBox checkPlanar = new JCheckBox("Planar motion correction");
    JCheckBox checkDepth = new JCheckBox("Depth motion correction");
    JCheckBox checkROI = new JCheckBox("Find ROIs");

    Object[] options = {"Ok", "Help", "Cancel"};

    static double errorAllowed;
    static boolean planarSelected;
    static boolean ROISelected;
    static boolean depthSelected;

    @Override
    public void actionPerformed(ActionEvent e) {
        mainPanel.add(error);
        mainPanel.add(perError);
        mainPanel.add(checkPlanar);
        mainPanel.add(checkROI);
        mainPanel.add(checkDepth);

        pop_up();
    }


    private void pop_up(){
        // Popup selection of parameters
        int input = JOptionPane.showOptionDialog(Controller.getInterframe(), mainPanel, "Customise analysis",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                options, null);

        if(input==0){
            // Convert the inputs to int or booleans to be used by function videoProcessor
            try{
            errorAllowed = Integer.parseInt(perError.getText());
            planarSelected= checkPlanar.isSelected();
            ROISelected= checkROI.isSelected();
            depthSelected= checkDepth.isSelected();

            process();
            Controller.setAnalysedImg(true); // Allow clicking of "Data" and "Save" tabs

            } catch (NumberFormatException ex){
                JOptionPane.showMessageDialog(Controller.getInterframe(), "Please enter an integer");
            }

        }else if(input==1){
            // help button
            System.out.println(input);
            JOptionPane.showMessageDialog(Controller.getInterframe(), "Choose percentual error allowed for cross-sectional area of the islet (suggested value: 10%).\n" +
                    "Frames with a cross-sectional area outside of this range will be discarded.");
            pop_up();
        }else if(input==2){
            // nothing to do close button
        }
    }

    private void process(){
        final JDialog dialog = new JDialog(Controller.getInterframe(),"",true);
        JPanel panel = new JPanel(new FlowLayout());
        JLabel text = new JLabel();
        text.setText("The video is being processed, please wait!");
        Font font = text.getFont();
        text.setFont(new Font(font.getFontName(),Font.PLAIN,20));

        final VideoProcessorError[] videoProcessorError = new VideoProcessorError[1];

        SwingWorker<Void, Void> worker = new SwingWorker<Void,Void>() {
            @Override
            protected Void doInBackground() throws InterruptedException{
                try {
                    System.out.println("Analysing: " + Uploaded.getFilePath());
                    VideoProcessor videoProcessor = new VideoProcessor(new Video(Uploaded.getFilePath()));
                    videoProcessorError[0] = videoProcessor.process((int) AnalyseListener.errorAllowed, AnalyseListener.planarSelected, AnalyseListener.depthSelected, AnalyseListener.ROISelected);
                    Controller.setVideoProcessor(videoProcessor);
                }catch (OutOfMemoryError e){
                    videoProcessorError[0] = VideoProcessorError.VIDEO_PROCESSOR_OUT_OF_MEMORY_ERROR;
                }
                return null;
            }

            @Override
            protected void done() {
                dialog.setVisible(false);
                if(videoProcessorError[0]==VideoProcessorError.VIDEO_PROCESSOR_SUCCESS){
                    System.out.println("Processing done");
                    JOptionPane.showMessageDialog(Controller.getInterframe(),"The processing is finished");
                }else if (videoProcessorError[0]==VideoProcessorError.VIDEO_PROCESSOR_NOT_VIDEO_ERROR) {
                    System.out.println(videoProcessorError[0]);
                    JOptionPane.showOptionDialog(Controller.getInterframe(), "ERROR, the processing failed because the video is either corrupt or the video has only one frame!",
                            "Warning",
                            JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
                }else if(videoProcessorError[0]==VideoProcessorError.VIDEO_PROCESSOR_OUT_OF_BOUNDS_ERROR){
                    System.out.println(videoProcessorError[0]);
                    JOptionPane.showOptionDialog(Controller.getInterframe(), "ERROR, the processing failed because the Islet is too close too the edge of the frame!",
                            "Warning",
                            JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
                }else if(videoProcessorError[0]==VideoProcessorError.VIDEO_PROCESSOR_TEMP_ERROR){
                    System.out.println(videoProcessorError[0]);
                    JOptionPane.showOptionDialog(Controller.getInterframe(), "ERROR, the processing failed because temporary files could not be created. " +
                                    "Change position of directory or try to delete temp.tiff and temp.nii files if present in directory!",
                            "Warning",
                            JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
                }else if(videoProcessorError[0]==VideoProcessorError.VIDEO_PROCESSOR_OUT_OF_MEMORY_ERROR){
                    System.out.println(videoProcessorError[0]);
                    JOptionPane.showOptionDialog(Controller.getInterframe(), "ERROR, the processing failed because the momory allocated to this app is too small. Analyse a shorter video or change memory allocated!",
                            "Warning",
                            JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
                }
            }
        };

        dialog.setUndecorated(true);
        panel.add(text);
        dialog.add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(Controller.getInterframe());

        worker.execute(); //here the process thread initiates
        dialog.setVisible(true);
    }
}
