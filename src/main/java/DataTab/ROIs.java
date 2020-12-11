package DataTab;

import javax.swing.*;
import java.awt.*;

public class ROIs extends JPanel {
    JLabel header, body;

    public ROIs(){
        // Layout when there has been no video uploaded
        header = new JLabel("No video has been uploaded yet");
        header.setFont(new Font(header.getFont().getName(), Font.PLAIN, 30));
        header.setHorizontalAlignment(SwingConstants.CENTER);

        body = new JLabel("Please go back to the Home tab and upload the desired video.");

        setLayout(new FlowLayout(FlowLayout.CENTER, 500, 200));
        add(header);
        add(body);
    }
}
