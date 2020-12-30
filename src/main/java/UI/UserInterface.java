package UI;

import videoprocessing.VideoProcessor;

public class UserInterface {

    private static VideoProcessor videoProcessor;

    public static VideoProcessor getVideoProcessor() {
        return videoProcessor;
    }

    public static void setVideoProcessor(VideoProcessor videoProcessor1) {
        videoProcessor = videoProcessor1;
    }

    public UserInterface(){
        // Controller takes care of switching between displays
        Controller controller = new Controller();
        controller.setVisible(true);
    }



}
