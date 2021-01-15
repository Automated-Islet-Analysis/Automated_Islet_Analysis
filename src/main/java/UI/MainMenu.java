/**
 * Contains the structure of the main menu and interacts with Controller for the display of the correct panels.
 *
 * @author Team Automated analysis of "islet in eye", Bioengineering department, Imperial College London
 *
 * Last modified: 11/01/2021
 */

package UI;

import UI.HomeTab.Home;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class MainMenu extends JMenuBar implements ActionListener {
    // Create components
    JMenu menuHome, menuData, menuSave, subMenuMCVid;
    JMenuItem dataROI, dataData, MCVidPlanar, MCVidDepth;
    JMenuItem saveROI,saveMCVidPlanar,saveMCVidDepth, saveData, saveAll;

    // Constructor
    public MainMenu() {
        menuHome = getMenuHome();
        menuData = getMenuData();
        menuSave = getMenuSave();

        // Add menus to the JMenuBar
        add(menuHome);
        add(menuData);
        add(menuSave);

        // Add keystroke as an alternative way to save all
        saveAll.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.CTRL_MASK));
    }

    @Override
    // Called whenever a menu item is selected
    // ActionEvent command is the text of the menu item selected
    public void actionPerformed(ActionEvent e) {
        String tabClicked = e.getActionCommand();
        // Changes display according to clicked menu item
        switch (tabClicked){
            case "Regions of interest":
                Home.setDisplay("ROIs");
                Home.setDisplay();
                break;

            case "Planar motion":
                Home.setDisplay("MCVideoPlanar");
                Home.setDisplay();
                break;

            case "Depth motion":
                Home.setDisplay("MCVideoDepth");
                Home.setDisplay();
                break;

            case "Save results":
                Home.setDisplay("SaveData") ;
                Home.setDisplay();
                break;

            case "Save regions of interest":
                Home.setDisplay("SaveROIs") ;
                Home.setDisplay();
                break;

            case "Save video of planar motion correction":
                Home.setDisplay("SavePlanarVideo");
                Home.setDisplay();
                break;

            case "Save video of depth motion correction":
                Home.setDisplay("SaveDepthVideo");
                Home.setDisplay();
                break;

            case "Save all":
                Home.setDisplay("SaveAll");
                Home.setDisplay();
                break;

            case "Results":
                Home.setDisplay("Results");
                Home.setDisplay();
                break;

            default:
                // Null
        }
    }

    // Create home tab
    private JMenu getMenuHome(){
        MenuListener menuListener = new MenuListener() {
            @Override
            // Action performed when clicking on Home button in the menu bar
            public void menuSelected(MenuEvent e) {
                // Change view to Home page
                if (Home.isFileUploaded()==true)
                    Home.setDisplay("Upload");
                else Home.setDisplay("home");
                Home.setDisplay();
            }
            @Override
            public void menuDeselected(MenuEvent e) { }
            @Override
            public void menuCanceled(MenuEvent e) { }
        };

        menuHome = new JMenu("Home          ");
        menuHome.setFont(new Font(menuHome.getFont().getName(), Font.BOLD, 15));
        menuHome.addMenuListener(menuListener); // add actionlistener

        return menuHome;
    }

    // Create data tab
    private JMenu getMenuData(){
        menuData = new JMenu("Data          ");
        menuData.setFont(new Font(menuData.getFont().getName(), Font.BOLD, 15));

        // Data Dropdown
        dataROI = new JMenuItem("Regions of interest");
        subMenuMCVid = new JMenu("Motion corrected videos");
        MCVidPlanar = new JMenuItem("Planar motion");
        MCVidDepth = new JMenuItem("Depth motion");
        dataData = new JMenuItem("Results");

        // ActionListeners
        dataROI.addActionListener(this);
        MCVidPlanar.addActionListener(this);
        MCVidDepth.addActionListener(this);
        dataData.addActionListener(this);

        menuData.add(dataROI);
        menuData.add(subMenuMCVid);
        subMenuMCVid.add(MCVidPlanar);
        subMenuMCVid.add(MCVidDepth);
        menuData.add(dataData);

        return menuData;
    }

    // Create save tab
    private JMenu getMenuSave(){
        menuSave = new JMenu("Save          ");
        menuSave.setFont(new Font(menuSave.getFont().getName(), Font.BOLD, 15));

        // Save Dropdown
        saveROI = new JMenuItem("Save regions of interest");
        saveMCVidPlanar = new JMenuItem("Save video of planar motion correction");
        saveMCVidDepth=new JMenuItem("Save video of depth motion correction");

        saveData = new JMenuItem("Save results");
        saveAll = new JMenuItem("Save all");

        // ActionListeners
        saveROI.addActionListener(this);
        saveMCVidPlanar.addActionListener(this);
        saveMCVidDepth.addActionListener(this);
        saveData.addActionListener(this);
        saveAll.addActionListener(this);

        menuSave.add(saveROI);
        menuSave.add(saveMCVidPlanar);
        menuSave.add(saveMCVidDepth);
        menuSave.add(saveData);
        menuSave.add(saveAll);

        return menuSave;
    }
}