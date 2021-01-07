package videoprocessing.process;

import ij.ImagePlus;
import ij.measure.ResultsTable;
import ij.plugin.filter.Binary;
import ij.process.ImageProcessor;
import videoprocessing.ImageJ.ParticleAnalyzer;
import videoprocessing.ImageJ.Thresholder;
import videoprocessing.Video;

import java.util.LinkedList;

import static java.lang.Double.MAX_VALUE;

public class DepthMotionCorrector extends Processor {
    private double thresholdArea;
    public DepthMotionCorrector(Video video, double thresholdArea) {
        super(video);
        this.thresholdArea=thresholdArea;
    }

    @Override
    public ProcessorError run(){
        // Find area of each frame
        // Initialise variable
        double area0=0;
        LinkedList<Integer> idxFramesInFocus = new LinkedList<>();
        if(video.getNumberOfFrames()==0) return ProcessorError.PROCESSOR_NO_DATA_ERROR;
        if(video.getNumberOfFrames()==1) return ProcessorError.PROCESSOR_IMAGE_ERROR;
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

        return ProcessorError.PROCESSOR_SUCCESS;
    }

}
