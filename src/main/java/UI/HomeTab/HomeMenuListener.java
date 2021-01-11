/**
 * Action listener of the home tab of the menu. The displayed panel depends on whether a file was already uploaded or not.2
 *
 * @author Team Automated analysis of "islet in eye", Bioengineering department, Imperial College London
 *
 * Last modified: 11/01/2021
 */

package UI.HomeTab;

import UI.Controller;

import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

class HomeMenuListener implements MenuListener {

    @Override
    public void menuSelected(MenuEvent e) {
        // Change view to Home page
        if (Controller.isFileUploaded()==true)
            Controller.setDisplay("Upload");
        else Controller.setDisplay("home");
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