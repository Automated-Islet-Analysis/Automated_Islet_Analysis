import ImageJ.Stack_Splitter;
import ImageJ.nifti_io.Nifti_Reader;
import ij.ImagePlus;
import ij.io.FileSaver;
import ij.io.Opener;
import ij.plugin.FolderOpener;
import org.itk.simple.Image;
import org.itk.simple.SimpleITK;
import org.opencv.core.Core;
import java.io.File;
import java.util.LinkedList;


public class Video {
    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }
    protected String filename;
    protected LinkedList<Image> frames = new LinkedList();
    protected LinkedList<Image> alignedFrames = new LinkedList();
    protected int numberOfFrames=0;
    public String name;
    protected String dirName;

    // Constructors
    public Video(String filename){
        this.filename=filename;
        File file = new File(filename);
        name = file.getName();
        dirName = file.getParent();
        readFrames();
    }


    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setAlignedFrames(LinkedList<Image> alignedFrames) {
        this.alignedFrames = alignedFrames;
    }

    public LinkedList<Image> getAlignedFrames() {
        return alignedFrames;
    }

    private void readFrames() {
        ImagePlus vid = new ImagePlus();
        Opener opener = new Opener();

        // Divide video into individual images
        vid = opener.openImage(filename);
        numberOfFrames = vid.getStackSize();
        Stack_Splitter stack_splitter = new Stack_Splitter();
        stack_splitter.run(vid);

        // Load individual files in linked list
        for (int i = 1; i <= numberOfFrames; i++) {
            Image img1 = new Image();
            img1 = SimpleITK.readImage(System.getProperty("user.dir") + "/temp/img/" + String.valueOf(i) + ".tif");
            frames.add(img1);
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

    public void saveAlignedFrames() {
        for (int i = 1; i < numberOfFrames - 1; i++) {

            // Save as nii format used by SimpleElastix
            SimpleITK.writeImage(alignedFrames.get(i), "temp/temp.nii");

            ImagePlus img = new ImagePlus();
            File file = new File(System.getProperty("user.dir") + "/temp/temp.nii");
            Nifti_Reader nifti_reader = new Nifti_Reader();
            img = nifti_reader.run(file);

            FileSaver fileSaver1 = new FileSaver(img);
            fileSaver1.saveAsPng(System.getProperty("user.dir") + "/temp/img/aligned" + String.valueOf(i) + ".png");

        }

        // Combine all the .png into a stack to save it as a video(tiff format)
        ImagePlus alignedVid = new ImagePlus();
        FolderOpener folderOpener = new FolderOpener();
        alignedVid = folderOpener.open(System.getProperty("user.dir") + "/temp/img");

        FileSaver fileSaver2 = new FileSaver(alignedVid);
        fileSaver2.saveAsTiff(System.getProperty("user.dir") + "/videos/" + name.substring(0,name.lastIndexOf(".")) + "_aligned_vid.tif");

        // Remove all temporary files from img
        File directory = new File(System.getProperty("user.dir")+"/temp/img");
        File[] files = directory.listFiles();
        for (File file : files){
            if (!file.delete()){System.out.println("Failed to delete "+file);}
        }
    }
}


