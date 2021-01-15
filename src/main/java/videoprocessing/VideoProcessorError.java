/**
 * Contains errors for the processing of the VideoProcessor class
 *
 * @author Team Automated analysis of "islet in eye", Bioengineering department, Imperial College London
 *
 * Last modified: 11/01/2021
 */

package videoprocessing;

public enum VideoProcessorError {
    VIDEO_PROCESSOR_SUCCESS,
    VIDEO_PROCESSOR_NOT_VIDEO_ERROR,
    VIDEO_PROCESSOR_DEPTH_MOTION_ERROR,
    VIDEO_PROCESSOR_OUT_OF_BOUNDS_ERROR,
    VIDEO_PROCESSOR_TEMP_ERROR,
    VIDEO_PROCESSOR_OUT_OF_MEMORY_ERROR
}