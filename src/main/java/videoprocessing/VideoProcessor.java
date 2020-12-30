package videoprocessing;

import ij.ImagePlus;
import ij.ImageStack;
import ij.io.FileSaver;
import ij.process.ImageProcessor;
import videoprocessing.process.CellAnalyzer;
import videoprocessing.process.CellFinder;
import videoprocessing.process.DepthMotionCorrector;
import videoprocessing.process.PlanarMotionCorrector;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class VideoProcessor{
    // Size of region at the identified cell where the ROI can be located (cellSize x cellSize)
    private int cellSize =50;
    // Size of the regions of interest (roiSize x roiSize)
    private final int roiSize =10;

    // Change in area(%) tolerated before disregarding frame for too much depth motion
    private double thresholdArea;

    // Video to be processed
    private Video video;

    // Image with all the roi indicated with their roi number
    private ImagePlus roiImage;
    // Video to show planar motion correction
    private ImagePlus planarCorrectionVid;
    // Video to show depth motion correction
    private ImagePlus depthCorrectionVid;

    // Getters
    public Video getVideo() { return video;}
    public double getThresholdArea() { return thresholdArea; }

    // Constructor
    public VideoProcessor(Video video){
        this.video = video;
    }

    // Process video depending on input values
    public void process(int thresholdArea,boolean planarMotionCorrect, boolean depthMotionCorrect,boolean findCells){
        // pop-up for progress
        JPanel popUp = new JPanel(new GridLayout(3,2));
        JLabel jLabelPlanar = new JLabel();
        JLabel jLabelDepth = new JLabel();
        JLabel jLabelCells = new JLabel();
        JLabel done = new JLabel();
        done.setText(": Done!");
        popUp.setSize(100,100);
        popUp.setVisible(true);


        // Set threshold for depth motion correction
        this.thresholdArea = thresholdArea;

        // Check if videos are larger than a single ROI, if so change ROI dimension to smallest dimension of input video
        if(video.getWidth()<= cellSize || video.getHeight()<= cellSize){
            if(video.getWidth()<video.getHeight()) cellSize =video.getWidth()-2;
            else if(video.getWidth()>video.getHeight()) cellSize =video.getHeight()-2;
        }

        // Perform planar motion correction (if needed)
        if(planarMotionCorrect){
            jLabelPlanar.setText("Planar Motion correction in progress");
            popUp.add(jLabelPlanar);
            System.out.println("Planar Motion correction in progress");
            PlanarMotionCorrector pMC = new PlanarMotionCorrector(video);
            pMC.run();
            video.setIjFrames(pMC.getIjFrames());
            video.setSEFrames(pMC.getSEFrames());
            // Create planar motion corrected video
            createPlanarCorrectionVid();
            popUp.add(done);
            System.out.println("Done");
        }else{
            // Clear SEFrames to avoid running out of memory
            video.clearSEFrames();
        }

        // Perform depth motion correction (if needed)
        if (depthMotionCorrect) {
            jLabelDepth.setText("Depth Motion correction in progress");
            popUp.add(jLabelDepth);
            System.out.println("Depth Motion correction in progress");
            DepthMotionCorrector dMC = new DepthMotionCorrector(video,thresholdArea);
            dMC.run();
            video.setIdxFramesInFocus(dMC.getIdxFramesInFocus());
            // Save depth motion corrected vid
            createDepthCorrectionVid();
            System.out.println("Done");
            popUp.add(done);
        } else {
            // Set that all frames are valid for further processing
            LinkedList<Integer>idxFramesInFocus = new LinkedList<>();
            for(int i=0;i<video.getNumberOfFrames();i++)idxFramesInFocus.add(i);
            video.setIdxFramesInFocus(idxFramesInFocus);
        }

        // Perform automatic detection of Beta-cells (if needed)
        if(findCells){
            jLabelCells.setText("Searching for Beta-cells");
            popUp.add(jLabelCells);
            System.out.println("Searching for Beta-cells");
            //Find Beta-cells
            CellFinder cellFinder = new CellFinder(video,cellSize);
            cellFinder.run();
            video.setCells(cellFinder.getCells());
            done.setText(": Done, found "+video.getCells().size()+" cells");
            popUp.add(done);
            System.out.println("Done, found "+video.getCells().size()+" cells");
        }
        // Save img that show the Cells/ROIs found for display on GUI
        createROIImage();
    }

    // Analyse the identified Beta-cells
    public void analyseCells(){
        System.out.println("Analysis Beta-cells");
        CellAnalyzer cellAnalyzer = new CellAnalyzer(video,roiSize);
        cellAnalyzer.run();
        video.setCells(cellAnalyzer.getCells());
        System.out.println("Done");
    }

    // Add multiple cells, used to add cells after manual selection of cells in GUI
    public void addCells(LinkedList<int[]> cellCoors){
        for(int[] coor:cellCoors){
            Cell cell = new Cell(coor,video.getCells().size()+1,0, cellSize);
            // Add cell to video
            video.addCell(cell);
        }
    }

    private void createPlanarCorrectionVid(){
        ImagePlus leftVid = video.getVid();
        ImagePlus rightVid = video.framesToImagePlus();

        // Combine left video and right video into one
        Combiner combiner = new Combiner();
        planarCorrectionVid =  combiner.combine(leftVid,rightVid);

        FileSaver fileSaver = new FileSaver(planarCorrectionVid);
    }

    public void savePlanarCorrectionVid(String path){
        FileSaver fileSaver = new FileSaver(planarCorrectionVid);
        fileSaver.saveAsTiff(path );
    }

    private void createDepthCorrectionVid(){
        ImagePlus leftVid;
        if(planarCorrectionVid==null)leftVid = video.getVid();
        else leftVid = video.framesToImagePlus();

        ImagePlus rightVid;
        ImageStack rightStack = new ImageStack();
        LinkedList<ImagePlus> ijFrames = video.getIjFrames();

        int p=0;
        for (int z = 0; z<ijFrames.size(); z++) {
            // Add frame if no depth motion
            if(video.getIdxFramesInFocus().get(p)==z){
                ImagePlus img = ijFrames.get(z);
                rightStack.addSlice(img.getProcessor());
                p++;
            }
            // Add black frame when depth motion
            else{
                ImageProcessor ip = ijFrames.get(0).getProcessor();
                ImageProcessor newIp = ip.createProcessor(ip.getWidth(), ip.getHeight());
                rightStack.addSlice(newIp);
            }
        }
        rightVid = new ImagePlus("right",rightStack);

        // Combine left video and right video into one
        Combiner combiner = new Combiner();
        depthCorrectionVid =  combiner.combine(leftVid,rightVid);
    }

    public void saveDepthCorrectionVid(String path){
        FileSaver fileSaver = new FileSaver(depthCorrectionVid);
        fileSaver.saveAsTiff(path);
    }

    // Save image with cells shown as numbers for display in GUI
    private void createROIImage(){
        // Select first frame because it is the reference frame
        ImagePlus img = video.getIjFrames().get(0);
        ImageProcessor ip = img.getProcessor();
        // Create copy of the first frame to prevent changing video
        ImageProcessor newIp = ip.createProcessor(ip.getWidth(), ip.getHeight());
        newIp.setPixels(ip.getPixelsCopy());
        String sImLabel = "Cells";
        ImagePlus roiImage = new ImagePlus(sImLabel, newIp);

        // For each cell draw cell number on the first frame
        for(Cell cell :video.getCells()){
            int[] coor = cell.getCoor();
            newIp.setColor(Color.BLACK);
            newIp.setFontSize(18);
            newIp.drawString(Integer.toString(cell.getCellNum()),coor[0]-5,coor[1]+10);
        }
        this.roiImage = roiImage;
    }

    // Save Image with all ROI shown
    public void saveRoiImage(String path){
        if(roiImage!=null) {
            // Save modified first frame
            FileSaver fileSaver = new FileSaver(roiImage);
            fileSaver.saveAsJpeg(path);
        }else System.out.println("The ROI image was not created yet!");
    }

    // Save Mean intensity measurements
    public void saveCellsMeanIntensity(String pathToDir){
        for(Cell cell:video.getCells())
            cell.saveMeanIntensityFile(video.getIdxFramesInFocus(),pathToDir);
    }

    // Save a summary of the results of the processing
    public void saveSummary(String path){
        // Create buffer writer to write .cvs file
        BufferedWriter br = null;
        // Try to create buffer
        try {
            br = new BufferedWriter(new FileWriter(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Headers and number of frames without depth motion
        StringBuilder sb = new StringBuilder();
        sb.append("ROI number,x coordinate(width),y coordinate(height),,,Number of frames without depth motions,");
        sb.append(video.getIdxFramesInFocus().size());
        sb.append(",ROI size,");
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

        // Add StringBuilder as a string to buffer writer
        try {
            br.write(sb.toString());
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
