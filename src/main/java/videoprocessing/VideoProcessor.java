package videoprocessing;

import ij.ImagePlus;
import ij.io.FileSaver;
import ij.measure.ResultsTable;
import ij.plugin.filter.Binary;
import ij.process.ImageProcessor;
import videoprocessing.ImageJ.ParticleAnalyzer;
import videoprocessing.ImageJ.Thresholder;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Math;
import java.io.File;
import java.util.LinkedList;

import static java.lang.Double.MAX_VALUE;

public class VideoProcessor{
    // Size of region at the identified cell where the ROI can be located (cellSize x cellSize)
    private int cellSize =50;
    // Size of the regions of interest (roiSize x roiSize)
    private final int roiSize =10;
    // Change in area(%) tolerated before disregarding frame for too much depth motion
    private double thresholdArea;
    // Video to be processed
    Video video;

    // Constructor
    public VideoProcessor(Video video){
        this.video = video;
        // Create/check if all temporary needed for processing are present
        makeTemp();
    }

    // Process video depending on input values
    public void process(int thresholdArea,boolean planarMotionCorrect, boolean depthMotionCorrect,boolean findCells){
        // Set threshold for depth motion correction
        this.thresholdArea = thresholdArea;

        // Clear all temporary files of past analysis
        clearTemp();

        // Check if videos are larger than a single ROI, if so change ROI dimension to smallest dimension of input video
        if(video.getWidth()<= cellSize || video.getHeight()<= cellSize){
            if(video.getWidth()<video.getHeight()) cellSize =video.getWidth()-2;
            else if(video.getWidth()>video.getHeight()) cellSize =video.getHeight()-2;
        }

        // Perform planar motion correction (if needed)
        if(planarMotionCorrect){
            System.out.println("Planar Motion correction in progress");
            planarMotionCorrect();
            // Save planar motion corrected video
            video.saveFrames("Planar_aligned");
            System.out.println("Done");
        }else{
            // Clear SEFrames to avoid running out of memory
            video.clearSEFrames();
        }

        // Perform depth motion correction (if needed)
        if (depthMotionCorrect) {
            System.out.println("Depth Motion correction in progress");
            depthMotionCorrect();
            // Save depth motion corrected vid
            video.saveFrames("Depth_aligned");
            System.out.println("Done");
        } else {
            // Set that all frames are valid for further processing
            LinkedList<Integer>idxFramesInFocus = new LinkedList<>();
            for(int i=0;i<video.getNumberOfFrames();i++)idxFramesInFocus.add(i);
            video.setIdxFramesInFocus(idxFramesInFocus);
        }

        // Perform automatic detection of Beta-cells (if needed)
        if(findCells){
            System.out.println("Searching for Beta-cells");
            //Find Beta-cells
            findCells();
            System.out.println("Done, found "+video.getCells().size()+" cells");
        }
        // Save img that show the Cells/ROIs found for display on GUI
        saveCells();
    }

    // Analyse the identified Beta-cells
    public void analyseCells(){
        System.out.println("Analysis Beta-cells");
        // create video for each region around the identified cell
        for(Cell cell :video.getCells()){
            // Find for each cell a ROI
            cell.setRoiIntracellular(roiSize,video.getIjFrames());
            // Save the ROI to video so that it can be used later for measurements
            cell.saveRoiVideo(video.getIjFrames(), video.getIdxFramesInFocus());
            // Compute the mean intensity of each ROI from save video
            cell.computeMeanIntensity(true);
            // Save computed mean intensities to cvs file
            cell.saveMeanIntensity(video.getIdxFramesInFocus());
        }
        System.out.println("Done");
    }

