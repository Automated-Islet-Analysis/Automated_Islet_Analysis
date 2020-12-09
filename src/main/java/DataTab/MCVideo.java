package DataTab;

import javax.swing.*;

public class MCVideo extends JPanel {
    JLabel welcome;

    public MCVideo(){
        welcome = new JLabel("This is the Motion corrected video");
        welcome.setHorizontalAlignment(JLabel.CENTER);
        welcome.setSize(400,100);
        add(welcome);
    }
}
