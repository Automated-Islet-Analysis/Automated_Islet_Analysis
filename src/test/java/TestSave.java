import UI.Controller;
import UI.HomeTab.Home;
import org.fest.swing.core.BasicRobot;
import org.fest.swing.core.Robot;
import org.fest.swing.finder.JFileChooserFinder;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JFileChooserFixture;
import org.fest.swing.fixture.JMenuItemFixture;
import org.fest.swing.fixture.JPopupMenuFixture;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import videoprocessing.Video;
import videoprocessing.VideoProcessor;

import java.io.File;
import java.io.PrintStream;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.TimeUnit;

public class TestSave {
    static private Robot robotf=BasicRobot.robotWithNewAwtHierarchy();
    static private FrameFixture controller;


    @Before
    public void setUp() {
        String filepath = System.getProperty("user.dir") + "/Video_for_Testing_short.tif";
        Video video = new Video(filepath);
        VideoProcessor videoProcessor = new VideoProcessor(video);

        videoProcessor.process(10, true, true, true);

        Home.setVideoProcessor(videoProcessor);

        controller = new FrameFixture(robotf, new Controller());
    }
    @After
    public void tearDown(){
        robotf.cleanUp();
        controller.cleanUp();
    }

    @Test
    public void testController() throws InterruptedException {
        Home.setFileUploaded(true);
        Home.setAnalysedImg(true);

        controller.menuItem("save").click();
        TimeUnit.SECONDS.sleep(1);
        controller.menuItem("saveROI").click();
        TimeUnit.SECONDS.sleep(1);
        JFileChooserFixture fileChooser = JFileChooserFinder.findFileChooser().withTimeout(10000).using(robotf);
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        controller.fileChooser().cancelButton().click();

        controller.menuItem("save").click();
        TimeUnit.SECONDS.sleep(1);
        controller.menuItem("saveResults").click();
        JFileChooserFixture fileChooser1 = JFileChooserFinder.findFileChooser().withTimeout(10000).using(robotf);
        fileChooser1.setCurrentDirectory(new File(System.getProperty("user.dir")));
        controller.fileChooser().cancelButton().click();
        TimeUnit.SECONDS.sleep(1);
        controller.menuItem("save").click();
        TimeUnit.SECONDS.sleep(1);
        controller.menuItem("saveMCPlanar").click();
        JFileChooserFixture fileChooser2 = JFileChooserFinder.findFileChooser().withTimeout(10000).using(robotf);
        fileChooser2.setCurrentDirectory(new File(System.getProperty("user.dir")));
        controller.fileChooser().cancelButton().click();
        TimeUnit.SECONDS.sleep(1);
        controller.menuItem("save").click();
        TimeUnit.SECONDS.sleep(1);
        controller.menuItem("saveMCDepth").click();
        JFileChooserFixture fileChooser3 = JFileChooserFinder.findFileChooser().withTimeout(10000).using(robotf);
        fileChooser3.setCurrentDirectory(new File(System.getProperty("user.dir")));
        controller.fileChooser().cancelButton().click();
        TimeUnit.SECONDS.sleep(1);
        controller.menuItem("save").click();
        TimeUnit.SECONDS.sleep(1);
        controller.menuItem("saveAll").click();
        JFileChooserFixture fileChooser4 = JFileChooserFinder.findFileChooser().withTimeout(10000).using(robotf);
        fileChooser4.setCurrentDirectory(new File(System.getProperty("user.dir")));
        controller.fileChooser().cancelButton().click();
    }
}

