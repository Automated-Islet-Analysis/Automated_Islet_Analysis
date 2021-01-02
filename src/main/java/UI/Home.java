package UI;

import javax.swing.*;
import java.awt.*;

public class Home extends JPanel {
    JLabel welcome;
    JLabel empty;
    JButton uploadBtn;
    JPanel subPanel;

    public Home(){
        subPanel = getSubPanel();
        add(subPanel);
    }

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

        subPanel = new JPanel(new GridLayout(3,1));
        subPanel.add(welcome);
        subPanel.add(empty);
        subPanel.add(uploadBtn);

        return subPanel;
    }
}