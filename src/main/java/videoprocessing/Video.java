package videoprocessing;

import videoprocessing.ImageJ.*;


import ij.ImagePlus;
import ij.io.FileSaver;
import ij.io.Opener;
import ij.plugin.FolderOpener;
import ij.process.ImageProcessor;
import org.itk.simple.Image;
import org.itk.simple.SimpleITK;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;


public class Video {
    private final String filename;
    private final String name;
    private final String dirName;
    // original file in ImagePlus format
    private ImagePlus vid;
    // Video width
    private int width;
    // Video height
    private int height;
    private int numberOfFrames=0;
    // Variable holds index of the frames without Depth motion(also called Z motion)
    private LinkedList<Integer> idxFramesInFocus = new LinkedList<>();
    // Holds frames in SimpleElastix and ImageJ frames for processing
    private LinkedList<Image> SEFrames = new LinkedList<>();
    private LinkedList<ImagePlus> ijFrames = new LinkedList<>();
    // The regions of interest of a video, those are the identified Beta-cells
    private LinkedList<Cell> cells = new LinkedList<>();


    // Constructors
    public Video(String filename){
        this.filename=filename;
        File file = new File(filename);
        name = file.getName();
        dirName = file.getParent();
        // Check if input file is corrupt
        checkUncorrupted();
        // Load frames into vid,SEFrames, ijFrames
        readFrames();
    }


    // Getters
    public String getDirName() {return dirName;}
    public String getFilename() {return filename;}
    public int getHeight() { return height;}
    public LinkedList<Integer> getIdxFramesInFocus() {return idxFramesInFocus; }
    public LinkedList<ImagePlus> getIjFrames() {return ijFrames;}
    public String getName() {return name;}
    public int getNumberOfFrames() {return numberOfFrames;}
    public LinkedList<Cell> getCells() {return cells;}
    public LinkedList<Image> getSEFrames() {return SEFrames;}
    public int getWidth() { return width; }

    //Setters
    public void setHeight(int height) { this.height = height;}
    public void setIjFrames(LinkedList<ImagePlus> ijFrames) {this.ijFrames = ijFrames;}
    public void setIdxFramesInFocus(LinkedList<Integer> idxFramesInFocus) {this.idxFramesInFocus = idxFramesInFocus;}
    public void setCells(LinkedList<Cell> regionsOfInterest) {this.cells = regionsOfInterest;}
    public void setSEFrames(LinkedList<Image> SEFrames) {this.SEFrames = SEFrames;}
    public void setWidth(int width){this.width = width;}

    // Additional variable manipulation
    public void addCell(Cell cell){cells.add(cell);}
    public void clearSEFrames(){SEFrames.clear();}

    // Load frames into vid ijFrames and SEFrames
    private void readFrames() {
        // Open input file
        vid = new ImagePlus();
        Opener opener = new Opener();
        vid = opener.openImage(filename);

        // Initialise variables
        numberOfFrames = vid.getStackSize();
        width = vid.getWidth();
        height = vid.getHeight();

        // Divide video into individual images
        Stack_Splitter stack_splitter = new Stack_Splitter();
        stack_splitter.run(vid);

        // Load individual files in linked list
        for (int i = 1; i <= numberOfFrames; i++) {
            Image image;
            ImagePlus imagePlus;

            image = SimpleITK.readImage(System.getProperty("user.dir") + "/temp/img/" + (i) + ".tif");
            SEFrames.add(image);
            imagePlus = opener.openImage(System.getProperty("user.dir") + "/temp/img/" + (i) + ".tif");
            ijFrames.add(imagePlus);
        }

        // Delete all temporary files
        File directory = new File(System.getProperty("user.dir") + "/temp/img");
        File[] files = directory.listFiles();
        for (File file : files) {
            if (!file.delete()) {
                System.out.println("Failed to delete " + file);
            }
        }
    }

    // Save Motion corrected frames into a video for display in GUI
    public void saveFrames(String outFilename) {
        // Variable for image on left of video
        ImagePlus leftVid;

        // Check if you need to add black frames(for Depth motion correction)
        if(idxFramesInFocus.size()==0){
            // No black frames, left image is the input video, right is motion corrected for planar motion
            leftVid = vid;
            for (int z = 0; z<numberOfFrames; z++) {
                ImagePlus img = ijFrames.get(z);
                FileSaver fS = new FileSaver(img);
                fS.saveAsPng(System.getProperty("user.dir") + "/temp/img/" + (z)+".png");
            }
        }
        else{
            // Black frames needed, left is motion corrected for planar motion(if planar motion corrected), right black frames for depth movement
            // Check if planar motion was performed and initialise left video accordingly
            File file = new File(System.getProperty("user.dir") +"/temp/video/Planar_aligned_single.tif");
            if(file.exists()) leftVid = new ImagePlus(file.getPath());
            else leftVid = vid;

            int p=0;
            for (int z = 0; z<numberOfFrames; z++) {
                // Add frame if no depth motion
                if(idxFramesInFocus.get(p)==z){
                    ImagePlus img = ijFrames.get(z);
                    FileSaver fS = new FileSaver(img);
                    fS.saveAsPng(System.getProperty("user.dir") + "/temp/img/" + (z)+".png");
                    p++;
                }
                // Add black frame when depth motion
                else{
                    ImageProcessor ip = ijFrames.get(0).getProcessor();
                    ImageProcessor newIp = ip.createProcessor(ip.getWidth(), ip.getHeight());
                    String sImLabel = String.valueOf(z);
                    ImagePlus im = new ImagePlus(sImLabel, newIp);
                    FileSaver fS = new FileSaver(im);
                    fS.saveAsPng(System.getProperty("user.dir") + "/temp/img/" + (z)+".png");
                }
            }
        }
        // Combine frames of right video into a video
        ImagePlus processedVid;
        FolderOpener folderOpener=new FolderOpener();
        processedVid = folderOpener.open(System.getProperty("user.dir") + "/temp/img");

        // Save video without combining for comparison
        FileSaver fS1 = new FileSaver(processedVid);
        fS1.saveAsTiff(System.getProperty("user.dir") +"/temp/video/"+outFilename+"_single.tif");

        // Combine left video and right video into one
        Combiner combiner = new Combiner();
        ImagePlus combinedVid = combiner.combine(leftVid,processedVid);

        // Save combined video
        FileSaver fS = new FileSaver(combinedVid);
        fS.saveAsTiff(System.getProperty("user.dir") +"/temp/video/"+outFilename+".tif");

        // Clear temporary files used
        File dir = new File(System.getProperty("user.dir") + "/temp/img");
        for(File file: dir.listFiles())
            if (!file.isDirectory())
                file.delete();
    }

    // Check if input file is corrupted
    private void checkUncorrupted(){
        BufferedImage img;
        try{
            File file=new File(filename);
            img=ImageIO.read(file);
        }
        catch (IOException e){
            System.out.println(e);
        }
    }
}


