/**
 * Pop-up for saving the images displaying the identified ROIs.
 *
 * @author Team Automated analysis of "islet in eye", Bioengineering department, Imperial College London
 *
 * Last modified: 14/01/2021
 */

package UI.SaveTab;
import UI.Controller;
import UI.HomeTab.Home;
import videoprocessing.SaveError;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class SaveROIs extends JFileChooser {

    // Constructor
    public SaveROIs(){
        setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        //Allow only for image extensions
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Images", "jpg");
        setFileFilter(filter);
    }


    public void save(){
        // Variable to access if save is successful
        SaveError saveError;

        // Create save pop-up
        int userSelection= showSaveDialog(SaveROIs.this);

        // Cancel save
        if(userSelection==1)return;

        // Save image
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = getSelectedFile();
            File fileWithExt = new File(fileToSave.getAbsolutePath()+".jpg");
            //Check if the file already exists and let the user choose between overwriting or cancelling
            if(fileWithExt.exists() && !fileToSave.isDirectory()) {
                JCheckBox check = new JCheckBox("Warning");
                Object[] options = {"Yes", "No, overwrite"};
                int x = JOptionPane.showOptionDialog(null, "This file already exist. Do you want to change its name?",
                        "Warning",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);

                //If decide to overwrite
                if (x ==1) {
                    //Delete the existing file
                    fileWithExt.delete();
                    //Replace it with a new one
                    saveError = Home.getVideoProcessor().saveRoiImage(fileWithExt.getPath());
                }else{
                    new SaveROIs().save();
                    return;
                }
            }else {
                //If doesn't want to overwrite or the file didn't exist
                saveError = Home.getVideoProcessor().saveRoiImage(fileWithExt.getPath());
            }
            String msg = "Unexpected error during save, try again.";
            if(saveError == SaveError.SAVE_SUCCESS)return;
            else if(saveError == SaveError.SAVE_TYPE_ERROR) msg="ERROR, wrong file extension. Should be .jpg or .jpeg";
            else if(saveError==SaveError.SAVE_WRITE_ERROR) msg="ERROR, unexpected write error. Close files with filename" +
                    " you are saving ROIs as or check that you have writing permissions for the path you specify.";
            Object[] options = {"Ok"};
            JOptionPane.showOptionDialog(Home.getInterframe(), msg,
                    "Warning",
                    JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
        }
    }
}
