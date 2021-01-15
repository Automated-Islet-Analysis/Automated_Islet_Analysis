package videoprocessing;

import ij.ImagePlus;
import ij.ImageStack;

import java.util.LinkedList;

public class MakeImage {

    private LinkedList<ImagePlus> frames;
    private ImagePlus imgOut;

    public MakeImage(LinkedList<ImagePlus> frames){
        this.frames=frames;
        makeImg();
    }

    public ImagePlus getImgOut(){return imgOut;}

    public void makeImg(){
        ImageStack stackOut = new ImageStack();
        for(ImagePlus ip:frames)
            stackOut.addSlice(ip.getProcessor());
        imgOut= new ImagePlus("out",stackOut);
    }
}
