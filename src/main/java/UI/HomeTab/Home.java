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
    JLabel welcome;
    JLabel empty;
    JButton uploadBtn;
    JPanel subPanel;

    // Constructor
    public Home(){
        subPanel = getSubPanel();
        add(subPanel);
    }

    // Create sub-panel
    private JPanel getSubPanel(){
        // Create a welcome text
        welcome = new JLabel("Welcome!");
        welcome.setFont(new Font(welcome.getFont().getName(), Font.PLAIN, 30));
        welcome.setHorizontalAlignment(SwingConstants.CENTER);

        // Upload file for analysis
        uploadBtn = new JButton("Upload");
        uploadBtn.addActionListener(new UploadListener());

        // Spacing
        empty = new JLabel("");

        // Set layout
        setLayout(new FlowLayout(FlowLayout.CENTER, 500, 200));

        // Add components to sub-panel
        subPanel = new JPanel(new GridLayout(3,1));
        subPanel.add(welcome);
        subPanel.add(empty);
        subPanel.add(uploadBtn);

        return subPanel;
    }
}