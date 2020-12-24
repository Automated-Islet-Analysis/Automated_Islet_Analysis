package imageprocessing;

import ij.process.ImageProcessor;
import imageprocessing.ImageJ.Stack_Splitter;
import ij.ImagePlus;
import ij.io.FileSaver;
import ij.io.Opener;
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

//    protected LinkedList<Image> SEFrames4Processing = new LinkedList();
//    protected LinkedList<ImagePlus> ijFrames4Processing = new LinkedList();

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


    public void saveFrames(String outFilename) {
        ImagePlus leftVid;

        if(idxFramesInFocus.size()==0){ // After Planar motion correction
            leftVid = vid;
            for (int z = 0; z<numberOfFrames; z++) {
                ImagePlus img = ijFrames.get(z);
                FileSaver fS = new FileSaver(img);
                fS.saveAsPng(System.getProperty("user.dir") + "/temp/img/" + String.valueOf(z)+".png");
            }
        }
        else{ // After Depth motion correction
            File file = new File(System.getProperty("user.dir") +"/temp/video/Planar_aligned_single.tif");
            if(file.exists()) leftVid = new ImagePlus(file.getPath());
            else leftVid = vid;

            int p=0;
            for (int z = 0; z<numberOfFrames; z++) {
                if(idxFramesInFocus.get(p)==z){
                    ImagePlus img = ijFrames.get(z);
                    FileSaver fS = new FileSaver(img);
                    fS.saveAsPng(System.getProperty("user.dir") + "/temp/img/" + String.valueOf(z)+".png");
                    p++;
                }
                else{
                    ImageProcessor ip = ijFrames.get(0).getProcessor();
                    ImageProcessor newip = ip.createProcessor(ip.getWidth(), ip.getHeight());
                    String sImLabel = String.valueOf(z);
                    ImagePlus im = new ImagePlus(sImLabel, newip);
                    FileSaver fS = new FileSaver(im);
                    fS.saveAsPng(System.getProperty("user.dir") + "/temp/img/" + String.valueOf(z)+".png");
                }
            }
        }

        ImagePlus processedVid=new ImagePlus();
        FolderOpener folderOpener=new FolderOpener();
        processedVid = folderOpener.open(System.getProperty("user.dir") + "/temp/img");
        FileSaver fS1 = new FileSaver(processedVid);
        fS1.saveAsTiff(System.getProperty("user.dir") +"/temp/video/"+outFilename+"_single.tif");

        Combiner combiner = new Combiner();
        ImagePlus combinedVid = combiner.combine(leftVid,processedVid);


        FileSaver fS = new FileSaver(combinedVid);
        fS.saveAsTiff(System.getProperty("user.dir") +"/temp/video/"+outFilename+".tif");


        File dir = new File(System.getProperty("user.dir") + "/temp/img");
        for(File file: dir.listFiles())
            if (!file.isDirectory())
                file.delete();
    }

}


