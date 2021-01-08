package DataTab;




//import com.sun.media.controls.VFlowLayout;

//import UI.Controller;
//import UI.UserInterface;
//import ij.ImagePlus;
//import videoprocessing.VideoProcessor;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class ROIs extends JPanel {
    private JButton addROI;
    private JPanel subPanel;
    private JLabel text, image;
    private ImageIcon imgIcon;

    private int numROI;
    private BufferedImage imageROI;
//    private ImagePlus imageP;
    private int cellSize;
//    private VideoProcessor videoProcessor;

    public ROIs(){
        setLayout(new FlowLayout(FlowLayout.CENTER, 30, 30));

        // Create elements
        addROI = new JButton("Add ROIs");
        addROI.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                ManualROISelection select = new ManualROISelection(videoProcessor.getRoiImage().getBufferedImage(),numROI,cellSize);
//                select.run();
//                videoProcessor = UserInterface.getVideoProcessor();
            }
        });
        subPanel = new JPanel();
    }

    public void updatePanel(){
        videoProcessor = UserInterface.getVideoProcessor();
        imageP = videoProcessor.getRoiImage();

        if(! (imageP==null)) {
            numROI = videoProcessor.getVideo().getCells().size();
            imageROI = imageP.getBufferedImage();
            imageROI = resizeImage(imageROI, (int) Math.round(imageROI.getWidth() * 0.65), (int) Math.round(imageROI.getHeight() * 0.65));

            cellSize = videoProcessor.getCellSize();

            text = new JLabel("Number of ROIs: " + numROI);
            imgIcon = new ImageIcon(imageROI);
            image = new JLabel(imgIcon);

            removeAll();
            subPanel.removeAll();

            // Add JFrame that hold image
            add(image, BorderLayout.PAGE_START);
            // Create JPanel to hold buttons in gridlayout
            subPanel.setLayout(new GridLayout(2, 1));
            subPanel.add(text);
            subPanel.add(addROI);
            add(subPanel, BorderLayout.PAGE_END);
        }

        else {   // if (imageP==null)
            removeAll();
            // Message to user
            text = new JLabel("Something went wrong. Please try again.");
            text.setFont(new Font(text.getFont().getFontName(),Font.PLAIN,15));
            add(text);
        }

    }

//    // Resize image to fit on display
    private BufferedImage resizeImage(BufferedImage imgIn,int w,int h){
        BufferedImage resizedImg = new BufferedImage(w,h,BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(imgIn, 0, 0, w,h, null);
        g2.dispose();
        return resizedImg;
    }
}