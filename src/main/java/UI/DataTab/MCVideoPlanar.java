package UI.DataTab;

import UI.TiffListener;
import UI.Uploaded;

import javax.swing.*;
import java.awt.*;

public class MCVideoPlanar extends JPanel {
    private static JLabel welcome;
    private static JLabel fileName;
    private static String filePath;
    static JButton imgButtonPlanar;

    public MCVideoPlanar(){
        welcome = new JLabel("This is the planar motion corrected video");
        welcome.setHorizontalAlignment(JLabel.CENTER);
        welcome.setSize(400,100);
        add(welcome);

        welcome = new JLabel("This is the depth motion corrected video");
        welcome.setHorizontalAlignment(JLabel.CENTER);
        welcome.setSize(400,100);
        add(welcome);
        filePath = "/Users/sachamaire/Desktop/Travail/Etudes/Imperial/Year3/Programming3/Temp/PlannarCorrected";

        // Set filepath of uploaded to have it called by TiffListener
        Uploaded.setFilePath(filePath);

        // Empty img panel
        imgButtonPlanar = new JButton();
        imgButtonPlanar.addActionListener(new TiffListener());

        //Set the uploaded button
        imgButtonPlanar=Uploaded.getImgButton();


        // Label for the text
        fileName.setFont(new Font(fileName.getFont().getName(), Font.PLAIN, 30));
        fileName.setHorizontalAlignment(SwingConstants.CENTER);

        // Set layout
        setLayout(new FlowLayout(FlowLayout.CENTER, 200, 30));
        add(fileName);

    }
}
