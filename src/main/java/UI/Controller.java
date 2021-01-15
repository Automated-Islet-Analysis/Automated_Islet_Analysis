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

public class Controller {
    // Frame that hold UI
    private static JFrame interframe;

    // Stores which panel is being displayed
    private static String display;
    private static String lastDisplay;

    // Panels of each part of menu
    private static Home home;
    private static ROIs rois;
    private static MCVideoPlanar mcvidPlanar;
    private static MCVideoDepth mcvidDepth;
    private static Results results;
    private static Uploaded upload;
    private static SaveDepthVideo saveDepthVideo;
    private static SaveData savedata;
    private static SaveAll saveall;
    private static SaveROIs saverois;
    private static SavePlanarVideo saveplanarvideo;

    // Store if tasks were already performed
    private static boolean fileUploaded;
    private static boolean analysedImg;
    private static  boolean meanIntensityMeasured;
    private static boolean reshaping;

    // Stores backend
    private static VideoProcessor videoProcessor = new VideoProcessor(null);

    // Getters
    public static Uploaded getUpload() { return upload;}
    public static VideoProcessor getVideoProcessor() {
        return videoProcessor;
    }
    public static JFrame getInterframe() { return interframe; }
    public static boolean isAnalysedImg() { return analysedImg; }
    public static boolean isFileUploaded() { return fileUploaded;}
    public static boolean isMeanIntensityMeasured() { return meanIntensityMeasured; }

    // Setters
    public static void setDisplay(String display) { Controller.display = display;}
    public static void setVideoProcessor(VideoProcessor videoProcessor1) {
        videoProcessor = videoProcessor1;
    }
    public static void setFileUploaded(boolean fileUploaded) { Controller.fileUploaded = fileUploaded; }
    public static void setAnalysedImg(boolean analysedImg) { Controller.analysedImg = analysedImg;}
    public static void setMeanIntensityMeasured(boolean meanIntensityMeasured) { Controller.meanIntensityMeasured = meanIntensityMeasured; }

    // Constructor
    public Controller() {
        // Set default panel
        display = "home";

        // Set no actions performed
        fileUploaded=false;
        analysedImg = false;
        meanIntensityMeasured = false;
        reshaping=false;

        // Set-up main frame of user-interface
        interframe = new JFrame("Automated analysis of Islet in eye");
        interframe.setSize(700, 700);
        interframe.addWindowListener(new WindowAdapter() {// Closes the program if close window clicked
            public void windowClosing(WindowEvent e) {
                interframe.dispose();
            }
        });
        interframe.addComponentListener(new ComponentListener() {
            // Update display if the window size changes
            @Override
            public void componentResized(ComponentEvent e) {
                reshaping=true;
                setDisplay();
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
        interframe.setJMenuBar(mainMenu);

        // Create all the panel views
        home = new Home();
        rois = new ROIs();
        mcvidPlanar = new MCVideoPlanar();
        mcvidDepth = new MCVideoDepth();
        upload = new Uploaded();
        saveDepthVideo = new SaveDepthVideo();
        savedata=new SaveData();
        saveall=new SaveAll();
        saverois=new SaveROIs();
        saveplanarvideo=new SavePlanarVideo();

        // Set user interface visible
        interframe.setVisible(true);
        setDisplay();
    }

    // Update panel displayed
    public static void setDisplay(){
        // Allows switching between panels
        if(display.equals("home")){
            interframe.setContentPane(home);
            interframe.invalidate();
            interframe.validate();
            lastDisplay = "home";
        }
        else if(display.equals("Upload")){
            interframe.setContentPane(upload);
            upload.updatePanel();
            interframe.invalidate();
            interframe.validate();
            lastDisplay = "Upload";
        }
        else if(display.equals("ROIs")){
            if (analysedImg){
                rois.updatePanel();
                interframe.setContentPane(rois);
                interframe.invalidate();
                interframe.validate();
                lastDisplay="ROIs";
            } else{
                popupNoFileAnalysed();
            }
        }
        else if(display.equals("MCVideoPlanar")){
            if (analysedImg) {
                interframe.setContentPane(mcvidPlanar);
                mcvidPlanar.updatePanel();
                interframe.invalidate();
                interframe.validate();
                lastDisplay="MCVideoPlanar";
            } else{
                popupNoFileAnalysed();
            }
        }
        else if(display.equals("MCVideoDepth")){
            if (analysedImg) {
                interframe.setContentPane(mcvidDepth);
                mcvidDepth.updatePanel();
                interframe.invalidate();
                interframe.validate();
                lastDisplay="MCVideoDepth";
            } else{
                popupNoFileAnalysed();
            }
        }
        else if(display.equals("Results")){
            if (analysedImg) {
                results = new Results(meanIntensityMeasured);
                results.showResults();
                interframe.setContentPane(results);
                interframe.invalidate();
                interframe.validate();
                lastDisplay="Results";
            } else{
                popupNoFileAnalysed();
            }
        }
        else if(display.equals("SaveROIs")){
            if(reshaping==true){
                setDisplay(lastDisplay);
            }
            else if (analysedImg ) {
                saverois.save();
            } else{
                popupNoFileAnalysed();
            }
        }
        else if(display.equals("SaveData")){
            if(reshaping==true){
                setDisplay(lastDisplay);
            }
            else if (analysedImg ) {
                savedata.save();

            } else{
                popupNoFileAnalysed();
            }
        }
        else if(display.equals("SaveAll")){
            if(reshaping==true){
                setDisplay(lastDisplay);
            }
            else if (analysedImg) {
                saveall.save();

            } else{
                popupNoFileAnalysed();
            }
        }
        else if(display.equals("SaveDepthVideo")){
            if(reshaping==true){
                setDisplay(lastDisplay);
            }
            else if (analysedImg) {
                saveDepthVideo.save();
            } else{
                popupNoFileAnalysed();
            }
        }
        else if(display.equals("SavePlanarVideo")){
            if(reshaping==true){
                setDisplay(lastDisplay);
            }
            else if (analysedImg ) {
                saveplanarvideo.save();
            } else{
                popupNoFileAnalysed();
            }
        }
    }

    // Pop-up to prevent being able to see data or save results before they are generated
    private static void popupNoFileAnalysed(){
        // Popup that tells the user that no file has been uploaded
        JOptionPane.showMessageDialog(interframe,
                "No file has been analysed yet. \n" +
                        "Please choose a file for upload and \n" +
                        "click on the 'Analyse' button",
                "alert", JOptionPane.ERROR_MESSAGE);
    }

}
