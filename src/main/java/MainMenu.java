import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JMenuBar implements ActionListener {
    JMenu menuHome; // One menu
    JMenu menuData;
    JMenu menuSave;
    JMenuItem dataROI; // Which has one item
    JMenuItem dataMCVid;
    JMenuItem saveROI;
    JMenuItem saveMCVid;
    JMenuItem saveData;
    JMenuItem saveAll;

    public MainMenu() {
        menuHome = new JMenu("Home");

        menuData = new JMenu("Data");
        dataROI = new JMenuItem("ROIs");
        dataMCVid = new JMenuItem("Motion corrected video");

        menuSave = new JMenu("Save");
        saveROI = new JMenuItem("Save ROIs");
        saveMCVid = new JMenuItem("Save motion corrected video");
        saveData = new JMenuItem("Save data");
        saveAll = new JMenuItem("Save all");

        menuHome.addMenuListener(new HomeMenuListener());

        dataROI.addActionListener(this);
        dataMCVid.addActionListener(this);

        saveROI.addActionListener(this);
        saveMCVid.addActionListener(this);
        saveData.addActionListener(this);
        saveAll.addActionListener(this);

        add(menuHome); // Add menus to the JMenuBar
        add(menuData);
        add(menuSave);

        menuData.add(dataROI); // Add the menu items to each menu
        menuData.add(dataMCVid);

        menuSave.add(saveROI);
        menuSave.add(saveMCVid);
        menuSave.add(saveData);
        menuSave.add(saveAll);
    }

    @Override
    // If 'this' is set as the ActionListener to all menu items, this method will
    // be called whenever any menu item is selected, but the ActionEvent
    // command will be the text of the menu item selected
    public void actionPerformed(ActionEvent e) {

        System.out.println(e.getActionCommand());
    }
}

