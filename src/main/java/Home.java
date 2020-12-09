import javax.swing.*;
import javax.swing.event.MenuListener;
import java.awt.event.ActionListener;

public class Home extends JPanel {
    JLabel welcome;

    public Home(){
        welcome = new JLabel("Welcome!");
        welcome.setHorizontalAlignment(JLabel.CENTER);
        welcome.setSize(400,100);
        add(welcome);
    }
}