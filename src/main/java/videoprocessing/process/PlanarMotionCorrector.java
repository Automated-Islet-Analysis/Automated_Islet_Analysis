package videoprocessing.process;

import ij.ImagePlus;
import org.itk.simple.Image;
import org.itk.simple.SimpleITK;
import videoprocessing.Video;

import java.io.File;
import java.util.LinkedList;

public class PlanarMotionCorrector extends Processor{

    public PlanarMotionCorrector(Video video) {
        super(video);
    }

    @Override
    public void run(){
        LinkedList<ImagePlus> ijFrames = new LinkedList<>();

        // Shift each frame using SimpleElastix
        for (Image frame : video.getSEFrames()){
            Image out;
            out = SimpleITK.elastix(video.getSEFrames().get(0),frame,"translation");

            SimpleITK.writeImage(out, "temp.nii");
            File file = new File(System.getProperty("user.dir") + "/temp.nii");
            ImageJ.nifti_io.Nifti_Reader nifti_reader = new ImageJ.nifti_io.Nifti_Reader();
            ijFrames.add(nifti_reader.run(file));

        }
        new File(System.getProperty("user.dir") + "/temp.nii").delete();

        video.setIjFrames(ijFrames);
        // Not needed anymore, better to delete it to reduce the risk of running out of memory
        video.clearSEFrames();

    }

}
