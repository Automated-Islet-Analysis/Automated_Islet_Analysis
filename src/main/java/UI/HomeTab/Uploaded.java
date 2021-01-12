/**
 * Panel used for display of uploaded video with the options to analyses video or upload an other video.
 *
 * @author Team Automated analysis of "islet in eye", Bioengineering department, Imperial College London
 *
 * Last modified: 11/01/2021
 */

package UI.HomeTab;

import UI.Controller;
import UI.Panel.VideoPanel;
import ij.ImagePlus;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Uploaded extends VideoPanel {
    private static JLabel fileName;
    private static String filePath;

    JButton btnUpload, btnAnalyse;
    JPanel subButtonPanel;
    JPanel subPanel;

    public static JLabel getFileName() {return fileName;}
    public static void setFileName(JLabel fileName) { Uploaded.fileName = fileName; }
    public static String getFilePath() { return filePath;}
    public static void setFilePath(String filePath) { Uploaded.filePath = filePath; }

    public Uploaded(){
        super(null,20,150);
        filePath = "";

        // Label for the text
        fileName = new JLabel("fileName");
        fileName.setFont(new Font(fileName.getFont().getName(), Font.PLAIN, 40));
        fileName.setHorizontalAlignment(SwingConstants.CENTER);

        // Two buttons. Upload to change uploaded file. Analyse to process the file.
        btnUpload = new JButton("Upload");
        btnUpload.addActionListener(new UploadListener());
        btnAnalyse = new JButton("Analyse");
        btnAnalyse.addActionListener(new AnalyseListener());
        btnAnalyse.setName("analyse");

        // Set layout
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        vidDisp.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        fileName.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        Font font = new Font(btnAnalyse.getFont().getFontName(),Font.PLAIN,15);
        btnAnalyse.setFont(font);
        btnUpload.setFont(font);

        add(vidDisp);
        add(fileName);
        add(Box.createVerticalStrut(10));

        subPanel = new JPanel(new FlowLayout());

        // Place buttons side by side
        subButtonPanel = new JPanel(new GridLayout(1,2));
        subButtonPanel.add(btnUpload);
        subButtonPanel.add(btnAnalyse);
        subPanel.add(subButtonPanel,BorderLayout.EAST);
        subPanel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        add(subPanel,BorderLayout.EAST);
        add(Box.createVerticalStrut(10));
    }


    @Override
    public void update() {
        if (video==null)
            this.video = new ImagePlus(filePath);
        BufferedImage img = video.getBufferedImage();
        img = resizeImage(img,20,150, Home.getInterframe());
        vidDisp.setIcon(new ImageIcon(img));
        vidDisp.setVisible(true);
    }
}