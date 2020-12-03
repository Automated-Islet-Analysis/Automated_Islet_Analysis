import org.itk.simple.ElastixImageFilter;
import org.itk.simple.Image;
import org.itk.simple.ParameterMap;
import org.itk.simple.SimpleITK;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.CLAHE;
import org.opencv.imgproc.Imgproc;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VideoProcessor extends Video{
    public VideoProcessor(Video video){
        super(video.filename);
        this.alignedFrames = video.alignedFrames;
    }

    // Perform motion correction on frames
    public void motionCorrect(){
        if (numberOfFrames<1){
            System.out.println("Error: motion correction on 0 frames");
        }
        else {

            ParameterMap parMap = SimpleITK.getDefaultParameterMap("translation");
            //parMap["ResultImageFormat"] = ['tif'];

            ElastixImageFilter fil = new ElastixImageFilter();
            fil.setFixedImage(frames.get(0));
            fil.setParameterMap(parMap);
            fil.printParameterMap();

            Image img = new Image();
            String msg;
            msg = img.getPixelIDTypeAsString();
            System.out.println(msg);

            for (int i=1; i<numberOfFrames ; i++){ /***/
                fil.setMovingImage(frames.get(i));
                fil.execute();

                Image out = new Image();
                out = fil.getResultImage();
                alignedFrames.add(out);

            }
        }
    }

    public static Mat[] applyCLAHE(Mat img, Mat L) {
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
