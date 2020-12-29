import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class MainMenu extends JMenuBar implements ActionListener {
    JMenu menuHome, menuData, menuSave, subMenuMCVid;
    static JMenuItem dataROI, dataData, MCVidPlanar, MCVidDepth;
    JMenuItem saveROI,saveMCVid, saveData, saveAll;

    public MainMenu() {



        menuHome = new JMenu("Home          ");
        menuHome.setFont(new Font(menuHome.getFont().getName(), Font.BOLD, 15));

        // Data dropdown
        menuData = new JMenu("Data          ");
        menuData.setFont(new Font(menuData.getFont().getName(), Font.BOLD, 15));

        dataROI = new JMenuItem("ROIs");
        subMenuMCVid = new JMenu("Motion corrected video");
        MCVidPlanar = new JMenuItem("Planar motion");
        MCVidDepth = new JMenuItem("Depth motion");
        dataData = new JMenuItem("Data");

        // Save dropdown
        menuSave = new JMenu("Save          ");
        menuSave.setFont(new Font(menuSave.getFont().getName(), Font.BOLD, 15));

        saveROI = new JMenuItem("Save ROIs");
        saveMCVid = new JMenuItem("Save motion corrected video");
        saveData = new JMenuItem("Save data");
        saveAll = new JMenuItem("Save all");

        // Action Listeners
        menuHome.addMenuListener(new HomeMenuListener());

        dataROI.addActionListener(this);
        MCVidPlanar.addActionListener(this);
        MCVidDepth.addActionListener(this);
        dataData.addActionListener(this);

        saveROI.addActionListener(this);
        saveMCVid.addActionListener(this);
        saveData.addActionListener(this);
        saveAll.addActionListener(this);

        // Add menus to the JMenuBar
        add(menuHome);
        add(menuData);
        add(menuSave);

        // Add the menu items to each menu
        menuData.add(dataROI);
        menuData.add(subMenuMCVid);
            subMenuMCVid.add(MCVidPlanar);
            subMenuMCVid.add(MCVidDepth);
        menuData.add(dataData);

        menuSave.add(saveROI);
        menuSave.add(saveMCVid);
        menuSave.add(saveData);
        menuSave.add(saveAll);

        // Add keystroke as an alternative way to save all
        saveAll.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.CTRL_MASK));

        dataROI.setName("ROIs");
        menuData.setName("Data");
        dataData.setName("dataData");
        menuHome.setName("Home");

        subMenuMCVid.setName("MCVideo");
        MCVidPlanar.setName("MCPlanar");
        MCVidDepth.setName("MCDepth");

        menuSave.setName("menuSave");
        saveROI.setName("saveROI");
        saveData.setName("saveData");
        saveMCVid.setName("saveMCVideo");
        saveAll.setName("saveAll");

    }

    @Override
    // This method will be called whenever any menu item is selected
    // The ActionEvent command will be the text of the menu item selected
    public void actionPerformed(ActionEvent e) {
        // Changes display according to clicked menu item
        if (e.getActionCommand() == "ROIs"){
            Home.display = "ROIs";
            Home.setDisplay();
        }else if (e.getActionCommand() == "Motion corrected video"){
            Home.display = "MCVideo";
            Home.setDisplay();
        }else if (e.getActionCommand() == "Data"){
            Home.display = "Data";
            Home.setDisplay();
        }
        System.out.println(e.getActionCommand());
    }
}

