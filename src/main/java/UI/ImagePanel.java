package UI;

import ij.ImagePlus;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {
    public ImagePanel(){}

    // Resize image to fit on display
    protected BufferedImage resizeImage(BufferedImage imgIn, int marginHorizontal, int marginVertical){
        int w,h;
        double ratio = (double)imgIn.getWidth()/(double)imgIn.getHeight();
        double frameRatio = ((double) Controller.interframe.getWidth()-marginHorizontal) / ((double)Controller.interframe.getHeight()-marginVertical);
        if (frameRatio < ratio) {
            double scaling = (double) (Controller.interframe.getWidth() - marginHorizontal) / (double) imgIn.getWidth();
            w =(int) Math.round(imgIn.getWidth() * scaling);
            h =(int) Math.round(imgIn.getHeight() * scaling);

        }else{
            double scaling = (double) (Controller.interframe.getHeight() - marginVertical) / (double) imgIn.getHeight();
            w = (int) Math.round(imgIn.getWidth() * scaling);
            h = (int) Math.round(imgIn.getHeight() * scaling);
        }

        BufferedImage resizedImg = new BufferedImage(w,h,BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(imgIn, 0, 0, w,h, null);
        g2.dispose();
        return resizedImg;
    }

}
