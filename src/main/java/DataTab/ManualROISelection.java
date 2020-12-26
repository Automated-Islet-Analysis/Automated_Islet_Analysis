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
    // 
    Dimension size = new Dimension();
    private double scalingOfImg = 0.65;


    private LinkedList<int[]> newROICoor = new LinkedList();

    private JLabel img;
    private MouseListener mouseListener = new MouseListener() {
        @Override
        public void mousePressed(MouseEvent e) {
            int  x,y;
            x = e.getX();
            y = e.getY();
            Graphics g = img.getGraphics();
            g.setColor(Color.red);
            g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 12));
            drawCenteredString(Integer.toString(numROI++),x,y,g);
            int[] coor = new int[2];
            coor[0]=x;
            coor[1]=y;
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

    public ManualROISelection(BufferedImage image, int numROI) {
        this.numROI = numROI;
        this.image = image;
        size.setSize(image.getWidth()*scalingOfImg, image.getHeight()*scalingOfImg);
        image = resizeImage(image,size.width,size.height);
        img = new JLabel(new ImageIcon(image));
        img.addMouseListener(mouseListener);
    }

    public LinkedList<int[]> run() {
        JFrame f = new JFrame("Select new ROIs");
        f.setLayout(new FlowLayout(FlowLayout.LEFT));

        f.add(img,BorderLayout.PAGE_START);

        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(2,1));
        JButton jButton = new JButton("Confirm");
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewROIS();
            }
        });
        jButton.setSize(100,30);
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

        f.setSize(size.width+105,size.height);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);
        f.setVisible(true);

        return newROICoor;
    }


    private BufferedImage resizeImage(BufferedImage imgIn,int w,int h){
        BufferedImage resizedImg = new BufferedImage(w,h,BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(imgIn, 0, 0, w,h, null);
        g2.dispose();
        return resizedImg;
    }

    // Adapted From http://www.java2s.com/Tutorial/Java/0261__2D-Graphics/Centertext.html
    public void drawCenteredString(String s, int x, int y, Graphics g) {
        FontMetrics fm = g.getFontMetrics();
        x = x - fm.stringWidth(s) / 2;
        y = y + fm.getAscent()/2;//  (fm.getAscent() + (h - (fm.getAscent() + fm.getDescent())) / 2);
        g.drawString(s, x, y);
    }

    private void addNewROIS(){
        // Best would be to add ROI directly on videoProcessor
    }
}