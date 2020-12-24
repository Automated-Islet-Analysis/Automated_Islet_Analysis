package imageprocessing;

import ij.ImagePlus;
import ij.io.FileSaver;
import ij.plugin.FolderOpener;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;

import java.io.File;

public class Combiner {

    public Combiner(){}

    public ImagePlus combine(ImagePlus imgLeft,ImagePlus imgRight) {
        if (imgLeft.getNFrames() == imgRight.getNFrames()) {
            ImageConverter imageConverterLeft = new ImageConverter(imgLeft);
            imageConverterLeft.convertToGray8();
            ImageConverter imageConverterRight = new ImageConverter(imgLeft);
//            imageConverterRight.convertToGray8();

            ImageProcessor ipLeft = imgLeft.getProcessor();
            ImageProcessor ipRight = imgRight.getProcessor();



            int width = ipLeft.getWidth() + ipRight.getWidth();
            int height;
            if (ipLeft.getHeight() > ipRight.getHeight()) {
                height = ipLeft.getHeight();
            } else {
                height = ipRight.getHeight();
            }

            ImageProcessor combinedImgIp = ipLeft.createProcessor(width, height);
            String sImLabel = "Combined ImagePlus";
            ImagePlus combinedImg = new ImagePlus(sImLabel, combinedImgIp);


            for (int i = 0; i < imgLeft.getNSlices(); i++) {
                imgLeft.setSlice(i);
                imgRight.setSlice(i);

                // Paint left image to output img
                for (int x = 0; x < imgLeft.getWidth(); x++) {
                    for (int y = 0; y < imgLeft.getHeight(); y++) {
                        combinedImgIp.setf(x, y, ipLeft.getf(x, y));
                    }
                }
                // Paint left image to output img
                for (int x = 0; x < imgRight.getWidth(); x++) {
                    for (int y = 0; y < imgRight.getHeight(); y++) {
                        combinedImgIp.setf(x + imgLeft.getWidth(), y, ipRight.getf(x, y));
                    }
                }

                FileSaver fileSaver = new FileSaver(combinedImg);
                fileSaver.saveAsPng(System.getProperty("user.dir") +"/temp/Combiner/"+Integer.toString(i)+".png");
            }

            FolderOpener folderOpener = new FolderOpener();
            ImagePlus combinedVid = folderOpener.openFolder(System.getProperty("user.dir") + "/temp/Combiner");

            // Delete temporary files
            File dir = new File(System.getProperty("user.dir") + "/temp/Combiner");
            for(File file: dir.listFiles())
                if (!file.isDirectory())
                    file.delete();

            return combinedVid;
        } else {
            System.out.println("ERROR: the videos must have the same number of slices!");
            return null;
        }
    }
}
