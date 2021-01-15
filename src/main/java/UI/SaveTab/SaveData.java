/**
 * Pop-up for the creation of a folder and saving the data.
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

public class SaveData extends JFileChooser {
    // Constructor
    public SaveData(){
        setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    }

    public void save(){
        // Variable to access if save is successful
        SaveError saveError1 = null;
        SaveError saveError2=null;

        // Create save pop-up
        //Make sure the Mean Intensity has been measured
        if(!Controller.isMeanIntensityMeasured()){
            Object[] options = {"Ok"};
            JOptionPane.showOptionDialog(Controller.getInterframe(), "Please measure the mean intensity of the ROIs" +
                            " first with the Measure intensity button in Data > Results!",
                    "Warning",
                    JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
            return;
        }else if(Controller.getVideoProcessor().getVideo().getCells().size()==0){
            Object[] options = {"Ok"};
            JOptionPane.showOptionDialog(Controller.getInterframe(), "No regions of interest present, please add them.",
                    "Warning",
                    JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
            return;
        }

        // Show pop-up
        int userSelection= showSaveDialog(SaveData.this);

        // Cancel save
        if(userSelection==1)return;

        // Folders and files for save
        File folder = getSelectedFile();
        File MIFolder = new File(folder.getAbsoluteFile()+"/mean_intensity_measurements");
        File summaryFile = new File(folder.getPath()+"/data_summary.csv");

        // Save results
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            //Check if the summaryFile already exists and is empty. If not empty let the user choose whether to overwrite it or cancel
            if(folder.listFiles().length!=0){
                JCheckBox check = new JCheckBox("Warning");
                Object[] options = {"Yes", "No, overwrite"};
                int x = JOptionPane.showOptionDialog(null, "This directory is not empty, some files might be overwritten." +
                                "Do you want to change directory?",
                        "Warning",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
                //If decide to overwrite it
                if (x ==0) {
                    new SaveData().save();
                    return;
                }
            }
            if(!folder.isDirectory()) {
                Object[] options = {"Ok"};
                JOptionPane.showOptionDialog(Controller.getInterframe(), "Please input an existing directory name!",
                        "Warning",
                        JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
                new SaveData().save();

            }else {
                // Create or empty measurements folder
                if(MIFolder.exists())
                    for(File file:MIFolder.listFiles())
                        file.delete();
                else MIFolder.mkdir();
                // Save files
                saveError1 = Controller.getVideoProcessor().saveCellsMeanIntensity(MIFolder.getAbsolutePath());

                if (summaryFile.exists())
                    summaryFile.delete();
                saveError2 = Controller.getVideoProcessor().saveSummary(summaryFile.getPath());

                System.out.println(saveError1 + "," + saveError2);
            }
            // Check that save was successful
            String msg = "Unexpected error during save, try again.";
            if(saveError1 == SaveError.SAVE_SUCCESS && saveError2 == SaveError.SAVE_SUCCESS)return;
            else if(saveError2 == SaveError.SAVE_TYPE_ERROR) msg="ERROR, wrong file extension. Should be .csv";
            else if(saveError1==SaveError.SAVE_WRITE_ERROR || saveError2==SaveError.SAVE_WRITE_ERROR) msg="ERROR, unexpected write error. " +
                    "Close files with filename you are saving planar motion correction as " +
                    "or check that you have writing permissions for the path you specify.";
            Object[] options = {"Ok"};
            JOptionPane.showOptionDialog(Controller.getInterframe(), msg,
                    "Warning",
                    JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
        }
    }
}
