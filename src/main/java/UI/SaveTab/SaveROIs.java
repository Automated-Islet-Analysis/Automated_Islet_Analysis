package UI.SaveTab;

import UI.Controller;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class SaveROIs extends JFileChooser {

    public SaveROIs(){
        setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Images", "jpg", "png");
        setFileFilter(filter);
    }

    public void save(){
        int userSelection= showSaveDialog(SaveROIs.this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = getSelectedFile();
            File fileWithExt = new File(fileToSave.getAbsolutePath()+".tif");
            if(fileWithExt.exists() && !fileToSave.isDirectory()) {
                JCheckBox check = new JCheckBox("Warning");
                Object[] options = {"Yes", "No, overwrite"};
                int x = JOptionPane.showOptionDialog(null, "This file already exist. Do you want to change its name?",
                        "Warning",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);

                if (check.isSelected() && x ==1) {
                    Controller.getVideoProcessor().saveRoiImage(fileWithExt.getPath());
                }
            }else {
                Controller.getVideoProcessor().saveRoiImage(fileWithExt.getPath());
            }
        }
    }
}
