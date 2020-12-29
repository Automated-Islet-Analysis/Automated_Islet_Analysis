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

import java.io.File;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.TimeUnit;

public class TestSave {
    static private Robot robotf=BasicRobot.robotWithNewAwtHierarchy();
    static private FrameFixture controller;


    @Before
    public void setUp(){
        controller=new FrameFixture(robotf,new Controller());
    }

    @After
    public void tearDown(){
        robotf.cleanUp();
        controller.cleanUp();
    }

    @Test
    public void testController() throws InterruptedException {
        controller.menuItem("menuSave").click();
        TimeUnit.SECONDS.sleep(1);
        controller.menuItem("saveROI").click();
        TimeUnit.SECONDS.sleep(1);
        controller.menuItem("saveData").click();
        TimeUnit.SECONDS.sleep(1);
        controller.menuItem("saveMCVideo").click();
        TimeUnit.SECONDS.sleep(1);
        controller.menuItem("saveAll").click();


    }
}

