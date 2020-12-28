package UI;

import UI.AnalyseListener;
import UI.UploadListener;

import javax.swing.*;
import java.awt.*;

public class Uploaded extends JPanel {
    static JLabel filename;
    static JPanel imgPanel;
    JButton btnUpload, btnAnalyse;
    JPanel subHPanel;

    public Uploaded(){
        // Empty img panel
        imgPanel = new JPanel();
        // Label for the text
        filename = new JLabel("filename");
        filename.setFont(new Font(filename.getFont().getName(), Font.PLAIN, 30));
        filename.setHorizontalAlignment(SwingConstants.CENTER);

        // Two buttons. Upload to change uploaded file. Analyse to process the file.
        btnUpload = new JButton("Upload");
        btnUpload.addActionListener(new UploadListener());
        btnAnalyse = new JButton("Analyse");
        btnAnalyse.addActionListener(new AnalyseListener());

        // Set layout
        setLayout(new FlowLayout(FlowLayout.CENTER, 200, 30));
        add(imgPanel);
        add(filename);

        // Place buttons side by side
        subHPanel = new JPanel(new GridLayout(1,2));
        subHPanel.add(btnUpload);
        subHPanel.add(btnAnalyse);
        add(subHPanel);
    }
}