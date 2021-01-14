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
        int userSelection= showSaveDialog(SaveAll.this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = getSelectedFile();
                //Create a folder to store everything
                File theDir = new File(fileToSave.getPath());
                //Check if already exists
                if (!theDir.exists()){
                    theDir.mkdirs();

                    //Save the videos and ROIs
                    Controller.getVideoProcessor().savePlanarCorrectionVid(theDir.getPath()+"\\_PlanarVideoCorrected.tif");
                    Controller.getVideoProcessor().saveDepthCorrectionVid(theDir.getPath()+"\\_DepthVideoCorrected.tif");
                    Controller.getVideoProcessor().saveRoiImage(theDir.getPath()+"\\_ROIs.jpg");
                    //Sub folder for SaveData
                    File saveDataFolder= new File(theDir.getPath()+"\\DataFolder");
                    saveDataFolder.mkdirs();
                    //Save the Data in a sub folder
                    Controller.getVideoProcessor().saveSummary(saveDataFolder.getPath()+"\\Data");
                }
            }
    }
}
