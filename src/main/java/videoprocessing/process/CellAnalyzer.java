package videoprocessing.process;

import videoprocessing.Cell;
import videoprocessing.Video;

public class CellAnalyzer extends Processor {
    private int roiSize;
    public CellAnalyzer(Video video,int roiSize) {
        super(video);
        this.roiSize=roiSize;
    }

    @Override
    public void run() {
        // create video for each region around the identified cell
        for(Cell cell :video.getCells()){
            // Find for each cell a ROI
            cell.setRoiIntracellular(roiSize,video.getIjFrames());
            // Save the ROI to video so that it can be used later for measurements
            cell.setPixROI(video.getIjFrames(), video.getIdxFramesInFocus());
            // Compute the mean intensity of each ROI from save video
            cell.computeMeanIntensity(true);
        }
    }
}
