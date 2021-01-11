//import UI.Controller;
//import org.fest.swing.core.BasicRobot;
//import org.fest.swing.core.Robot;
//import org.fest.swing.finder.JFileChooserFinder;
//import org.fest.swing.fixture.*;
//import org.junit.After;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.io.File;
//import java.util.concurrent.TimeUnit;
//
//public class TestHome {
//    static private FrameFixture controller;
//    static private Robot robotf=BasicRobot.robotWithNewAwtHierarchy();
//
//    @Before
//    public void setUp(){
//        controller=new FrameFixture(robotf,new Controller());
//    }
//
//    @After
//    public void tearDown(){
//        controller.cleanUp();
//        robotf.cleanUp();
//    }
//
//    @Test
//    public void testController() throws InterruptedException {
//        String label=controller.label("welcomeLabel").text();
//        Assert.assertEquals("Welcome!",label);
//        TimeUnit.SECONDS.sleep(1);
//        controller.button("button").click();
//
//
//        JFileChooserFixture fileChooser = JFileChooserFinder.findFileChooser().withTimeout(10000).using(robotf);
//        fileChooser.selectFiles(new File(System.getProperty("user.dir")+"/img/im/2 2.tif"));
//        TimeUnit.SECONDS.sleep(1);
//        fileChooser.cancelButton().click();
//        TimeUnit.SECONDS.sleep(1);
//        controller.button("analyse").click();
//        TimeUnit.SECONDS.sleep(1);
//        controller.optionPane().textBox("perError").selectText(0,2);
//        controller.optionPane().textBox("perError").enterText("10");
//        TimeUnit.SECONDS.sleep(1);
//        controller.optionPane().checkBox("checkPlanar").click();
//        TimeUnit.SECONDS.sleep(1);
//        controller.optionPane().checkBox("checkDepth").click();
//        TimeUnit.SECONDS.sleep(1);
//        controller.optionPane().checkBox("checkROI").click();
//
//        TimeUnit.SECONDS.sleep(1);
//        controller.optionPane().buttonWithText("Ok").click();
//        TimeUnit.SECONDS.sleep(1);
//
//        controller.optionPane().dialog().close();
//
//        controller.button("analyse").click();
//        TimeUnit.SECONDS.sleep(1);
//        controller.optionPane().buttonWithText("Help").click();
//        TimeUnit.SECONDS.sleep(1);
//
//        controller.optionPane().dialog().close();
//
//        controller.button("analyse").click();
//        TimeUnit.SECONDS.sleep(1);
//        controller.optionPane().buttonWithText("Cancel").click();
//        TimeUnit.SECONDS.sleep(1);
//
//    }
//}
