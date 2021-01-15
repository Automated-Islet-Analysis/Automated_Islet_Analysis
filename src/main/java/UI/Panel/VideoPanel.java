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
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

public abstract class VideoPanel extends DynamicPanel {
    // Create components
    protected JLabel vidDisp;
    protected ImagePlus video;

    // Variable for display of play button
    protected Polygon playArrow = new Polygon();

    // Play video when image is pressed
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

    // Set video
    public void setVideo(ImagePlus video) { this.video = video; }

    // Create video panel
    public VideoPanel(ImagePlus video,int marginHorizontal,int marginVertical){
        this.video = video;
        this.marginHorizontal=marginHorizontal;
        this.marginVertical=marginVertical;

        // Empty img panel
        vidDisp = new JLabel();
        vidDisp.addMouseListener(mouseListener);

        playArrow.npoints=3;
    }

    // Play video
    protected void playVideo(ImagePlus video){

        int speed = 1000/18; // 18 frames per second
        int finalNumImages = video.getNSlices();
        final int[] frame = {0};
        java.util.Timer timer = new Timer();

        // Set a timer to change between frames of the video
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if(frame[0] <finalNumImages){
                    video.setSlice(frame[0]);
                    BufferedImage img = video.getBufferedImage();
                    img = resizeImage(img, Home.getInterframe());
                    vidDisp.setIcon(new ImageIcon(img));
                    vidDisp.setVisible(true);
                    frame[0]++;
                }else{
                    timer.cancel();
                    Home.setDisplay();
                }
            }
        };
        timer.scheduleAtFixedRate(task,0,speed);
    }

    public abstract void updatePanel();


    protected void drawPlay(Graphics g){
        double w = (double)video.getWidth()*scalingOfImg ;
        double h = (double)video.getHeight()*scalingOfImg;

        playArrow.xpoints = new int[]{(int)w/2-20,(int)w/2-20,(int)w/2+30};
        playArrow.ypoints = new int[]{(int)h/2 +25,(int)h/2-25,(int)h/2};

        g.setColor(Color.BLACK);
        g.drawPolygon(playArrow);
        g.fillPolygon(playArrow);
    }
}