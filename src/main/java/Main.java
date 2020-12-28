
import DataTab.Data;
import videoprocessing.VideoProcessor;

import java.awt.*;

public class Main extends Frame {

    //Initiate an object of type video processor with its getter and setter to be accessible from all functions

    public static VideoProcessor getVideoProcessor() {
        return videoProcessor;
    }

    public static void setVideoProcessor(VideoProcessor videoProcessor) {
        Main.videoProcessor = videoProcessor;
    }

    private static VideoProcessor videoProcessor;



    public static void main(final String[] args) {
        // Controller takes care of switching between displays
        Controller controller = new Controller();
        controller.setVisible(true);

    }
}

