import ij.ImagePlus;
import ij.io.Opener;
import imageprocessing.Video;
import org.itk.simple.SimpleITK;
import org.junit.Assert;
import org.junit.Test;

import java.awt.*;
import java.util.LinkedList;
import org.itk.simple.Image;

public class TestVideo{
    @Test
    public void testReadingFrames(){
        String filename="/Users/fabio/desktop/university/YEAR_3/programming_3/Project/prg3_img/1.tif";
        Video video=new Video(filename);
        String expectedName="1.tif";
        String expectedDirName="/Users/fabio/desktop/university/YEAR_3/programming_3/Project/prg3_img";

        Assert.assertEquals(video.getFilename(),filename);
        Assert.assertEquals(video.getName(),expectedName);
        Assert.assertEquals(video.getDirName(),expectedDirName);

        LinkedList<Image> SEframes= new LinkedList();
        LinkedList<ImagePlus> ijFrames=new LinkedList();

        SEframes=video.getSEframes();
        ijFrames=video.getijframes();

        Opener opener=new Opener();
        for (int i=1; i<=ijFrames.size(); i++){
            Image img1=new Image();
            ImagePlus imagePlus=new ImagePlus();
            img1 = SimpleITK.readImage( "/Users/fabio/desktop/university/YEAR_3/programming_3/Project/prg3_img/singleFrames"+ Integer.toString(i) + "a.tif");
            imagePlus=opener.openImage("/Users/fabio/desktop/university/YEAR_3/programming_3/Project/prg3_img/singleFrames"+ Integer.toString(i) + "a.tif");
            Assert.assertEquals(SEframes.get(i),img1);
            Assert.assertEquals(ijFrames.get(i),imagePlus);
        }
    }
}
