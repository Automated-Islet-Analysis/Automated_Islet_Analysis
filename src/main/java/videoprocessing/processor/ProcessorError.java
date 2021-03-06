/**
 * Contains errors for all Processor classes
 *
 * @author Team Automated analysis of "islet in eye", Bioengineering department, Imperial College London
 *
 * Last modified: 11/01/2021
 */

package videoprocessing.processor;

public enum ProcessorError {
    PROCESSOR_SUCCESS,
    PROCESSOR_IMAGE_ERROR,
    PROCESSOR_TEMP_WRITE_ERROR,
    PROCESSOR_TEMP_READ_ERROR,
    PROCESSOR_TEMP_DELETE_ERROR,
    PROCESSOR_NO_FRAME_IN_FOCUS_ERROR,
    PROCESSOR_CELL_OUT_OF_FRAME_ERROR,
    PROCESSOR_NO_DATA_ERROR
}
