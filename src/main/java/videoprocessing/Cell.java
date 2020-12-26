package videoprocessing;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.Roi;
import ij.io.FileSaver;
import ij.plugin.FolderOpener;
import ij.plugin.filter.Convolver;
import ij.process.ImageProcessor;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

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

    // Constructor
    public Cell(int[] coorCell, int cellNum, int frameNum, int cellSize ){
        this.coorCell=coorCell; // Coordinate of the center of the cell
        this.cellNum=cellNum;
        this.frameNum=frameNum;
        this.cellSize=cellSize;
        this.roiExtracellular= new Roi(coorCell[0]-Math.floor(cellSize/2),coorCell[1]-Math.floor(cellSize/2),cellSize,cellSize);
    }

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

    // Save ROI as video so that it can be used later on to compute the mean intensity
    public void saveRoiVideo(LinkedList<ImagePlus>ijFrames, LinkedList<Integer> idxFramesInFocus){
        // Video of cell through time
        ImagePlus videoCell;
        // Video of ROI through time
        ImagePlus videoRoi;


        // Crop cell and ROI from full frames
        // Variable for temporary processing
        ImagePlus img;
        // Loop over valid frames for processing
        for(int j : idxFramesInFocus){
            img = ijFrames.get(j);
            // crop cell in frame
            img.setRoi(roiExtracellular);
            img = img.crop();
            // Set ROI in cell
            img.setRoi(roiIntracellular);
            // Save frame of Cell with ROI
            FileSaver fileSaver = new FileSaver(img);
            fileSaver.saveAsPng(System.getProperty("user.dir") + "/temp/video/cells/img/" + j + ".png");
            // Crop and save frame of ROI
            img = img.crop();
            FileSaver fileSaver1 = new FileSaver(img);
            fileSaver1.saveAsPng(System.getProperty("user.dir") + "/temp/video/ROI/img/" + j + ".png");
        }
        // Combine the frames individual frames to video
        FolderOpener folderOpener = new FolderOpener();
        videoCell = folderOpener.openFolder(System.getProperty("user.dir") + "/temp/video/cells/img");
        videoRoi = folderOpener.openFolder(System.getProperty("user.dir") + "/temp/video/ROI/img");

        // Set ROI on cell video for display
        videoCell.setRoi(roiIntracellular);

        // Save videos
        FileSaver fileSaver1 = new FileSaver(videoCell);
        FileSaver fileSaver2 = new FileSaver(videoRoi);
        fileSaver1.saveAsTiff(System.getProperty("user.dir") + "/temp/video/cells/" + cellNum + ".tif");
        fileSaver2.saveAsTiff(System.getProperty("user.dir") + "/temp/video/ROI/" + cellNum+ ".tif");
    }

    // Compute mean intensity of ROI from saved video
    public void computeMeanIntensity(boolean smooth){
        // Load ROI video
        ImagePlus vid = new ImagePlus(System.getProperty("user.dir")+"/temp/video/ROI/"+cellNum + ".tif");
        // Initialise array that holds values of mean intensity
        meanIntensity = new double[vid.getNSlices()];

        // Kernel used for smoothing to remove noise
        float[] kernel = new float[9];
        for (int j = 0;j<9;j++) kernel[j] = 1.f /(9);

        // Find mean intensity of each frame
        for(int i=0;i<vid.getNSlices();i++){
            // Get i-th frame
            vid.setSlice(i);
            ImageProcessor ip = vid.getProcessor();
            // Create copy of frame to prevent modifying original ijFrames LinkedList
            ImageProcessor imageProcessor = ip.createProcessor(ip.getWidth(), ip.getHeight());
            imageProcessor.setPixels(ip.getPixelsCopy());
            String sImLabel = String.valueOf(i);
            ImagePlus im = new ImagePlus(sImLabel, imageProcessor);
            im.setCalibration(vid.getCalibration());

            // Perform smoothing if necessary
            int width = vid.getWidth();
            int height = vid.getHeight();
            if(smooth) {
                Convolver convolver = new Convolver();
                convolver.convolve(imageProcessor, kernel, width, height);
            }

            // Find mean intensity
            meanIntensity[i]=0;
            for(int j=0;j<vid.getWidth();j++) {
                for (int z = 0; z < vid.getHeight(); z++) {
                    meanIntensity[i] = meanIntensity[i] + imageProcessor.getf(j, z) / (width * height);
                }
            }
        }
    }

    // Save mean intensity measurements to .cvs file
    public void saveMeanIntensity(LinkedList<Integer> idxFramesInFocus){
        // Only save measurements if it was compute beforehand
        if(meanIntensity.length==0){
            System.out.println("ERROR : mean intensity is not computed yet");
        }
        else{
            // Make sure that file can be written
            BufferedWriter br = null;
            try {
                br = new BufferedWriter(new FileWriter(System.getProperty("user.dir") + "/temp/MI_data/"+ cellNum+".csv"));
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
