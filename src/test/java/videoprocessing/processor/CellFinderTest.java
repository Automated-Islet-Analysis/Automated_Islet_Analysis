/**
 * Test of CellFinder
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

public class CellFinderTest {
    private String filePath;
    private Video video;
    private VideoProcessor videoProcessor;
    private CellFinder cellFinder;

    @Before
    public void setUp() {
        filePath = System.getProperty("user.dir")+"/videos/Video_for_Testing_short.tif";
        video=new Video(filePath);
        videoProcessor = new VideoProcessor(video);
        cellFinder = new CellFinder(video, 25);
        videoProcessor.process(10,false,false,true);
        videoProcessor.analyseCells();
    }

    @Test
    //given a non corrupted video, the run() method is tested by assessing the correct return of ProcessorError
    public void testRunning(){
        Assert.assertEquals(ProcessorError.PROCESSOR_SUCCESS,cellFinder.run());
    }
}
