// Source https://coderanch.com/t/492739/java/mouse-click-image-draw-point
// Modified on 22/12/2020

package UI.DataTab;

import UI.Controller;
import UI.HomeTab.Home;
import videoprocessing.Cell;
import videoprocessing.VideoProcessor;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import javax.swing.*;


public class ManualROISelection extends JPanel{
    // Image for display
    BufferedImage image;
    // Stores number of ROI/cells shown on display
    private int numROI=0;
    private int cellSize;
    // Dimensions of video
    Dimension size = new Dimension();
    // Scaling of video used for display so that it fits on screen
    private double scalingOfImg = 0.65;
    // Stores coordinates of selected ROIs
    private LinkedList<Cell> newCells = new LinkedList<>();
    // Hold image for display
    private JLabel img;

    // Mouse listeners that lisstend to mousePressed on JLabel img only
    private MouseListener mouseListener = new MouseListener() {
        @Override
        public void mousePressed(MouseEvent e) {
            // get pixel coordinates on JLabel img
            int  x,y;
            x = e.getX();
            y = e.getY();
            Graphics g = img.getGraphics();

            // Draw number of selected new ROI
            g.setColor(Color.blue);
            g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 12));
            drawCenteredString(Integer.toString(numROI++ + 1),x,y,g);

            // Add coordinates of new ROI
            int[] coor = new int[2];
            coor[0]= (int) Math.floor(x/scalingOfImg);
            coor[1]= (int) Math.floor(y/scalingOfImg);
            newCells.add(new Cell(coor,numROI,0,cellSize));
        }
        @Override
        public void mouseClicked(MouseEvent e  ) {}
        @Override
        public void mouseReleased(MouseEvent e) {}
        @Override
        public void mouseEntered(MouseEvent e) {}
        @Override
        public void mouseExited(MouseEvent e) {}
    };

    // Constructor
    public ManualROISelection(BufferedImage image, int numROI,int cellSize) {
        // Set variables needed for display
        this.numROI = numROI;
        this.image = image;
        this.cellSize=cellSize;
        size.setSize(image.getWidth()*scalingOfImg, image.getHeight()*scalingOfImg);

        // Resize image otherwise img is too big
        image = resizeImage(image,size.width,size.height);
        img = new JLabel(new ImageIcon(image));
        // Add Mouse listener to img
        img.addMouseListener(mouseListener);
    }

    // Open pop up and allow selection of new ROI and save them or disregard the changes
    public LinkedList<Cell> run() {
        // Pop-up window
        JDialog dialog = new JDialog();
//        JFrame f = new JFrame(null,"Select new ROIs");
        setLayout(new FlowLayout(FlowLayout.LEFT));

        // Add JFrame that hold image
        add(img,BorderLayout.PAGE_START);

        // Create JPanel to hold buttons in gridlayout
        JPanel jPanel = new JPanel(new GridLayout(1,2));

        // Button to confirm the newly added ROI/Cells
        JButton jButton = new JButton("Confirm");
        // Add action listener
        jButton.addActionListener(new ActionListener() {
            @Override
            // Save newly selected ROI/Cells to the videoProcessor
            public void actionPerformed(ActionEvent e) {
                addNewROIS();
                dialog.dispose();
            }
        });
        jButton.setSize(100,30);
        // Button to cancel the addition of new ROIs/cells
        JButton jButton1 = new JButton("Cancel");
        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        jButton1.setSize(100,30);
        jPanel.add(jButton);
        jPanel.add(jButton1);
        add(jPanel);

        setSize(size.width+30,size.height+90);
        dialog.setContentPane(this);
        dialog.setSize(this.getSize());
        Home.subPanel.repaint();
        dialog.setLocationRelativeTo(Home.subPanel);
        Home.subPanel.invalidate();
        Home.subPanel.validate();
        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dialog.setResizable(false);
        dialog.setVisible(true);

        return newCells;
    }

    // Resize image to fit on display
    private BufferedImage resizeImage(BufferedImage imgIn,int w,int h){
        BufferedImage resizedImg = new BufferedImage(w,h,BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(imgIn, 0, 0, w,h, null);
        g2.dispose();
        return resizedImg;
    }

    // Draw cell numbers on GUI
    // Adapted From http://www.java2s.com/Tutorial/Java/0261__2D-Graphics/Centertext.html
    public void drawCenteredString(String s, int x, int y, Graphics g) {
        FontMetrics fm = g.getFontMetrics();
        x = x - fm.stringWidth(s) / 2;
        y = y + fm.getAscent()/2;//  (fm.getAscent() + (h - (fm.getAscent() + fm.getDescent())) / 2);
        g.drawString(s, x, y);
    }



    // Add manually selected ROI to VideoProcessor
    private void addNewROIS(){
        VideoProcessor videoProcessor= Home.getVideoProcessor();
        videoProcessor.addCells(newCells);
        videoProcessor.createROIImage();
        Home.setVideoProcessor(videoProcessor);

        ROIs rois = new ROIs();
        rois.updatePanel();
        Home.getInterframe().add(rois);
        Home.getInterframe().invalidate();
        Home.getInterframe().validate();
        if(newCells.size()>0)Home.setMeanIntensityMeasured(false);
    }
}
