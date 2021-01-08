package UI;

import ij.ImagePlus;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

public abstract class VideoPanel extends ImagePanel {
    protected JLabel vidDisp;
    protected ImagePlus video;

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

    public VideoPanel(ImagePlus video){
        this.video = video;
        // Empty img panel
        vidDisp = new JLabel();
        vidDisp.addMouseListener(mouseListener);
    }

    protected void playVideo(ImagePlus video){

        int speed = 1000/12; // 12 frames per second
        int finalNumImages = video.getNSlices();
        final int[] frame = {0};
        java.util.Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if(frame[0] <finalNumImages){
                    video.setSlice(frame[0]);
                    BufferedImage img = video.getBufferedImage();
                    img = resizeImage(img,20,100);
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
