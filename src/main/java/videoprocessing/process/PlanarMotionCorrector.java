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
    public ProcessorError run(){
        LinkedList<ImagePlus> ijFrames = new LinkedList<>();
        LinkedList<Image> SEFrames = video.getSEFrames();

        if(SEFrames.size()==0) return ProcessorError.PROCESSOR_NO_DATA_ERROR;
        if(SEFrames.size()==1) return ProcessorError.PROCESSOR_IMAGE_ERROR;
        // Shift each frame using SimpleElastix
        for (Image frame : SEFrames){
            Image out;
            out = SimpleITK.elastix(SEFrames.get(0),frame,"translation");

            try {
                SimpleITK.writeImage(out, "temp.nii");
            }catch (Exception e){
                return ProcessorError.PROCESSOR_TEMP_WRITE_ERROR;
            }
            File file = new File(System.getProperty("user.dir") + "/temp.nii");
            try{
                ImageJ.nifti_io.Nifti_Reader nifti_reader = new ImageJ.nifti_io.Nifti_Reader();
                ijFrames.add(nifti_reader.run(file));
            }catch (Exception e){
                return ProcessorError.PROCESSOR_TEMP_READ_ERROR;
            }
        }
        try {
            new File(System.getProperty("user.dir") + "/temp.nii").delete();
        }catch (Exception e){
            return ProcessorError.PROCESSOR_TEMP_DELETE_ERROR;
        }

        video.setIjFrames(ijFrames);
        // Not needed anymore, better to delete it to reduce the risk of running out of memory
        video.clearSEFrames();

        return ProcessorError.PROCESSOR_SUCCESS;
    }

}
