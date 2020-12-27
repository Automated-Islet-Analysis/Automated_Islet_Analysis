import videoprocessing.VideoProcessor;
import videoprocessing.Video;

public class Main {

    private static VideoProcessor videoProcessor;

    public static void main(String[] args) {
        // Path of video to be analyzed
        String filePath = System.getProperty("user.dir")+"/videos/2.tif";

        // processing button
        VideoProcessor videoProcessor = new VideoProcessor(new Video(filePath));
        videoProcessor.process(10,true,true,true);

        // Add cells
//        videoProcessor.addCells(int[] coordinates);

        // Analysis/measurement button
        videoProcessor.analyseCells();

        // Different save options
            videoProcessor.saveRoiImage(System.getProperty("user.dir") + "/temp/"+ "ROIs.jpg"); // Needs to be JPG
        videoProcessor.saveSummary(System.getProperty("user.dir") + "/temp"+ "/Analysis_recap.csv"); // Needs to be .cvs
        videoProcessor.saveCellsMeanIntensity(System.getProperty("user.dir") + "/temp/MI_data"); //is a folder
        videoProcessor.savePlanarCorrectionVid(System.getProperty("user.dir") + "/Planar_motion_correction.tif"); // Needs to be a Tiff
        videoProcessor.saveDepthCorrectionVid(System.getProperty("user.dir") + "/Depth_motion_correction.tif"); // Needs to be a Tiff
    }


}
