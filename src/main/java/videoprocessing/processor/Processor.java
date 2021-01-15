/**
 * Class that contains all the common features of a processor of the backend,
 * ie. video to process, getters, and generic run function
 *
 * @author Team Automated analysis of "islet in eye", Bioengineering department, Imperial College London
 *
 * Last modified: 11/01/2021
 */

package videoprocessing.processor;

import ij.ImagePlus;
import org.itk.simple.Image;
import videoprocessing.Cell;
import videoprocessing.Video;

import java.util.LinkedList;

public abstract class Processor {
    // Video that holds data for processing
    protected Video video;

    // Constructor
    public Processor(Video video){
        this.video = video;
    }

    // Getters
    public LinkedList<ImagePlus> getIjFrames(){
        return video.getIjFrames();
    }
    public LinkedList<Image> getSEFrames(){
        return video.getSEFrames();
    }
    public LinkedList<Integer> getIdxFramesInFocus(){
        return video.getIdxFramesInFocus();
    }
    public LinkedList<Cell> getCells(){
        return video.getCells();
    }

    // Main function of a processor (equivalent to run function in ImageJ Plugin class)
    public abstract ProcessorError run();
}