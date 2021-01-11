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

    public void actionPerformed(ActionEvent e) {
        // Popup. Choose which file to upload
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "TIFF Images", "tif", "tiff");
        chooser.setFileFilter(filter);
        chooser.showOpenDialog(Home.getInterframe());

        // Display the name of the file
        JLabel fileName = Uploaded.getFileName();
        fileName.setText(chooser.getSelectedFile().getName());
        fileName.setFont(new Font(fileName.getFont().getName(), Font.PLAIN, 20));
        Uploaded.setFileName(fileName);
        Home.getUpload().setVideo(null);

        // Save the path of the file
        File file = chooser.getSelectedFile();
        if(!(new ImagePlus(file.getAbsolutePath())==null)) {
            Uploaded.setFilePath(file.getAbsolutePath());

            // Refresh the frame display
            Home.setDisplay("Upload");
            Home.setFileUploaded(true);
            Home.setDisplay();
        }else{
            JOptionPane.showMessageDialog(Home.getInterframe(), "The upload was unsuccessful, check if the file is corrupted!");
        }
    }
}