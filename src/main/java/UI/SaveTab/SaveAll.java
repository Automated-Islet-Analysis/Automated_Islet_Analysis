/**
 * Pop-up for the creation of a folder and saving the processed videos, image with ROIs and data (in a subfolder).
 *
 * @author Team Automated analysis of "islet in eye", Bioengineering department, Imperial College London
 *
 * Last modified: 14/01/2021
 */

package UI.SaveTab;

import UI.Controller;
import javax.swing.*;
import java.io.File;

public class SaveAll extends JFileChooser {

    // Constructor
    public SaveAll(){
        setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

    }

    public void save(){
        // Create save pop-up

        //Make sure the Mean Intensity has been measured
        if(!Controller.isMeanIntensityMeasured()){
            Object[] options = {"Ok"};
            JOptionPane.showOptionDialog(Controller.getInterframe(), "Please measure the mean intensity of the ROIs first with the Measure intensity button in Data > Results!",
                    "Warning",
                    JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
            return;
        }
        int userSelection= showSaveDialog(SaveAll.this);
        if (userSelection==1)return;

        File folderToSave = getSelectedFile();

        if(folderToSave.listFiles().length!=0){
            JCheckBox check = new JCheckBox("Warning");
            Object[] options = {"Yes", "No, overwrite"};
            int x = JOptionPane.showOptionDialog(null, "This directory is not empty, some files might be overwritten." +
                            "Do you want to change directory?",
                    "Warning",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);

            //If decide to overwrite it
            if (x ==0) {
                new SaveAll().save();
                return;
            }
        }

        if (userSelection == JFileChooser.APPROVE_OPTION) {
                //Create a folder to store everything
                File theDir = new File(folderToSave.getPath());
                //Check if already exists
                if (!theDir.exists()){
                    //Create the non-existing directory
                    theDir.mkdirs();
                    //Call the method to save
                    callSavers(theDir);
                }else{
                    //Call the method to save
                    callSavers(theDir);
                }
            }
    }
    public void callSavers(File directory){
        //Save the videos and ROIs
        Controller.getVideoProcessor().savePlanarCorrectionVid(directory.getPath()+"/planar_video_corrected.tif");
        Controller.getVideoProcessor().saveDepthCorrectionVid(directory.getPath()+"/depth_video_corrected.tif");
        Controller.getVideoProcessor().saveRoiImage(directory.getPath()+"/ROIs.jpg");

        //Sub folder for SaveData
        File saveDataFolder= new File(directory.getPath()+"/data_folder");
        File MIFolder  = new File(saveDataFolder+"/mean_intensity_measurements");

        // Make sure all necessary folders are present and empty
        if(saveDataFolder.exists())
            saveDataFolder.delete();
        saveDataFolder.mkdir();
        if(MIFolder.exists())
            MIFolder.delete();
        MIFolder.mkdir();

        //Save the Data in a sub folder
        Controller.getVideoProcessor().saveCellsMeanIntensity(MIFolder.getAbsolutePath());
        Controller.getVideoProcessor().saveSummary(saveDataFolder.getPath()+"/data_summary.csv");
    }
}
