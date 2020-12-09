import DataTab.MCVideo;
import DataTab.ROIs;
import DataTab.Data;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Controller extends Frame {
    static JFrame interframe;
    static public String display;
    static Home home;
    static ROIs rois;
    static MCVideo mcvid;
    static Data data;


    public Controller() {
        display = "home";

        // Set up the frame
        interframe = new JFrame("Interface");
        interframe.setSize(600, 600);

        interframe.addWindowListener(new WindowAdapter() {// Closes the program if close window clicked
            public void windowClosing(WindowEvent e) {
                interframe.dispose();
            }
        });

        MainMenu mainMenu = new MainMenu();
        interframe.setJMenuBar(mainMenu);

        home = new Home();
        rois = new ROIs();
        mcvid = new MCVideo();
        data = new Data();

        interframe.setVisible(true);
        setDisplay();
    }

    static void setDisplay(){
        if(display == "home"){
            interframe.setContentPane(home);
            interframe.invalidate();
            interframe.validate();
        }
        else if(display == "ROIs"){
            interframe.setContentPane(rois);
            interframe.invalidate();
            interframe.validate();
        }
        else if(display == "MCVideo"){
            interframe.setContentPane(mcvid);
            interframe.invalidate();
            interframe.validate();
        }
        else if(display == "Data"){
            interframe.setContentPane(data);
            interframe.invalidate();
            interframe.validate();
        }
    }
}
