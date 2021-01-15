/**
 * Pop-up for saving the depth corrected video.
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


public class SaveDepthVideo extends JFileChooser {

    // Constructor
    public SaveDepthVideo(){
        setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        //Allow tif extensions only
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "TIFF Images", "tif", "tiff");
        setFileFilter(filter);
    }

    public void save(){
        // Variable to access if save is successful
        SaveError saveError;

        // Check if video exists
        if(Controller.getVideoProcessor().getDepthCorrectionVid()==null){
            Object[] options = {"Ok"};
            JOptionPane.showOptionDialog(Controller.getInterframe(), "Depth motion correction was not applied to video. " +
                            "Please first select depth motion correction during processing.",
                    "Warning",
                    JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
        }

        // Create save pop-up
        int userSelection= showSaveDialog(SaveDepthVideo.this);

        // Cancel save
        if(userSelection==1)return;

        // Save file
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
                    fileWithExt.delete();
                    saveError = Controller.getVideoProcessor().saveDepthCorrectionVid(fileWithExt.getPath());
                //If decide not to overwrite it, pop up again
                }else{
                    new SaveDepthVideo().save();
                    return;
                }
            }else {
                saveError = Controller.getVideoProcessor().saveDepthCorrectionVid(fileWithExt.getPath());
            }
            // Check that save was successful
            String msg = "Unexpected error during save, try again.";
            if(saveError == SaveError.SAVE_SUCCESS)return;
            else if(saveError == SaveError.SAVE_TYPE_ERROR) msg="ERROR, wrong file extension. Should be .tif or .tiff";
            else if(saveError==SaveError.SAVE_WRITE_ERROR) msg="ERROR, unexpected write error. Close files with filename" +
                    " you are saving depth motion correction as or check that you have writing permissions for the path you specify.";
            Object[] options = {"Ok"};
            JOptionPane.showOptionDialog(Controller.getInterframe(), msg,
                    "Warning",
                    JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
        }
    }
}


