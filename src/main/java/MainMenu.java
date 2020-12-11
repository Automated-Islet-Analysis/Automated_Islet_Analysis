
import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JMenuBar implements ActionListener {
    JMenu menuHome; // Menus
    JMenu menuData;
    JMenu menuSave;

    JMenuItem dataROI; // Which have items
    JMenuItem dataMCVid;
    JMenuItem dataData;

    JMenuItem saveROI;
    JMenuItem saveMCVid;
    JMenuItem saveData;
    JMenuItem saveAll;

    public MainMenu() {
        menuHome = new JMenu("Home");

        menuData = new JMenu("Data");
        dataROI = new JMenuItem("ROIs");
        dataMCVid = new JMenuItem("Motion Corrected Video");
        dataData = new JMenuItem("Data");

        menuSave = new JMenu("Save");
        saveROI = new JMenuItem("Save ROIs");
        saveMCVid = new JMenuItem("Save Motion Corrected Video");
        saveData = new JMenuItem("Save Data");
        saveAll = new JMenuItem("Save All");

        menuHome.addMenuListener(new HomeMenuListener());

        dataROI.addActionListener(this);
        dataMCVid.addActionListener(this);
        dataData.addActionListener(this);

        saveROI.addActionListener(this);
        saveMCVid.addActionListener(this);
        saveData.addActionListener(this);
        saveAll.addActionListener(this);

        add(menuHome); // Add menus to the JMenuBar
        add(menuData);
        add(menuSave);

        menuData.add(dataROI); // Add the menu items to each menu
        menuData.add(dataMCVid);
        menuData.add(dataData);

        menuSave.add(saveROI);
        menuSave.add(saveMCVid);
        menuSave.add(saveData);
        menuSave.add(saveAll);

    }

    @Override
    // This method will be called whenever any menu item is selected
    // The ActionEvent command will be the text of the menu item selected
    public void actionPerformed(ActionEvent e) {
        // It's creating a new frame, but I want it to change the past one
        // I don't know how to access the function without a controller object
        if (e.getActionCommand() == "ROIs"){
            Controller.display = "ROIs";
            Controller.setDisplay();
        }else if (e.getActionCommand() == "Motion corrected video"){
            Controller.display = "MCVideo";
            Controller.setDisplay();
        }else if (e.getActionCommand() == "Data"){
            Controller.display = "Data";
            Controller.setDisplay();
        }
        System.out.println(e.getActionCommand());
    }
}

