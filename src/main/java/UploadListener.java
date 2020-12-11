import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;


class UploadListener implements ActionListener {

    @Override

    public void actionPerformed(ActionEvent e) {

        // Popup. Choose which file to upload
        JFileChooser chooser = new JFileChooser();
        // Later change to video formats
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "JPG & GIF Images", "jpg", "gif");
        chooser.setFileFilter(filter);
        chooser.showOpenDialog(null);

        // Display the name of the file
        Uploaded.filename.setText(chooser.getSelectedFile().getName());
        Uploaded.filename.setFont(new Font(Uploaded.filename.getFont().getName(), Font.PLAIN, 20));

        // Save the path of the file
        final File file = chooser.getSelectedFile();
        String filePath = file.getAbsolutePath();
        if (file == null) {
            return;
        }

        SwingWorker sw = new SwingWorker() {
            ImageIcon imgIcon;
            @Override
            protected Object doInBackground() throws Exception {
                //Scales the image (change dimensions if needed)
                imgIcon = new ImageIcon(scaleImage(350, 350, ImageIO.read(new File(file.getAbsolutePath()))));
                return null;
            }

            @Override
            protected void done() {
                super.done();
                // Display the image
                Uploaded.picture.setIcon(imgIcon);
            }
        };
        sw.execute();

        // Refresh the frame display
        Controller.display = "Upload";
        Controller.setDisplay();
    }

    public static BufferedImage scaleImage(int w, int h, BufferedImage img) throws Exception {
        BufferedImage bufImg;
        bufImg = new BufferedImage(w, h, BufferedImage.TRANSLUCENT);
        Graphics2D g2d = bufImg.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
        g2d.drawImage(img, 0, 0, w, h, null);
        g2d.dispose();
        return bufImg;
    }
}
