package videoprocessing;

import org.junit.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CellTest{
    private static String filePath = System.getProperty("user.dir")+"/videos/Video_for_Testing_short.tif";
    private String expectedDataPath=System.getProperty("user.dir")+"/img/Unit_testing/Data/";
    private static Video video;

    @BeforeClass
    public static void setUp(){
        File folder = new File(System.getProperty("user.dir") + "/temp");
        File folder1 = new File(System.getProperty("user.dir") + "/temp/MI_data");
        if(folder.exists())
            folder.delete();
        folder.mkdir();
        if(folder1.exists())
            folder1.delete();
        folder1.mkdir();

        video=new Video(filePath);
        VideoProcessor videoProcessor=new VideoProcessor(video);
        videoProcessor.process(10,true,true,true);
        videoProcessor.analyseCells();

        videoProcessor.saveCellsMeanIntensity(System.getProperty("user.dir") + "/temp/MI_data"); //is a folder
    }

    @AfterClass
    public static void tearDown(){
        File directory=new File(System.getProperty("user.dir") + "/temp/MI_data");
        for(File f: directory.listFiles())
            f.delete();

        directory.delete();
        new File(directory.getParent()).delete();
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
