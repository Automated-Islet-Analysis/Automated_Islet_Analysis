package UI.SaveTab;

import UI.Controller;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class SaveData extends JFileChooser {

    public SaveData(){
        setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

    }

    public void save(){
        int userSelection= showSaveDialog(SaveData.this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = getSelectedFile();
            File fileWithExt = new File(fileToSave.getAbsolutePath());
            if(fileWithExt.exists() && !fileToSave.isDirectory()) {
                JCheckBox check = new JCheckBox("Warning");
                Object[] options = {"Yes", "No, overwrite"};
                int x = JOptionPane.showOptionDialog(null, "This file already exist. Do you want to change its name?",
                        "Warning",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);

                if (check.isSelected() && x ==1) {
                    Controller.getVideoProcessor().saveSummary(fileWithExt.getPath());
                }
            }else {
                Controller.getVideoProcessor().saveSummary(fileWithExt.getPath());
            }
        }
    }
}
