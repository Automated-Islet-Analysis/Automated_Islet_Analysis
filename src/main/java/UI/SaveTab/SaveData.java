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
        int userSelection= showSaveDialog(SaveData.this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = getSelectedFile();
            File fileWithExt = new File(fileToSave.getAbsolutePath());
            //Check if the file already exists and let the user choose whether to overwrite it or cancel
            if(fileWithExt.exists() && !fileToSave.isDirectory()) {
                JCheckBox check = new JCheckBox("Warning");
                Object[] options = {"Yes", "No, overwrite"};
                int x = JOptionPane.showOptionDialog(null, "This file already exist. Do you want to change its name?",
                        "Warning",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);

                //If decide to overwrite it
                if (check.isSelected() && x ==1) {
                    Controller.getVideoProcessor().saveSummary(fileWithExt.getPath());
                }
                //If cancelled previous operation or i the file did not exist
            }else {
                Controller.getVideoProcessor().saveSummary(fileWithExt.getPath());
            }
        }
    }
}
