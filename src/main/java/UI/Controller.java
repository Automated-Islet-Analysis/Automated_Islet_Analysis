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

public class Controller {
    public static JFrame interframe;
    static public String display;

    static Home home;
    static ROIs rois;
    static MCVideoPlanar mcvidPlanar;
    static MCVideoDepth mcvidDepth;
    static Data data;
    static Uploaded upload;

    static public boolean fileUploaded;
    static public boolean analysedImg;
    static public boolean meanIntensityMeasured;


    public Controller() {
        display = "home";
        fileUploaded=false;
        analysedImg = false;
        meanIntensityMeasured=false;

        // Set up the frame
        interframe = new JFrame("ROI detection");
        interframe.setSize(550, 550);

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
        upload = new Uploaded();

        interframe.setVisible(true);
        setDisplay();
    }

    static public void setDisplay(){
        // Allows switching between panels
        if(display.equals("home")){
            interframe.setContentPane(home);
            interframe.invalidate();
            interframe.validate();
        }
        else if(display.equals("Upload")){
            interframe.setContentPane(upload);
            upload.setDim();
            interframe.setSize(upload.getWidth(),upload.getHeight());
            interframe.invalidate();
            interframe.validate();
        }
        else if(display.equals("ROIs")){
            if (analysedImg == true){
                rois.updatePanel();
                interframe.setContentPane(rois);
                interframe.setSize(rois.getWidth(),rois.getHeight());
                interframe.invalidate();
                interframe.validate();
            } else{
                popupNoFile();
            }
        }
        else if(display.equals("MCVideoPlanar")){
            if (analysedImg == true) {
                interframe.setContentPane(mcvidPlanar);
                mcvidPlanar.update();
                interframe.setSize(mcvidPlanar.getWidth(),mcvidPlanar.getHeight());
                interframe.invalidate();
                interframe.validate();
            } else{
                popupNoFile();
            }
        }
        else if(display.equals("MCVideoDepth")){
            if (analysedImg == true) {
                interframe.setContentPane(mcvidDepth);
                mcvidDepth.update();
                interframe.setSize(mcvidDepth.getWidth(),mcvidDepth.getHeight());
                interframe.invalidate();
                interframe.validate();
            } else{
                popupNoFile();
            }
        }
        else if(display.equals("Results")){
            if (analysedImg == true) {
                data = new Data(meanIntensityMeasured);
                interframe.setContentPane(data);
                data.showResults();
                interframe.setSize(700,700);
                interframe.invalidate();
                interframe.validate();
            } else{
                popupNoFile();
            }
        }
    }

    private static void popupNoFile(){
        // Popup that tells the user that no file has been uploaded
        JOptionPane.showMessageDialog(interframe,
                "No file has been analysed yet. \n" +
                        "Please choose a file for upload and \n" +
                        "click on the 'Analyse' button",
                "alert", JOptionPane.ERROR_MESSAGE);
    }

}
