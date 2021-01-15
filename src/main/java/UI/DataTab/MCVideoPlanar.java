/**
 * Panel used for display of the results of the planar motion correction.
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

public class MCVideoPlanar extends VideoPanel {
    private static JLabel msg;

    // Constructor. Create all components
    public MCVideoPlanar(){
        super(null,20,100);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Title
        msg = new JLabel("Original video(left) and video with planar motion correction(right).");
        msg.setFont(new Font(msg.getFont().getFontName(),Font.BOLD,18));
        msg.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        add(Box.createVerticalStrut(25));
        add(msg,BorderLayout.CENTER);
        add(Box.createVerticalStrut(25));

        vidDisp.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        add(vidDisp);
    }

    @Override
    // Display video if planar motion correction has been executed
    public void updatePanel(){
        this.video = Home.getVideoProcessor().getPlanarCorrectionVid();

        //Check if the video has already been analyzed
        if(video==null){
            removeAll();
            // Prompt the user if no video was uploaded
            msg = new JLabel("The video was not corrected for planar motion, no preview available!");
            msg.setAlignmentX(JComponent.CENTER_ALIGNMENT);
            msg.setFont(new Font(msg.getFont().getFontName(),Font.PLAIN,18));
            add(Box.createVerticalStrut(50));
            add(msg);
        }
        //Display the video if already analyzed
        else{
            BufferedImage img = video.getBufferedImage();
            img = resizeImage(img,Home.getInterframe());
            drawPlay(img.getGraphics());
            vidDisp.setIcon(new ImageIcon(img));
            vidDisp.setVisible(true);
        }
    }
}