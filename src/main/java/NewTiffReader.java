import com.sun.media.ui.Scroll;

import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.media.jai.widget.ScrollingImagePanel;
import javax.swing.*;

public class NewTiffReader {
    RenderedOp rescaledImage;
    ScrollingImagePanel imagePanel;
    RenderedImage src;

    public NewTiffReader(String filename) {
        src = (RenderedImage) JAI.create("fileload", filename);

        ParameterBlock pb  = new ParameterBlock();
        pb.addSource(src);// The source image
        pb.add(null);// The region of the image to scan. Null means all of it
        pb.add(1); // The horizontal sampling rate. 1 means all points
        pb.add(1); // The vertical sampling rate

        RenderedOp op = JAI.create("extrema", pb);

        // Extract values to calculate re-scaling params
        // We want to extend the range of gray values to occupy the full 2¹⁶ range
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
    }

    public ScrollingImagePanel getImgPanel () {
        imagePanel = new ScrollingImagePanel(rescaledImage, 700, 550);
        return imagePanel;
    }

}