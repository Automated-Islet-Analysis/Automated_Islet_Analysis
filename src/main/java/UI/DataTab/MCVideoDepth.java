package UI.DataTab;
import UI.TiffListener;
import UI.Uploaded;

import javax.swing.*;
import java.awt.*;

public class MCVideoDepth extends JPanel {
    private static JLabel welcome;
    private static JLabel fileName;
    private static String filePath;
    static JButton imgButtonDepth;
    public MCVideoDepth(){

        welcome = new JLabel("This is the depth motion corrected video");
        welcome.setHorizontalAlignment(JLabel.CENTER);
        welcome.setSize(400,100);
        add(welcome);
        filePath = "/Users/sachamaire/Desktop/Travail/Etudes/Imperial/Year3/Programming3/Temp/DepthCorrected";

        // Set filepath of uploaded to have it called by TiffListener
        Uploaded.setFilePath(filePath);
        
        // Empty img panel
//        imgButtonDepth = new JButton();
//        imgButtonDepth.addActionListener(new TiffListener());

//        //Set the uploaded button
//        imgButtonDepth=Uploaded.getImgButton();
//
//
//        // Label for the text
//        fileName.setFont(new Font(fileName.getFont().getName(), Font.PLAIN, 30));
//        fileName.setHorizontalAlignment(SwingConstants.CENTER);
//
//        // Set layout
//        setLayout(new FlowLayout(FlowLayout.CENTER, 200, 30));
//        add(fileName);

    }
}
