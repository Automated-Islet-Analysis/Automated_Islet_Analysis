// Source https://coderanch.com/t/492739/java/mouse-click-image-draw-point
// Modified on 22/12/2020

package DataTab;

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
    // Dimensions of video
    Dimension size = new Dimension();
    // Scaling of video used for display so that it fits on screen
    private double scalingOfImg = 0.65;
    // Stores coordinates of selected ROIs
    private LinkedList<int[]> newROICoor = new LinkedList();
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
            g.setColor(Color.red);
            g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 12));
            drawCenteredString(Integer.toString(numROI++),x,y,g);

            // Add coordinates of new ROI
            int[] coor = new int[2];
            coor[0]= (int) Math.floor(x/scalingOfImg);
            coor[1]= (int) Math.floor(y/scalingOfImg);
            newROICoor.add(coor);
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
    public ManualROISelection(BufferedImage image, int numROI) {
        // Set variables needed for display
        this.numROI = numROI;
        this.image = image;
        size.setSize(image.getWidth()*scalingOfImg, image.getHeight()*scalingOfImg);

        // Resize image otherwise img is too big
        image = resizeImage(image,size.width,size.height);
        img = new JLabel(new ImageIcon(image));
        // Add Mouse listener to img
        img.addMouseListener(mouseListener);
    }

    // Open pop up and allow selection of new ROI and save them or disregard the changes
    public LinkedList<int[]> run() {
        // Pop-up window
        JFrame f = new JFrame("Select new ROIs");
        f.setLayout(new FlowLayout(FlowLayout.LEFT));

        // Add JFrame that hold image
        f.add(img,BorderLayout.PAGE_START);

        // Create JPanel to hold buttons in gridlayout
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(2,1));
        // Button to confirm the newly added ROI/Cells
        JButton jButton = new JButton("Confirm");
        // Add action listener
        jButton.addActionListener(new ActionListener() {
            @Override
            // Save newly selected ROI/Cells to the videoProcessor
            public void actionPerformed(ActionEvent e) {
                addNewROIS();
                f.dispose();
            }
        });
        jButton.setSize(100,30);
        // Button to cancel the addition of new ROIs/cells
        JButton jButton1 = new JButton("Cancel");
        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.dispose();
            }
        });
        jButton1.setSize(100,30);
        jPanel.add(jButton);
        jPanel.add(jButton1);
        f.add(jPanel,BorderLayout.WEST);

        // Display pop-up
        f.setSize(size.width+105,size.height);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);
        f.setVisible(true);

        return newROICoor;
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
        // Call videoProcessor.addCells(newROICoor);
        // Calle videoProcessor.saveCells();
    }
}