package UI.SaveTab;

import UI.Controller;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import ij.ImagePlus;
import ij.io.FileInfo;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;


import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class SaveDepthVideo extends JPanel implements ActionListener {

    JButton saveButton;
    JTextArea log;
    JFileChooser chooser;

    public SaveDepthVideo(){
        //create the file chooser
        chooser= new JFileChooser();

        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "TIFF Images", "tif", "tiff");
        chooser.setFileFilter(filter);

    }

    @Override

    public void actionPerformed(ActionEvent e) {
        int userSelection= chooser.showSaveDialog(SaveDepthVideo.this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = chooser.getSelectedFile();
            Controller.getVideoProcessor().saveDepthCorrectionVid(fileToSave.getPath()+".tif");
            


           /* BufferedImage bufferedImage=videoToSave.getBufferedImage();
            WritableRaster raster = bufferedImage .getRaster();
            DataBufferByte data   = (DataBufferByte) raster.getDataBuffer();
           byte dataFile[]= data.getData();
            //Files.readAllBytes(fileToSave.toPath());
            try (OutputStream out = Files.newOutputStream(fileToSave.toPath(), CREATE)) {
                {
                    //out.write(dataFile, 0, dataFile.length);
                    //out.write(bufferedImage, OutputFormat.TIFF, )
                }
            } catch (IOException ioException) {
            ioException.printStackTrace();
        }*/


        }
    }
}
    //cite here for this part: https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing/examples/components/FileChooserDemoProject/src/components/FileChooserDemo.java
    //protected static ImageIcon createImageIcon(String path){
    //    java.net.URL imgURL= SaveDepthVideo.class.getResource(path);
    //    if (imgURL != null) {
    //    return new ImageIcon(imgURL);
    //    } else {
    //        System.err.println("Couldn't find file: " + path);
    //        return null;
      //  }
    //}

