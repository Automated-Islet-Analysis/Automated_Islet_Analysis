/**
 * Processor to analyze cells, this consists of finding 1 ROI for each cell and measuring its mean intensity through time.
 *
 * @author Team Automated analysis of "islet in eye", Bioengineering department, Imperial College London
 *
 * Last modified: 11/01/2021
 */

package videoprocessing.processor;

import videoprocessing.Cell;
import videoprocessing.CellError;
import videoprocessing.Video;

public class CellAnalyzer extends Processor {
    // holds size/side of roi
    private int roiSize;

    // Constructor
    public CellAnalyzer(Video video,int roiSize) {
        super(video);
        this.roiSize=roiSize;
    }

    // Analyze all cells found previously
    @Override
    public ProcessorError run() {
        CellError cellError;
        if(video.getCells().size()==0) return ProcessorError.PROCESSOR_NO_DATA_ERROR;
        for(Cell cell :video.getCells()){
            // Find for each cell a ROI
            cell.setRoiIntracellular(roiSize,video.getIjFrames());
            // Find pixel values of roi
            cellError =cell.setPixROI(video.getIjFrames(), video.getIdxFramesInFocus());
            if(cellError==CellError.CELL_NO_FRAME_IN_FOCUS_ERROR) return ProcessorError.PROCESSOR_NO_FRAME_IN_FOCUS_ERROR;
            // Compute the mean intensity of each ROI
            cellError = cell.computeMeanIntensity();
            if(cellError==CellError.CELL_NO_ROI_ERROR) return ProcessorError.PROCESSOR_NO_DATA_ERROR;
        }
        return ProcessorError.PROCESSOR_SUCCESS;
    }
}
