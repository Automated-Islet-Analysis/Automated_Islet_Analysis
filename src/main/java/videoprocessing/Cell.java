/**
 * Class that deals with individual cells with for each cell 1 ROI.
 * Contains the variables describing the cell/ROI.
 * Contains also the methods to find and analyse ROI in cell and save the results of analysis
 *
 * @author Team Automated analysis of "islet in eye", Bioengineering department, Imperial College London
 *
 * Last modified: 11/01/2021
 */

package videoprocessing;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.Roi;
import ij.process.ImageProcessor;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.stream.DoubleStream;

public class Cell {
    // Coordinate in pixels of the middle of the cell
    private final int[] coorCell ;
    // Coordinate in pixels of the ROI in the intracellular fluid of the cell
    private final int[] coorRoi = new int[2];
    // Number of the frame where the cell was during automatic search, equals zero if the cell was found manually
    private final int frameNum;
    // Cell number
    private final int cellNum;
    // Mean intensity through time
    private double[] meanIntensity;
    // ImageJ ROI of cell in frame
    private final Roi roiExtracellular;
    // ImageJ ROI of ROI in cell
    private Roi roiIntracellular;
    // Cell size, equivalent of cellSize in VideoProcessor class, (cellSize x cellSize)
    private final int cellSize;
    // Pixel values of the ROI for the different frames without depth motion
    private LinkedList<double[]> pixROI = new LinkedList<>();

    // Getters
    public int getCellNum() {
        return cellNum;
    }
    public int[] getCoor() {
        return coorCell;
    }
    public int[] getCoorRoi() {
        return coorRoi;
    }

    // Setters
    public void setRoiIntracellular(int roiSize, LinkedList<ImagePlus>ijFrames) {
        // Crop frame where the cell was found to only cell
        ImagePlus frame ;
        frame = ijFrames.get(frameNum);
        frame.setRoi(roiExtracellular);
        frame = frame.crop();

        // Find square with greatest intensity in cell to use as ROI
        // Create kernel to find average intensity in square
        float[] kernel = new float[roiSize * roiSize];
        for (int j = 0; j< roiSize * roiSize; j++) kernel[j] = 1.f /(roiSize * roiSize);

        // Image to BufferImage for quicker convolution
        int width = frame.getWidth();
        int height = frame.getHeight();
        BufferedImage bI;
        bI = frame.getBufferedImage();
        // Buffer image for output of convolution
        BufferedImage bIOut=new BufferedImage(width,height,BufferedImage.TYPE_BYTE_GRAY);

        // Apply filtering
        Kernel kernel1 = new Kernel(roiSize, roiSize,kernel);
        ConvolveOp convolveOp = new ConvolveOp(kernel1, ConvolveOp.EDGE_ZERO_FILL,null);
        convolveOp.filter(bI,bIOut);

        // Find center of square with greatest average intensity
        // Next 12 lines adapted from https://imagejdocu.tudor.lu/plugin/analysis/find_min_max/start
        double pixMax = Double.NEGATIVE_INFINITY;
        int xMax = 0;
        int yMax = 0;
        for(int y = 0; y < bIOut.getHeight(); ++y) {
            for(int x = 0; x < bIOut.getWidth(); ++x) {
                if (bIOut.getRGB(x,y) > pixMax) {
                    pixMax = bIOut.getRGB(x,y);
                    xMax = x;
                    yMax = y;
                }
            }
        }

        // Set coordinates of found ROI
        coorRoi[0] = coorCell[0]-Math.round(cellSize/2) + xMax;
        coorRoi[1] = coorCell[1]-Math.round(cellSize/2) + yMax;

        //Create ROI at the square with greatest intensity
        roiIntracellular = IJ.Roi(xMax-Math.floor(roiSize /2),yMax-Math.floor(roiSize /2), roiSize, roiSize);
    }

    // Constructor
    public Cell(int[] coorCell, int cellNum, int frameNum, int cellSize ){
        this.coorCell=coorCell; // Coordinate of the center of the cell
        this.cellNum=cellNum;
        this.frameNum=frameNum;
        this.cellSize=cellSize;
        this.roiExtracellular= new Roi(coorCell[0]-Math.floor(cellSize/2),coorCell[1]-Math.floor(cellSize/2),cellSize,cellSize);
    }

    // Save ROI as video so that it can be used later on to compute the mean intensity
    public CellError setPixROI(LinkedList<ImagePlus>ijFrames, LinkedList<Integer> idxFramesInFocus){
        // Crop cell and ROI from full frames
        // Variable for temporary processing
        ImagePlus img;
        // Loop over valid frames for processing
        Rectangle rectangle = roiIntracellular.getBounds();
        if(idxFramesInFocus.size()==0)return CellError.CELL_NO_FRAME_IN_FOCUS_ERROR;
        for(int j : idxFramesInFocus){
            double[] pix = new double[rectangle.width*rectangle.height];
            img = ijFrames.get(j);
            // crop cell in frame
            img.setRoi(roiExtracellular);
            img = img.crop();
            // Set ROI in cell
            img.setRoi(roiIntracellular);
            img = img.crop();
            ImageProcessor iP = img.getProcessor();
            for(int x=0;x<rectangle.width;x++)
                for(int y=0;y<rectangle.height;y++)
                    pix[y*x + y] = iP.getf(x,y);
            pixROI.add(pix);
        }
        return CellError.CELL_SUCCESS;
    }

    // Compute mean intensity of ROI from saved video
    public CellError computeMeanIntensity(){
        if(pixROI.get(0).length==0) return CellError.CELL_NO_ROI_ERROR;
        int nPix = pixROI.get(0).length;
        meanIntensity = new double[pixROI.size()];
        // Find mean intensity of each frame
        for(int i=0;i< pixROI.size();i++)
            meanIntensity[i]= DoubleStream.of(pixROI.get(i)).sum() /nPix ;
        return CellError.CELL_SUCCESS;
    }

    // Save mean intensity measurements to .cvs file
    public void saveMeanIntensityFile(LinkedList<Integer> idxFramesInFocus,String pathToDir){
        // Only save measurements if it was compute beforehand
        if(meanIntensity.length==0){
            System.out.println("ERROR : mean intensity is not computed yet");
        }
        else{
            // Make sure that file can be written
            BufferedWriter br = null;
            try {
                br = new BufferedWriter(new FileWriter(pathToDir+"/"+ cellNum+".csv"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Header
            StringBuilder sb = new StringBuilder();
            sb.append("Frame number");
            sb.append(",");
            sb.append("Mean intensity");
            sb.append(",");
            sb.append(",");
            sb.append("Coordinate(pixel)");
            sb.append(",");
            sb.append(coorRoi[0]);
            sb.append(",");
            sb.append(coorRoi[1]);
            sb.append("\n");

            // Append mean intensity measurements
            for (int i=0;i<meanIntensity.length;i++) {
                sb.append(idxFramesInFocus.get(i));
                sb.append(",");
                sb.append(meanIntensity[i]);
                sb.append("\n");
            }

            // Write to buffer and close file
            try {
                br.write(sb.toString());
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
