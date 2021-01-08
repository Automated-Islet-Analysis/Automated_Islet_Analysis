package videoprocessing;

import ij.ImagePlus;
import ij.ImageStack;
import ij.process.ImageProcessor;

import java.util.LinkedList;


public class Combiner {
    //stores frames of combined video. Needed for unit testing
    private LinkedList<ImagePlus> combVidFrames;


    // Constructor
    public Combiner(){combVidFrames=new LinkedList<>();}

    //getters
    public  LinkedList<ImagePlus> getCombVidFrames(){return combVidFrames;}


    // Combines two ImagePlus and combines them horizontally
    // Two ImagePlus need to have the same number of slices
    public ImagePlus combine(ImagePlus imgLeft,ImagePlus imgRight) {
        ImageStack imageStack = new ImageStack();
        if (imgLeft.getNSlices() == imgRight.getNSlices()) {
            // Required for project because video to be analysed can have 16-bit pixels while output always 8-bit
            // and want to make sure both images have same data type for pixels
//            ImageConverter imageConverterLeft = new ImageConverter(imgLeft);
//            imageConverterLeft.convertToGray8();

            // Get processors for processing
            ImageProcessor ipLeft =imgLeft.getProcessor();
            ImageProcessor ipRight = imgRight.getProcessor();

            // Make sure that both vide can fit
            int width = ipLeft.getWidth() + ipRight.getWidth();
            int height;
            if (ipLeft.getHeight() > ipRight.getHeight()) {
                height = ipLeft.getHeight();
            } else {
                height = ipRight.getHeight();
            }

            // Combine all slices
            for (int i = 0; i < imgLeft.getNSlices(); i++) {
                // Image for temporary processing
                ImageProcessor combinedImgIp = ipLeft.createProcessor(width, height);
                String sImLabel = "Combined ImagePlus";
                ImagePlus combinedImg = new ImagePlus(sImLabel, combinedImgIp);

                imgLeft.setSlice(i);
                imgRight.setSlice(i);

                // Paint left image to output img
                for (int x = 0; x < imgLeft.getWidth(); x++) {
                    for (int y = 0; y < imgLeft.getHeight(); y++) {
                        combinedImgIp.setf(x, y, ipLeft.getf(x, y));
                    }
                }
                // Paint right image to output img
                for (int x = 0; x < imgRight.getWidth(); x++) {
                    for (int y = 0; y < imgRight.getHeight(); y++) {
                        combinedImgIp.setf(x + imgLeft.getWidth(), y, ipRight.getf(x, y));
                    }
                }
                // Save combined frame
                imageStack.addSlice(combinedImgIp);

                //Needed for UnitTesting
                combVidFrames.add(combinedImg);
            }
            ImagePlus combinedVid = new ImagePlus("out",imageStack);
            return combinedVid;
        } else {
            System.out.println("ERROR: the videos must have the same number of slices!");
            return null;
        }
    }
}
