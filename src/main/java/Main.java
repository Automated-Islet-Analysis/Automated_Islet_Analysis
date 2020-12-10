import imageprocessing.Video;
import imageprocessing.VideoProcessor;

public class Main {
    public static void main(String[] args) {
        // Init video file
        String fileName = new String(System.getProperty("user.dir")+"/videos/1.tif");
        Video vid = new Video(fileName);

        // Apply processing on the video
//        VideoProcessor videoProcessor = new VideoProcessor(vid);
//        videoProcessor.run(2);

    }
}