    // Save a summary of the results of the processing
    public void saveSummary(){
        // Create buffer writer to write .cvs file
        BufferedWriter br = null;
        // Try to create buffer
        try {
            br = new BufferedWriter(new FileWriter(System.getProperty("user.dir") + "/temp/Analysis_recap.csv"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Headers and number of frames without depth motion
        StringBuilder sb = new StringBuilder();
        sb.append("ROI number");
        sb.append(",");
        sb.append("x coordinate(width)");
        sb.append(",");
        sb.append("y coordinate(height)");
        sb.append(",,,");
        sb.append("Number of frames without depth motions");
        sb.append(",");
        sb.append(video.getIdxFramesInFocus().size());
        sb.append(",ROI size");
        sb.append(",");
        sb.append(roiSize +"x"+ roiSize);
        sb.append("\n");

        // Write the number of the cell and its coordinates
        for (Cell cell : video.getCells()){
            int[] coorCell = cell.getCoorRoi();
            sb.append(cell.getCellNum());
            sb.append(",");
            sb.append(coorCell[0]);
            sb.append(",");
            sb.append(coorCell[1]);
            sb.append("\n");
        }

        // Add stringbuiler as a string to buffer writer
        try {
            br.write(sb.toString());
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Perform motion correction on frames
    private void planarMotionCorrect(){
        LinkedList<ImagePlus>ijFrames = new LinkedList<>();

        for (Image frame : video.getSEFrames()){
            Image out;
            out = SimpleITK.elastix(video.getSEFrames().get(0),frame,"translation");

            SimpleITK.writeImage(out, "temp/temp.nii");
            File file = new File(System.getProperty("user.dir") + "/temp/temp.nii");
            ImageJ.nifti_io.Nifti_Reader nifti_reader = new ImageJ.nifti_io.Nifti_Reader();
            ijFrames.add(nifti_reader.run(file));

        }
        video.setIjFrames(ijFrames);
        // Not needed anymore, better to delete it to reduce the risk of running out of memory
        video.clearSEFrames();
    }

    // Identify and remove for processing frames that have a varying position in the depth direction
    private void depthMotionCorrect(){
        // Find area of each frame
        // Initialise variable
        double area0=0;
        LinkedList<Integer>idxFramesInFocus = new LinkedList<>();
        for (int i=0;i<video.getNumberOfFrames();i++) {
            ImagePlus forProcessing;

            // Create copy without pointer
            ImageProcessor ip = video.getIjFrames().get(i).getProcessor(); // ***
            ImageProcessor newIp = ip.createProcessor(ip.getWidth(), ip.getHeight());
            newIp.setPixels(ip.getPixelsCopy());
            String sImLabel = String.valueOf(i);
            ImagePlus forProcessing1 = new ImagePlus(sImLabel, newIp);
            forProcessing1.setCalibration(video.getIjFrames().get(i).getCalibration());


            // Threshold the image, output is binary stack
            Thresholder thresholder = new Thresholder( forProcessing1);
            thresholder.run("mask");
            forProcessing = thresholder.getImagePlus();

            // Remove noise, ImageJ equivalent: ImageJ Process > Binary > Erode
            Binary binary = new Binary();
            binary.setup("erode", forProcessing);
            binary.run(forProcessing.getProcessor());

            // Make mask by removing holes in the Islet body, ImageJ equivalent: ImageJ Process > Binary > Fill Holes
            binary.setup("fill", forProcessing);
            binary.run(forProcessing.getProcessor());

            // Find Area of the cell trough time by fitting ellipse to cell, ImageJ equivalent: ImageJ Analyze > Analyze Particles
            ResultsTable resultsTable = new ResultsTable();
            ParticleAnalyzer particleAnalyzer = new ParticleAnalyzer(resultsTable,0,MAX_VALUE,0,1 );
            particleAnalyzer.analyze(forProcessing);
            double area;
            area = particleAnalyzer.sliceArea();

            // Keep frame for processing if the area is less that thresholdArea% different from the area of the first frame
            if (i==0){
                area0 = area;
                idxFramesInFocus.add(i);
            } else{
                if(((area0-area)/area0)*((area0-area)/area0) < (thresholdArea/100)*(thresholdArea/100)){ // if difference in area is smaller than 10% keep frame else disregard
                    idxFramesInFocus.add(i);
                }
            }
        }
        video.setIdxFramesInFocus(idxFramesInFocus);
    }

    // Automatic search of Beta-cells
    private void findCells() {
        // Variables for search of cells  after filtering
        // Number of peaks=cells to be found per frame
        final int nPeaksPerFrame = 15;
        // Minimal distance between two cells on individual frame
        final int disBtwPeaksFrame = 50;
        // Minimal distance between cells in overall video
        final int distBtwPeaks = 30;

        // Variables of circular kernel for filtering
        // radius of the inner circle that has value -2 and inner radius of torus with value 0
        final double rMinusTwo = 7.5;
        // outer radius of torus that has value = 0
        final double rZero = 8.5;
        // side of square filter, needs to be uneven
        final int sizeFilter = 31;

        // Variable that stores all the cells found = nPeaksPerFrame x number of frames
        LinkedList<int[]> coor = new LinkedList<>();
        // Variable that stores all cells from coor that are at least distBtwPeaks appart
        LinkedList<int[]> coorUnique = new LinkedList<>();

        // Create kernel, circular kernel with -2 circle in middle, a torus with value 0 around it and 1's for all other values of the kernel
        // Initialise kernel
        float[] kernel = new float[sizeFilter*sizeFilter];

        // Array that stores distances from center(ie. (16,16) for 31x31) of the kernel
        double[][] dist = new double[sizeFilter][sizeFilter];
        // Find the distances from the center
        for (int i = 0; i<sizeFilter; i++){
            for (int j =0; j<sizeFilter; j++){
                dist[i][j] = Math.sqrt(Math.pow(i-Math.ceil(sizeFilter/2),2) + Math.pow(j-Math.ceil(sizeFilter/2),2));
            }
        }

        // Give correct value to kernel element depending on the distance to the center
        int filtSum = 0;
        for (int i = 0; i<sizeFilter; i++){
            for (int j = 0; j<sizeFilter; j++){
                if (dist[i][j]<rMinusTwo) kernel[i*sizeFilter + j]=-2.f;
                else if(dist[i][j]<rZero) kernel[i*sizeFilter + j]=0.f;
                else kernel[i*sizeFilter + j]=1.f ;
                filtSum = (int) (filtSum +kernel[i*sizeFilter + j]);
            }
        }
        // Normalise filter to avoid that convolution yields results over 256
        for (int i = 0; i<sizeFilter; i++) {
            for (int j = 0; j < sizeFilter; j++) {
                kernel[i*sizeFilter + j] = kernel[i*sizeFilter + j]/filtSum;
            }
        }

        // Loop over each image and find the nPeaksPerFrame highest peaks
        int width = video.getIjFrames().get(0).getWidth();
        int height = video.getIjFrames().get(0).getHeight();
        for (int i:video.getIdxFramesInFocus()){ //
            // Image to BufferImage for quicker 2D convolution
            BufferedImage bI;
            bI = video.getIjFrames().get(i).getBufferedImage();
            // Initialise output buffer image for convolution
            BufferedImage bIOut=new BufferedImage(width,height,BufferedImage.TYPE_BYTE_GRAY);

            // Apply circular kernel
            Kernel kernel1 = new Kernel(sizeFilter,sizeFilter,kernel);
            ConvolveOp convolveOp = new ConvolveOp(kernel1, ConvolveOp.EDGE_NO_OP,null);
            convolveOp.filter(bI,bIOut);

            // Find nPeaksPerFrame maxima per frame
            // Next 12 lines adapted from https://imagejdocu.tudor.lu/plugin/analysis/find_min_max/start
            for (int z = 0; z< nPeaksPerFrame; z++){
                // Find maximum
                double max = Double.NEGATIVE_INFINITY;
                int xMax = 0;
                int yMax = 0;
                for(int y = 0; y < height; ++y) {
                    for(int x = 0; x < width; ++x) {
                        if (bIOut.getRGB(x,y) > max) {
                            max = bIOut.getRGB(x,y);
                            xMax = x;
                            yMax = y;
                        }
                    }
                }
                int[] c=new int[3];
                c[0]=xMax;
                c[1]=yMax;
                c[2]=i;
                // Add coordinate of the maximum
                coor.add(c);
                // Set the pixel values in a square around the last identified maximum to 0
                // so that next maximum is different from all the already identified maxima
                for (int p = -disBtwPeaksFrame; p<= disBtwPeaksFrame; p++){
                    for (int q = -disBtwPeaksFrame; q<= disBtwPeaksFrame; q++){
                        if((yMax+p>0) &&(xMax+q>0) &&(xMax+q<width-sizeFilter) &&(yMax+p<height-sizeFilter))
                            bIOut.setRGB(xMax+q,yMax+p,0);
                    }
                }
            }
        }

        // Keep only the peaks that are at least distBtwPeaks apart
        coorUnique.add(coor.get(0));
        int[] cellCoor = new int[2];
        cellCoor[0]=coor.get(0)[0];
        cellCoor[1]=coor.get(0)[1];
        addCell(cellCoor,coor.get(0)[2]);
        for (int i=1;i<coor.size();i++){
            double minDist=Math.sqrt(Math.pow(coor.get(i)[0]-coorUnique.get(0)[0],2) + Math.pow(coor.get(i)[1]-coorUnique.get(0)[1],2));
            for(int j=1;j<coorUnique.size();j++){
                double distance = Math.sqrt(Math.pow(coor.get(i)[0]-coorUnique.get(j)[0],2) + Math.pow(coor.get(i)[1]-coorUnique.get(j)[1],2));
                if(distance<minDist) minDist=distance;
            }
            if(minDist>distBtwPeaks){
                int[] cellCoor1 = new int[2];
                cellCoor1[0]=coor.get(i)[0];
                cellCoor1[1]=coor.get(i)[1];
                coorUnique.add(coor.get(i));
                addCell(cellCoor1,coor.get(i)[2]);
            }
        }
    }

    // Add identified cell to video
    private void addCell(int[] cellCoor, int frameNum){
        // Create a Cell
        Cell cell = new Cell(cellCoor,video.getCells().size()+1,frameNum, cellSize);
        // Add cell to video
        video.addCell(cell);
    }

    // Add multiple cells, used to add cells after manual selection of cells in GUI
    private void addCells(LinkedList<int[]> cellCoors){
        for(int[] coor:cellCoors){
            addCell(coor,0);
        }
    }

    // Save image with cells shown as numbers for display in GUI
    private void saveCells(){
        // Select first frame because it is the reference frame
        ImagePlus img = video.getIjFrames().get(0);
        ImageProcessor ip = img.getProcessor();
        // Create copy of the first frame to prevent changing video
        ImageProcessor newIp = ip.createProcessor(ip.getWidth(), ip.getHeight());
        newIp.setPixels(ip.getPixelsCopy());
        String sImLabel = "Cells";
        ImagePlus forProcessing = new ImagePlus(sImLabel, newIp);

        // For each cell draw cell number on the first frame
        for(Cell cell :video.getCells()){
            int[] coor = cell.getCoor();
            newIp.setColor(Color.BLACK);
            newIp.setFontSize(18);
            newIp.drawString(Integer.toString(cell.getCellNum()),coor[0]-5,coor[1]+10);
        }
        // Save modified first frame
        FileSaver fileSaver=new FileSaver(forProcessing);
        fileSaver.saveAsJpeg(System.getProperty("user.dir") + "/temp/" + video.getName().substring(0,video.getName().lastIndexOf(".")) + "_ROIs.jpg");
    }

    // Clear all temporary files
    private void clearTemp(){
        File dir = new File(System.getProperty("user.dir") + "/temp");
        for(File file: dir.listFiles())
            if (!file.isDirectory())
                file.delete();
            else {
                for(File file1: file.listFiles())
                    if (!file1.isDirectory())
                        file1.delete();
                    else{
                        for(File file2: file1.listFiles())
                            if (!file2.isDirectory())
                                file2.delete();
                            else{
                                for(File file3: file2.listFiles())
                                    if (!file3.isDirectory())
                                        file3.delete();
                            }
                    }
            }
        File dir2 = new File(System.getProperty("user.dir") + "/img");
        for(File file: dir2.listFiles())
            if (!file.isDirectory())
                file.delete();
    }

    // Make sure that all necessary folders in temp are present for temporary files during processing
    private void makeTemp(){
        LinkedList<File>folders = new LinkedList<>();
        folders.add(new File(System.getProperty("user.dir")+"/temp/Combiner"));
        folders.add(new File(System.getProperty("user.dir")+"/temp/img"));
        folders.add(new File(System.getProperty("user.dir")+"/temp/video"));
        folders.add(new File(System.getProperty("user.dir")+"/temp/video/cells"));
        folders.add(new File(System.getProperty("user.dir")+"/temp/video/ROI"));

        for(File folder:folders){
            try{
                folder.mkdir();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

}
