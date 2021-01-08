package UI;

import UI.DataTab.MCVideoDepth;
import UI.DataTab.MCVideoPlanar;
import UI.DataTab.ROIs;
import UI.DataTab.Results;
import UI.SaveTab.*;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Controller {
    public static JFrame interframe;
    static public String display;

    static Home home;
    static ROIs rois;
    static MCVideoPlanar mcvidPlanar;
    static MCVideoDepth mcvidDepth;
    static Results results;
    static Uploaded upload;
    static SaveDepthVideo savevideo;
    static SaveData savedata;
    static SaveAll saveall;
    static SaveROIs saverois;
    static SavePlanarVideo saveplanarvideo;

    static public boolean fileUploaded;
    static public boolean analysedImg;
    static public boolean meanIntensityMeasured;


    public Controller() {
        display = "home";
        fileUploaded=false;
        analysedImg = false;
        meanIntensityMeasured = false;

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
        savevideo= new SaveDepthVideo();
        savedata=new SaveData();
        saveall=new SaveAll();
        saverois=new SaveROIs();
        saveplanarvideo=new SavePlanarVideo();


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
            interframe.invalidate();
            interframe.validate();
        }
        else if(display.equals("ROIs")){
            if (analysedImg){
                rois.updatePanel();
                interframe.setContentPane(rois);
                interframe.invalidate();
                interframe.validate();
            } else{
                popupNoFile();
            }
        }
        else if(display.equals("MCVideoPlanar")){
            if (analysedImg) {
                interframe.setContentPane(mcvidPlanar);
                mcvidPlanar.update();
                interframe.invalidate();
                interframe.validate();
            } else{
                popupNoFile();
            }
        }
        else if(display.equals("MCVideoDepth")){
            if (analysedImg) {
                interframe.setContentPane(mcvidDepth);
                mcvidDepth.update();
                interframe.invalidate();
                interframe.validate();
            } else{
                popupNoFile();
            }
        }
        else if(display.equals("Results")){
            if (analysedImg) {
                results = new Results(meanIntensityMeasured);
                if(meanIntensityMeasured){results.showResults();}
                interframe.setContentPane(results);
                interframe.invalidate();
                interframe.validate();
            } else{
                popupNoFile();
            }
        }
        else if(display.equals("SaveROIs")){
            if (analysedImg) {
                interframe.setContentPane(saverois);
                interframe.invalidate();
                interframe.validate();
            } else{
                popupNoFile();
            }
        }
        else if(display.equals("SaveAll")){
            if (analysedImg) {
                interframe.setContentPane(saveall);
                interframe.invalidate();
                interframe.validate();
            } else{
                popupNoFile();
            }
        }
        else if(display.equals("SaveDepthVideo")){
            if (analysedImg) {
                interframe.setContentPane(savevideo);
                interframe.invalidate();
                interframe.validate();
            } else{
                popupNoFile();
            }
        }
        else if(display.equals("SavePlanarVideo")){
            if (analysedImg) {
                interframe.setContentPane(saveplanarvideo);
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
