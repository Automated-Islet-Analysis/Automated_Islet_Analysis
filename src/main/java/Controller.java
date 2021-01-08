import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Controller extends JFrame {
//    static JFrame interframe;
    static public String display;
    static Home home;

    public Controller() {

        this.setSize(700, 800);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
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
