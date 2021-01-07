package UI;

import UI.DataTab.MCVideoDepth;
import UI.DataTab.MCVideoPlanar;
import UI.DataTab.ROIs;
import UI.DataTab.Data;
import UI.SaveTab.*;
import videoprocessing.VideoProcessor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Controller extends JFrame {
    public static JFrame interframe;
    static public String display;

    static Home home;
    static ROIs rois;
    static MCVideoPlanar mcvidPlanar;
    static MCVideoDepth mcvidDepth;
    static Data data;
    static Uploaded upload;
    static SaveVideo savevideo;
    static SaveData savedata;
    static SaveAll saveall;
    static SaveROIs saverois;
    static SavePlanarVideo saveplanarvideo;

    static public boolean analysedImg;

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
        savevideo= new SaveVideo();
        savedata=new SaveData();
        saveall=new SaveAll();
        saverois=new SaveROIs();
        saveplanarvideo=new SavePlanarVideo();


        interframe.setVisible(true);
        setDisplay();
    }

    static void setDisplay(){
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
                interframe.invalidate();
                interframe.validate();
            } else{
                popupNoFile();
            }
        }
        else if(display.equals("Data")){
            if (analysedImg == true) {
                interframe.setContentPane(data);
                interframe.invalidate();
                interframe.validate();
            } else{
                popupNoFile();
            }
        }
        else if(display.equals("SaveROIs")){
            if (analysedImg == true) {
                interframe.setContentPane(saverois);
                interframe.invalidate();
                interframe.validate();
            } else{
                popupNoFile();
            }
        }
        else if(display.equals("SaveAll")){
            if (analysedImg == true) {
                interframe.setContentPane(saveall);
                interframe.invalidate();
                interframe.validate();
            } else{
                popupNoFile();
            }
        }
        else if(display.equals("SaveDepthVideo")){
            if (analysedImg == true) {
                interframe.setContentPane(savevideo);
                interframe.invalidate();
                interframe.validate();
            } else{
                popupNoFile();
            }
        }
        else if(display.equals("SavePlanarVideo")){
            if (analysedImg == true) {
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
        JOptionPane.showMessageDialog(null,
                "No file has been analysed yet. \n" +
                        "Please choose a file for upload and \n" +
                        "click on the 'Analyse' button",
                "alert", JOptionPane.ERROR_MESSAGE);
    }
}
