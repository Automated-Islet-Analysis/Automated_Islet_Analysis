/**
 * Panel of the home screen where you can upload a file.
 *
 * @author Team Automated analysis of "islet in eye", Bioengineering department, Imperial College London
 *
 * Last modified: 11/01/2021
 */

package UI.HomeTab;

import UI.Controller;
import UI.DataTab.MCVideoDepth;
import UI.DataTab.MCVideoPlanar;
import UI.DataTab.ROIs;
import UI.DataTab.Results;
import UI.SaveTab.*;
import videoprocessing.VideoProcessor;

import javax.swing.*;
import java.awt.*;

public class Home extends JPanel {
    // Create components
    static JLabel welcome;
    static JLabel empty;
    static JButton uploadBtn;
    static JPanel subPanel, uploadBtnPanel,welcomePanel;

    // Stores which panel is being displayed
    private static String display;
    private static String lastDisplay;

    // Panels of each part of menu
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


    //     Stores backend
    private static VideoProcessor videoProcessor = new VideoProcessor(null);

//     Getters
    public static Uploaded getUpload() { return upload;}
    public static VideoProcessor getVideoProcessor() {
        return videoProcessor;
    }
    public static JPanel getInterframe() { return Home.subPanel; }
    public static boolean isAnalysedImg() { return analysedImg; }
    public static boolean isFileUploaded() { return fileUploaded;}
    public static boolean isMeanIntensityMeasured() { return meanIntensityMeasured; }

    // Setters
    public static void setDisplay(String display) { Home.display = display;}
    public static void setVideoProcessor(VideoProcessor videoProcessor1) {
        videoProcessor = videoProcessor1;
    }
    public static void setFileUploaded(boolean fileUploaded) { Home.fileUploaded = fileUploaded; }
    public static void setAnalysedImg(boolean analysedImg) { Home.analysedImg = analysedImg;}
    public static void setMeanIntensityMeasured(boolean meanIntensityMeasured) { Home.meanIntensityMeasured = meanIntensityMeasured; }


    // Constructor
    public Home(){
        display = "home";
        analysedImg = false;
        fileUploaded=false;
        meanIntensityMeasured=false;

        rois = new ROIs();
        mcvidPlanar = new MCVideoPlanar();
        mcvidDepth = new MCVideoDepth();
        upload = new Uploaded();
        savedata=new SaveData();
        saveall=new SaveAll();
        saverois=new SaveROIs();
        saveplanarvideo=new SavePlanarVideo();
        saveDepthVideo=new SaveDepthVideo();

        welcome = new JLabel("Welcome!");
        welcome.setFont(new Font(welcome.getFont().getName(), Font.PLAIN, 30));
        welcome.setHorizontalAlignment(SwingConstants.CENTER);

        uploadBtn = new JButton("Upload");
        uploadBtn.setSize(100,30);
        uploadBtn.addActionListener(new UploadListener());

        empty = new JLabel("");

        welcome.setName("welcomeLabel");
        uploadBtn.setName("UploadButton");

        welcomePanel=new JPanel();
        welcomePanel.add(welcome);
        uploadBtnPanel=new JPanel();
        uploadBtnPanel.add(uploadBtn);

        // Set layout
        subPanel=new JPanel();

        setLayout(new BorderLayout());
        setDisplay();

        add(subPanel,BorderLayout.CENTER);
    }

    public static void setDisplay(){
        // Allows switching between panels
        if(display.equals("home")){
            subPanel.removeAll();
            subPanel.setLayout(new GridLayout(3,1));
            subPanel.repaint();
            subPanel.add(empty);
            subPanel.add(welcomePanel,BorderLayout.CENTER);
            subPanel.add(uploadBtnPanel,BorderLayout.CENTER);
            subPanel.revalidate();
            lastDisplay = "home";
        }
        else if(display.equals("Upload")){
            subPanel.removeAll();
            subPanel.setLayout(new GridLayout(1,1));
            subPanel.repaint();
            subPanel.add(upload);
            upload.updatePanel();
            subPanel.revalidate();
            lastDisplay = "Upload";
        }
        else if(display.equals("ROIs")){
            if (analysedImg){
                subPanel.removeAll();
                subPanel.setLayout(new GridLayout(1,1));
                subPanel.repaint();
                subPanel.add(rois);
                rois.updatePanel();
                subPanel.revalidate();
                lastDisplay="ROIs";
            } else{
                popupNoFileAnalysed();
            }
        }
        else if(display.equals("MCVideoPlanar")){
            if (analysedImg) {
                subPanel.removeAll();
                subPanel.setLayout(new GridLayout(1,1));
                subPanel.repaint();
                subPanel.add(mcvidPlanar);
                mcvidPlanar.updatePanel();
                subPanel.revalidate();
                lastDisplay="MCVideoPlanar";
            } else{
                popupNoFileAnalysed();
            }
        }
        else if(display.equals("MCVideoDepth")){
            if (analysedImg) {
                subPanel.removeAll();
                subPanel.setLayout(new GridLayout(1,1));
                subPanel.repaint();
                subPanel.add(mcvidDepth);
                mcvidDepth.updatePanel();
                subPanel.revalidate();
                lastDisplay="MCVideoDepth";
            } else{
                popupNoFileAnalysed();
            }
        }
        else if(display.equals("Results")){
            if (analysedImg) {
                results = new Results(meanIntensityMeasured);
                results.showResults();
                subPanel.removeAll();
                subPanel.setLayout(new GridLayout(1,1));
                subPanel.repaint();
                subPanel.add(results);
                subPanel.revalidate();
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
        JOptionPane.showMessageDialog(subPanel,
                "No file has been analysed yet. \n" +
                        "Please choose a file for upload and \n" +
                        "click on the 'Analyse' button",
                "alert", JOptionPane.ERROR_MESSAGE);
    }

}
