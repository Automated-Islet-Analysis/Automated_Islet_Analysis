import javax.swing.*;
import java.awt.*;

public class Uploaded extends JPanel {
    static JLabel filename;
    static JPanel imgPanel, uploadPanel,analysePanel,fileNamePanel;
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
        uploadPanel=new JPanel();
        analysePanel=new JPanel();
        btnUpload = new JButton("Upload");
        btnUpload.addActionListener(new UploadListener());
        uploadPanel.add(btnUpload);
        btnAnalyse = new JButton("Analyse");
        btnAnalyse.addActionListener(new AnalyseListener());
        analysePanel.add(btnAnalyse);

        // Set layout
        setLayout(new GridLayout(2,1));
        setLayout(new BorderLayout());
        fileNamePanel=new JPanel();
        fileNamePanel.add(filename);
//        add(fileNamePanel);
//        fileNamePanel.setVisible(false);
//        fileNamePanel.setPreferredSize(new Dimension(600,100));
        imgPanel.setSize(600,600);
        add(imgPanel,BorderLayout.NORTH);
//        imgPanel.setVisible(false);


        // Place buttons side by side
        subHPanel = new JPanel(new GridLayout(1,3));
        subHPanel.add(fileNamePanel);
        subHPanel.add(uploadPanel);
        subHPanel.add(analysePanel);
        subHPanel.setSize(200,40);
        add(subHPanel,BorderLayout.CENTER);
        btnAnalyse.setName("analyse");
    }
}