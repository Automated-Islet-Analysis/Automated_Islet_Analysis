package UI;
import javax.swing.*;
import java.awt.*;
import java.lang.Exception;

public class Uploaded extends JPanel {
    private static JLabel fileName;
    private static String filePath;

    JButton btnUpload, btnAnalyse;
    JPanel subHPanel;
    static JLabel vidDisp;

    public static JLabel getFileName() {return fileName;}
    public static void setFileName(JLabel fileName) { Uploaded.fileName = fileName; }
    public static String getFilePath() { return filePath;}
    public static void setFilePath(String filePath) { Uploaded.filePath = filePath; }

    public Uploaded(){
        filePath = "";

        vidDisp = new JLabel();
        vidDisp.addMouseListener(new TiffListener());


        // Label for the text
        fileName = new JLabel("fileName");
        fileName.setFont(new Font(fileName.getFont().getName(), Font.PLAIN, 30));
        fileName.setHorizontalAlignment(SwingConstants.CENTER);

        // Two buttons. Upload to change uploaded file. Analyse to process the file.
        btnUpload = new JButton("Upload");
        btnUpload.addActionListener(new UploadListener());
        btnAnalyse = new JButton("Analyse");
        btnAnalyse.addActionListener(new AnalyseListener());

        // Set layout
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 25));
        add(vidDisp);
        add(fileName,BorderLayout.WEST);

        // Place buttons side by side
        subHPanel = new JPanel(new GridLayout(1,2));
        subHPanel.add(btnUpload);
        subHPanel.add(btnAnalyse);
        add(subHPanel,BorderLayout.EAST);
    }


    public void setDim(){
        if(! (vidDisp.getIcon()==null))
            setSize(vidDisp.getIcon().getIconWidth()+50,vidDisp.getIcon().getIconHeight()+150);
    }
}