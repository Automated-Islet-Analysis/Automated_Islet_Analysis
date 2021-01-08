import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class AnalyseListener implements ActionListener {
    JPanel mainPanel = new JPanel(new GridLayout(3, 2));

    JLabel error = new JLabel("Allowed CS error (%)");
    JTextField perError = new JTextField("10", 5); // find a way of getting jtext field text
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
        perError.setName("perError");
        checkPlanar.setName("checkPlanar");
        checkDepth.setName("checkDepth");
        checkROI.setName("checkROI");

        mainPanel.add(error);
        mainPanel.add(perError);
        mainPanel.add(checkPlanar);
        mainPanel.add(checkROI);
        mainPanel.add(checkDepth);


        // Popup.
        pop_up();
    }

    private void pop_up() {
        // Popup selection of parameters
        int input = JOptionPane.showOptionDialog(null, mainPanel, "Customise analysis",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                options, null);

        if (input == 0) {
            // Convert the inputs to int or booleans to be used by function videoProcessor
            try {
                errorAllowed = Integer.parseInt(perError.getText());
                planarSelected = checkPlanar.isSelected();
                ROISelected = checkROI.isSelected();
                depthSelected = checkDepth.isSelected();

                process();
                Home.analysedImg = true; // Allow clicking of "Data" and "Save" tabs

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter an integer");
            }
        } else if (input == 1) {
            // help button
            System.out.println(input);
            JOptionPane.showMessageDialog(null, "Choose the allowed margin of difference in cross-sectional area of the islet in between frames (suggested value: 10%)");
            pop_up();
        } else if (input == 2) {
            // nothing to do close button
        }
    }

    private void process() {
        final JDialog dialog = new JDialog();
        JPanel panel = new JPanel(new FlowLayout());
        JLabel text = new JLabel();
        text.setText("The video is being processed, please wait!");
        Font font = text.getFont();
        text.setFont(new Font(font.getFontName(), Font.PLAIN, 20));

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws InterruptedException {
                System.out.println("Analysing: " + Uploaded.getFilePath());
                VideoProcessor videoProcessor = new VideoProcessor(new Video(Uploaded.getFilePath()));
                videoProcessor.process((int) AnalyseListener.errorAllowed, AnalyseListener.planarSelected, AnalyseListener.depthSelected, AnalyseListener.ROISelected);
                UserInterface.setVideoProcessor(videoProcessor);
                return null;
            }

            @Override
            protected void done() {
                System.out.println("Processing done");
                dialog.setVisible(false);
                JOptionPane.showMessageDialog(Home.subPanel, "The processing is finished");
            }
        };

        dialog.setUndecorated(true);
        panel.add(text);
        dialog.add(panel);
        dialog.pack();
        Home.subPanel.repaint();
        dialog.setLocationRelativeTo(Home.subPanel);
        Home.subPanel.invalidate();
        Home.subPanel.validate();

        worker.execute(); //here the process thread initiates
        dialog.setVisible(true);
    }
}
