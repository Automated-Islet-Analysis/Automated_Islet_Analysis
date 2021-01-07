package videoprocessing;

import ij.ImagePlus;
import ij.process.ImageProcessor;
import org.itk.simple.SimpleITK;
import org.junit.Assert;
import org.junit.Test;

import java.awt.*;
import java.awt.image.PixelGrabber;

public class VideoProcessorTest {
    @Test
    public void testCretingRoiImage(){
        String expectedFile=System.getProperty("user.dir")+"/img/Unit_testing/Data/ROIs.jpg";
        String actualFile=System.getProperty("user.dir")+"/temp/ROIs.jpg";

        Image expectedImg=Toolkit.getDefaultToolkit().getImage(expectedFile);
        Image actualImg=Toolkit.getDefaultToolkit().getImage(actualFile);

        try{
            PixelGrabber grabImage1Pixels = new PixelGrabber(expectedImg, 0, 0, -1, -1, false);
            PixelGrabber grabImage2Pixels = new PixelGrabber(actualImg, 0, 0, -1, -1, false);

            int[] expectedImgData = null;

            if (grabImage1Pixels.grabPixels()) {
                int width = grabImage1Pixels.getWidth();
                int height = grabImage1Pixels.getHeight();
                expectedImgData = new int[width * height];
                expectedImgData = (int[]) grabImage1Pixels.getPixels();
            }
            int[] actualImgData = null;

            if (grabImage2Pixels.grabPixels()) {
                int width = grabImage2Pixels.getWidth();
                int height = grabImage2Pixels.getHeight();
                actualImgData = new int[width * height];
                actualImgData = (int[]) grabImage2Pixels.getPixels();
            }

            Assert.assertArrayEquals(expectedImgData,actualImgData);


        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreatingPlanarCorrectionVid(){
        String expectedFile=System.getProperty("user.dir")+"/img/Unit_testing/Data/Planar_motion_correction.tif";
        String actualFile=System.getProperty("user.dir")+"Planar_motion_correction.tif";

        ImagePlus expectedImg=new ImagePlus(expectedFile);
        ImagePlus actualImg=new ImagePlus(actualFile);

        ImageProcessor imgProcessor1=expectedImg.getProcessor();
        ImageProcessor imgProcessor2=actualImg.getProcessor();

        int width=expectedImg.getWidth();
        int height=expectedImg.getHeight();

        long differenceImg=0;

        for(int y=0; y<height;y++) {
            for(int x=0;x<width;x++) {
                int grayImg1= (int) imgProcessor1.getf(x,y);
                int grayImg2= (int) imgProcessor2.getf(x,y);
                differenceImg+=Math.abs(grayImg1-grayImg2);
            }
        }

        if (differenceImg!=0) {
            Assert.fail();
        }
    }
}
