import javax.imageio.ImageIO;
import javax.media.jai.RenderedOp;
import javax.media.jai.widget.ScrollingImagePanel;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;


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

        Uploaded.vidDisp.removeAll(); // If updating, remove previous image
        Uploaded.vidDisp.setIcon(tiffReader.getImg());

        // Refresh the frame display
        Home.display = "Upload";
        Home.fileUploaded=true;
        Home.setDisplay();
    }
}