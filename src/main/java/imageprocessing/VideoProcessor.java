package imageprocessing;

import ij.ImagePlus;
import ij.io.FileSaver;
import ij.measure.ResultsTable;
import ij.plugin.filter.Binary;
import ij.process.ImageProcessor;
import imageprocessing.ImageJ.ParticleAnalyzer;
import imageprocessing.ImageJ.Thresholder;
import org.itk.simple.*;
import org.itk.simple.Image;

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

public class VideoProcessor extends Video {
    private LinkedList<RegionOfIntrest> regionOfIntrests = new LinkedList();
    private int roi_size=50;
    private int kernel_size =10;
    private double thresholdArea=10; // Change in area(%) tolerated

    public VideoProcessor(Video video){
        super(video.filename);
    }

    public void process(int thresholdArea,boolean planarMotionCorrect, boolean depthMotionCorrect,boolean findROIs){
        this.thresholdArea = thresholdArea;

        // Clear all temporary files of past analysis
        clearTemp();

        // Check if videos are larger than 100 by 100 pixels
        if(ijFrames.get(0).getWidth()<=roi_size || ijFrames.get(0).getHeight()<=roi_size ){
            if(ijFrames.get(0).getWidth()<ijFrames.get(0).getHeight()) roi_size =ijFrames.get(0).getWidth()-2;
            else if(ijFrames.get(0).getWidth()>ijFrames.get(0).getHeight()) roi_size =ijFrames.get(0).getHeight()-2;
        }

        // Perform planar motion correction (if needed)
        if(planarMotionCorrect){
            System.out.println("Planar Motion correction in progress");
            motionCorrect();
            // Save planar motion corrected video
            saveFrames("Planar_aligned");

            System.out.println("Done");
        }else{
            saveFrames("Planar_aligned"); // Was not aligned but show that was not aligned on gui
            SEframes.clear(); // Avoid running out of memory
        }

        // Perform depth motion correction (if needed)
        if (depthMotionCorrect) {
            System.out.println("Depth Motion correction in progress");
            removeZMotion();
            // Save depth motion corrected vid
            saveFrames("Depth_aligned");
            System.out.println("Done");
        } else {
            saveFrames("Depth_aligned");
            for(int i=0;i<ijFrames.size();i++)idxFramesInFocus.add(i);
        }

        // Perform automatic detection of Beta-cells (if needed)
        if(findROIs){
            System.out.println("Searching for Beta-cells");
            //Find cells
            findCells();
            System.out.println("Done, found "+regionOfIntrests.size()+" cells");
        }
        saveROIs();
    }

    // Finds region of highest intensity in each cell and measure and save the mean intensity of it
    public void analyseCells(){
        System.out.println("Analysis Beta-cells");
        // create video for each region around the identified cell
        for(int i=0;i<regionOfIntrests.size();i++){
            regionOfIntrests.get(i).setRoiIntracellular(kernel_size,ijFrames);
            regionOfIntrests.get(i).saveRoi(ijFrames,idxFramesInFocus);
            regionOfIntrests.get(i).computeMeanIntensity(true);
            regionOfIntrests.get(i).saveMeanIntensity(idxFramesInFocus);
        }
        System.out.println("Done");
    }

    // Perform motion correction on frames
    public void motionCorrect(){
        ijFrames.clear();
        for (int i=0; i<SEframes.size(); i++){ /***/
            Image out = new Image();
            out = SimpleITK.elastix(SEframes.get(0),SEframes.get(i),"translation");

            SimpleITK.writeImage(out, "temp/temp.nii");
            File file = new File(System.getProperty("user.dir") + "/temp/temp.nii");
            ImageJ.nifti_io.Nifti_Reader nifti_reader = new ImageJ.nifti_io.Nifti_Reader();
            ijFrames.add(nifti_reader.run(file));


//            SimpleITK.writeImage(out,"temp/temp.tif");
//            ImagePlus img = new ImagePlus(System.getProperty("user.dir") + "/temp/temp.tif");
//
//            ijFrames.add(img);
        }
        // Not needed anymore, better to delete it to reduce the risk of running out of memory
        SEframes.clear();
    }

