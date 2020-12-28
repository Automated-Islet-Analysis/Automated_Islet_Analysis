package UI.DataTab;

import javax.swing.*;

public class MCVideoPlanar extends JPanel {
    JLabel welcome;

    public MCVideoPlanar(){
        welcome = new JLabel("This is the planar motion corrected video");
        welcome.setHorizontalAlignment(JLabel.CENTER);
        welcome.setSize(400,100);
        add(welcome);
    }
}
