package imageprocessing;

import ij.ImagePlus;
import ij.measure.ResultsTable;
import ij.plugin.filter.Binary;
//import ij.plugin.filter.ParticleAnalyzer;
import ij.process.ImageProcessor;
import imageprocessing.ImageJ.ParticleAnalyzer;
import imageprocessing.ImageJ.Thresholder;
import org.itk.simple.ElastixImageFilter;
import org.itk.simple.Image;
import org.itk.simple.ParameterMap;
import org.itk.simple.SimpleITK;

import static java.lang.Double.MAX_VALUE;

public class VideoProcessor extends Video {
    private ImageProcessor imageProcessor;

    public VideoProcessor(Video video){
        super(video.filename);
        this.alignedFrames = video.alignedFrames;
        imageProcessor = vid.getProcessor();
    }

    public void run(int var){
        removeZMotion();
        motionCorrect();
    }


    // Perform motion correction on frames
    public void motionCorrect(){
        ParameterMap parMapTrans = SimpleITK.getDefaultParameterMap("translation");

        ElastixImageFilter fil = new ElastixImageFilter();
        fil.setFixedImage(this.SEFrames4Processing.get(0));
        fil.setParameterMap(parMapTrans);
        fil.printParameterMap();

        Image img = new Image();
        String msg;
        msg = img.getPixelIDTypeAsString();
        System.out.println(msg);

        for (int i=1; i<numberOfFrames ; i++){ /***/
            fil.setMovingImage(SEFrames4Processing.get(i));
            fil.execute();

            Image out = new Image();
            out = fil.getResultImage();
            alignedFrames.add(out);
        }
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


}
