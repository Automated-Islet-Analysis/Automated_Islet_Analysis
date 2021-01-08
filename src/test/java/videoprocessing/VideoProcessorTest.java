package videoprocessing;

import org.junit.Assert;
import org.junit.Test;

public class VideoProcessorTest {
    @Test
    public void testCretingRoiImage(){
        String expectedFile=System.getProperty("user.dir")+"/img/Unit_testing/Data/ROIs.jpg";
        String actualFile=System.getProperty("user.dir")+"/temp/ROIs.jpg";

        CompareImages compareImages=new CompareImages(expectedFile,actualFile);
        long differenceImg=compareImages.getDifferenceImg();

        if (differenceImg!=0) {
            Assert.fail();
        }
    }
    @Test
    public void testCreatingPlanarCorrectionVid(){
        String expectedFile=System.getProperty("user.dir")+"/img/Unit_testing/Data/Planar_motion_correction.tif";
        String actualFile=System.getProperty("user.dir")+"/Planar_motion_correction.tif";

        CompareImages compareImages=new CompareImages(expectedFile,actualFile);
        long differenceImg=compareImages.getDifferenceImg();

        if (differenceImg!=0) {
            Assert.fail();
        }
    }
    @Test
    public void testCreatingDepthCorrectionVid(){
        String expectedFile=System.getProperty("user.dir")+"/img/Unit_testing/Data/Depth_motion_correction.tif";
        String actualFile=System.getProperty("user.dir")+"/Depth_motion_correction.tif";

        CompareImages compareImages=new CompareImages(expectedFile,actualFile);
        long differenceImg=compareImages.getDifferenceImg();

        if (differenceImg!=0) {
            Assert.fail();
        }
    }
}
