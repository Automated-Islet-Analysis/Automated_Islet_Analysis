package videoprocessing;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import videoprocessing.processor.PlanarMotionCorrector;
import videoprocessing.processor.ProcessorError;

public class PlanarMotionCorrectorTest {
    private String filePath;
    private Video video;
    private VideoProcessor videoProcessor;
    private PlanarMotionCorrector planarMotionCorrector;

    @Before
    public void setUp() {
        filePath = System.getProperty("user.dir")+"/videos/Video_for_Testing_short.tif";
        video=new Video(filePath);
        videoProcessor = new VideoProcessor(video);
        planarMotionCorrector = new PlanarMotionCorrector(video);
        videoProcessor.process(10,true,false,false);
    }

    @Test
    public void testRunning(){
        Assert.assertEquals(ProcessorError.PROCESSOR_SUCCESS,planarMotionCorrector.run());
    }
}
