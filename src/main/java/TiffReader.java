//import com.sun.media.jai.codec.*;
//import sun.awt.image.ImageDecoder;
//
//import javax.media.jai.NullOpImage;
//import javax.media.jai.OpImage;
//import javax.media.jai.widget.ImageCanvas;
//import javax.swing.*;
//import java.awt.*;
//import java.awt.image.BufferedImage;
//import java.awt.image.ColorModel;
//import java.awt.image.RenderedImage;
//import java.awt.image.WritableRaster;
//import java.io.File;
//import java.io.IOException;
//import java.util.Hashtable;
//
//public class TiffReader extends Frame {
//    private static final long serialVersionUID = 1L;
//    public TiffReader(final String filename) throws IOException {
//
//        /*//Create Frame
//        JFrame frame = new JFrame("Interface");
//
//
//
//        //Dropdown menu 1
//        String Home[]={"HomeTab","Upload"};
//        final JComboBox cb2=new JComboBox(Home);
//        //Dropdown menu 2
//        String Data[]={"DataTab.ROIs","Motion Corrected Video","Data"};
//        final JComboBox cb1=new JComboBox(Data);
//
//        //Drop
//
//        setLayout( null );
//        setTitle( " TIFF Reader" );
//        final File file = new File( filename );
//        final SeekableStream stream = new FileSeekableStream( file );
//        final TIFFDecodeParam param = null;
//        final ImageDecoder dec = (ImageDecoder) ImageCodec.createImageDecoder( "tiff", stream, param );
//        // Which of the multiple images in the TIFF file do we want to load
//        // 0 refers to the first, 1 to the second and so on.
//        final int imageToLoad = 2;
//        final RenderedImage renderedTiffChqImg =
//                new NullOpImage( dec.decodeAsRenderedImage( imageToLoad ), null, OpImage.OP_IO_BOUND, null );
//        BufferedImage chqBufferedImg =
//                new BufferedImage( renderedTiffChqImg.getWidth(), renderedTiffChqImg.getHeight(),
//                        BufferedImage.TYPE_BYTE_GRAY );
//        chqBufferedImg = convertRenderedImgToBuffImg( renderedTiffChqImg );
//        final JPanel addPanel = new JPanel();
//        addPanel.setLayout( null );
//        addPanel.setBounds( 100, 100, 800, 400 );
//        add( addPanel );
//        final ImageCanvas chqImgCanvas = new ImageCanvas( chqBufferedImg );
//        chqImgCanvas.setBounds( 50, 50, 800, 600 );
//        final Graphics g = chqBufferedImg.createGraphics();
//        final int imageW = addPanel.getWidth();
//        final int imageH = addPanel.getHeight();
//        g.drawImage( chqBufferedImg, 50, 50, imageW, imageH, null, null );
//        addPanel.add( chqImgCanvas );
//        setSize( 120, 120 );
//        setUndecorated( true );
//
//
//        cb1.setBounds(0,0,90,20);
//        frame.add(cb1);
//        frame.setSize(500,500);
//        frame.add(addPanel);
//        frame.setVisible(true);*/
//    }
//    public BufferedImage convertRenderedImgToBuffImg( final RenderedImage chqBufferedImg ) {
//        if (chqBufferedImg instanceof BufferedImage) {
//            return (BufferedImage) chqBufferedImg;
//        }
//        final ColorModel cm = chqBufferedImg.getColorModel();
//        final int width = chqBufferedImg.getWidth();
//        final int height = chqBufferedImg.getHeight();
//        final WritableRaster raster = cm.createCompatibleWritableRaster( width, height );
//        final boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
//        final Hashtable<String[], Object> properties = new Hashtable<>();
//        final String[] keys = chqBufferedImg.getPropertyNames();
//        if (keys != null) {
//            for (int i = 0; i < keys.length; i++) {
//                properties.put( keys, chqBufferedImg.getProperty( keys[i] ) );
//            }
//        }
//        final BufferedImage result = new BufferedImage( cm, raster, isAlphaPremultiplied, properties );
//        chqBufferedImg.copyData( raster );
//        return result;
//    }
//    public static void main( final String[] args ) {
//        final String filename = "/Users/sachamaire/Desktop/130318_cg38_0R2L_afterIVinsulin-1.tif";
//        try {
//            final TiffReader window= new TiffReader( filename );
//            // window.pack();
//            //window.show();
//        } catch (final java.io.IOException ioe) {
//            System.out.println( ioe );
//        }
//    }
//}