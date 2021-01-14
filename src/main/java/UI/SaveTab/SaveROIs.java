/**
 * Pop-up for letting the user specifically save the images displaying the identified ROIs.
 *
 * @author Team Automated analysis of "islet in eye", Bioengineering department, Imperial College London
 *
 * Last modified: 14/01/2021
 */
package UI.SaveTab;
import UI.Controller;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class SaveROIs extends JFileChooser {

    public SaveROIs(){
        setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        //Allow only for image extensions
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Images", "jpg", "png");
        setFileFilter(filter);
    }


    public void save(){
        int userSelection= showSaveDialog(SaveROIs.this);

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
                if (check.isSelected() && x ==1) {
                    Controller.getVideoProcessor().saveRoiImage(fileWithExt.getPath());
                }
            }else {
                //If doesn't want to overwrite or the file didn't exist
                Controller.getVideoProcessor().saveRoiImage(fileWithExt.getPath());
            }
        }
    }
}
