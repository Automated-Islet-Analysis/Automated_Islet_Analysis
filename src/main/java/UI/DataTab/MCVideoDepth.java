/**
 * Panel used for display of the results of the depth motion correction.
 *
 * @author Team Automated analysis of "islet in eye", Bioengineering department, Imperial College London
 *
 * Last modified: 11/01/2021
 */

package UI.DataTab;

import UI.Controller;
import UI.HomeTab.Home;
import UI.Panel.VideoPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MCVideoDepth extends VideoPanel {
    private static JLabel msg;

    public MCVideoDepth(){
        super(null,20,100);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        msg = new JLabel("Depth motion corrected video");
        msg.setFont(new Font(msg.getFont().getFontName(),Font.BOLD,18));
        msg.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        add(Box.createVerticalStrut(25));
        add(msg);
        add(Box.createVerticalStrut(25));

        vidDisp.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        add(vidDisp);
    }

    @Override
    public void update(){
        this.video = Home.getVideoProcessor().getDepthCorrectionVid();

        if(video==null){
            removeAll();
            // Message to user
            msg = new JLabel("The video was not corrected for depth motion, no preview available!");
            msg.setAlignmentX(JComponent.CENTER_ALIGNMENT);
            msg.setFont(new Font(msg.getFont().getFontName(),Font.PLAIN,18));
            add(Box.createVerticalStrut(50));
            msg.setFont(new Font(msg.getFont().getFontName(),Font.PLAIN,20));
            add(msg);
        }
        else {
            BufferedImage img = video.getBufferedImage();
            img = resizeImage(img, 20,100, Home.getInterframe());
            vidDisp.setIcon(new ImageIcon(img));
            vidDisp.setVisible(true);
        }
    }
}