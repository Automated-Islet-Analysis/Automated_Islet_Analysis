/**
 * Test of PlanarMotionCorrector class
 *
 * @author Team Automated analysis of "islet in eye", Bioengineering department, Imperial College London
 *
 * Last modified: 11/01/2021
 */

package videoprocessing.processor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import videoprocessing.Video;
import videoprocessing.VideoProcessor;

public class PlanarMotionCorrectorTest {
    private String filePath;
    private Video video;
    private VideoProcessor videoProcessor;
    private PlanarMotionCorrector planarMotionCorrector;

    @Before
    public void setUp() {
        filePath = System.getProperty("user.dir")+"/videos/Video_for_Testing_short.tif";
        videoProcessor = new VideoProcessor(new Video(filePath));
        planarMotionCorrector = new PlanarMotionCorrector(new Video(filePath));
    }

    @Test
    //Given a non corrupted video, the method run() is tested by assessing the correct return of ProcessorError
    public void testRunning(){
        Assert.assertEquals(ProcessorError.PROCESSOR_SUCCESS,planarMotionCorrector.run());
    }
}
