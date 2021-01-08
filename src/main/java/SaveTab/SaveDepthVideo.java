package SaveTab;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

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
        //Save button
        saveButton=new JButton("Save Depth Corrected Video");
        saveButton.addActionListener(this);
        JPanel panel=new JPanel();
        panel.add(saveButton);

        add(panel, BorderLayout.CENTER);
    }

    @Override

    public void actionPerformed(ActionEvent e) {
        int userSelection= chooser.showSaveDialog(SaveDepthVideo.this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = chooser.getSelectedFile();
            log.append("Saving: "+ fileToSave.getName());
            System.out.println("Save as file: " + fileToSave.getAbsolutePath());
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