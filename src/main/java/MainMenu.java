import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JMenuBar implements ActionListener {
    JMenu menuHome; // One menu
    JMenu menuData;
    JMenu menuSave;

    JMenuItem dataROI; // Which has one item
    JMenuItem dataMCVid;
    JMenuItem dataData;

    JMenuItem saveROI;
    JMenuItem saveMCVid;
    JMenuItem saveData;
    JMenuItem saveAll;

    public MainMenu() {
        menuHome = new JMenu("Home");

        // Data dropdown
        menuData = new JMenu("Data");
        dataROI = new JMenuItem("ROIs");
        dataMCVid = new JMenuItem("Motion corrected video");
        dataData = new JMenuItem("Data");

        // Save dropdown
        menuSave = new JMenu("Save");
        saveROI = new JMenuItem("Save ROIs");
        saveMCVid = new JMenuItem("Save motion corrected video");
        saveData = new JMenuItem("Save data");
        saveAll = new JMenuItem("Save all");

        // Action Listeners
        menuHome.addMenuListener(new HomeMenuListener());

        dataROI.addActionListener(this);
        dataMCVid.addActionListener(this);
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
        // Changes display according to clicked menu item
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

