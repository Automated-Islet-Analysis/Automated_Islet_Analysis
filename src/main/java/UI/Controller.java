package UI;

import UI.DataTab.MCVideoDepth;
import UI.DataTab.MCVideoPlanar;
import UI.DataTab.ROIs;
import UI.DataTab.Results;
import UI.HomeTab.Home;
import UI.HomeTab.MainMenu;
import UI.HomeTab.Uploaded;
import UI.SaveTab.*;
import videoprocessing.VideoProcessor;

import javax.swing.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Controller extends JFrame {
    static Home home;

    public Controller() {
        home=new Home();
        // Set-up main frame of user-interface
        this.setTitle("Automated analysis of Islet in eye");
        this.setSize(700, 700);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        this.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                home.setDisplay();
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
        this.setJMenuBar(mainMenu);

        this.setContentPane(home);
        this.setVisible(true);
    }
}

