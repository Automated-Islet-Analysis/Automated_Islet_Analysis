/**
 * Splits a stack into multiple substacks. The number of such
 *  substacks is user specified. Each substack may contain only a single
 *  image. Original code modified to change return type of run.
 *
 * @author Patrick Kelly <phkelly@ucsd.edu>
 *
 * Modified on 26th April 01 by Greg Joss  gjoss@rna.bio.mq.edu.au
 * Modified on 30st April 2003 by Bob Cunningham  rcunning@acm.org
 * Modified on 11th January 2021  by Team Automated analysis of "islet in eye", Bioengineering department, Imperial College London
 */


package videoprocessing.ImageJ;

import ij.*;
import ij.process.*;
import ij.plugin.*;
import java.lang.*;
import java.awt.image.*;
import java.text.DecimalFormat;
import java.util.LinkedList;



public class Stack_Splitter implements PlugIn {

    LinkedList<ImagePlus> images = new LinkedList<>();
    // --------------------------------------------------
    /** Splits the stack. */
    @Override
    public void run(String s) {}

    public LinkedList<ImagePlus> run(ImagePlus vid) {

        ImagePlus imp = vid;

        if(imp==null) {
            IJ.noImage();
            return null ;
        }

        // Make sure we have a stack.
        if(imp.getStackSize()==1) {
            IJ.error("SplitStack: Must call this plugin on image stack.");
            return null;
        }

        // Prompt user to enter the number of stacks
        int numStacks = imp.getStackSize();

        // Note, default is to break stack into separate images.
        int numSubStacks = numStacks;
        if(!imp.lock())
            return null;    // exit if in use
        processStack(imp,numSubStacks);
        imp.unlock();
        return images;
    }

    // --------------------------------------------------
    /** Tests to determine if number of substacks evenly divides
     number of slices in stack.  */
    protected boolean numOK(int nss,   // number of substacks
                            int ns     // number of slices in stack
    ) {
        // Sanity check.
        if(nss <= 0 || nss > ns)
            return false;

        // Number must evenly divide.
        return (ns % nss)==0;
    }


    // --------------------------------------------------
    protected void processStack(ImagePlus imp, int numSubStacks) {
        // Determine number of images in each stack.
        int numImages = imp.getStackSize()/numSubStacks;

        if(numImages==1)
            stack2images(imp); // special case 1 image per substack
        else
            stack2stacks(imp,numImages,numSubStacks);
    }

    // --------------------------------------------------
    /** Handle case where each substack consists of a single image
     only. In this case, we wish to generate new images, not new
     stacks.  */
    protected void stack2images(ImagePlus imp) {
        String sLabel = imp.getTitle();

        ImageStack stack = imp.getStack();

        int sz = stack.getSize();
        int currentSlice = imp.getCurrentSlice();  // to reset ***

        DecimalFormat df = new DecimalFormat("0000");         // for title

        for(int n=1;n<=sz;++n) {
            String sImLabel = String.valueOf(n);
            imp.setSlice(n);   // activate next slice ***

            // Get current image processor from stack.  What ever is
            // used here should do a COPY pixels from old processor to
            // new. For instance, ImageProcessor.crop() returns copy.
            ImageProcessor ip = imp.getProcessor(); // ***
            ImageProcessor newip = ip.createProcessor(ip.getWidth(),
                    ip.getHeight());
            newip.setPixels(ip.getPixelsCopy());

            // Create a suitable label, using the slice label if possible
            sImLabel = imp.getStack().getSliceLabel(n);
            if (sImLabel == null || sImLabel.length() < 1) {
                sImLabel = "slice"+df.format(n)+"_"+sLabel;
            }
            // Create new image corresponding to this slice.
            ImagePlus im = new ImagePlus(sImLabel, newip);
            im.setCalibration(imp.getCalibration());

            /** Code changed to save image instead of showing image*/
            images.add(im);

        }

        // Reset original stack state.
        imp.setSlice(currentSlice); // ***
        if(imp.isProcessor()) {
            ImageProcessor ip = imp.getProcessor();
            ip.setPixels(ip.getPixels()); //***
        }
        imp.setSlice(currentSlice);
    }

    // --------------------------------------------------
    /** General case where each substack is a stack. */
    protected void stack2stacks(ImagePlus imp,
                                int numImages, int numSubStacks) {
        DecimalFormat df = new DecimalFormat("0000");   // for title
        String sBaseName = imp.getTitle();              // for title
        String sSliceLabel = "";

        ImageStack stack = imp.getStack();
        int currentSlice = imp.getCurrentSlice();   // to reset ***

        // Would it be better to call imp.getStack().getColorModel()
        // here?
        ColorModel cm = imp.createLut().getColorModel();

        for(int nss=1,index=1; nss<=numSubStacks; ++nss) {
            // New image stack.
            ImageStack ims = new ImageStack(stack.getWidth(),
                    stack.getHeight(),
                    cm);

            // Populate this stack.
            for(int n=1; n<=numImages; ++n, ++index) {      // RWC
                imp.setSlice(index);  // activate next slice *** RWC

                // RWC: Create a label, using the slice label if possible
                sSliceLabel = stack.getSliceLabel(index);
                if (sSliceLabel !=null && sSliceLabel.length() < 1) {
                    sSliceLabel = "slice_"+df.format(n);
                }

                // Get pixels from associated processor. Make sure
                // input pixels are copy of original.
                ims.addSlice(sSliceLabel,  imp.getProcessor().duplicate());
            }

            // Substack name.
            String sStackName = "stk_"+df.format(nss)+"_"+sBaseName;

            // Create new image using this substack.
            ImagePlus nimp = new ImagePlus(sStackName, ims);
            nimp.setCalibration(imp.getCalibration());

            // Display this new substack.
            nimp.show();
        }

        // Reset original stack state.
        imp.setSlice(currentSlice); // ***
        if(imp.isProcessor()) {
            ImageProcessor ip = imp.getProcessor();
            ip.setPixels(ip.getPixels());// ***
        }
        imp.setSlice(currentSlice);

    }


}   // SplitStack class
