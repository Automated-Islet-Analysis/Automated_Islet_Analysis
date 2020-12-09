import imageprocessing.Video;
import imageprocessing.VideoProcessor;

public class Main {
    public static void main(String[] args) {
        // Init video file
        String fileName = new String("C:/Users/olivi/OneDrive - Imperial College London/Year 3/Programming 3/Project/Image_treatment--Windows/videos/2_aligned_vid.tif");
        Video vid = new Video(fileName);

        // Apply processing on the video
        VideoProcessor videoProcessor = new VideoProcessor(vid);
        videoProcessor.run(2);

    }
}
