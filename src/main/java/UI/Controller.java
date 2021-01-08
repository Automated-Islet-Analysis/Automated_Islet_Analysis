package UI;

import javax.swing.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Controller extends JFrame {
//    static JFrame interframe;
    static public String display;
    static Home home;

    public Controller() {

        this.setSize(550, 550);

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

        home=new Home();

        // Set up menu bar
        MainMenu mainMenu = new MainMenu();
        this.setJMenuBar(mainMenu);

        this.setContentPane(home);
        this.setTitle("ROIs Detection");
        this.setVisible(true);
    }
}
