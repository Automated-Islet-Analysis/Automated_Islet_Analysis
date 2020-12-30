import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.*;

// Ref: https://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html
class AnalyseListener implements ActionListener {
    JPanel mainPanel = new JPanel(new GridLayout(3,2));

    JLabel error = new JLabel("Allowed CS Error (%)");
    JTextField perError = new JTextField(5);
    JCheckBox checkPlanar = new JCheckBox("Planar Motion Correction");
    JCheckBox checkDepth = new JCheckBox("Depth Motion Correction");
    JCheckBox checkROI = new JCheckBox("Find ROIs");

    Object[] options = {"ok","help", "cancel"};
    /*JButton ok = new JButton("Ok");
    JButton help = new JButton("Help");
    JButton cancel = new JButton("Cancel");
    JButton[] options = {ok, help, cancel};*/



    @Override

    public void actionPerformed(ActionEvent e) {
        mainPanel.add(error);
        mainPanel.add(perError);
        mainPanel.add(checkPlanar);
        mainPanel.add(checkROI);
        mainPanel.add(checkDepth);

        // Popup
         int n = JOptionPane.showOptionDialog(null, mainPanel, "Customise Analysis",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                options, null);

        if (n == JOptionPane.NO_OPTION){
            System.out.println(n);
            JOptionPane.showMessageDialog(null, "Choose the allowed margin of difference in cross-sectional area of the islet in between frames (suggested value: 10%)");
        }
        // else if JOptionPane.YES_OPTION --> call function that analyses vid!!!
    }



}
