import videoprocessing.VideoProcessor;
import videoprocessing.Video;

public class Main {
    public static void main(String[] args) {
        // Path of video to be analyzed
        String filePath = System.getProperty("user.dir")+"/videos/2.tif";

        VideoProcessor videoProcessor = new VideoProcessor(new Video(filePath));
        // Apply processing on the video
        videoProcessor.process(10,true,true,true);
        videoProcessor.analyseCells();
        videoProcessor.saveSummary();
    }
}
