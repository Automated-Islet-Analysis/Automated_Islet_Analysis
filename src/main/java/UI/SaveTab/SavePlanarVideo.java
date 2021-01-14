/**
 * Pop-up for letting specifically save the planar corrected video.
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

public class SavePlanarVideo extends JFileChooser {

    public SavePlanarVideo(){
        //create the file chooser
        setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        //Allow only for tif extensions files
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "TIFF Images", "tif", "tiff");
        setFileFilter(filter);
    }

    public void save(){
        int userSelection= showSaveDialog(SavePlanarVideo.this);

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
                if (check.isSelected() && x ==1) {
                    Controller.getVideoProcessor().savePlanarCorrectionVid(fileWithExt.getPath());
                }
                //If cancelled previous operation or i the file did not exist
            }else {
                Controller.getVideoProcessor().savePlanarCorrectionVid(fileWithExt.getPath());
            }
        }
    }
}
