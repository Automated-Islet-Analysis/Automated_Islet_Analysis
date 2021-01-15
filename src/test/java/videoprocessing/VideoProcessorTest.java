package videoprocessing;

import org.junit.*;

import java.io.File;

public class VideoProcessorTest {
    private static String filePath = System.getProperty("user.dir")+"/videos/Video_for_Testing_short.tif";
    private String expectedDataPath=System.getProperty("user.dir")+"/img/Unit_testing/Data/";
    private static Video video;
    private static VideoProcessor videoProcessor;
//
    @BeforeClass
    public static void setUp(){
        video=new Video(filePath);
        videoProcessor=new VideoProcessor(video);
        videoProcessor.process(10,true,true,true);
        videoProcessor.analyseCells();

        videoProcessor.saveRoiImage(System.getProperty("user.dir") + "/temp/"+ "ROIs.jpg"); // Needs to be JPG
        videoProcessor.savePlanarCorrectionVid(System.getProperty("user.dir") + "/Planar_motion_correction.tif"); // Needs to be a Tiff
        videoProcessor.saveDepthCorrectionVid(System.getProperty("user.dir") + "/Depth_motion_correction.tif"); // Needs to be a Tiff
    }

    @AfterClass
    public static void tearDown(){
        File file1=new File(System.getProperty("user.dir") + "/Depth_motion_correction.tif");
        file1.delete();

        File file2=new File(System.getProperty("user.dir") + "/Planar_motion_correction.tif");
        file2.delete();

        File file3=new File(System.getProperty("user.dir") + "/temp/"+ "ROIs.jpg");
        file3.delete();

        File file4=new File(System.getProperty("user.dir") + "/temp"+ "/Analysis_recap.csv");
        file4.delete();

    }
    @Test
    public void testCreatingRoiImage(){
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
        String actualFile=System.getProperty("user.dir") + "/Planar_motion_correction.tif";

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

    @Test
    public void testSavingSummary(){
        Assert.assertEquals(SaveError.SAVE_SUCCESS,videoProcessor.saveSummary(System.getProperty("user.dir") + "/temp"+ "/Analysis_recap.csv"));

    }
}
