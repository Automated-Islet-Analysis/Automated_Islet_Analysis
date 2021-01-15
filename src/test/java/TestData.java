import UI.Controller;
import UI.HomeTab.Home;
import org.fest.swing.core.BasicRobot;
import org.fest.swing.core.Robot;
import org.fest.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import videoprocessing.Video;
import videoprocessing.VideoProcessor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.concurrent.TimeUnit;

    public class TestData {
        static private Robot robotf=BasicRobot.robotWithNewAwtHierarchy();
        static private FrameFixture controller;

        private final ByteArrayOutputStream outputStreamCaptor=new ByteArrayOutputStream();



        @Before
        public void setUp(){
            String filepath=System.getProperty("user.dir")+"/Video_for_Testing_short.tif";
            Video video =new Video(filepath);
            VideoProcessor videoProcessor=new VideoProcessor(video);

            videoProcessor.process(10,true,true,true);

            Home.setVideoProcessor(videoProcessor);

            controller=new FrameFixture(robotf,new Controller());
            System.setOut(new PrintStream(outputStreamCaptor));
        }

        @After
        public void tearDown(){
            robotf.cleanUp();
            controller.cleanUp();


        }

        @Test
        public void testController() throws InterruptedException, IOException {
            Home.setFileUploaded(true);
            Home.setAnalysedImg(true);

            controller.menuItem("Data").click();
            TimeUnit.SECONDS.sleep(1);
            controller.menuItem("ROIs").click();
            TimeUnit.SECONDS.sleep(1);

            controller.menuItem("Data").click();
            controller.menuItem("Results").click();
            TimeUnit.SECONDS.sleep(1);

            controller.menuItem("Data").click();
            controller.menuItem("MCVideo").click();
            TimeUnit.SECONDS.sleep(1);
            controller.menuItem("MCPlanar").click();

            controller.menuItem("Data").click();
            controller.menuItem("MCVideo").click();
            TimeUnit.SECONDS.sleep(1);
            controller.menuItem("MCDepth").click();

        }
    }
