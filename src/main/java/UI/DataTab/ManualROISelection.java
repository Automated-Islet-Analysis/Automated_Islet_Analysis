/**
 * Class that is used to show a pop-up where you can manually select ROI. After confirming the new ROI,
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

    // Variable for display
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
            Graphics g = image.getGraphics();

            // Draw number of selected new ROI
            g.setColor(Color.blue);
            g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 12));
            drawCenteredString(Integer.toString(numROI++ + 1),(int)Math.round(x/scalingOfImg),(int)Math.round(y/scalingOfImg),g);

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
        super();

        this.image=image;
        // Pop-up window
        dialog = new JDialog(Controller.getInterframe(),"Select new ROIs",true);
        dialog.setSize(Controller.getInterframe().getSize());

        // Set variables needed for display
        this.numROI = numROI;
        this.cellSize=cellSize;

        // Resize image otherwise img is too big
        image = resizeImage(image,15,90,dialog);
        imgDisp.setIcon(new ImageIcon(image));

        // Add Mouse listener to img
        imgDisp.addMouseListener(mouseListener);

        // Add component listener to dialog
        dialog.addComponentListener(componentListener);
    }

    // Open pop up and allow selection of new ROI and save them or disregard the changes
    public LinkedList<Cell> run() {
        // Pop-up window
        JDialog dialog = new JDialog(Controller.getInterframe(),"Select new ROIs",true);
        //JFrame f = new JFrame(null,"Select new ROIs");
        setLayout(new FlowLayout(FlowLayout.CENTER));

        // Add JLabel that hold image
        add(imgDisp,BorderLayout.PAGE_START);

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

        // Setup buttonPanel
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(jButton);
        buttonPanel.add(jButton1);
        buttonPanel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        add(buttonPanel,BorderLayout.LINE_START);

        dialog.setContentPane(this);
        dialog.setLocationRelativeTo(Controller.getInterframe());
        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dialog.setResizable(true);
        dialog.setVisible(true);

        return newCells;
    }

    @Override
    public void updatePanel() {
        BufferedImage image = resizeImage(this.image,15,90,dialog);
        imgDisp.setIcon(new ImageIcon(image));

        removeAll();
        // Add JLabel that hold image
        add(imgDisp,BorderLayout.PAGE_START);
        add(buttonPanel,BorderLayout.LINE_START);
        this.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        dialog.setContentPane(this);
    }
    // Draw cell numbers on GUI
    // Adapted From http://www.java2s.com/Tutorial/Java/0261__2D-Graphics/Centertext.html
    public void drawCenteredString(String s, int x, int y, Graphics g) {
        g.setFont(new Font(g.getFont().getFontName(),Font.PLAIN,18));
        FontMetrics fm = g.getFontMetrics();
        x = x - fm.stringWidth(s) / 2;
        y = y + fm.getAscent()/2;
        g.drawString(s, x, y);
        updatePanel();
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