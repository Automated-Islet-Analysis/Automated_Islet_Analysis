import imageprocessing.VideoProcessor;
import imageprocessing.Video;

public class Main {
    public static void main(String[] args) {
        // Init video file - to do when file is being uploaded
        String fileName = new String(System.getProperty("user.dir")+"/videos/2_aligned_vid.tif");
        Video vid = new Video(fileName);
        VideoProcessor videoProcessor = new VideoProcessor(vid);

        // Apply processing on the video
        videoProcessor.process(10,false,false,true);
        videoProcessor.analyseCells();
        videoProcessor.saveSummary();
    }
}
