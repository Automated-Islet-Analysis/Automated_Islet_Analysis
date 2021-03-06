/**
 * Panel used for display of dynamic images.
 *
 * @author Team Automated analysis of "islet in eye", Bioengineering department, Imperial College London
 *
 * Last modified: 11/01/2021
 */

package UI.Panel;

import javax.swing.*;
import java.awt.image.BufferedImage;

public abstract class ImagePanel extends DynamicPanel {
    // Hold image for display
    protected JLabel imgDisp;
    // Image for display
    protected BufferedImage image;
    // ImageIcon of imgDisp
    protected ImageIcon imgIcon;

    // Constructor
    public ImagePanel(int marginHorizontal, int marginVertical){
        this.marginHorizontal=marginHorizontal;
        this.marginVertical=marginVertical;
        imgIcon = new ImageIcon();
        imgDisp = new JLabel();
    }

    // Function to updatePanel panel if needed
    public abstract void updatePanel();

}
