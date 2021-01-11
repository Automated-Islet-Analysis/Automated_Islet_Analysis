package videoprocessing.processor;

import videoprocessing.Cell;
import videoprocessing.CellError;
import videoprocessing.Video;

public class CellAnalyzer extends Processor {
    private int roiSize;
    public CellAnalyzer(Video video,int roiSize) {
        super(video);
        this.roiSize=roiSize;
    }

    @Override
    public ProcessorError run() {
        // create video for each region around the identified cell
        for(Cell cell :video.getCells()){
            // Find for each cell a ROI
            cell.setRoiIntracellular(roiSize,video.getIjFrames());
            // Save the ROI to video so that it can be used later for measurements
            CellError cellError;
            cellError =cell.setPixROI(video.getIjFrames(), video.getIdxFramesInFocus());
            if(cellError==CellError.CELL_NO_FRAME_IN_FOCUS_ERROR) return ProcessorError.PROCESSOR_NO_FRAME_IN_FOCUS_ERROR;
            // Compute the mean intensity of each ROI from save video
            cellError = cell.computeMeanIntensity();
            if(cellError==CellError.CELL_NO_ROI_ERROR) return ProcessorError.PROCESSOR_NO_DATA_ERROR;
        }
        return ProcessorError.PROCESSOR_SUCCESS;
    }
}
