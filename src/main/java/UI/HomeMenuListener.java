package UI;

import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

class HomeMenuListener implements MenuListener {

    @Override
    public void menuSelected(MenuEvent e) {
        // Change view to Home page
        if (Home.fileUploaded==true)
            Home.display = "upload";
        else Home.display="home";
        Home.setDisplay();
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
