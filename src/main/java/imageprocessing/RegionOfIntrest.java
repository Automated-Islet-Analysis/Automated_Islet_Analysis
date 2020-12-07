package imageprocessing;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.Roi;
import ij.io.FileSaver;
import ij.plugin.Concatenator;
import ij.plugin.FolderOpener;
import ij.process.ImageProcessor;

import java.io.File;
import java.util.LinkedList;

public class RegionOfIntrest {
    private int[] coor ;
    private int frameNum; // Number of the frame where the cell was detected
    private int roiNum;
    private double[] meanIntensity;
    private Roi roiExtracellular;
    private Roi roiIntracellular;
    private int kernel_size;
    private int roi_size;


    public RegionOfIntrest(int[] coor, int roiNum, int frameNum, int roi_size , int kernel_size){
        this.coor=coor;
        this.roiNum=roiNum;
        this.frameNum=frameNum;
        this.roi_size=roi_size;
        this.roiExtracellular= new Roi(coor[0]-Math.floor(roi_size/2)+Math.round(kernel_size/2),coor[1]-Math.floor(roi_size/2)+Math.round(kernel_size/2),roi_size,roi_size);
    }

    public int[] getCoor() {
        return coor;
    }

        public int getRoiNum() {
        return roiNum;
    }

    public void setRoiNum(int roiNum) {
        this.roiNum = roiNum;
    }

    public Roi getRoiExtracellular() {
        return roiExtracellular;
    }

    public void setRoiIntracellular(int kernel_size, LinkedList<ImagePlus>ijFrames4Processing) {
        // Find square with greatest intensity
        // Filter
        double[][] kernel = new double[kernel_size][kernel_size];
        for (int j = 0;j<kernel_size*kernel_size;j++) kernel[Math.floorDiv(j,kernel_size)][j % kernel_size] = 1;
        // Find frame when cell was found
        ImagePlus frame_ = new ImagePlus();
        Convolution convolution = new Convolution();
        frame_ = ijFrames4Processing.get(frameNum);
        frame_.setRoi(roiExtracellular);
        frame_ = frame_.crop();
        ImageProcessor iP = frame_.getProcessor();
        double[][] frame = new double[roi_size][roi_size];
        for(int j=0;j<roi_size;j++){
            for(int z=0;z<roi_size;z++){
                if((j>0) &&(z>0) &&(j<iP.getHeight()) &&(z<iP.getWidth()))
                    frame[j][z] = iP.getf(z,j);
                else frame[j][z] =0;
            }
        }

        // Filter
        frame = convolution.convolution2D(frame, roi_size,roi_size,kernel,kernel_size,kernel_size);
        // Find max
        double vPix_max = Double.NEGATIVE_INFINITY;
        int xMax = 0;
        int yMax = 0;
        for(int y = 0; y < roi_size-kernel_size; ++y) {
            for(int x = 0; x < roi_size-kernel_size; ++x) {
                if (frame[y][x] > vPix_max) {
                    vPix_max = frame[y][x];
                    xMax = x;
                    yMax = y;
                }
            }
        }
        roiIntracellular = IJ.Roi(xMax+Math.floor(kernel_size/2),yMax+Math.floor(kernel_size/2),kernel_size,kernel_size); // needs some recentering due to conv()
    }

    public void computeMeanIntensity(){
        ImagePlus vid = new ImagePlus(System.getProperty("user.dir")+"/temp/video/ROI/"+String.valueOf(roiNum) + ".tif");
        meanIntensity = new double[vid.getNFrames()];

        // Find intensity foe each frame
        for(int i=0;i<vid.getNFrames();i++){
            // Get i-th frame
            ImagePlus frame = new ImagePlus();
            vid.setSlice(i);
            ImageProcessor ip = vid.getProcessor(); // ***
            ImageProcessor newip = ip.createProcessor(ip.getWidth(),
                    ip.getHeight());
            newip.setPixels(ip.getPixelsCopy());
            String sImLabel = String.valueOf(i);
            ImagePlus im = new ImagePlus(sImLabel, newip);
            im.setCalibration(vid.getCalibration());
            ImageProcessor imageProcessor;
            imageProcessor = im.getProcessor();

            // Find mean intensity
            meanIntensity[i]=0;
            for(int j=0;j<vid.getWidth();j++){
                for(int z=0;z<vid.getHeight();z++){
                    meanIntensity[i] = meanIntensity[i] + imageProcessor.getf(j,z)/(vid.getHeight()*vid.getWidth());
                }
            }
        }
    }

    public void saveRoi(LinkedList<ImagePlus>ijFrames){
        ImagePlus videoCell = new ImagePlus();
        ImagePlus videoRoi = new ImagePlus();
        ImagePlus img = new ImagePlus();

        // Crop the region of interest around the cell and in the cell
        for(int j=0;j<ijFrames.size();j++){
            img = ijFrames.get(j);
            img.setRoi(roiExtracellular);
            img = img.crop();
            img.setRoi(roiIntracellular);
            FileSaver fileSaver = new FileSaver(img);
            fileSaver.saveAsPng(System.getProperty("user.dir") + "/temp/video/cells/img/" + String.valueOf(j) + ".png");
            img = img.crop();
            FileSaver fileSaver1 = new FileSaver(img);
            fileSaver1.saveAsPng(System.getProperty("user.dir") + "/temp/video/ROI/img/" + String.valueOf(j) + ".png");
        }
        FolderOpener folderOpener = new FolderOpener();
        videoCell = folderOpener.openFolder(System.getProperty("user.dir") + "/temp/video/cells/img");
        videoRoi = folderOpener.openFolder(System.getProperty("user.dir") + "/temp/video/ROI/img");

        videoCell.setRoi(roiIntracellular);

        FileSaver fileSaver1 = new FileSaver(videoCell);
        FileSaver fileSaver2 = new FileSaver(videoRoi);
        fileSaver1.saveAsTiff(System.getProperty("user.dir") + "/temp/video/cells/" + String.valueOf(roiNum) + ".tif");
        fileSaver2.saveAsTiff(System.getProperty("user.dir") + "/temp/video/ROI/" + String.valueOf(roiNum) + ".tif");
    }
}
