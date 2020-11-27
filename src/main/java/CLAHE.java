
import org.w3c.dom.Document;
import org.opencv.core.Algorithm;
import org.opencv.core.Imgproc;
import java.util.ArrayList;
import java.util.Arrays;

public class CLAHE extends Algorithm {


    private static Mat[] applyCLAHE(Mat img, Mat L) {
        Mat[] result = new Mat[2];
        CLAHE clahe = Imgproc.createCLAHE();
        clahe.setClipLimit(2.0);
        Mat L2 = new Mat();
        clahe.apply(L, L2);
        Mat LabIm2 = new Mat();
        List<Mat> lab = new ArrayList<>();
        Core.split(img, lab);
        Core.merge(new ArrayList<>(Arrays.asList(L2, lab.get(1), lab.get(2))), LabIm2);
        Mat img2 = new Mat();
        Imgproc.cvtColor(LabIm2, img2, Imgproc.COLOR_Lab2BGR);
        result[0] = img2;
        result[1] = L2;
        return result;
    }
}
