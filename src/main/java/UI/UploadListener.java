package UI;

import javax.media.jai.JAI;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.RenderedImage;
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

        // Save the path of the file
        File file = chooser.getSelectedFile();
        Uploaded.setFilePath(file.getAbsolutePath());

        // Create rendered img and display it
        RenderedImage src = JAI.create("fileload", Uploaded.getFilePath());
        NewTiffReader tiffReader =  new NewTiffReader(src);

        Uploaded.imgButton.removeAll(); // If updating, remove previous image
        Uploaded.imgButton.setIcon(tiffReader.getImg());

        // Refresh the frame display
        Controller.display = "Upload";
        Controller.setDisplay();
    }
}
