import DataTab.*;
import SaveTab.*;
import DataTab.ROIs;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class Main extends Frame {

    public static void main( final String[] args ) {
        Controller controller = new Controller();
        controller.setVisible(true);

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
