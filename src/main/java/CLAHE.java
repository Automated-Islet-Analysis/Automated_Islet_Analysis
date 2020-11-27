import com.sun.org.apache.xml.internal.security.algorithms.Algorithm;
import org.opencv.imgproc;
import org.w3c.dom.Document;

public class CLAHE extends Algorithm {


    public CLAHE(Document doc, String algorithmURI) {
        super(doc, algorithmURI);
    }

    @Override
    public String getBaseLocalName() {
        return null;
    }
}
