/**
 * Processor to perform planar motion correction.
 *
 * @author Team Automated analysis of "islet in eye", Bioengineering department, Imperial College London
 *
 * Last modified: 11/01/2021
 */

package videoprocessing.processor;

import ij.ImagePlus;
import org.itk.simple.Image;
import org.itk.simple.SimpleITK;
import videoprocessing.ImageJ.nifti_io.Nifti_Reader;
import videoprocessing.Video;

import java.io.File;
import java.util.LinkedList;

public class PlanarMotionCorrector extends Processor{

    // Constructor
    public PlanarMotionCorrector(Video video) {
        super(video);
    }

    // Perform planar motion correction using SimplElastix toolkit
    @Override
    public ProcessorError run(){
        LinkedList<ImagePlus> ijFrames = new LinkedList<>();
        LinkedList<Image> SEFrames = video.getSEFrames();

        // Check if motion correction can be performed (at least two frames)
        if(SEFrames.size()==0) return ProcessorError.PROCESSOR_NO_DATA_ERROR;
        if(SEFrames.size()==1) return ProcessorError.PROCESSOR_IMAGE_ERROR;

        // Shift each frame using SimpleElastix to align with first frame
        for (Image frame : SEFrames){

            Image out;
            /* Reference 1 - taken from https://simpleelastix.readthedocs.io/HelloWorld.html*/
            out = SimpleITK.elastix(SEFrames.get(0),frame,"translation");
            /* end of reference 1 */

            try {
                SimpleITK.writeImage(out, "temp.nii");
            }catch (Exception e){
                return ProcessorError.PROCESSOR_TEMP_WRITE_ERROR;
            }
            File file = new File(System.getProperty("user.dir") + "/temp.nii");
            try{
                Nifti_Reader nifti_reader = new Nifti_Reader();
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

        // Update ijFrames of video to contain motion corrected frames
        video.setIjFrames(ijFrames);

        return ProcessorError.PROCESSOR_SUCCESS;
    }
}
