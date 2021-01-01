package videoprocessing;

public class CombinerTest {
    package videoprocessing;

import ij.ImagePlus;
import ij.process.ImageProcessor;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;

    public class CombinerTest {
        @Test
        public void testCombining(){
            //List storing all frames of the combined video from Combiner
            LinkedList<ImagePlus> combinedVid;
            //List storing the expected frames of the combined video
            ImagePlus expectedCombinedVid;

            //Input for Combiner method combine
            ImagePlus leftImg=new ImagePlus(System.getProperty("user.dir") + "/img/Unit_testing/leftImg.tif");
            ImagePlus rightImg=new ImagePlus(System.getProperty("user.dir") + "/img/Unit_testing/rightImg.tif");

            //Create instance of Combiner
            Combiner combiner=new Combiner();
            //Call method combiner to produce combinedVid
            combiner.combine(leftImg,rightImg);
            //Getter for combined video frames
            combinedVid=combiner.getVidFrames();

            //Frames of the combinedVid and expectedCombinedVid are compared by taking the
            //difference of their intensity stored in pixelDiff
            long pixelDiff=0;
            for(int i=0;i<combinedVid.size();i++){
                expectedCombinedVid=new ImagePlus(System.getProperty("user.dir") + "/img/Unit_testing/combinedVid"+Integer.toString(i+1)+".tif");
                ImageProcessor imageProcessor=expectedCombinedVid.getProcessor();
                ImageProcessor imageProcessor1=combinedVid.get(i).getProcessor();

                int width=expectedCombinedVid.getWidth();
                int height=expectedCombinedVid.getHeight();

                for(int x=0;x<height;x++){
                    for(int y=0;x<width;x++){
                        int grayIP=(int) imageProcessor.getf(x,y);
                        int grayIP1=(int) imageProcessor1.getf(x,y);
                        pixelDiff+=Math.abs(grayIP-grayIP1);
                    }
                }
            }

            //If pixelDiff is not equal to zero, the two video are not the same and the test
            //is failed
            if (pixelDiff!=0) {
                Assert.fail();
            }
        }
    }

}
