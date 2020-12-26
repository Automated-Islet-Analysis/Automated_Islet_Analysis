import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;


import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.media.jai.widget.ScrollingImagePanel;
import javax.swing.*;

public class NewTiffReader {
    RenderedOp rescaledImage;
    JLabel imageLabel;
    RenderedImage src;
    BufferedImage bufImage;

    public NewTiffReader(String filename) {
        src = (RenderedImage) JAI.create("fileload", filename);

        ParameterBlock pb  = new ParameterBlock();
        pb.addSource(src);// The source image
        pb.add(null);// The region of the image to scan. Null means all of it
        pb.add(1); // The horizontal sampling rate. 1 means all points
        pb.add(1); // The vertical sampling rate

        RenderedOp op = JAI.create("extrema", pb);

        // Extract values to calculate re-scaling params
        // We want to extend the range of gray values to occupy the full 2^16 range
        double[][] extrema = (double[][])op.getProperty("extrema"); // get the min and max gray values

        double minGrayValue = extrema[1][0];
        double maxGrayValue = extrema[0][0];
        double scale = 65000 / (minGrayValue - maxGrayValue);
        int darkeningConstant = 1; // Increase to make image darker
        double offset = darkeningConstant*(65000 * maxGrayValue)/(maxGrayValue - minGrayValue);

        double[] constants = {scale}; //The per-band constants to multiply by.
        double[] offsets = {offset}; // The per-band offsets to be added.

        ParameterBlock rescalingPB  = new ParameterBlock();
        rescalingPB.addSource(src);
        rescalingPB.add(constants);
        rescalingPB.add(offsets);
        // Use parameters to create the image
        rescaledImage = JAI.create("rescale", rescalingPB);
        bufImage = rescaledImage.getAsBufferedImage();
        bufImage = resizeImage(bufImage, 500,450);
    }

    private BufferedImage resizeImage(BufferedImage imgIn,int w,int h){
        BufferedImage resizedImg = new BufferedImage(w,h,BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(imgIn, 0, 0, w,h, null);
        g2.dispose();
        return resizedImg;
    }

    public JLabel getImgLabel () {
        ImageIcon imgIcon = new ImageIcon(bufImage);
        imageLabel = new JLabel(imgIcon);
        return imageLabel;
    }

}