package videoprocessing;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import videoprocessing.processor.CellFinder;
import videoprocessing.processor.ProcessorError;

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
    public void testRunning(){
        Assert.assertEquals(ProcessorError.PROCESSOR_SUCCESS,cellFinder.run());
    }
}
