/**
 * Panel used for display of the results of the depth motion correction.
 *
 * @author Team Automated analysis of "islet in eye", Bioengineering department, Imperial College London
 *
 * Last modified: 11/01/2021
 */

package UI.DataTab;

import UI.Controller;
import UI.Panel.VideoPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MCVideoDepth extends VideoPanel {
    private static JLabel msg,msg1;

    // Constructor. Create all components
    public MCVideoDepth(){
        super(null,20,165);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        //Message
        msg = new JLabel("Original video(left) and video with depth motion correction(right).");
        msg1 = new JLabel(" Disregarded frames appear in black.");
        msg.setFont(new Font(msg.getFont().getFontName(),Font.BOLD,18));
        msg1.setFont(new Font(msg.getFont().getFontName(),Font.BOLD,18));
        msg.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        msg1.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        add(Box.createVerticalStrut(25));
        add(msg);
        add(msg1);
        add(Box.createVerticalStrut(25));

        vidDisp.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        add(vidDisp);
    }

    @Override
    // Display video if depth motion correction has been executed
    public void updatePanel(){
        this.video = Controller.getVideoProcessor().getDepthCorrectionVid();

        //Check if the video has already been analyzed
        if(video==null){
            removeAll();
            // Prompt the user if the video has not yet been analyzed
            msg = new JLabel("The video was not corrected for depth motion, no preview available!");
            msg.setAlignmentX(JComponent.CENTER_ALIGNMENT);
            msg.setFont(new Font(msg.getFont().getFontName(),Font.PLAIN,18));
            add(Box.createVerticalStrut(50));
            add(msg);
        }
        //Display the video if already processed
        else {
            BufferedImage img = video.getBufferedImage();
            img = resizeImage(img,Controller.getInterframe());
            drawPlay(img.getGraphics());
            vidDisp.setIcon(new ImageIcon(img));
            vidDisp.setVisible(true);
        }
    }
}
