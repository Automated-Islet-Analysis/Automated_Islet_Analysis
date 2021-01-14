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

    // Components to allow personalization of the analysis
    JLabel error = new JLabel("Allowed CS error (%)");
    JTextField perError = new JTextField("10",5); // find a way of getting jtext field text
    JCheckBox checkPlanar = new JCheckBox("Planar motion correction");
    JCheckBox checkDepth = new JCheckBox("Depth motion correction");
    JCheckBox checkROI = new JCheckBox("Find ROIs");

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

        pop_up();
    }

    // Set up the pop-up dialog
    private void pop_up(){
        // Call pop-up
        int input = JOptionPane.showOptionDialog(Controller.getInterframe(), mainPanel, "Customise analysis",
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
            Controller.setAnalysedImg(true); // Allow clicking of "Data" and "Save" tabs

            } catch (NumberFormatException ex){
                // Catch exception for non-integer input
                JOptionPane.showMessageDialog(Controller.getInterframe(), "Please enter an integer");
            }

        }else if(input==1){ // Help option button
            System.out.println(input);
            JOptionPane.showMessageDialog(Controller.getInterframe(), "Choose percentual error allowed for cross-sectional area of the islet (suggested value: 10%).\n" +
                    "Frames with a cross-sectional area outside of this range will be discarded.");
            pop_up();
        }else if(input==2){ // Close option button
            // Nothing to do, just close pop-up
        }
    }

    //
    private void process(){
        // Create components of message to user
        final JDialog dialog = new JDialog(Controller.getInterframe(),"",true);
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
                VideoProcessor videoProcessor = new VideoProcessor(new Video(Uploaded.getFilePath()));
                videoProcessorError[0] = videoProcessor.process((int) AnalyseListener.errorAllowed, AnalyseListener.planarSelected, AnalyseListener.depthSelected, AnalyseListener.ROISelected);
                Controller.setVideoProcessor(videoProcessor);
                return null;
            }

            @Override
            // Notify user that processing is done
            protected void done() {
                dialog.setVisible(false);
                JOptionPane.showMessageDialog(Controller.getInterframe(),"The processing is finished");
            }
        };

        // Style dialog
        dialog.setUndecorated(true);
        panel.add(text);
        dialog.add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(Controller.getInterframe());

        // Start the swing worker
        worker.execute();
        dialog.setVisible(true);
    }
}
