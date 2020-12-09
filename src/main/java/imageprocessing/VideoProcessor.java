package imageprocessing;

import ij.ImagePlus;
import ij.gui.Roi;
import ij.io.FileSaver;
import ij.measure.ResultsTable;
import ij.plugin.Concatenator;
import ij.plugin.filter.Binary;
import ij.process.ImageProcessor;
import imageprocessing.ImageJ.ParticleAnalyzer;
import imageprocessing.ImageJ.Thresholder;
import org.itk.simple.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.IOException;
import java.lang.Math;
import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;

import static ij.IJ.Roi;
import static java.lang.Double.MAX_VALUE;

public class VideoProcessor extends Video {
    private LinkedList<RegionOfIntrest> regionOfIntrests = new LinkedList();
    private int roi_size=100;
    private int kernel_size =10;

    public VideoProcessor(Video video){
        super(video.filename);
    }

    public void run(int arg){
        // Clear all temporary files of past analysis
        cleartemp();

        if (arg==1) {
            removeZMotion();
        } else {
            ijFrames4Processing = ijFrames;
            SEFrames4Processing = SEframes;
        }
        if(arg==1){
            motionCorrect();
            // Save motion corrected video
            saveAlignedFrames();
        }


        // Find cells
        findCells();
        analyseCells();
        saveROIs();
    }


    // Identify and remove for processing frames that have a varying position in the Z direction
    public void removeZMotion(){
        // Find area of each frame
        double area0=0;
        for (int i=0;i<numberOfFrames;i++) {
            ImagePlus forProcessing = new ImagePlus();

            // Threshold the image, output is binary stack
            Thresholder thresholder = new Thresholder( this.ijFrames.get(i));
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
            } else{
                if(((area0-area)/area0)*((area0-area)/area0) < 0.01){ // if difference in area is smaller than 10% keep frame else disregard
                    this.idxFramesInFocus.add(i);
                    ijFrames4Processing.add(ijFrames.get(i));
                    SEFrames4Processing.add(SEframes.get(i));
                }
            }
        }
    }


    // Perform motion correction on frames
    public void motionCorrect(){
        ijFrames4Processing.clear();
        for (int i=0; i<SEFrames4Processing.size(); i++){ /***/
            Image out = new Image();
            out = SimpleITK.elastix(SEFrames4Processing.get(0),SEFrames4Processing.get(i),"translation");

            SimpleITK.writeImage(out,"temp/temp.tif");
            ImagePlus img = new ImagePlus(System.getProperty("user.dir") + "/temp/temp.tif");

            ijFrames4Processing.add(img);
        }
        // Not needed anymore, better to delete it to reduce the risk of out of memory
        SEFrames4Processing.clear();

    }

    public void findCells() {
        LinkedList<int[]> coor = new LinkedList();
        LinkedList<int[]> coorUnique = new LinkedList();

        // Create filter
        // Dim of circular filter with -2 in middle
        double rMinustwo = 7.5;
        double rZero = 8.5;
        int sizeFilter = 31; // Needs to be uneven
        // init filter
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
        for (int i=0;i<ijFrames4Processing.size();i++){ //
            System.out.println(i);

            // Image to BufferImage for quicker conv2D
            int width = ijFrames4Processing.get(i).getWidth();
            int height = ijFrames4Processing.get(i).getHeight();
            BufferedImage bI = new BufferedImage(width,height,BufferedImage.TYPE_BYTE_GRAY);;
            bI = ijFrames4Processing.get(i).getBufferedImage();
            BufferedImage bIOut=new BufferedImage(width,height,BufferedImage.TYPE_BYTE_GRAY);

            // Apply circular filter
            Kernel kernel1 = new Kernel(sizeFilter,sizeFilter,kernel);
            ConvolveOp convolveOp = new ConvolveOp(kernel1, ConvolveOp.EDGE_NO_OP,null);
            convolveOp.filter(bI,bIOut);



            // Find three maxima per frame
            for (int z=0;z<5;z++){ // Find the three maxima
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
                for (int p=-30;p<=30;p++){
                    for (int q=-30;q<=30;q++){
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
        RegionOfIntrest regionOfIntrest_ = new RegionOfIntrest(a,regionOfIntrests.size()+1,coor.get(0)[2],roi_size);
        regionOfIntrests.add(regionOfIntrest_);
        for (int i=1;i<coor.size();i++){
            double minDist=Math.sqrt(Math.pow(coor.get(i)[0]-coorUnique.get(0)[0],2) + Math.pow(coor.get(i)[1]-coorUnique.get(0)[1],2));
            for(int j=1;j<coorUnique.size();j++){
                double distance = Math.sqrt(Math.pow(coor.get(i)[0]-coorUnique.get(j)[0],2) + Math.pow(coor.get(i)[1]-coorUnique.get(j)[1],2));
                if(distance<minDist) minDist=distance;
            }
            if(minDist>30){
                int[] a_ = new int[2];
                a_[0]=coor.get(i)[0];
                a_[1]=coor.get(i)[1];
                coorUnique.add(coor.get(i));
                RegionOfIntrest regionOfIntrest = new RegionOfIntrest(a_,regionOfIntrests.size()+1,coor.get(i)[2],roi_size);
                regionOfIntrests.add(regionOfIntrest);
            }
        }
    }

    public void analyseCells(){
        // create video for each region around the identified cell
        System.out.println(regionOfIntrests.size());
        for(int i=0;i<regionOfIntrests.size();i++){
            regionOfIntrests.get(i).setRoiIntracellular(kernel_size,ijFrames4Processing);
            regionOfIntrests.get(i).saveRoi(ijFrames4Processing);
            regionOfIntrests.get(i).computeMeanIntensity();
            regionOfIntrests.get(i).saveMeanIntensity();
        }
    }

    public void saveROIs(){
        ImagePlus img = ijFrames4Processing.get(0);
        ImageProcessor iP = img.getProcessor();

        // change all the ROI to a black cube on the image
        for(int i=0;i<regionOfIntrests.size();i++){
            int[] coor = regionOfIntrests.get(i).getCoor();
            for (int p=-7;p<=7;p++){
                for (int q=-7;q<=7;q++){
                    iP.setf(coor[0]+p,coor[1]+q,0);
                }
            }
        }
        FileSaver fileSaver=new FileSaver(img);
        fileSaver.saveAsPng(System.getProperty("user.dir") + "/img/" + name.substring(0,name.lastIndexOf(".")) + "_ROI.png");
    }

    public void cleartemp(){
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
