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
import java.lang.Math;
import java.io.File;
import java.util.LinkedList;

import static ij.IJ.Roi;
import static java.lang.Double.MAX_VALUE;

public class VideoProcessor extends Video {
    private LinkedList<RegionOfIntrest> regionOfIntrests = new LinkedList();
    private int roi_size=100;
    private int kernel_size =5;

    public VideoProcessor(Video video){
        super(video.filename);
    }

    public void run(int arg){
        if (arg==1) {
            removeZMotion();
        } else {
            ijFrames4Processing = ijFrames;
            SEFrames4Processing = SEframes;
        }
        if(arg==1){
            motionCorrect();
        }

        // Find cells
        findCells();
        analyseCells();
        saveROI();
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
        ParameterMap parMapTrans = SimpleITK.getDefaultParameterMap("translation");
        ElastixImageFilter fil = new ElastixImageFilter();
        fil.setFixedImage(this.SEFrames4Processing.get(0));
        fil.setParameterMap(parMapTrans);

        for (int i=1; i<SEFrames4Processing.size(); i++){ /***/
            fil.setMovingImage(SEFrames4Processing.get(i));
            fil.execute();

            Image out = new Image();
            out = fil.getResultImage();
            SEFrames4Processing.set(i,out);

            SimpleITK.writeImage(out, "temp/temp.nii");
            File file = new File(System.getProperty("user.dir") + "/temp/temp.nii");
            ImageJ.nifti_io.Nifti_Reader nifti_reader = new ImageJ.nifti_io.Nifti_Reader();
            ijFrames4Processing.set(i,nifti_reader.run(file));
        }
    }

    public void findCells(){
        LinkedList<int[]> coor = new LinkedList();
        LinkedList<int[]> coorUnique = new LinkedList();

        // Create filter
        // Dim of circular filter with -2 in middle
        double rMinustwo = 6.5;
        double rZero = 7.5;
        int sizeFilter = 31; // Needs to be uneven
        // init filter
        double[][] kernel = new double[sizeFilter][sizeFilter];
        // Create array with distances from origin(eg (16,16) for 31x31)
        double[] dist = new double[sizeFilter*sizeFilter];
        for (int i = (int) -Math.ceil(sizeFilter/2); i<Math.ceil(sizeFilter/2); i++){
            for (int j = (int) -Math.ceil(sizeFilter/2); j<Math.ceil(sizeFilter/2); j++){
                dist[(int) (sizeFilter*(i+Math.ceil(sizeFilter/2)) + j+Math.ceil(sizeFilter/2))]= Math.sqrt(i*i + j*j);
            }
        }
        // Find the distances within the circle of -2 and 0
        for(int i=0; i<sizeFilter*sizeFilter;i++){
            if (dist[i]<rMinustwo) kernel[Math.floorDiv(i,sizeFilter)][i%sizeFilter]=-2;
            else if(dist[i]<rZero) kernel[Math.floorDiv(i,sizeFilter)][i%sizeFilter]=0;
            else kernel[Math.floorDiv(i,sizeFilter)][i%sizeFilter]=1;
        }

        // Loop over each image and find the three highest peaks
        Convolution convolution = new Convolution();
        for (int i=0;i<ijFrames4Processing.size();i++){
            System.out.println(i);
            // Convert Image to ImagePlus
            ImagePlus img = new ImagePlus();
            img = ijFrames4Processing.get(i);
            ImageProcessor imgP;
            imgP = img.getProcessor();

            // Image to array for quicker conv2D
            int width=img.getWidth();
            int height=img.getHeight();
            double[][] frame = new double[height][width];
            for(int y = 0; y < height; ++y) {
                for(int x = 0; x < width; ++x) {
                    frame[y][x] = imgP.getf(x, y);
                }
            }

            // Apply circular filter
            frame = convolution.convolution2D(frame, height,width,kernel,sizeFilter,sizeFilter);
            // Find three maxima per frame
            for (int z=0;z<3;z++){ // Find the three maxima
                double vPix_max = Double.NEGATIVE_INFINITY;
                int xMax = 0;
                int yMax = 0;
                for(int y = 0; y < height-sizeFilter; ++y) {
                    for(int x = 0; x < width-sizeFilter; ++x) {
                        if (frame[y][x] > vPix_max) {
                            vPix_max = frame[y][x];
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
                for (int p=-50;p<=50;p++){
                    for (int q=-50;q<=50;q++){
                        frame[yMax+p][xMax+q]=0;
                    }
                }
            }
        }
        // Find a unique representation of all found maxima
        coorUnique.add(coor.get(0));
        for (int i=1;i<coor.size();i++){
            double minDist=Math.sqrt(Math.pow(2,coor.get(i)[0]-coorUnique.get(0)[0]) + Math.pow(2,coor.get(i)[1]-coorUnique.get(0)[1]));
            for(int j=1;j<coorUnique.size();j++){
                double distance = Math.sqrt(Math.pow(2,coor.get(i)[0]-coorUnique.get(j)[0]) + Math.pow(2,coor.get(i)[1]-coorUnique.get(j)[1]));
                if(distance<minDist) minDist=distance;
            }
            if(minDist>30){
                int[] a = new int[2];
                a[0]=coor.get(i)[0];
                a[1]=coor.get(i)[1];
                coorUnique.add(coor.get(i));
                regionOfIntrests.add(new RegionOfIntrest(a,regionOfIntrests.size()+1,coor.get(i)[2],roi_size));
            }
        }
    }

    public void analyseCells(){
        roi_size = 100; // needs to be even
        kernel_size =5;
        // create video for each region around the identified cell
        System.out.println(regionOfIntrests.size());
        for(int i=0;i<regionOfIntrests.size();i++){
            regionOfIntrests.get(i).setRoiIntracellular(kernel_size,ijFrames4Processing);
            regionOfIntrests.get(i).saveRois(ijFrames4Processing);
            regionOfIntrests.get(i).computeMeanIntensity();
        }
    }

    public void saveROI(){
        ImagePlus img = ijFrames4Processing.get(0);
        ImageProcessor iP = img.getProcessor();

        for(int i=0;i<regionOfIntrests.size();i++){
            int[] coor = regionOfIntrests.get(i).getCoor();
            for (int p=-10;p<=10;p++){
                for (int q=-10;q<=10;q++){
                    iP.setf(coor[0]+p,coor[1]+q,0);
                }
            }
        }
        FileSaver fileSaver=new FileSaver(img);
        fileSaver.saveAsPng(System.getProperty("user.dir") + "/img/" + name.substring(0,name.lastIndexOf(".")) + "_ROI.png");

    }
}
