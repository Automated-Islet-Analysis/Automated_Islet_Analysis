package UI.DataTab;

import UI.UserInterface;
import ij.ImagePlus;
import videoprocessing.VideoProcessor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.TimerTask;
import java.util.Timer;

public class MCVideoPlanar extends JPanel {
    private static JLabel msg;
    private JLabel vid;

    public MCVideoPlanar(){
        msg = new JLabel("This is the planar motion corrected video");
        msg.setFont(new Font(msg.getFont().getFontName(),Font.PLAIN,20));
        msg.setHorizontalAlignment(JLabel.CENTER);
        add(msg);

        // Empty img panel
        vid = new JLabel();
        vid.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}

            @Override
            public void mousePressed(MouseEvent e) {
                playVideo();
            }

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });

        add(vid);
    }

    public void update(){
        VideoProcessor videoProcessor = UserInterface.getVideoProcessor();
        ImagePlus video = videoProcessor.getPlanarCorrectionVid();

        BufferedImage img = video.getBufferedImage();
        img = resizeImage(img,(int)Math.round(img.getWidth()*0.3),(int)Math.round(img.getHeight()*0.3));
        vid.setIcon(new ImageIcon(img));
        vid.setVisible(true);
        setSize(vid.getIcon().getIconWidth()+100,vid.getIcon().getIconHeight()+130);
    }

    private void playVideo(){
        VideoProcessor videoProcessor = UserInterface.getVideoProcessor();
        ImagePlus video = videoProcessor.getPlanarCorrectionVid();

        int speed = 1000/18; // 18 frames per second
        int finalNumImages = video.getNSlices();
        final int[] frame = {0};
        Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.out.println("done");
                if(frame[0] <finalNumImages){
                    System.out.println("test");
                    video.setSlice(frame[0]);
                    BufferedImage img = video.getBufferedImage();
                    img = resizeImage(img,(int)Math.round(img.getWidth()*0.3),(int)Math.round(img.getHeight()*0.3));
                    vid.setIcon(new ImageIcon(img));
                    vid.setVisible(true);
                    frame[0]++;
                }else{
                    timer.cancel();
                }
            }
        };
        timer.scheduleAtFixedRate(task,0,speed);
    }

    // Resize image to fit on display
    private BufferedImage resizeImage(BufferedImage imgIn, int w, int h){
        BufferedImage resizedImg = new BufferedImage(w,h,BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(imgIn, 0, 0, w,h, null);
        g2.dispose();
        return resizedImg;
    }
}
