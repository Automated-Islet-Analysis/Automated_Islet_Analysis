package videoprocessing.processor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import videoprocessing.Video;
import videoprocessing.VideoProcessor;

public class DepthMotionCorrectorTest {
    private String filePath;
    private Video video;
    private VideoProcessor videoProcessor;
    private DepthMotionCorrector depthMotionCorrector;

    @Before
    public void setUp() {
        filePath = System.getProperty("user.dir")+"/videos/Video_for_Testing_short.tif";
        video=new Video(filePath);
        videoProcessor = new VideoProcessor(video);
        depthMotionCorrector = new DepthMotionCorrector(video, 10);
    }

    @Test
    public void testRunning(){
        Assert.assertEquals(ProcessorError.PROCESSOR_SUCCESS,depthMotionCorrector.run());
    }
}