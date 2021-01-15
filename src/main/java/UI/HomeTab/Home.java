/**
 * Panel of the home screen where you can upload a file.
 *
 * @author Team Automated analysis of "islet in eye", Bioengineering department, Imperial College London
 *
 * Last modified: 11/01/2021
 */

package UI.HomeTab;

import javax.swing.*;
import java.awt.*;

public class Home extends JPanel {
    // Create components
    JLabel welcome, description1, description2;
    JButton uploadBtn;

    // Constructor
    public Home(){
        // Set layout
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        updateDisplay();
    }

    // Display components
    private void updateDisplay() {
        // Create a welcome text
        welcome = new JLabel("Welcome!");
        welcome.setFont(new Font(welcome.getFont().getName(), Font.PLAIN, 30));
        welcome.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        // Brief description of software functionality
        description1 = new JLabel("Upload a TIFF video and start processing an islet.");
        description2 = new JLabel("You can motion correct the video, identify regions of interest and measure their intensity.");
        description1.setFont(new Font(description1.getFont().getName(), Font.PLAIN, 18));
        description2.setFont(new Font(description2.getFont().getName(), Font.PLAIN, 18));
        description1.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        description2.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        // Upload file for analysis
        uploadBtn = new JButton("Upload video");
        uploadBtn.setFont(new Font(description1.getFont().getName(), Font.PLAIN, 18));
        uploadBtn.addActionListener(new UploadListener());
        uploadBtn.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        // Add components to panel
        add(Box.createVerticalStrut(150));
        add(welcome);
        add(Box.createVerticalStrut(50));
        add(description1);
        add(description2);
        add(Box.createVerticalStrut(50));
        add(uploadBtn);
    }

}