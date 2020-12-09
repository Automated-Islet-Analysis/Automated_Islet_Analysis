import javax.swing.*;
import java.awt.*;

public class Uploaded extends JPanel {
    JLabel welcome1, welcome2;
    JLabel empty;
    JButton btnUpload, btnAnalyse;
    JPanel subPanel, subsubPanel;
    static String filename;

    public Uploaded(){
        /*
        I want a changeable name for the second label that shows the name of the file name.
        Now it creates an empty label. Text doesn't change automatically.
        I need to create a function to update the text.
        */

        filename  = "";

        welcome1 = new JLabel("Name:");
        welcome2 = new JLabel(filename);
        welcome1.setFont(new Font(welcome1.getFont().getName(), Font.PLAIN, 30));
        welcome2.setFont(new Font(welcome2.getFont().getName(), Font.PLAIN, 30));
        welcome1.setHorizontalAlignment(SwingConstants.CENTER);
        welcome2.setHorizontalAlignment(SwingConstants.CENTER);

        // Two buttons. Upload to change uploaded file. Analyse to process the file.
        btnUpload = new JButton("Upload");
        btnUpload.addActionListener(new UploadListener());
        btnAnalyse = new JButton("Analyse");
        btnAnalyse.addActionListener(new AnalyseListener());

        // For spacing
        empty = new JLabel("");

        // Set layout
        setLayout(new FlowLayout(FlowLayout.CENTER, 500, 200));

        subPanel = new JPanel(new GridLayout(4,1));
        subPanel.add(welcome1);
        subPanel.add(welcome2);
        subPanel.add(empty);

        subsubPanel = new JPanel(new GridLayout(1,2));
        subsubPanel.add(btnUpload);
        subsubPanel.add(btnAnalyse);

        subPanel.add(subsubPanel);

        add(subPanel);
    }
}