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

import static sun.security.util.KnownOIDs.Data;

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
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = getSelectedFile();
                //Create a folder to store everything
                File theDir = new File(fileToSave.getPath());
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
        Controller.getVideoProcessor().savePlanarCorrectionVid(directory.getPath()+"\\_PlanarVideoCorrected.tif");
        Controller.getVideoProcessor().saveDepthCorrectionVid(directory.getPath()+"\\_DepthVideoCorrected.tif");
        Controller.getVideoProcessor().saveRoiImage(directory.getPath()+"\\_ROIs.jpg");
        //Sub folder for SaveData
        File saveDataFolder= new File(directory.getPath()+"\\DataFolder");
        saveDataFolder.mkdirs();
        //Save the Data in a sub folder
        Controller.getVideoProcessor().saveCellsMeanIntensity(saveDataFolder.getAbsolutePath());
        Controller.getVideoProcessor().saveSummary(saveDataFolder.getPath()+"\\Data_Summary.csv");


    }
}
