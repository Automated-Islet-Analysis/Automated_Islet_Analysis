/**
 * Pop-up saving the planar motion corrected video.
 *
 * @author Team Automated analysis of "islet in eye", Bioengineering department, Imperial College London
 *
 * Last modified: 14/01/2021
 */
package UI.SaveTab;
import UI.Controller;
import videoprocessing.SaveError;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class SavePlanarVideo extends JFileChooser {

    // Constructor
    public SavePlanarVideo(){
        //create the file chooser
        setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        //Allow only for tif extensions files
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "TIFF Images", "tif", "tiff");
        setFileFilter(filter);
    }

    public void save(){
        // Variable to access if save is successful
        SaveError saveError;

        // Check if video exists
        if(Controller.getVideoProcessor().getPlanarCorrectionVid()==null){
            Object[] options = {"Ok"};
            JOptionPane.showOptionDialog(Controller.getInterframe(), "Planar motion correction was not applied to video. " +
                            "Please first select planar motion correction during processing.",
                    "Warning",
                    JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
            return;
        }

        // Create save pop-up
        int userSelection= showSaveDialog(SavePlanarVideo.this);

        // Cancel save
        if(userSelection==1)return;

        // Save video
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = getSelectedFile();
            File fileWithExt = new File(fileToSave.getAbsolutePath()+".tif");
            //Check if the file already exists and let the user choose whether to overwrite it or cancel
            if(fileWithExt.exists() && !fileToSave.isDirectory()) {
                JCheckBox check = new JCheckBox("Warning");
                Object[] options = {"Yes", "No, overwrite"};
                int x = JOptionPane.showOptionDialog(null, "This file already exist. Do you want to change its name?",
                        "Warning",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);

                //If decide to overwrite it
                if (x ==1) {
                    //delete the existing file
                    fileWithExt.delete();
                    //Create a new one
                    saveError = Controller.getVideoProcessor().savePlanarCorrectionVid(fileWithExt.getPath());
                }else{
                    new SavePlanarVideo().save();
                    return;
                }
                //If cancelled previous operation or i the file did not exist
            }else {
                saveError =Controller.getVideoProcessor().savePlanarCorrectionVid(fileWithExt.getPath());
            }

            // Check that save was successful
            String msg = "Unexpected error during save, try again.";
            if(saveError == SaveError.SAVE_SUCCESS)return;
            else if(saveError == SaveError.SAVE_TYPE_ERROR) msg="ERROR, wrong file extension. Should be .tif or .tiff";
            else if(saveError==SaveError.SAVE_WRITE_ERROR) msg="ERROR, unexpected write error. Close files with filename" +
                    " you are saving planar motion correction as or check that you have writing permissions for the path you specify.";
            Object[] options = {"Ok"};
            JOptionPane.showOptionDialog(Controller.getInterframe(), msg,
                    "Warning",
                    JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
        }
    }
}
