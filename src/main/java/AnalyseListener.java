import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class AnalyseListener implements ActionListener {

    @Override

    public void actionPerformed(ActionEvent e) {
        // Popup.
        JOptionPane.showMessageDialog(Controller.interframe, "Analysing data...");
        /*
        Add functionalities: choose parameters for analysis
         */
    }
}
