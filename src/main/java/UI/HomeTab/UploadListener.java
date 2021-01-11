package UI.HomeTab;

import UI.Controller;

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
        // Later change to video formats
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "TIFF Images", "tif", "tiff");
        chooser.setFileFilter(filter);
        chooser.showOpenDialog(null);

        // Display the name of the file
        JLabel fileName = Uploaded.getFileName();
        fileName.setText(chooser.getSelectedFile().getName());
        fileName.setFont(new Font(fileName.getFont().getName(), Font.PLAIN, 20));
        Uploaded.setFileName(fileName);
        Controller.getUpload().setVideo(null);

        // Save the path of the file
        File file = chooser.getSelectedFile();
        Uploaded.setFilePath(file.getAbsolutePath());

        // Refresh the frame display
        Controller.setDisplay("Upload");
        Controller.setFileUploaded(true);
        Controller.setDisplay();
    }
}
