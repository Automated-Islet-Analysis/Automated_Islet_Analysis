/**
 * Test of Video class
 *
 * @author Team Automated analysis of "islet in eye", Bioengineering department, Imperial College London
 *
 * Last modified: 11/01/2021
 */
package videoprocessing;

import ij.ImagePlus;
import ij.process.ImageProcessor;
import junit.framework.TestCase;
import org.itk.simple.Image;
import org.itk.simple.SimpleITK;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.LinkedList;

public class VideoTest extends TestCase {
    @Test
    public void testReadingFrames(){
        //Checks if the frames of the video uploaded are equal to the actual video frames
        String filename=System.getProperty("user.dir")+"/videos/Video_for_Testing_short.tif";
        Video video=new Video(filename);

        String expectedName="Video_for_Testing_short.tif";
        String expectedDirName=System.getProperty("user.dir")+"\\videos";

        Assert.assertEquals(video.getFilename(),filename);
        Assert.assertEquals(video.getName(),expectedName);
        System.out.println(video.getDirName());
        Assert.assertEquals(video.getDirName(),expectedDirName);

        LinkedList<Image> SEFrames;
        LinkedList<ImagePlus> ijFrames;
        SEFrames =video.getSEFrames();
        ijFrames=video.getIjFrames();
        
        //Comparison by pixel between each frame
        long differenceSE=0;
        long differenceij=0;
        for (int i=0; i<ijFrames.size(); i++) {
            ImagePlus img1 = new ImagePlus(System.getProperty("user.dir") + "/img/Unit_testing/" +(i+1) + ".tif");
            ImageProcessor imageProcessor = img1.getProcessor();
            SimpleITK.writeImage(SEFrames.get(i), System.getProperty("user.dir") + "/temp.tif");
            ImagePlus imgSE = new ImagePlus(System.getProperty("user.dir") + "/temp.tif");
            ImageProcessor imageProcessorSE = imgSE.getProcessor();
            ImageProcessor imageProcessorij = ijFrames.get(i).getProcessor();


            int width= img1.getWidth();
            int height=img1.getHeight();

            for(int y=0; y<height;y++) {
                for(int x=0;x<width;x++) {
                    int grayImg1= (int) imageProcessor.getf(x,y);
                    int grayImgSE= (int) imageProcessorSE.getf(x,y);
                    int grayImgij=(int) imageProcessorij.getf(x,y);
                    differenceSE+=Math.abs(grayImg1-grayImgSE);
                    differenceij+=Math.abs(grayImg1-grayImgij);
                }
            }
        }
        File file = new File(System.getProperty("user.dir") + "/temp.tif");
        file.delete();
        if (differenceij!=0||differenceSE!=0) {
            Assert.fail();
        }
    }

}

