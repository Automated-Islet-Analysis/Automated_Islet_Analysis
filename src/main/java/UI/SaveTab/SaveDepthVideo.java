package UI.SaveTab;

import UI.Controller;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;


public class SaveDepthVideo extends JFileChooser {

    public SaveDepthVideo(){
        setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "TIFF Images", "tif", "tiff");
        setFileFilter(filter);
    }

    public void save(){
        int userSelection= showSaveDialog(SaveDepthVideo.this);

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
                    Controller.getVideoProcessor().saveDepthCorrectionVid(fileWithExt.getPath());
                }
            }else {
                Controller.getVideoProcessor().saveDepthCorrectionVid(fileWithExt.getPath());
            }
        }
    }
}


