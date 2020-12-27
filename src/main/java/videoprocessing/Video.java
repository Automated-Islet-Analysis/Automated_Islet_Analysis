package videoprocessing;

import ij.ImageStack;
import ij.io.FileSaver;
import videoprocessing.ImageJ.*;

import ij.ImagePlus;
import ij.io.Opener;
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
    public ImagePlus getVid() { return vid; }
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
        ijFrames = stack_splitter.run(vid);

        // Load individual files in linked list
        for (int i = 1; i <= numberOfFrames; i++) {
            FileSaver fileSaver = new FileSaver(ijFrames.get(i-1));
            fileSaver.saveAsTiff(System.getProperty("user.dir") + "/temp.tif");
            Image image;
            image = SimpleITK.readImage(System.getProperty("user.dir") + "/temp.tif");
            SEFrames.add(image);

        }
        File file = new File(System.getProperty("user.dir") + "/temp.tif");
        file.delete();
    }

    public ImagePlus framesToImagePlus(){
        ImageStack stackOut = new ImageStack();
        for(ImagePlus imagePlus:ijFrames)
            stackOut.addSlice(imagePlus.getProcessor());
        return new ImagePlus("out",stackOut);
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


