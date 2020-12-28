package UI;

import UI.DataTab.MCVideoDepth;
import UI.DataTab.MCVideoPlanar;
import UI.DataTab.ROIs;
import UI.DataTab.Data;
import videoprocessing.VideoProcessor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Controller extends Frame {
    static JFrame interframe;
    static public String display;

    static Home home;
    static ROIs rois;
    static MCVideoPlanar mcvidPlanar;
    static MCVideoDepth mcvidDepth;
    static Data data;
    static Uploaded upload;

    static public boolean analysedImg;

    public static VideoProcessor getVideoProcessor() {
        return videoProcessor;
    }

    public static void setVideoProcessor(VideoProcessor videoProcessor) {
        Controller.videoProcessor = videoProcessor;
    }

    public static VideoProcessor videoProcessor;


    public Controller() {
        display = "home";
        analysedImg = false;

        // Set up the frame
        interframe = new JFrame("ROI detection");
        interframe.setSize(600, 600);

        interframe.addWindowListener(new WindowAdapter() {// Closes the program if close window clicked
            public void windowClosing(WindowEvent e) {
                interframe.dispose();
            }
        });

        // Set up menu bar
        MainMenu mainMenu = new MainMenu();
        interframe.setJMenuBar(mainMenu);

        // Create all the panel views
        home = new Home();
        rois = new ROIs();
        mcvidPlanar = new MCVideoPlanar();
        mcvidDepth = new MCVideoDepth();
        data = new Data();
        upload = new Uploaded();

        interframe.setVisible(true);
        setDisplay();
    }

    static void setDisplay(){
        // Allows switching between panels
        if(display == "home"){
            interframe.setContentPane(home);
            interframe.invalidate();
            interframe.validate();
        }
        else if(display == "Upload"){
            interframe.setContentPane(upload);
            interframe.invalidate();
            interframe.validate();
        }
        else if(display == "ROIs"){
            if (analysedImg == true){
                interframe.setContentPane(rois);
                interframe.invalidate();
                interframe.validate();
            } else{
                popupNoFile();
            }
        }
        else if(display == "MCVideoPlanar"){
            if (analysedImg == true) {
                interframe.setContentPane(mcvidPlanar);
                interframe.invalidate();
                interframe.validate();
            } else{
                popupNoFile();
            }
        }
        else if(display == "MCVideoDepth"){
            if (analysedImg == true) {
                interframe.setContentPane(mcvidDepth);
                interframe.invalidate();
                interframe.validate();
            } else{
                popupNoFile();
            }
        }
        else if(display == "Data"){
            if (analysedImg == true) {
                interframe.setContentPane(data);
                interframe.invalidate();
                interframe.validate();
            } else{
                popupNoFile();
            }
        }
    }

    private static void popupNoFile(){
        // Popup that tells the user that no file has been uploaded
        JOptionPane.showMessageDialog(null,
                "No file has been analysed yet. \n" +
                        "Please choose a file for upload and \n" +
                        "click on the 'Analyse' button",
                "alert", JOptionPane.ERROR_MESSAGE);
    }
}
