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
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class Home extends JPanel {
    static JLabel welcome;
    static JLabel empty;
    static JButton uploadBtn;
    public static JPanel subPanel,welcomePanel,uploadBtnPanel;

    // Stores which panel is being displayed
    private static String display;

    // Panels of each part of menu
    private static ROIs rois;
    private static MCVideoPlanar mcvidPlanar;
    private static MCVideoDepth mcvidDepth;
    private static Results results;
    private static Uploaded upload;
    private static SaveDepthVideo savevideo;
    private static SaveData savedata;
    private static SaveAll saveall;
    private static SaveROIs saverois;
    private static SavePlanarVideo saveplanarvideo;

    // Store if tasks were already performed
    private static boolean fileUploaded;
    private static boolean analysedImg;
    private static  boolean meanIntensityMeasured;

    // Stores backend
    private static VideoProcessor videoProcessor = new VideoProcessor(null);

    // Getters
    public static Uploaded getUpload() { return upload;}
    public static VideoProcessor getVideoProcessor() {
        return videoProcessor;
    }
    public static JPanel getInterframe() { return subPanel; }
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

    public Home(){
        display = "home";
        analysedImg = false;
        fileUploaded=false;
        meanIntensityMeasured=false;

        rois = new ROIs();
        mcvidPlanar = new MCVideoPlanar();
        mcvidDepth = new MCVideoDepth();
        upload = new Uploaded();
        savevideo= new SaveDepthVideo();
        savedata=new SaveData();
        saveall=new SaveAll();
        saverois=new SaveROIs();
        saveplanarvideo=new SavePlanarVideo();

        welcome = new JLabel("Welcome!");
        welcome.setFont(new Font(welcome.getFont().getName(), Font.PLAIN, 30));
        welcome.setHorizontalAlignment(SwingConstants.CENTER);

        uploadBtn = new JButton("Upload");
        uploadBtn.addActionListener(new UploadListener());

        empty = new JLabel("");

        welcome.setName("welcomeLabel");
        uploadBtn.setName("button");

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
        }
        else if(display.equals("Upload")){
            subPanel.removeAll();
            subPanel.setLayout(new GridLayout(1,1));
            subPanel.repaint();
            subPanel.add(upload);
            upload.update();
            subPanel.revalidate();
        }
        else if(display.equals("ROIs")){
            if (analysedImg){
                subPanel.removeAll();
                subPanel.setLayout(new GridLayout(1,1));
                subPanel.repaint();
                rois.updatePanel();
                subPanel.add(rois);
                subPanel.revalidate();
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
                mcvidPlanar.update();
                subPanel.revalidate();
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
                mcvidDepth.update();
                subPanel.revalidate();
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
            } else{
                popupNoFileAnalysed();
            }
        }
//        else if(display.equals("SaveROIs")){
//            if (analysedImg) {
//                interframe.setContentPane(saverois);
//                interframe.invalidate();
//                interframe.validate();
//            } else{
//                popupNoFileAnalysed();
//            }
//        }
//        else if(display.equals("SaveAll")){
//            if (analysedImg) {
//                interframe.setContentPane(saveall);
//                interframe.invalidate();
//                interframe.validate();
//            } else{
//                popupNoFileAnalysed();
//            }
//        }
        else if(display.equals("SaveDepthVideo")){
            if (analysedImg) {
                subPanel.removeAll();
                subPanel.setLayout(new GridLayout(1,1));
                subPanel.repaint();
                subPanel.add(savevideo);
                subPanel.revalidate();
            } else{
                popupNoFileAnalysed();
            }
        }
        else if(display.equals("SavePlanarVideo")){
            if (analysedImg) {
                subPanel.removeAll();
                subPanel.setLayout(new GridLayout(1,1));
                subPanel.repaint();
                subPanel.add(saveplanarvideo);
                subPanel.revalidate();
            } else{
                popupNoFileAnalysed();
            }
        }

    }

    private static void popupNoFileAnalysed(){
        // Popup that tells the user that no file has been uploaded
        JOptionPane.showMessageDialog(null,
                "No file has been analysed yet. \n" +
                        "Please choose a file for upload and \n" +
                        "click on the 'Analyse' button",
                "alert", JOptionPane.ERROR_MESSAGE);
    }

}