    // Identify and remove for processing frames that have a varying position in the Z direction
    public void removeZMotion(){
        // Find area of each frame
        double area0=0;
        for (int i=0;i<numberOfFrames;i++) {
            ImagePlus forProcessing = new ImagePlus();

            // Create copy without pointer
            ImageProcessor ip = ijFrames.get(i).getProcessor(); // ***
            ImageProcessor newip = ip.createProcessor(ip.getWidth(), ip.getHeight());
            newip.setPixels(ip.getPixelsCopy());
            String sImLabel = String.valueOf(i);
            ImagePlus forProcessing1 = new ImagePlus(sImLabel, newip);
            forProcessing1.setCalibration(ijFrames.get(i).getCalibration());


            // Threshold the image, output is binary stack
            Thresholder thresholder = new Thresholder( forProcessing1);
            thresholder.run(new String("mask"));
            forProcessing = thresholder.getImagePlus();


            Binary binary = new Binary();
            // Remove noise with ImageJ Process > Binary > Erode
            binary.setup("erode", forProcessing);
            binary.run(forProcessing.getProcessor());
            // Make mask by removing holes in the Islet body with ImageJ Process > Binary > Fill Holes
            binary.setup("fill", forProcessing);
            binary.run(forProcessing.getProcessor());

            // Find Area of the cell trough time with ImageJ Analyze > Analyze Particles
            ResultsTable resultsTable = new ResultsTable();
            ParticleAnalyzer particleAnalyzer = new ParticleAnalyzer(resultsTable,0,MAX_VALUE,0,1 );

            particleAnalyzer.analyze(forProcessing);
            double area;
            area = particleAnalyzer.sliceArea();

            if (i==0){
                area0 = area;
                this.idxFramesInFocus.add(i);
//                ijFrames4Processing.add(ijFrames.get(i));
//                SEFrames4Processing.add(SEframes.get(i));
            } else{
                if(((area0-area)/area0)*((area0-area)/area0) < (thresholdArea/100)*(thresholdArea/100)){ // if difference in area is smaller than 10% keep frame else disregard
                    this.idxFramesInFocus.add(i);
//                    ijFrames4Processing.add(ijFrames.get(i));
//                    SEFrames4Processing.add(SEframes.get(i));
                }
            }
        }
    }

    // Automatic selection of individual Beta-cells
    public void findCells() {
        // Variables for maxima after filtering
        int nPeaksPerIslet = 15;
        int disBtwPeaksIslet = 50;
        int distBtwPeaks = 30;
        // Variables for circular kernel
        double rMinustwo = 7.5; // radius of the inner circle that has value -2
        double rZero = 8.5;     // outer radius of disk that has value = 0
        int sizeFilter = 31;    // side of square filter, needs to be uneven


        LinkedList<int[]> coor = new LinkedList();
        LinkedList<int[]> coorUnique = new LinkedList();

        // Create kernel
        // Dim of circular filter with -2 circle in middle, then a disk with value 0 around that and 1 for all other values of the kernel
        // init kernel
        float[] kernel = new float[sizeFilter*sizeFilter];
        // Create array with distances from origin(eg (16,16) for 31x31)
        double[][] dist = new double[sizeFilter][sizeFilter];

        // Find the distances within the circle of -2 and 0
        for (int i = 0; i<sizeFilter; i++){
            for (int j =0; j<sizeFilter; j++){
                dist[i][j] = Math.sqrt(Math.pow(i-Math.ceil(sizeFilter/2),2) + Math.pow(j-Math.ceil(sizeFilter/2),2));
            }
        }
        // Give correct value depending on the distance to the center of the filter
        int filt_sum = 0;
        for (int i = 0; i<sizeFilter; i++){
            for (int j = 0; j<sizeFilter; j++){
                if (dist[i][j]<rMinustwo) kernel[i*sizeFilter + j]=-2.f;
                else if(dist[i][j]<rZero) kernel[i*sizeFilter + j]=0.f;
                else kernel[i*sizeFilter + j]=1.f ;
                filt_sum = (int) (filt_sum +kernel[i*sizeFilter + j]);
            }
        }
        // Normalise filter to avoid that convolution yields results over 256
        for (int i = 0; i<sizeFilter; i++) {
            for (int j = 0; j < sizeFilter; j++) {
                kernel[i*sizeFilter + j] = kernel[i*sizeFilter + j]/filt_sum;
            }
        }

        // Loop over each image and find the three highest peaks
        for (int i:idxFramesInFocus){ //
            // Image to BufferImage for quicker conv2D
            int width = ijFrames.get(i).getWidth();
            int height = ijFrames.get(i).getHeight();
            BufferedImage bI = new BufferedImage(width,height,BufferedImage.TYPE_BYTE_GRAY);;
            bI = ijFrames.get(i).getBufferedImage();
            BufferedImage bIOut=new BufferedImage(width,height,BufferedImage.TYPE_BYTE_GRAY);

            // Apply circular filter
            Kernel kernel1 = new Kernel(sizeFilter,sizeFilter,kernel);
            ConvolveOp convolveOp = new ConvolveOp(kernel1, ConvolveOp.EDGE_NO_OP,null);
            convolveOp.filter(bI,bIOut);

            // Find nPeaksPerIslet maxima per frame
            for (int z=0;z<nPeaksPerIslet;z++){ // Find the three maxima
                double vPix_max = Double.NEGATIVE_INFINITY;
                int xMax = 0;
                int yMax = 0;
                for(int y = 0; y < height-sizeFilter; ++y) {
                    for(int x = 0; x < width-sizeFilter; ++x) {
                        if (bIOut.getRGB(x,y) > vPix_max) {
                            vPix_max = bIOut.getRGB(x,y);
                            xMax = x;
                            yMax = y;
                        }
                    }
                }
                int[] c=new int[3];
                c[0]=xMax;
                c[1]=yMax;
                c[2]=i;
                coor.add(c);
                for (int p=-disBtwPeaksIslet;p<=disBtwPeaksIslet;p++){
                    for (int q=-disBtwPeaksIslet;q<=disBtwPeaksIslet;q++){
                        if((yMax+p>0) &&(xMax+q>0) &&(xMax+q<width-sizeFilter) &&(yMax+p<height-sizeFilter))
                                bIOut.setRGB(xMax+q,yMax+p,0);
                    }
                }
            }
        }
        // Find a unique representation of all found maxima
        coorUnique.add(coor.get(0));
        int[] a = new int[2];
        a[0]=coor.get(0)[0];
        a[1]=coor.get(0)[1];
        addCells(a,coor.get(0)[2]);
        for (int i=1;i<coor.size();i++){
            double minDist=Math.sqrt(Math.pow(coor.get(i)[0]-coorUnique.get(0)[0],2) + Math.pow(coor.get(i)[1]-coorUnique.get(0)[1],2));
            for(int j=1;j<coorUnique.size();j++){
                double distance = Math.sqrt(Math.pow(coor.get(i)[0]-coorUnique.get(j)[0],2) + Math.pow(coor.get(i)[1]-coorUnique.get(j)[1],2));
                if(distance<minDist) minDist=distance;
            }
            if(minDist>distBtwPeaks){
                int[] a_ = new int[2];
                a_[0]=coor.get(i)[0];
                a_[1]=coor.get(i)[1];
                coorUnique.add(coor.get(i));
                addCells(a_,coor.get(i)[2]);
            }
        }
    }

