package videoprocessing;

import ij.ImagePlus;
import ij.process.ImageProcessor;
import org.junit.Assert;

public class CompareImages {
    private String expectedFile;
    private String actualFile;

    private ImagePlus expectedImg;
    private ImagePlus actualImg;

    private long differenceImg;

    //Constructor
    public CompareImages(String expectedFile, String actualFile){
        this.expectedFile=expectedFile;
        this.actualFile=actualFile;

        doCompare();

    }

    //getters
    public long getDifferenceImg(){return differenceImg;}

    public void doCompare(){
        expectedImg=new ImagePlus(expectedFile);
        actualImg=new ImagePlus(actualFile);

        ImageProcessor imgProcessor1=expectedImg.getProcessor();
        ImageProcessor imgProcessor2=actualImg.getProcessor();

        int width=expectedImg.getWidth();
        int height=expectedImg.getHeight();
        //Compare the images pixel by pixel by taking the absolute value of the difference between the pixel intensity
        for(int y=0; y<height;y++) {
            for(int x=0;x<width;x++) {
                int grayImg1= (int) imgProcessor1.getf(x,y);
                int grayImg2= (int) imgProcessor2.getf(x,y);
                //variable storing the cumulative difference between pixel
                //if differenceImg=0, then the two images are considered equal
                differenceImg+=Math.abs(grayImg1-grayImg2);
            }
        }
    }
}
