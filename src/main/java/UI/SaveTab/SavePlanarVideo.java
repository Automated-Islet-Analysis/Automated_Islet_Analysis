package UI.SaveTab;

import UI.Controller;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class SavePlanarVideo extends JPanel implements ActionListener {

    JButton saveButton;
    JTextArea log;
    JFileChooser chooser;
    private static JLabel msg;

    public SavePlanarVideo(){
        //create the file chooser
        chooser= new JFileChooser();

        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "TIFF Images", "tif", "tiff");
        chooser.setFileFilter(filter);

    }

    @Override

    public void actionPerformed(ActionEvent e) {
        int userSelection= chooser.showSaveDialog(SavePlanarVideo.this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = chooser.getSelectedFile();
            if(fileToSave.exists() && !fileToSave.isDirectory()) {
                JCheckBox check = new JCheckBox("Warning");
                Object[] options = {"Yes", "No, overwrite"};
                int x = JOptionPane.showOptionDialog(null, "This file already exist. Do you want to change to change its name?]",
                        "Warning",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);

                if (check.isSelected() && x ==1) {
                    Controller.getVideoProcessor().savePlanarCorrectionVid(fileToSave.getPath()+".tif");
                }
            }
        }

    }
    //cite here for this part: https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing/examples/components/FileChooserDemoProject/src/components/FileChooserDemo.java
    protected static ImageIcon createImageIcon(String path){
        java.net.URL imgURL= SaveDepthVideo.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }


}