    public void addCells(int[] coorNewCell,int frameNum){
        RegionOfIntrest regionOfIntrest_ = new RegionOfIntrest(coorNewCell,regionOfIntrests.size()+1,frameNum,roi_size);
        regionOfIntrests.add(regionOfIntrest_);
    }

    // Used to add ROI for manual selection of ROI
    public void addCells(LinkedList<int[]> coorNewCells){
        for(int i=0;i<coorNewCells.size();i++){
            RegionOfIntrest regionOfIntrest_ = new RegionOfIntrest(coorNewCells.get(i),regionOfIntrests.size()+1,0,roi_size);
            regionOfIntrests.add(regionOfIntrest_);
        }
    }

    // Save image with Roi shown as numbers on first frame
    public void saveROIs(){
        ImagePlus img = ijFrames.get(0);
        ImageProcessor ip = img.getProcessor();

        ImageProcessor newip = ip.createProcessor(ip.getWidth(), ip.getHeight());
        newip.setPixels(ip.getPixelsCopy());
        String sImLabel = "ROIs";
        ImagePlus forProcessing = new ImagePlus(sImLabel, newip);


        // change all the ROI to a black cube on the image
        for(int i=0;i<regionOfIntrests.size();i++){
            int[] coor = regionOfIntrests.get(i).getCoor();
            newip.setColor(Color.BLACK);
            newip.setFontSize(18);
            newip.drawString(Integer.toString(regionOfIntrests.get(i).getRoiNum()),coor[0]-5,coor[1]+10);
        }
        FileSaver fileSaver=new FileSaver(forProcessing);
        fileSaver.saveAsJpeg(System.getProperty("user.dir") + "/temp/" + name.substring(0,name.lastIndexOf(".")) + "_ROI.jpg");
    }

    public void saveSummary(){
        BufferedWriter br = null;
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
        sb.append(",");
        sb.append(",");
        sb.append(",");
        sb.append("Number of frames without depth motions");
        sb.append(",");
        sb.append(Integer.toString(idxFramesInFocus.size()));
        sb.append(",ROI size");
        sb.append(",");
        sb.append(Integer.toString(kernel_size)+"x"+Integer.toString(kernel_size));
        sb.append("\n");

        for (RegionOfIntrest ROI : regionOfIntrests){
            int[] coorROI = ROI.getCoorRoi();

            sb.append(Integer.toString(ROI.getRoiNum()));
            sb.append(",");
            sb.append(Integer.toString(coorROI[0]));
            sb.append(",");
            sb.append(Integer.toString(coorROI[1]));
            sb.append("\n");
        }

        try {
            br.write(sb.toString());
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Clear all temporary files
    public void clearTemp(){
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
}
