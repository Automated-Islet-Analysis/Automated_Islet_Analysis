/**
 * Panel used for display of dynamic videos.
 *
 * @author Team Automated analysis of "islet in eye", Bioengineering department, Imperial College London
 *
 * Last modified: 11/01/2021
 */

package UI.Panel;

import UI.Controller;
import UI.HomeTab.Home;
import ij.ImagePlus;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

public abstract class VideoPanel extends DynamicPanel {

    protected JLabel vidDisp;
    protected ImagePlus video;
    protected final int hMargin;
    protected final int vMargin;

    MouseListener mouseListener = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {}
        @Override
        public void mousePressed(MouseEvent e) {
            playVideo(video);
        }
        @Override
        public void mouseReleased(MouseEvent e) {}
        @Override
        public void mouseEntered(MouseEvent e) {}
        @Override
        public void mouseExited(MouseEvent e) {}
    };

    public void setVideo(ImagePlus video) { this.video = video; }

    public VideoPanel(ImagePlus video,int hMargin,int vMargin){
        this.video = video;
        this.hMargin=hMargin;
        this.vMargin=vMargin;

        // Empty img panel
        vidDisp = new JLabel();
        vidDisp.addMouseListener(mouseListener);
    }

    protected void playVideo(ImagePlus video){

        int speed = 1000/18; // 18 frames per second
        int finalNumImages = video.getNSlices();
        final int[] frame = {0};
        java.util.Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if(frame[0] <finalNumImages){
                    video.setSlice(frame[0]);
                    BufferedImage img = video.getBufferedImage();
                    img = resizeImage(img,hMargin,vMargin, Home.getInterframe());
                    vidDisp.setIcon(new ImageIcon(img));
                    vidDisp.setVisible(true);
                    frame[0]++;
                }else{
                    timer.cancel();
                }
            }
        };
        timer.scheduleAtFixedRate(task,0,speed);
    }

    public abstract void update();

}
