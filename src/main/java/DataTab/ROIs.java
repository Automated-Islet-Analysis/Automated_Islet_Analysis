package DataTab;

import javax.swing.*;

public class ROIs extends JPanel {
    JLabel welcome;

    public ROIs(){
        welcome = new JLabel("These are the ROIs");
        welcome.setHorizontalAlignment(JLabel.CENTER);
        welcome.setSize(400,100);
        add(welcome);
    }
}
