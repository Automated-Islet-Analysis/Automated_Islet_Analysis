/**
 * Panel used for display of videos and images where the size of the displayed image depends
 * on the size of the frame it is part of.
 * This enables the visualisation of the videos in greater detail by opening the app in full-screen mode.
 *
 * @author Team Automated analysis of "islet in eye", Bioengineering department, Imperial College London
 *
 * Last modified: 11/01/2021
 */

package UI.Panel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class DynamicPanel extends JPanel {
    // Scaling of video used for display so that it fits on screen
    protected double scalingOfImg = 0;

    protected int marginHorizontal;
    protected int marginVertical;

    // Constructor
    public DynamicPanel(){}

    // Resize image to fit on display - JFrame input
    protected BufferedImage resizeImage(BufferedImage imgIn, JFrame frame){
        int w,h;
        double ratio = (double)imgIn.getWidth()/(double)imgIn.getHeight();
        double frameRatio = ((double) frame.getWidth()-marginHorizontal) / ((double)frame.getHeight()-marginVertical);

        // Adapt to fit in display based on width ratio
        if (frameRatio < ratio) {
            double scaling = (double) (frame.getWidth() - marginHorizontal) / (double) imgIn.getWidth();
            w =(int) Math.round(imgIn.getWidth() * scaling);
            h =(int) Math.round(imgIn.getHeight() * scaling);
            this.scalingOfImg=scaling;
        }
        // Adapt to fit in display based on height ratio
        else{
            double scaling = (double) (frame.getHeight() - marginVertical) / (double) imgIn.getHeight();
            w = (int) Math.round(imgIn.getWidth() * scaling);
            h = (int) Math.round(imgIn.getHeight() * scaling);
            this.scalingOfImg=scaling;
        }

        // Create image with scaled dimensions
        BufferedImage resizedImg = new BufferedImage(w,h,BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(imgIn, 0, 0, w,h, null);
        g2.dispose();
        return resizedImg;
    }

    // Resize image to fit on display - JDialog input
    protected BufferedImage resizeImage(BufferedImage imgIn, JDialog frame){
        int w,h;
        double ratio = (double)imgIn.getWidth()/(double)imgIn.getHeight();
        double frameRatio = ((double) frame.getWidth()-marginHorizontal) / ((double)frame.getHeight()-marginVertical);

        // Adapt to fit in display based on width ratio
        if (frameRatio < ratio) {
            double scaling = (double) (frame.getWidth() - marginHorizontal) / (double) imgIn.getWidth();
            w =(int) Math.round(imgIn.getWidth() * scaling);
            h =(int) Math.round(imgIn.getHeight() * scaling);
            this.scalingOfImg=scaling;

        }
        // Adapt to fit in display based on height ratio
        else{
            double scaling = (double) (frame.getHeight() - marginVertical) / (double) imgIn.getHeight();
            w = (int) Math.round(imgIn.getWidth() * scaling);
            h = (int) Math.round(imgIn.getHeight() * scaling);
            this.scalingOfImg=scaling;
        }

        // Create image with scaled dimensions
        BufferedImage resizedImg = new BufferedImage(w,h,BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(imgIn, 0, 0, w,h, null);
        g2.dispose();
        return resizedImg;
    }

}
