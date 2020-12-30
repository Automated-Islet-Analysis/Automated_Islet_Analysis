import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class AnalyseListener implements ActionListener {
    JPanel mainPanel;

    JLabel error;
    JTextField perError;
    JCheckBox checkPlanar;
    JCheckBox checkDepth;
    JCheckBox checkROI;

    Object[] options = {"Ok", "Help", "Cancel"};

    public AnalyseListener(){
        mainPanel = new JPanel(new GridLayout(3,2));
        error = new JLabel("Allowed CS error (%)");
        perError = new JTextField(5);
        checkPlanar = new JCheckBox("Planar motion correction");
        checkDepth = new JCheckBox("Depth motion correction");
        checkROI = new JCheckBox("Find ROIs");

        mainPanel.add(error);
        mainPanel.add(perError);
        mainPanel.add(checkPlanar);
        mainPanel.add(checkROI);
        mainPanel.add(checkDepth);

        perError.setName("perError");
        checkPlanar.setName("checkPlanar");
        checkDepth.setName("checkDepth");
        checkROI.setName("checkROI");
    }

    @Override

    public void actionPerformed(ActionEvent e) {

        // Popup.
        JOptionPane.showOptionDialog(null, mainPanel, "Customise analysis",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                options, null);



    }
}
