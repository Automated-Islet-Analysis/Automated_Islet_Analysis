
import videoprocessing.Video;
import videoprocessing.VideoProcessor;
import videoprocessing.VideoProcessor;
import videoprocessing.Video;

import java.awt.*;

public class Main extends Frame {

    public static void main(String[] args) {
        // Controller takes care of switching between displays
        Controller controller = new Controller();
        controller.setVisible(true);
        // Path of video to be analyzed
        String filePath = System.getProperty("user.dir")+"/videos/2.tif";

        VideoProcessor videoProcessor = new VideoProcessor(new Video(filePath));
        // Apply processing on the video
        videoProcessor.process(10,true,true,true);
        videoProcessor.analyseCells();
        videoProcessor.saveSummary();
    }

}
