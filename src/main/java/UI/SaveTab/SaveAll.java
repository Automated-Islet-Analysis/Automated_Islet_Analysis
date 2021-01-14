package UI.SaveTab;

import UI.Controller;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class SaveAll extends JFileChooser {

    public SaveAll(){
        setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

    }

    public void save(){
        int userSelection= showSaveDialog(SaveAll.this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = getSelectedFile();
                //Create a folder to store everything
                File theDir = new File(fileToSave.getPath()+fileToSave.getName());
                if (!theDir.exists()){
                    theDir.mkdirs();

                    //Save the videos and ROIs
                    Controller.getVideoProcessor().savePlanarCorrectionVid(theDir.getPath()+".tif");
                    Controller.getVideoProcessor().saveDepthCorrectionVid(theDir.getPath()+".tif");
                    Controller.getVideoProcessor().saveRoiImage(theDir.getPath()+".jpg");
                    //Sub folder for SaveData
                    File saveDataFolder= new File(theDir.getPath());
                    saveDataFolder.mkdirs();
                    //Save the Data
                    Controller.getVideoProcessor().saveSummary(saveDataFolder.getPath());
                }
            }
    }
}
