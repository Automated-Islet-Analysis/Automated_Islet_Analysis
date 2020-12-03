import ImageJ.Stack_Splitter;
import ImageJ.nifti_io.Nifti_Reader;
import ImageJ.nifti_io.Nifti_writer;
import ij.ImagePlus;
import ij.io.FileSaver;
import ij.io.Opener;
import ij.plugin.Concatenator;
import ij.plugin.FolderOpener;
import org.itk.simple.*;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.CLAHE;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Init video file
        String fileName = new String("C:/Users/olivi/OneDrive - Imperial College London/Year 3/Programming 3/Project/Image_treatment-/videos/1.tif");
        Video vid = new Video(fileName);
        // Apply processing on the video
        VideoProcessor videoProcessor = new VideoProcessor(vid);
        videoProcessor.motionCorrect();
        // Save processed video
        vid.setAlignedFrames(videoProcessor.getAlignedFrames());
        vid.saveAlignedFrames();
    }
}
