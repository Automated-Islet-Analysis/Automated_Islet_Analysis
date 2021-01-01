import javax.swing.*;
import java.awt.*;

public class Home extends JPanel {
    JLabel welcomeLabel;
    JLabel emptyLabel;
    JButton uploadButton;
    JPanel subPanel;

    public Home(){
        subPanel = getSubPanel();
        add(subPanel);
    }

    private JPanel getSubPanel(){
        // Create a welcome text
        welcomeLabel = new JLabel("Welcome!");
        welcomeLabel.setFont(new Font(welcomeLabel.getFont().getName(), Font.PLAIN, 30));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Upload file for analysis
        uploadButton = new JButton("Upload");
        uploadButton.addActionListener(new UploadListener());

        // Spacing
        emptyLabel = new JLabel("");

        // Set layout
        setLayout(new FlowLayout(FlowLayout.CENTER, 500, 200));

        subPanel = new JPanel(new GridLayout(3,1));
        subPanel.add(welcomeLabel);
        subPanel.add(emptyLabel);
        subPanel.add(uploadButton);

        return subPanel;
    }
}