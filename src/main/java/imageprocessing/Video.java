package imageprocessing;

import imageprocessing.ImageJ.Stack_Splitter;
import ij.ImagePlus;
import ij.io.FileSaver;
import ij.io.Opener;
//import ij.plugin.Concatenator;
import ij.plugin.FolderOpener;
import org.itk.simple.Image;
import org.itk.simple.SimpleITK;
import org.opencv.core.Core;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedList;
import java.io.IOException; 


public class Video {
    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }
    protected String filename;
    public String name;
    private String dirName;

    private ImagePlus vid;
    protected int numberOfFrames=0;
    // Variable holds idx of frames without Z motion
    protected LinkedList<Integer> idxFramesInFocus = new LinkedList();
    // Frames loaded when the class is constructed for both SimpleElastix and ImageJ
    protected LinkedList<Image> SEframes = new LinkedList();
    protected LinkedList<ImagePlus> ijFrames = new LinkedList();

    protected LinkedList<Image> SEFrames4Processing = new LinkedList();
    protected LinkedList<ImagePlus> ijFrames4Processing = new LinkedList();

    // Constructors
    public Video(String filename){
        this.filename=filename;
        File file = new File(filename);
        name = file.getName();
        dirName = file.getParent();
        checkUncorrupted();
        readFrames();
    }

    public void checkUncorrupted(){
        BufferedImage img=null;
        try{
            File file=new File(filename);

            img=ImageIO.read(file);
        }
        catch (IOException e){
            System.out.println(e);
        }
    }

    public String getFilename() {return filename;}
    public String getName() {return name;}
    public String getDirName() {return dirName;}

//    public void setFilename(String filename) {this.filename = filename;}

    private void readFrames() {
        vid = new ImagePlus();
        Opener opener = new Opener();

        // Divide video into individual images
        vid = opener.openImage(filename);
        numberOfFrames = vid.getStackSize();
        Stack_Splitter stack_splitter = new Stack_Splitter();
        stack_splitter.run(vid);

        // Load individual files in linked list
        for (int i = 1; i <= numberOfFrames; i++) {
            Image img1 = new Image();
            ImagePlus imagePlus = new ImagePlus();

            img1 = SimpleITK.readImage(System.getProperty("user.dir") + "/temp/img/" + String.valueOf(i) + ".tif");
            SEframes.add(img1);
            imagePlus = opener.openImage(System.getProperty("user.dir") + "/temp/img/" + String.valueOf(i) + ".tif");
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

    public LinkedList<Image> getSEframes() {return SEframes;}
    public LinkedList<ImagePlus> getijframes() {return ijFrames;}


    public void saveAlignedFrames() {
        for (int z = 0; z<this.ijFrames4Processing.size(); z++) {
            ImagePlus img = ijFrames4Processing.get(z);
            FileSaver fS = new FileSaver(img);
            fS.saveAsPng(System.getProperty("user.dir") + "/temp/img/" + String.valueOf(z)+".png");
        }
        ImagePlus vid=new ImagePlus();
        FolderOpener folderOpener=new FolderOpener();
        vid = folderOpener.open(System.getProperty("user.dir") + "/temp/img");

        FileSaver fS = new FileSaver(vid);
        fS.saveAsTiff(System.getProperty("user.dir") +"/videos/"+name.substring(0,name.lastIndexOf("."))+"_aligned_vid.tif");

        File dir = new File(System.getProperty("user.dir") + "/temp/img");
        for(File file: dir.listFiles())
            if (!file.isDirectory())
                file.delete();
    }
}


