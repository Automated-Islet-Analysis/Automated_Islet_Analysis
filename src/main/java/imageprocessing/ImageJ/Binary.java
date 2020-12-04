package imageprocessing.ImageJ;


import ij.IJ;
import ij.ImagePlus;
import ij.Prefs;
import ij.gui.DialogListener;
import ij.gui.GenericDialog;
import ij.plugin.filter.EDM;
import ij.plugin.filter.ExtendedPlugInFilter;
import ij.plugin.filter.PlugInFilterRunner;
import ij.plugin.frame.ThresholdAdjuster;
import ij.process.ByteProcessor;
import ij.process.FloodFiller;
import ij.process.ImageProcessor;
import java.awt.AWTEvent;

public class Binary implements ExtendedPlugInFilter, DialogListener {
    static final int MAX_ITERATIONS = 100;
    static final String NO_OPERATION = "Nothing";
    static final String[] outputTypes = new String[]{"Overwrite", "8-bit", "16-bit", "32-bit"};
    static final String[] operations = new String[]{"Nothing", "Erode", "Dilate", "Open", "Close", "Outline", "Fill Holes", "Skeletonize"};
    static int iterations = 1;
    static int count = 1;
    String operation = "Nothing";
    String arg;
    ImagePlus imp;
    PlugInFilterRunner pfr;
    boolean doOptions;
    boolean previewing;
    boolean escapePressed;
    int foreground;
    int background;
    int flags = 16941123;
    int nPasses;

    public Binary() {
    }

    public int setup(String arg, ImagePlus imp) {
        this.arg = arg;
        IJ.register(ij.plugin.filter.Binary.class);
        this.doOptions = arg.equals("options");
        if (this.doOptions) {
            if (imp == null) {
                return 512;
            }

            ImageProcessor ip = imp.getProcessor();
            if (!(ip instanceof ByteProcessor)) {
                return 512;
            }

            if (!((ByteProcessor)ip).isBinary()) {
                return 512;
            }
        }

        return this.flags;
    }

    public int showDialog(ImagePlus imp, String command, PlugInFilterRunner pfr) {
        if (this.doOptions) {
            this.imp = imp;
            this.pfr = pfr;
            GenericDialog gd = new GenericDialog("Binary Options");
            gd.addNumericField("Iterations (1-100):", (double)iterations, 0, 3, "");
            gd.addNumericField("Count (1-8):", (double)count, 0, 3, "");
            gd.addCheckbox("Black background", Prefs.blackBackground);
            gd.addCheckbox("Pad edges when eroding", Prefs.padEdges);
            gd.addChoice("EDM output:", outputTypes, outputTypes[EDM.getOutputType()]);
            if (imp != null) {
                gd.addChoice("Do:", operations, this.operation);
                gd.addPreviewCheckbox(pfr);
                gd.addDialogListener(this);
                this.previewing = true;
            }

            gd.addHelp("http://imagej.nih.gov/ij/docs/menus/process.html#options");
            gd.showDialog();
            this.previewing = false;
            if (gd.wasCanceled()) {
                return 4096;
            } else if (imp == null) {
                this.dialogItemChanged(gd, (AWTEvent)null);
                return 4096;
            } else {
                return this.operation.equals("Nothing") ? 4096 : IJ.setupDialog(imp, this.flags);
            }
        } else if (!((ByteProcessor)imp.getProcessor()).isBinary()) {
            IJ.error("8-bit binary (black and white only) image required.");
            return 4096;
        } else {
            return IJ.setupDialog(imp, this.flags);
        }
    }

