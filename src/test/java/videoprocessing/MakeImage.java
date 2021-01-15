/**
 * Class making an ImagePlus from a stack of ImagePlus frames
 *
 * @author Team Automated analysis of "islet in eye", Bioengineering department, Imperial College London
 *
 * Last modified: 11/01/2021
 */
package videoprocessing;

import ij.ImagePlus;
import ij.ImageStack;

import java.util.LinkedList;

public class MakeImage {

    private LinkedList<ImagePlus> frames;
    private ImagePlus imgOut;
    
    //Constructor
    public MakeImage(LinkedList<ImagePlus> frames){
        this.frames=frames;
        makeImg();
    }
    
    //getters
    public ImagePlus getImgOut(){return imgOut;}

    //makes an image out of a stack of ImagePlus frames
    public void makeImg(){
        ImageStack stackOut = new ImageStack();
        for(ImagePlus ip:frames)
            stackOut.addSlice(ip.getProcessor());
        imgOut= new ImagePlus("out",stackOut);
    }
}
