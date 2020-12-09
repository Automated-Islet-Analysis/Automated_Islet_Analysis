import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

class UploadListener implements ActionListener {

    @Override

    public void actionPerformed(ActionEvent e) {

        // Popup. Choose which file to upload
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "JPG & GIF Images", "jpg", "gif");
        chooser.setFileFilter(filter);
        chooser.showOpenDialog(null);
        Uploaded.filename =  chooser.getSelectedFile().getName();

        System.out.println(Uploaded.filename);
        /*if (f == null) {
            return;
        }
        //from file choose, open TiffReader to display it
        try {
            final TiffReader window= new TiffReader( Controller.filename );
                window.pack();
                window.show();
        } catch (final java.io.IOException ioe) {
            System.out.println( ioe );
        }*/

        // Refresh the frame display
        Controller.display = "Upload";
        Controller.setDisplay();
    }
}
