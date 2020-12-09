import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;


public class Main extends Frame {

    public static void main( final String[] args ) {
        //Create Frame
        JFrame interframe = new JFrame("Interface");
        interframe.setSize(600,600);
        interframe.setVisible(true);

        MainMenu mainMenu = new MainMenu();
        interframe.setJMenuBar(mainMenu);

        //Welcome Message
        final JLabel welcomeM=new JLabel();
        welcomeM.setHorizontalAlignment(JLabel.CENTER);
        welcomeM.setSize(400,100);
        String WelcomeMessage = "Welcome on your image anaylsis interface";
        welcomeM.setText(WelcomeMessage);


        /*
        //Call corresponding class on dropdown menu
        cb2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox<String> comboBox=(JComboBox<String>) e.getSource();
                String selectedPage=(String) comboBox.getSelectedItem();

                //popup to select path
                if (selectedPage.equals("Upload")){
                    JFileChooser chooser = new JFileChooser();
                    chooser.showOpenDialog(null);
                    final File f = chooser.getSelectedFile();
                    String filename = f.getAbsolutePath();
                    if (f == null) {
                        return;
                    }

                    //from file choose, open TiffReader to display it 
                    try {
                        final TiffReader window= new TiffReader( filename );
                        // window.pack();
                        //window.show();
                    } catch (final java.io.IOException ioe) {
                        System.out.println( ioe );
                    }

                }
            }
        });*/

    }


}
