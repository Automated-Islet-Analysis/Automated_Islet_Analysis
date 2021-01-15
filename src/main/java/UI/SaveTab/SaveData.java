/**
 * Pop-up for the creation of a folder and saving the data.
 *
 * @author Team Automated analysis of "islet in eye", Bioengineering department, Imperial College London
 *
 * Last modified: 14/01/2021
 */
package UI.SaveTab;
import UI.Controller;
import javax.swing.*;
import java.io.File;

public class SaveData extends JFileChooser {

    // Constructor
    public SaveData(){
        setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    }

    public void save(){
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

        int userSelection= showSaveDialog(SaveData.this);
        if(userSelection==1)return;

        File folder = getSelectedFile();
        File MIFolder = new File(folder.getAbsoluteFile()+"/mean_intensity_measurements");
        File summaryFile = new File(folder.getPath()+"/data_summary.csv");

        if(summaryFile.exists() || MIFolder.listFiles().length!=0){
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

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            //Check if the summaryFile already exists and let the user choose whether to overwrite it or cancel
            if(!folder.isDirectory()) {
                Object[] options = {"Ok"};
                JOptionPane.showOptionDialog(Controller.getInterframe(), "Please input an existing directory summaryFile name!",
                        "Warning",
                        JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
            }else {
                // Create or empty measurements folder
                if(MIFolder.exists())
                    MIFolder.delete();
                MIFolder.mkdir();
                // Save files
                Controller.getVideoProcessor().saveCellsMeanIntensity(MIFolder.getAbsolutePath());

                if (summaryFile.exists())
                    summaryFile.delete();
                Controller.getVideoProcessor().saveSummary(summaryFile.getPath());
            }
        }
    }
}