    public boolean dialogItemChanged(GenericDialog gd, AWTEvent e) {
        iterations = (int)gd.getNextNumber();
        count = (int)gd.getNextNumber();
        boolean bb = Prefs.blackBackground;
        Prefs.blackBackground = gd.getNextBoolean();
        if (Prefs.blackBackground != bb) {
            ThresholdAdjuster.update();
        }

        Prefs.padEdges = gd.getNextBoolean();
        gd.setSmartRecording(EDM.getOutputType() == 0);
        EDM.setOutputType(gd.getNextChoiceIndex());
        gd.setSmartRecording(false);
        boolean isInvalid = gd.invalidNumber();
        if (iterations < 1) {
            iterations = 1;
            isInvalid = true;
        }

        if (iterations > 100) {
            iterations = 100;
            isInvalid = true;
        }

        if (count < 1) {
            count = 1;
            isInvalid = true;
        }

        if (count > 8) {
            count = 8;
            isInvalid = true;
        }

        if (isInvalid) {
            return false;
        } else {
            if (this.imp != null) {
                this.operation = gd.getNextChoice();
                this.arg = this.operation.toLowerCase();
            }

            return true;
        }
    }

    public void setNPasses(int nPasses) {
        this.nPasses = nPasses;
    }

    public void run(ImageProcessor ip) {
        int fg = Prefs.blackBackground ? 255 : 0;
        this.foreground = ip.isInvertedLut() ? 255 - fg : fg;
        this.background = 255 - this.foreground;
        ip.setSnapshotCopyMode(true);
        if (this.arg.equals("outline")) {
            this.outline(ip);
        } else if (this.arg.startsWith("fill")) {
            this.fill(ip, this.foreground, this.background);
        } else if (this.arg.startsWith("skel")) {
            ip.resetRoi();
            this.skeletonize(ip);
        } else if (this.arg.startsWith("erode")){
            this.doIterations(ip, "erode");
        } else if (!this.arg.equals("erode") && !this.arg.equals("dilate")) {
            if (this.arg.equals("open")) {
                this.doIterations(ip, "erode");
                this.doIterations(ip, "dilate");
            } else if (this.arg.equals("close")) {
                this.doIterations(ip, "dilate");
                this.doIterations(ip, "erode");
            }
        } else {
            this.doIterations((ByteProcessor)ip, this.arg);
        }

        ip.setSnapshotCopyMode(false);
        ip.setBinaryThreshold();
    }

    void doIterations(ImageProcessor ip, String mode) {
        if (!this.escapePressed) {
            if (!this.previewing && iterations > 1) {
                IJ.showStatus(this.arg + "... press ESC to cancel");
            }

            for(int i = 0; i < iterations; ++i) {
                if (Thread.currentThread().isInterrupted()) {
                    return;
                }

                if (IJ.escapePressed()) {
                    this.escapePressed = true;
                    ip.reset();
                    return;
                }

                if (mode.equals("erode")) {
                    ((ByteProcessor)ip).erode(count, this.background);
                } else {
                    ((ByteProcessor)ip).dilate(count, this.background);
                }
            }

        }
    }

    void outline(ImageProcessor ip) {
        if (Prefs.blackBackground) {
            ip.invert();
        }

        ((ByteProcessor)ip).outline();
        if (Prefs.blackBackground) {
            ip.invert();
        }

    }

    void skeletonize(ImageProcessor ip) {
        int fg = Prefs.blackBackground ? 255 : 0;
        if (ip.isInvertedLut()) {
            fg = 255 - fg;
        }

        ((ByteProcessor)ip).skeletonize(fg);
    }

    void fill(ImageProcessor ip, int foreground, int background) {
        int width = ip.getWidth();
        int height = ip.getHeight();
        FloodFiller ff = new FloodFiller(ip);
        ip.setColor(127);

        int x;
        for(x = 0; x < height; ++x) {
            if (ip.getPixel(0, x) == background) {
                ff.fill(0, x);
            }

            if (ip.getPixel(width - 1, x) == background) {
                ff.fill(width - 1, x);
            }
        }

        for(x = 0; x < width; ++x) {
            if (ip.getPixel(x, 0) == background) {
                ff.fill(x, 0);
            }

            if (ip.getPixel(x, height - 1) == background) {
                ff.fill(x, height - 1);
            }
        }

        byte[] pixels = (byte[])((byte[])ip.getPixels());
        int n = width * height;

        for(int i = 0; i < n; ++i) {
            if (pixels[i] == 127) {
                pixels[i] = (byte)background;
            } else {
                pixels[i] = (byte)foreground;
            }
        }

    }
}
