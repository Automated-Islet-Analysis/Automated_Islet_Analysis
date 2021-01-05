package UI;
import com.sun.media.jai.codec.*;

import javax.media.jai.NullOpImage;
import javax.media.jai.OpImage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.Timer;


class TiffListener implements ActionListener {
    @Override

    public void actionPerformed(ActionEvent e) {
        // Get filename
        String filepath = Uploaded.filePath;
        File file = new File(filepath);

        SeekableStream stream = null;
        try {
            stream = new FileSeekableStream(file);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        final TIFFDecodeParam param = new TIFFDecodeParam();
        final ImageDecoder dec = ImageCodec.createImageDecoder("tiff", stream, param);

        int numImages = 0;
        try {
            numImages = TIFFDirectory.getNumDirectories(stream);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        // Create a timer to display each frame of the TIFF at time intervals
        int speed = 1000/18; // 18 frames per second
        int finalNumImages = numImages;
        Timer timer = new Timer(speed, null);
        timer.addActionListener(new ActionListener() {
            int imageToLoad = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                imageToLoad++;
                RenderedImage renderedImg = null;

                if (imageToLoad >= finalNumImages){
                    timer.stop(); // stop timer when all images have been displayed
                } else {
                    try {
                        renderedImg = new NullOpImage(dec.decodeAsRenderedImage(imageToLoad), null, OpImage.OP_IO_BOUND, null);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }

                NewTiffReader tiffReader = new NewTiffReader(renderedImg);
                // Display the image
                Uploaded.imgButton.setIcon(tiffReader.getImg());
                Uploaded.imgButton.setVisible(true);
            }
        });

        timer.setInitialDelay(0);
        timer.start();

    }

}
