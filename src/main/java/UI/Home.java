package UI;

import UI.UploadListener;

import javax.swing.*;
import java.awt.*;

public class Home extends JPanel {
    JLabel welcome;
    JLabel empty;
    JButton button;
    JPanel subPanel;

    public Home(){
        // Create a welcome text
        welcome = new JLabel("Welcome!");
        welcome.setFont(new Font(welcome.getFont().getName(), Font.PLAIN, 30));
        welcome.setHorizontalAlignment(SwingConstants.CENTER);

        // Upload file for analysis
        button = new JButton("Upload");
        button.addActionListener(new UploadListener());

        // Spacing
        empty = new JLabel("");

        // Set layout
        setLayout(new FlowLayout(FlowLayout.CENTER, 500, 200));

        subPanel = new JPanel(new GridLayout(3,1));
        subPanel.add(welcome);
        subPanel.add(empty);
        subPanel.add(button);

        add(subPanel);
    }
}