import UI.Controller;
import UI.HomeTab.Home;
import org.fest.swing.core.*;
import org.fest.swing.core.Robot;
import org.fest.swing.finder.JFileChooserFinder;
import org.fest.swing.fixture.*;
import org.fest.swing.timing.Condition;
import org.fest.swing.timing.Pause;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import videoprocessing.Video;
import videoprocessing.VideoProcessor;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Collection;
import java.util.concurrent.TimeUnit;


public class TestHome {
    static private FrameFixture controller;
    static private Robot robotf=BasicRobot.robotWithNewAwtHierarchy();
    private volatile ByteArrayOutputStream outputStreamCaptor=new ByteArrayOutputStream();

    @Before
    public void setUp() throws InterruptedException {
        controller=new FrameFixture(robotf,new Controller());
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @After
    public void tearDown(){
        controller.cleanUp();
        robotf.cleanUp();
    }

    @Test
    public void testController() throws InterruptedException {
        String label=controller.label("welcomeLabel").text();
        Assert.assertEquals("Welcome!",label);
        TimeUnit.SECONDS.sleep(1);
        controller.button("button").click();


        JFileChooserFixture fileChooser = JFileChooserFinder.findFileChooser().withTimeout(10000).using(robotf);
        fileChooser.selectFiles(new File(System.getProperty("user.dir")+"/img/im/2 2.tif"));
        TimeUnit.SECONDS.sleep(1);
        fileChooser.cancelButton().click();
        TimeUnit.SECONDS.sleep(1);
        controller.button("analyse").click();
        TimeUnit.SECONDS.sleep(1);
        controller.optionPane().textBox("perError").selectText(0,2);
        controller.optionPane().textBox("perError").enterText("10");
        TimeUnit.SECONDS.sleep(1);
        controller.optionPane().checkBox("checkPlanar").click();
        TimeUnit.SECONDS.sleep(1);
        controller.optionPane().checkBox("checkDepth").click();
        TimeUnit.SECONDS.sleep(1);
        controller.optionPane().checkBox("checkROI").click();

        TimeUnit.SECONDS.sleep(1);
        controller.optionPane().buttonWithText("Ok").click();

        final ComponentMatcher matcher=new TypeMatcher(JOptionPane.class);
        Pause.pause(new Condition("Waiting for processing") {
            @Override
            public boolean test() {
                Collection<Component> list=
                        controller.robot.finder().findAll(controller.target,matcher);
                return list.size()>0;
            }
        },100000);

        controller.optionPane().dialog().close();

        controller.button("analyse").click();
        TimeUnit.SECONDS.sleep(1);
        controller.optionPane().buttonWithText("Help").click();
        TimeUnit.SECONDS.sleep(1);

        controller.optionPane().dialog().close();

        controller.button("analyse").click();
        TimeUnit.SECONDS.sleep(1);
        controller.optionPane().buttonWithText("Cancel").click();
        TimeUnit.SECONDS.sleep(1);
        }
    }


