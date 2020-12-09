import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;


public class Main extends Frame {

    public static void main( final String[] args ) {

        //Create Frame
        JFrame interframe = new JFrame("Interface");
        interframe.setSize(600,600);

        interframe.addWindowListener(new WindowAdapter() {// Closes the program if close window clicked
            public void windowClosing(WindowEvent e) { interframe.dispose();
            } });

        MainMenu mainMenu = new MainMenu();
        interframe.setJMenuBar(mainMenu);

        interframe.setLayout(new GridLayout(1,1));

        Home myhome = new Home();
        //myhome = getHomePanel();
        interframe.add(myhome);

        interframe.setVisible(true);
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
