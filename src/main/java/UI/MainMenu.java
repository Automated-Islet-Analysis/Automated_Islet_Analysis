/**
 * Class containes all the structure of the main menu and interacts with Controller for the display of the correct panels.
 *
 * @author Team Automated analysis of "islet in eye", Bioengineering department, Imperial College London
 *
 * Last modified: 11/01/2021
 */

package UI;

import UI.HomeTab.HomeMenuListener;
import UI.SaveTab.SaveDepthVideo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class MainMenu extends JMenuBar implements ActionListener {
    JMenu menuHome, menuData, menuSave, subMenuMCVid;
    JMenuItem dataROI, dataData, MCVidPlanar, MCVidDepth;
    JMenuItem saveROI,saveMCVidPlanar,saveMCVidDepth, saveData, saveAll;

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
    // This method will be called whenever any menu item is selected
    // The ActionEvent command will be the text of the menu item selected
    public void actionPerformed(ActionEvent e) {
        // Changes display according to clicked menu item
        if (e.getActionCommand().equals("ROIs")){
            Controller.setDisplay("ROIs");
            Controller.setDisplay();
        }else if (e.getActionCommand().equals("Planar motion")){
            Controller.setDisplay("MCVideoPlanar");
            Controller.setDisplay();
        }else if (e.getActionCommand().equals("Depth motion")){
            Controller.setDisplay("MCVideoDepth");
            Controller.setDisplay();
        }else if (e.getActionCommand().equals("Save Data")){
            Controller.setDisplay("SaveData") ;
            Controller.setDisplay();
        }else if (e.getActionCommand().equals("Save ROIs")){
            Controller.setDisplay("SaveROIs") ;
            Controller.setDisplay();
        }else if (e.getActionCommand().equals("Save Planar Corrected Video")){
            Controller.setDisplay("SavePlanarVideo");
            Controller.setDisplay();
        }else if (e.getActionCommand().equals("Save Depth Corrected Video")){
            Controller.setDisplay("SaveDepthVideo");
            Controller.setDisplay();
        }else if (e.getActionCommand().equals("Save All")){
            Controller.setDisplay("SaveAll");
            Controller.setDisplay();
        }

        System.out.println(e.getActionCommand());
    }

    private JMenu getMenuHome(){
        menuHome = new JMenu("Home          ");
        menuHome.setFont(new Font(menuHome.getFont().getName(), Font.BOLD, 15));
        menuHome.addMenuListener(new HomeMenuListener()); // add actionlistener

        return menuHome;
    }

    private JMenu getMenuData(){
        menuData = new JMenu("Data          ");
        menuData.setFont(new Font(menuData.getFont().getName(), Font.BOLD, 15));

        // Data Dropdown
        dataROI = new JMenuItem("ROIs");
        subMenuMCVid = new JMenu("Motion Corrected Video");
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

    private JMenu getMenuSave(){
        menuSave = new JMenu("Save          ");
        menuSave.setFont(new Font(menuSave.getFont().getName(), Font.BOLD, 15));

        // Save Dropdown
        saveROI = new JMenuItem("Save ROIs");
        saveMCVidPlanar = new JMenuItem("Save Planar Corrected Video");
        saveMCVidDepth=new JMenuItem("Save Depth Corrected Video");

        saveData = new JMenuItem("Save Data");
        saveAll = new JMenuItem("Save All");

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

