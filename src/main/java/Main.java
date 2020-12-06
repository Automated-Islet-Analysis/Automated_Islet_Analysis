import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Main extends Frame {

    public static void main( final String[] args ) {
        //Create Frame
        JFrame interframe = new JFrame("Interface");

        //Dropdown menu 1
        String Home[]={"Home","Upload"};
        final JComboBox cb2=new JComboBox(Home);
        //Dropdown menu 2
        String Data[]={"ROIs","Motion Corrected Video","Data"};
        final JComboBox cb1=new JComboBox(Data);

        //Dropdown menu 3
        String Save[]={"Save ROIs","Save Data", "Save Analysed Video", "Save All"};
        final JComboBox cb3=new JComboBox(Save);

        //Welcome Message
        final JLabel welcomeM=new JLabel();
        welcomeM.setHorizontalAlignment(JLabel.CENTER);
        welcomeM.setSize(400,100);
        String WelcomeMessage = "Welcome on your image anaylsis interface";
        welcomeM.setText(WelcomeMessage);

        //Set the Drop down menus
        cb2.setBounds(0,0,90,20);
        cb1.setBounds(90,0,90,20);
        cb3.setBounds(180,0,90,20);
        interframe.setSize(500,500);
        interframe.add(cb1);
        interframe.add(cb2);
        interframe.add(cb3);

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
        });

    }


}
