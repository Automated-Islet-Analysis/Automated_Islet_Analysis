/**
 * Class that is used to show a pop-up where you can manually select ROIs. After confirming the new ROIs,
 * these are added to the cells of the videoProcessor.
 *
 * @author Team Automated analysis of "islet in eye", Bioengineering department, Imperial College London
 *
 * Last modified: 11/01/2021
 */

package UI.DataTab;

import UI.Controller;
import UI.Panel.ImagePanel;
import videoprocessing.Cell;
import videoprocessing.VideoProcessor;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import javax.swing.*;


public class ManualROISelection extends ImagePanel {
    // Stores number of ROI/cells shown on display
    private int numROI=0;
    private int cellSize;

    // Stores coordinates of selected ROIs
    private LinkedList<Cell> newCells = new LinkedList<>();

    // variable for display
    JDialog dialog=new JDialog();
    JPanel buttonPanel = new JPanel();
    JButton jButton = new JButton("Confirm");
    JButton jButton1 = new JButton("Cancel");

    // Mouse listeners that listens to mousePressed on JLabel imgDisp only
    // Source https://coderanch.com/t/492739/java/mouse-click-image-draw-point
    // Modified on 22/12/2020
    private MouseListener mouseListener = new MouseListener() {
        @Override
        public void mousePressed(MouseEvent e) {
            // get pixel coordinates on JLabel img
            int  x,y;
            x = e.getX();
            y = e.getY();

            // Add coordinates of new ROI
            int[] coor = new int[2];
            coor[0]= (int) Math.floor(x/scalingOfImg);
            coor[1]= (int) Math.floor(y/scalingOfImg);
            newCells.add(new Cell(coor,numROI+newCells.size()+1,0,cellSize));

            updatePanel();
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

    // Window listener to update image when window changes size
    private ComponentListener componentListener = new ComponentListener() {
        @Override
        public void componentResized(ComponentEvent e) {
            updatePanel();
        }
        @Override
        public void componentMoved(ComponentEvent e) { }
        @Override
        public void componentShown(ComponentEvent e) { }
        @Override
        public void componentHidden(ComponentEvent e) { }
    };

    // Constructor
    public ManualROISelection(BufferedImage image, int numROI,int cellSize) {
        super(15,90);

        // Set variables needed for display
        this.image = new BufferedImage(image.getWidth(),image.getHeight(),BufferedImage.TYPE_INT_RGB);
        Graphics g = this.image.getGraphics();
        g.setColor(Color.BLUE);
        g.drawImage(image,0,0,null);
        this.numROI = numROI;
        this.cellSize=cellSize;

        // Define pop-up window
        dialog = new JDialog(Controller.getInterframe(),"Select new ROIs by clicking on the image!",true);
        dialog.setSize(Controller.getInterframe().getSize());
        dialog.setLocationRelativeTo(Controller.getInterframe());
        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Set panel layout
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));

        // Setup buttons and button pane
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        // Button to confirm the newly added ROI/Cells
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
        // Add action listener to close pop-up
        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        jButton1.setSize(100,30);

        imgDisp.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        // Resize image otherwise img is too big
        image = resizeImage(image,dialog);
        imgDisp.setIcon(new ImageIcon(image));

        // Add Listeners
        imgDisp.addMouseListener(mouseListener);
        dialog.addComponentListener(componentListener);
    }

    // Open pop up and allow selection of new ROI and save them or disregard the changes
    public LinkedList<Cell> run() {
        // Add JLabel that hold image
        add(imgDisp,BorderLayout.WEST);

        // Add buttons
        buttonPanel.add(jButton);
        buttonPanel.add(jButton1);
        add(buttonPanel);

        // Show dialog
        dialog.setContentPane(this);
        dialog.setVisible(true);
        return newCells;
    }

    @Override
    // Make component in frame visible
    public void updatePanel() {
        // Reset displayed image to original image
        BufferedImage image_ = resizeImage(this.image,dialog);

        BufferedImage image = new BufferedImage(image_.getWidth(),image_.getHeight(),BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.setFont(new Font(this.image.getGraphics().getFont().getFontName(),Font.BOLD,14));
        g.setColor(Color.BLUE);
        g.drawImage(image_,0,0,null);

        int n=0;
        for(Cell cell : newCells) {
            int coor[] = cell.getCoor();
            System.out.println(coor[0] * scalingOfImg+","+coor[1]* scalingOfImg);
            drawCenteredString(Integer.toString(numROI+n+1), (int) Math.round(coor[0] * scalingOfImg), (int) Math.round(coor[1] * scalingOfImg), g);
            n++;
        }
        imgIcon = new ImageIcon(image);
        imgDisp.setIcon(imgIcon);
    }


    // Draw cell numbers on GUI
    // Adapted From http://www.java2s.com/Tutorial/Java/0261__2D-Graphics/Centertext.html
    public void drawCenteredString(String s, int x, int y, Graphics g) {
        FontMetrics fm = g.getFontMetrics();
        x = x - fm.stringWidth(s) / 2;
        y = y + fm.getAscent()/2;
        g.drawString(s,x, y);
    }

    // Add manually selected ROI to VideoProcessor
    private void addNewROIS(){
        VideoProcessor videoProcessor= Controller.getVideoProcessor();
        videoProcessor.addCells(newCells);
        videoProcessor.createROIImage();
        Controller.setVideoProcessor(videoProcessor);

        ROIs rois = new ROIs();
        rois.updatePanel();
        Controller.getInterframe().setContentPane(rois);
        Controller.getInterframe().invalidate();
        Controller.getInterframe().validate();
        if(newCells.size()>0)Controller.setMeanIntensityMeasured(false);
    }
}
