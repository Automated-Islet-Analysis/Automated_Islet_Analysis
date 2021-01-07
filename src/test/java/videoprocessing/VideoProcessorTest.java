package videoprocessing;

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
}
