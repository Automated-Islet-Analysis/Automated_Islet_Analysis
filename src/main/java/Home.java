import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import javax.swing.event.MenuListener;
import java.awt.event.ActionListener;

public class Home extends JPanel {
    // upload button and welcome message

    //Variable declaration
    private javax.swing.JPanel homePanel;
    private javax.swing.JButton jbUpload;
    private javax.swing.JTextField jtext_welcome;



    public Home(){
        homePanel = new JPanel();
        jbUpload = new JButton();
        init_home();
    }

    private void init_home(){
        homePanel.setLayout(new GridLayout(4,1));
        homePanel.add(jbUpload);

        //Welcome Message
        final JLabel welcomeM=new JLabel();
        welcomeM.setHorizontalAlignment(JLabel.CENTER);
        welcomeM.setSize(400,100);
        String WelcomeMessage = "Welcome to your image analysis interface";
        welcomeM.setText(WelcomeMessage);
    }

    /*public JPanel getHomePanel() {
        return homePanel;
    }*/
}
