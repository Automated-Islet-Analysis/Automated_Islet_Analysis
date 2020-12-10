import ij.ImagePlus;
import ij.io.Opener;
import imageprocessing.Video;
import org.itk.simple.SimpleITK;
import org.junit.Assert;
import org.junit.Test;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;


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



//        Opener opener=new Opener();
//        for (int i=1; i<=ijFrames.size(); i++){
//            Image img1=new Image();
//            ImagePlus imagePlus=new ImagePlus();
//            img1 = SimpleITK.readImage( System.getProperty("user.dir") + "/img/Unit_testing/" + String.valueOf(i) + ".tif");
//            imagePlus=opener.openImage(System.getProperty("user.dir") + "/img/Unit_testing/" + String.valueOf(i) + ".tif");
//            Assert.assertEquals(SEframes.get(i),img1);
//            Assert.assertEquals(ijFrames.get(i),imagePlus);
//        }


        BufferedImage img1=null;
        BufferedImage imgij=null;
        BufferedImage imgSE=null;
        long differenceSE=0;
        long differenceij=0;
        for (int i=1; i<=ijFrames.size(); i++) {
            File fileImg = new File(System.getProperty("user.dir") + "/img/Unit_testing/" + String.valueOf(i) + ".tif");
            img1 = ImageIO.read(fileImg);

            imgSE=ImageIO.read((ImageInputStream) SEframes.get(i));
            imgij=ImageIO.read((ImageInputStream) ijFrames.get(i));

            int width=imgSE.getWidth();
            int height=imgSE.getHeight();

            for(int y=0; y<height;y++) {
                for(int x=0;x<width;x++) {
                    int grayImg1=img1.getRGB(x,y)&0xFF;
                    int grayImgSE=imgSE.getRGB(x,y)&0xFF;
                    int grayImgij=imgij.getRGB(x,y)&0xFF;
                    differenceSE+=Math.abs(grayImg1-grayImgSE);
                    differenceij+=Math.abs(grayImg1-grayImgij);
                }
            }
        }
        if (differenceij!=0||differenceSE!=0) {
            Assert.fail();
        }

    }
}
