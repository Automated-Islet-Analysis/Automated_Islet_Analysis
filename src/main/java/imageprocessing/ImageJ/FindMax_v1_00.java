package imageprocessing.ImageJ;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.plugin.PlugIn;
import ij.process.ImageProcessor;

// code adapted from https://imagejdocu.tudor.lu/plugin/analysis/find_min_max/start

public class FindMax_v1_00 implements PlugIn {
    int[] xMax = new int[3];
    int[] yMax= new int[3];

    public int[] getxMax() {
        return xMax;
    }

    public int[] getyMax() {
        return yMax;
    }

    public FindMax_v1_00() {
    }

    public  void run(String arg){}

    public void run(ImagePlus imp) {
        if (imp == null) {
            IJ.noImage();
        } else {
            int nWidth = imp.getWidth();
            int nHeight = imp.getHeight();
            String strNom_img_src = imp.getTitle();
            int nNb_slices = imp.getNSlices();
            ImageStack imgstackIn = imp.getImageStack();
            int nNum_current_slice = imp.getCurrentSlice();

            for(int nNum_slice = 1; nNum_slice <= nNb_slices; ++nNum_slice) {
                ImageProcessor ipIn = imgstackIn.getProcessor(nNum_slice);
                int nNb_channels = ipIn.getNChannels();
                String strFormat;
                if (nNb_channels >= 3) {
                    strFormat = "%9.0f";
                } else {
                    strFormat = "%9.3f";
                }

                for (int z=0;z<3;z++){ // Find the three maxima
                    float vPix_max = (float)ipIn.minValue();
                    xMax[z] = 0;
                    yMax[z] = 0;


                    for(int y = 0; y < ipIn.getHeight(); ++y) {
                        for(int x = 0; x < ipIn.getWidth(); ++x) {
                            float vPix = ipIn.getf(x, y);
                            if (vPix > vPix_max) {
                                vPix_max = vPix;
                                xMax[z] = x;
                                yMax[z] = y;
                            }
                        }
                    }
                    for (int p=-50;p<=50;p++){
                        for (int q=-50;q<=50;q++){

                            ipIn.setf(xMax[z]+p,yMax[z]+q,0);
                        }
                    }
                }
            }
        }
    }
}

