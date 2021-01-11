package UI.Panel;

import UI.Controller;
import ij.ImagePlus;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class ImagePanel extends DynamicPanel {
    // Hold image for display
    protected JLabel imgDisp;
    // Image for display
    protected BufferedImage image;
    // ImageIcon of imgDisp
    protected ImageIcon imgIcon;

    public ImagePanel(){
        imgIcon = new ImageIcon();
        imgDisp = new JLabel();
    }

    public abstract void updatePanel();

}
