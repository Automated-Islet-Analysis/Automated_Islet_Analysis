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
    JTextField perError = new JTextField("10",5);
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


        // Popup selection of parameters
        int input = JOptionPane.showOptionDialog(null, mainPanel, "Customise analysis",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                options, null);

        if(input==0){
            // Convert the inputs to int or booleans to be used by function videoProcessor
            errorAllowed= Integer.parseInt(perError.getText());
            planarSelected= checkPlanar.isSelected();
            ROISelected= checkROI.isSelected();
            depthSelected= checkDepth.isSelected();

            process(errorAllowed, planarSelected, ROISelected, depthSelected);
        }else if(input==1){
            // help button
        }else if(input==2){
            // nothing to do close button
        }
    }


    private void process(double errorAllowed, boolean planarSelected, boolean ROISelected, boolean depthSelected){
        // Code for pop-up from https://riptutorial.com/swing/example/3977/create-a--please-wait-----popup
        final JDialog processing = new JDialog(Controller.interframe);
        JPanel p1 = new JPanel(new BorderLayout());
        JLabel jLabel = new JLabel("Please wait, processing in progress!");
        jLabel.setFont(new Font(jLabel.getFont().getName(),Font.PLAIN,20));
        p1.add(jLabel, BorderLayout.CENTER);
        processing.getContentPane().add(p1);
        processing.setUndecorated(true);
        processing.pack();
        processing.setLocationRelativeTo(Controller.interframe);
        processing.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        processing.setModal(true);

        SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws InterruptedException{
                VideoProcessor videoProcessor = new VideoProcessor(new Video(UploadListener.getFilePath()));
                videoProcessor.process((int) AnalyseListener.errorAllowed,AnalyseListener.planarSelected,
                        AnalyseListener.depthSelected,AnalyseListener.ROISelected);
                UserInterface.setVideoProcessor(videoProcessor);
                return  "Done";
            }
            @Override
            protected void done() {
                processing.dispose();
        }
        };
        worker.execute(); //here the process thread initiates
        processing.setVisible(true);
        try
        {
            worker.get(); //here the parent thread waits for completion
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}
