package videoprocessing;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CellTest {
    private String filePath = System.getProperty("user.dir")+"/videos/Video_forDemo.tif";
    private String expectedDataPath=System.getProperty("user.dir")+"/img/Unit_testing/Data/";
    private static Video video;

    @Before
    public void setUp(){
        video=new Video(filePath);
        VideoProcessor videoProcessor=new VideoProcessor(video);
        videoProcessor.process(10,true,true,true);
    }
    @Test
    public void testSavingMeanIntensityFile() throws IOException {
        for(int i=1;i<video.getCells().size();i++){
            //Read expected and output csv files (1 file for each Region of interest)
            BufferedReader readerExpected=new BufferedReader(new FileReader(expectedDataPath+"MI_data/"+i+".csv"));
            BufferedReader readerActual=new BufferedReader(new FileReader(System.getProperty("user.dir")+"/temp/MI_data/"+i+".csv"));
            String lineExpected;
            String lineActual;
            //Read and compare the two files line by line
            for(int j=0;j<video.getNumberOfFrames();j++){
                lineExpected=readerExpected.readLine();
                lineActual=readerActual.readLine();
                Assert.assertEquals(lineExpected,lineActual);
            }
        }
    }
    @Test
    public void testSettingRoiIntracellular() throws IOException{
        BufferedReader readerExpected=new BufferedReader(new FileReader(expectedDataPath+"Analysis_recap.csv"));
        readerExpected.readLine();
        for(Cell cell:video.getCells()){
            int[] coorRoi=cell.getCoorRoi();
            String[] line=readerExpected.readLine().split(",");
            Assert.assertEquals(line[1],Integer.toString(coorRoi[0]));
            Assert.assertEquals(line[2],Integer.toString((coorRoi[1])));
        }
    }

}