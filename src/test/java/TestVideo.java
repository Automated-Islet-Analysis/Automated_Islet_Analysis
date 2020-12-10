import ij.ImagePlus;
import ij.io.Opener;
import imageprocessing.Video;
import org.itk.simple.SimpleITK;
import org.junit.Assert;
import org.junit.Test;
import javax.imageio.ImageIO;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import org.itk.simple.Image;

public class TestVideo{
    @Test
    public void testReadingFrames() throws IOException {
        String filename=System.getProperty("user.dir")+"/videos/1.tif";
        Video video=new Video(filename);
        String expectedName="1.tif";
        String expectedDirName=System.getProperty("user.dir")+"\\videos";

        Assert.assertEquals(video.getFilename(),filename);
        Assert.assertEquals(video.getName(),expectedName);
        System.out.println(video.getDirName());
        Assert.assertEquals(video.getDirName(),expectedDirName);

        LinkedList<Image> SEframes= new LinkedList();
        LinkedList<ImagePlus> ijFrames=new LinkedList();
        SEframes=video.getSEframes();
        ijFrames=video.getijframes();



        Opener opener=new Opener();
        for (int i=1; i<=ijFrames.size(); i++){
            Image img1=new Image();
            ImagePlus imagePlus=new ImagePlus();
            img1 = SimpleITK.readImage( System.getProperty("user.dir") + "/img/Unit_testing/" + String.valueOf(i) + ".tif");
            imagePlus=opener.openImage(System.getProperty("user.dir") + "/img/Unit_testing/" + String.valueOf(i) + ".tif");
            Assert.assertEquals(SEframes.get(i),img1);
            Assert.assertEquals(ijFrames.get(i),imagePlus);
        }

    }
}
