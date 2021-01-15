/**
 * ActionListener that enables the user to choose a file to upload (only tif/tiff format) and initiates the change
 * of displayed panel.
 *
 * @author Team Automated analysis of "islet in eye", Bioengineering department, Imperial College London
 *
 * Last modified: 11/01/2021
 */

package UI.HomeTab;

import UI.Controller;
import ij.ImagePlus;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;


public class UploadListener implements ActionListener {

    @Override
    // Action initiated when Upload button is clicked
    public void actionPerformed(ActionEvent e) {
        // Popup. Choose which file to upload
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "TIFF Images", "tif", "tiff");
        chooser.setFileFilter(filter);
        chooser.showOpenDialog(Controller.getInterframe());

        // Display the name of the file
        JLabel fileName = Uploaded.getFileName();
        fileName.setText(chooser.getSelectedFile().getName());
        fileName.setFont(new Font(fileName.getFont().getName(), Font.PLAIN, 20));
        Uploaded.setFileName(fileName);
        Controller.getUpload().setVideo(null);

        // Save the path of the file
        File file = chooser.getSelectedFile();
        if(!(new ImagePlus(file.getAbsolutePath())==null)) {
            Uploaded.setFilePath(file.getAbsolutePath());

            // Refresh the frame display
            Controller.setDisplay("Upload");
            Controller.setFileUploaded(true);
            Controller.setDisplay();
        }else{
            JOptionPane.showMessageDialog(Controller.getInterframe(), "The upload was unsuccessful, check if the file is corrupted!");
        }
    }
}
