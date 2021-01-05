package videoprocessing.process;

import ij.ImagePlus;
import org.itk.simple.Image;
import videoprocessing.Cell;
import videoprocessing.Video;

import java.util.LinkedList;

public abstract class Processor {
    protected Video video;

    public Processor(Video video){
        this.video = video;
    }

    public abstract ProcessorError run();

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

}
