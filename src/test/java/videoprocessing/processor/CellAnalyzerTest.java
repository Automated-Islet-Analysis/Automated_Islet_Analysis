//package videoprocessing;
//
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import videoprocessing.processor.CellAnalyzer;
//import videoprocessing.processor.ProcessorError;
//
//public class CellAnalyzerTest {
//    private String filePath;
//    private Video video;
//    private VideoProcessor videoProcessor;
//    private CellAnalyzer cellAnalyzer;
//
//    @Before
//    public void setUp() {
//        filePath = System.getProperty("user.dir")+"/videos/Video_for_Demo.tif";
//        video=new Video(filePath);
//        videoProcessor = new VideoProcessor(video);
//        cellAnalyzer = new CellAnalyzer(video, 10);
//        videoProcessor.process(10,false,false,true);
//        videoProcessor.analyseCells();
//    }
//
//    @Test
//    public void testRunning(){
//        Assert.assertEquals(ProcessorError.PROCESSOR_SUCCESS,cellAnalyzer.run());
//    }
//
//}
