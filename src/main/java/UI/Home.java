package UI;

import UI.DataTab.Results;
import UI.DataTab.MCVideoDepth;
import UI.DataTab.MCVideoPlanar;
import UI.DataTab.ROIs;
import UI.SaveTab.*;

import javax.swing.*;
import java.awt.*;

public class Home extends JPanel {
    private JLabel welcome;
    private static JLabel empty;
    private JButton button;
    public static JPanel subPanel,welcomePanel,buttonPanel;

    static public String display;

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


    public Home(){
        display = "home";
        analysedImg = true;
        fileUploaded=false;
        meanIntensityMeasured=false;

        // Create all the panel views
//        home = new Home();
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

        button = new JButton("Upload");
        button.addActionListener(new UploadListener());

        empty = new JLabel("");

        welcome.setName("welcomeLabel");
        button.setName("button");

        welcomePanel=new JPanel();
        welcomePanel.add(welcome);
        buttonPanel=new JPanel();
        buttonPanel.add(button);

        // Set layout
        subPanel=new JPanel();

        setLayout(new BorderLayout());
        setDisplay();


        add(subPanel,BorderLayout.CENTER);
    }


    public static void setDisplay(){
        // Allows switching between panels
        if(display == "home"){
            subPanel.removeAll();
            subPanel.setLayout(new GridLayout(3,1));
            subPanel.repaint();
            subPanel.add(empty);
            subPanel.add(welcomePanel,BorderLayout.CENTER);
            subPanel.add(buttonPanel,BorderLayout.CENTER);
            subPanel.revalidate();
        }
        else if(display == "Upload"){
            subPanel.removeAll();
            subPanel.setLayout(new GridLayout(1,1));
            subPanel.repaint();
            subPanel.add(upload);
            subPanel.invalidate();
            subPanel.validate();
        }
        else if(display == "ROIs"){
            if (analysedImg == true){
                subPanel.removeAll();
                subPanel.setLayout(new GridLayout(1,1));
                subPanel.repaint();
                rois.updatePanel();
                subPanel.add(rois);
                subPanel.revalidate();
            } else{
                popupNoFile();
            }
        }
        else if(display == "MCVideoPlanar"){
            if (analysedImg == true) {
                subPanel.removeAll();
                subPanel.setLayout(new GridLayout(1,1));
                subPanel.repaint();
                subPanel.add(mcvidPlanar);
                subPanel.revalidate();
            } else{
                popupNoFile();
            }
        }
        else if(display == "MCVideoDepth"){
            if (analysedImg == true) {
                subPanel.removeAll();
                subPanel.setLayout(new GridLayout(1,1));
                subPanel.repaint();
                subPanel.add(mcvidDepth);
                subPanel.revalidate();
            } else{
                popupNoFile();
            }
        }
        else if(display == "Results") {
            if (analysedImg) {
                results = new Results(meanIntensityMeasured);
                if (meanIntensityMeasured) {
                    results.showResults();
                }
                subPanel.removeAll();
                subPanel.setLayout(new GridLayout(1,1));
                subPanel.repaint();
                subPanel.add(results);
                subPanel.revalidate();
            } else {
                popupNoFile();
            }
        }
        else if(display.equals("SaveROIs")){
            if (analysedImg) {
                subPanel.removeAll();
                subPanel.setLayout(new GridLayout(1,1));
                subPanel.repaint();
//                subPanel.add(saverois);
                subPanel.revalidate();
            } else{
                popupNoFile();
            }
        }
        else if(display.equals("SaveAll")){
            if (analysedImg) {
                subPanel.removeAll();
                subPanel.setLayout(new GridLayout(1,1));
                subPanel.repaint();
//                subPanel.add(saveall);
                subPanel.revalidate();
            } else{
                popupNoFile();
            }
        }
        else if(display.equals("SaveDepthVideo")){
            if (analysedImg) {
                subPanel.removeAll();
                subPanel.setLayout(new GridLayout(1,1));
                subPanel.repaint();
                subPanel.add(savevideo);
                subPanel.revalidate();
            } else{
                popupNoFile();
            }
        }
        else if(display.equals("SavePlanarVideo")) {
            if (analysedImg) {
                subPanel.removeAll();
                subPanel.setLayout(new GridLayout(1,1));
                subPanel.repaint();
                subPanel.add(saveplanarvideo);
                subPanel.revalidate();
            } else {
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