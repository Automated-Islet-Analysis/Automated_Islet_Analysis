/**
 * Main class of the user interface. Holds main JFrame, possible JPanels that can be displayed,
 * variables on the state of the user interface and forms the link between the back-end and the front-end.
 *
 * @author Team Automated analysis of "islet in eye", Bioengineering department, Imperial College London
 *
 * Last modified: 11/01/2021
 */

package UI;

import UI.DataTab.MCVideoDepth;
import UI.DataTab.MCVideoPlanar;
import UI.DataTab.ROIs;
import UI.DataTab.Results;
import UI.HomeTab.Home;
import UI.HomeTab.Uploaded;
import UI.Panel.DynamicPanel;
import UI.SaveTab.*;
import videoprocessing.VideoProcessor;

import javax.swing.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Controller extends JFrame{
    // Frame that hold UI
//    private static JFrame interframe;
    private static Home home;


    private static boolean reshaping;



    // Constructor
    public Controller() {
        // Set default panel
        home=new Home();

        // Set no actions performed
//        fileUploaded=false;
//        analysedImg = false;
//        meanIntensityMeasured = false;
        reshaping=false;

        // Set-up main frame of user-interface
        // Set-up main frame of user-interface
        this.setTitle("Automated analysis of Islet in eye");
        this.setSize(700, 700);
        this.addWindowListener(new WindowAdapter() {// Closes the program if close window clicked
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        this.addComponentListener(new ComponentListener() {
            // Update display if the window size changes
            @Override
            public void componentResized(ComponentEvent e) {
                reshaping=true;
                home.setDisplay();
                reshaping=false;
            }
            @Override
            public void componentMoved(ComponentEvent e) {}
            @Override
            public void componentShown(ComponentEvent e) {}
            @Override
            public void componentHidden(ComponentEvent e){}
        });

        // Set up menu bar
        MainMenu mainMenu = new MainMenu();
        this.setJMenuBar(mainMenu);

        // Create all the panel views
//        rois = new ROIs();
//        mcvidPlanar = new MCVideoPlanar();
//        mcvidDepth = new MCVideoDepth();
//        upload = new Uploaded();
//        saveDepthVideo = new SaveDepthVideo();
//        savedata=new SaveData();
//        saveall=new SaveAll();
//        saverois=new SaveROIs();
//        saveplanarvideo=new SavePlanarVideo();

        // Set user interface visible
        this.setContentPane(home);
        this.setVisible(true);
    }

    // Update panel displayed

}

