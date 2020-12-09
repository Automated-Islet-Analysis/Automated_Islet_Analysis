package DataTab;

import javax.swing.*;

public class Data extends JPanel {
    JLabel welcome;

    public Data(){
        welcome = new JLabel("This is all the data");
        welcome.setHorizontalAlignment(JLabel.CENTER);
        welcome.setSize(400,100);
        add(welcome);
    }
}
