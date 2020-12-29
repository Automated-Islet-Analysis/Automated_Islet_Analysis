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

    public class TestData {
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
            controller.menuItem("Data").click();
            TimeUnit.SECONDS.sleep(1);
            controller.menuItem("ROIs").click();
            TimeUnit.SECONDS.sleep(1);
            controller.button("addROIButton").click();
            TimeUnit.SECONDS.sleep(1);

            controller.menuItem("dataData").click();
            TimeUnit.SECONDS.sleep(1);
            controller.button("measureIntensity").click();
            TimeUnit.SECONDS.sleep(1);

            controller.menuItem("MCVideo").click();
            TimeUnit.SECONDS.sleep(1);
            controller.menuItem("MCPlanar").click();
            TimeUnit.SECONDS.sleep(1);
            controller.menuItem("MCDepth").click();

        }
    }
