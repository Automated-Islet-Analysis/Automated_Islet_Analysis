import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class HomeMenuListener implements MenuListener {

    @Override
    public void menuSelected(MenuEvent e) {
        // Change view to Home page
        Controller.display = "home";
        Controller.setDisplay();
    }

    @Override
    public void menuDeselected(MenuEvent e) {
        System.out.println("menuDeselected");
    }

    @Override
    public void menuCanceled(MenuEvent e) {
        System.out.println("menuCanceled");
    }
}
