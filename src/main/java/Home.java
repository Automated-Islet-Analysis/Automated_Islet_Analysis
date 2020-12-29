import DataTab.Data;
import DataTab.MCVideoDepth;
import DataTab.MCVideoPlanar;
import DataTab.ROIs;
import jdk.internal.jimage.ImageStrings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Home extends JPanel {
    private JLabel welcome;
    private static JLabel empty;
    private JButton button;
    private static JPanel subPanel,welcomePanel,buttonPanel;

    static public String display;

    static Home home;
    static ROIs rois;
    static MCVideoPlanar mcvidPlanar;
    static MCVideoDepth mcvidDepth;
    static Data data;
    static Uploaded upload;

    static public boolean analysedImg;


    public Home(){
        display = "home";
        analysedImg = true;

        // Create all the panel views
//        home = new Home();
        rois = new ROIs();
        mcvidPlanar = new MCVideoPlanar();
        mcvidDepth = new MCVideoDepth();
        data = new Data();
        upload = new Uploaded();

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


    static void setDisplay(){
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
        else if(display == "Data"){
            if (analysedImg == true) {
                subPanel.removeAll();
                subPanel.repaint();
                subPanel.add(data);
                subPanel.revalidate();
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