package videoprocessing;

import ij.ImagePlus;
import ij.ImageStack;
import ij.io.FileSaver;
import ij.process.ImageProcessor;
import org.itk.simple.Image;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.LinkedList;

public class CombinerTest {
    @Test
    public void testCombining(){
        //List storing all frames of the combined video from Combiner
        LinkedList<ImagePlus> combinedVid;

        //Input for Combiner method combine
        ImagePlus leftImg=new ImagePlus(System.getProperty("user.dir") + "/img/Unit_testing/1.tif");
        ImagePlus rightImg=new ImagePlus(System.getProperty("user.dir") + "/img/Unit_testing/2.tif");

        //Create instance of Combiner
        Combiner combiner=new Combiner();
        //Call method combiner to produce combinedVid
        combiner.combine(leftImg,rightImg);
        //Getter for combined video frames
        combinedVid=combiner.getCombVidFrames();
        
        //Save the combined video to user directory
        MakeImage makeImage=new MakeImage(combinedVid);
        ImagePlus combVidOut=makeImage.getImgOut();

        FileSaver fileSaver=new FileSaver(combVidOut);
        String actualFile=System.getProperty("user.dir")+"/img/actualCombVid.tif";
        fileSaver.saveAsTiff(actualFile);
        
        //Path of expected combined video
        String expectedFile=System.getProperty("user.dir")+"/img/Unit_testing/ExpectedCombinedVid.tif";
        
        //Compare the two combined videos
        CompareImages compareImages=new CompareImages(expectedFile,actualFile);

        long differenceImg= compareImages.getDifferenceImg();

        if (differenceImg!=0)
            Assert.fail();

        File file=new File(actualFile);
        file.delete();
    }

}

