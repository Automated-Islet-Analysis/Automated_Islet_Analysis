package UI.DataTab;

import javax.swing.*;

public class MCVideoDepth extends JPanel {
    JLabel welcome;

    public MCVideoDepth(){
        welcome = new JLabel("This is the depth motion corrected video");
        welcome.setHorizontalAlignment(JLabel.CENTER);
        welcome.setSize(400,100);
        add(welcome);
    }
}
