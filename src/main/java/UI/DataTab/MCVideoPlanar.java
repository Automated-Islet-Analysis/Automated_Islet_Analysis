package UI.DataTab;

import UI.UserInterface;
import UI.VideoPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MCVideoPlanar extends VideoPanel {
    private static JLabel msg;

    public MCVideoPlanar(){
        super(null,20,100);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Title
        msg = new JLabel("Planar motion corrected video");
        msg.setFont(new Font(msg.getFont().getFontName(),Font.BOLD,20));
        msg.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        add(msg,BorderLayout.CENTER);

        vidDisp.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        add(vidDisp);
    }

    @Override
    public void update(){
        this.video = UserInterface.getVideoProcessor().getPlanarCorrectionVid();

        if(video==null){
            removeAll();
            // Message to user
            msg = new JLabel("The video was not corrected for planar motion, no preview available!");
            msg.setFont(new Font(msg.getFont().getFontName(),Font.PLAIN,30));
            add(msg);
        }
        else{
            BufferedImage img = video.getBufferedImage();
            img = resizeImage(img,20,100);
            vidDisp.setIcon(new ImageIcon(img));
            vidDisp.setVisible(true);
        }
    }
}
