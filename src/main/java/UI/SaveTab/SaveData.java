/**
 * Pop-up for letting the user create a folder and specifically save the data.
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

    public SaveData(){
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

        int userSelection= showSaveDialog(SaveData.this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File folder = getSelectedFile();
            //Check if the file already exists and let the user choose whether to overwrite it or cancel
            if(!folder.isDirectory()) {
                Object[] options = {"Ok"};
                JOptionPane.showOptionDialog(Controller.getInterframe(), "Please input an existing directory file name!",
                        "Warning",
                        JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
            }else {
                File MIFolder = new File(folder.getAbsoluteFile()+"/mean_intensity_measurements");
                if(MIFolder.exists())
                    MIFolder.delete();
                MIFolder.mkdir();
                Controller.getVideoProcessor().saveCellsMeanIntensity(MIFolder.getAbsolutePath());
                Controller.getVideoProcessor().saveSummary(folder.getPath()+"/Summary.csv");
            }
        }
    }
}
