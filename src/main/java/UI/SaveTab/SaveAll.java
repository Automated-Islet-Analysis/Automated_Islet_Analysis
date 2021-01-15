/**
 * Pop-up for the creation of a folder and saving the processed videos, image with ROIs and data (in a subfolder).
 *
 * @author Team Automated analysis of "islet in eye", Bioengineering department, Imperial College London
 *
 * Last modified: 14/01/2021
 */

package UI.SaveTab;

import UI.Controller;
import videoprocessing.SaveError;

import javax.swing.*;
import java.io.File;

public class SaveAll extends JFileChooser {

    // Constructor
    public SaveAll(){
        setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

    }

    public void save(){

        //Make sure the Mean Intensity has been measured
        if(!Controller.isMeanIntensityMeasured()){
            Object[] options = {"Ok"};
            JOptionPane.showOptionDialog(Controller.getInterframe(), "Please measure the mean intensity of the ROIs first with the Measure intensity button in Data > Results!",
                    "Warning",
                    JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
            return;
        }
        // Show pop-up
        int userSelection= showSaveDialog(SaveAll.this);

        // Cancel save
        if (userSelection==1)return;

        // Folder for save
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
        SaveError saveError1=null;
        SaveError saveError2=null;
        SaveError saveError3=null;
        SaveError saveError4=null;
        SaveError saveError5=null;

        //Save the videos and ROIs
        if(Controller.getVideoProcessor().getPlanarCorrectionVid()!=null)
            saveError1=Controller.getVideoProcessor().savePlanarCorrectionVid(directory.getPath()+"/planar_video_corrected.tif");
        else saveError1=SaveError.SAVE_SUCCESS;
        if(Controller.getVideoProcessor().getDepthCorrectionVid()!=null)
            saveError2 = Controller.getVideoProcessor().saveDepthCorrectionVid(directory.getPath() + "/depth_video_corrected.tif");
        else saveError2 = SaveError.SAVE_SUCCESS;
        saveError3=Controller.getVideoProcessor().saveRoiImage(directory.getPath()+"/ROIs.jpg");

        //Sub folder for SaveData
        File saveDataFolder= new File(directory.getPath()+"/data_folder");
        File MIFolder  = new File(saveDataFolder+"/mean_intensity_measurements");

        // Make sure all necessary folders are present and empty
        if(saveDataFolder.exists())
            saveDataFolder.delete();
        saveDataFolder.mkdir();
        MIFolder.mkdir();

        //Save the Data in a sub folder
        saveError4=Controller.getVideoProcessor().saveCellsMeanIntensity(MIFolder.getAbsolutePath());
        saveError5=Controller.getVideoProcessor().saveSummary(saveDataFolder.getPath()+"/data_summary.csv");

        // Check that save was successful
        String msg = "Unexpected error during save, try again.";
        if(saveError1 == SaveError.SAVE_SUCCESS && saveError2 == SaveError.SAVE_SUCCESS && saveError3 == SaveError.SAVE_SUCCESS
            && saveError4 == SaveError.SAVE_SUCCESS &&saveError5 == SaveError.SAVE_SUCCESS)return;
        else if(saveError1 == SaveError.SAVE_WRITE_ERROR || saveError2 == SaveError.SAVE_WRITE_ERROR || saveError3 == SaveError.SAVE_WRITE_ERROR
                || saveError4 == SaveError.SAVE_WRITE_ERROR ||saveError5 == SaveError.SAVE_WRITE_ERROR) msg="ERROR, unexpected write error. Close files from the folder you are saving to" +
                " and check that you have writing permissions for the path you specify.";
        Object[] options = {"Ok"};
        JOptionPane.showOptionDialog(Controller.getInterframe(), msg,
                "Warning",
                JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
    }
}
