import videoprocessing.Video;
import videoprocessing.VideoProcessor;

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


class UploadListener implements ActionListener {

    public static String filePath;


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
        Uploaded.filename.setText(chooser.getSelectedFile().getName());
        Uploaded.filename.setFont(new Font(Uploaded.filename.getFont().getName(), Font.PLAIN, 20));

        // Save the path of the file
        File file = chooser.getSelectedFile();
        filePath = file.getAbsolutePath();


        NewTiffReader tiffReader =  new NewTiffReader(filePath);
        Uploaded.imgPanel.removeAll(); // If updating, remove previous image
        Uploaded.imgPanel.add(tiffReader.getImgPanel());

        // Refresh the frame display
        Controller.display = "Upload";
        Controller.setDisplay();



    }
    public static String getFile(){
        return filePath;
    }





}
