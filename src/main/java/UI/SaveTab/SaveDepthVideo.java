package UI.SaveTab;

import UI.Controller;
import ij.ImagePlus;
import videoprocessing.VideoProcessor;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class SaveDepthVideo extends JPanel implements ActionListener {
    @Override

    public void actionPerformed(ActionEvent e) {

        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "TIFF Images", "tif", "tiff");
        chooser.setFileFilter(filter);
        //chooser.setSelectedFile();
        int userSelection = chooser.showSaveDialog(Controller.interframe);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = chooser.getSelectedFile();
            System.out.println("Save as file: " + fileToSave.getAbsolutePath());
        }

    }
}
