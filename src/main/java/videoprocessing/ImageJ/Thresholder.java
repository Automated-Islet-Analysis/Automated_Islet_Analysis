/**
 * Class to make ImagePlus binary using a threshold. Modified to remove all user input and take inputs from other code.
 *
 * @author Rasband, W.S., ImageJ, U. S. National Institutes of Health, Bethesda, Maryland, USA, https://imagej.nih.gov/ij/
 *
 * Modified by Team Automated analysis of "islet in eye", Bioengineering department, Imperial College London
 *
 * Last modified: 11/01/2021
 */

package videoprocessing.ImageJ;


import ij.CompositeImage;
import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.LookUpTable;
import ij.Macro;
import ij.Prefs;
import ij.Undo;
import ij.gui.GenericDialog;
import ij.gui.Toolbar;
import ij.measure.Measurements;
import ij.plugin.PlugIn;
import ij.plugin.frame.Recorder;
import ij.process.AutoThresholder;
import ij.process.ByteProcessor;
import ij.process.ColorProcessor;
import ij.process.ImageProcessor;
import ij.process.StackProcessor;
import java.awt.Choice;
import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

public class Thresholder implements PlugIn, Measurements, ItemListener {
    public static final String[] methods = AutoThresholder.getMethods();
    public static final String[] backgrounds = new String[]{"Default", "Dark", "Light"};
    private double minThreshold;
    private double maxThreshold;
    private boolean autoThreshold;
    private boolean showLegacyDialog = true;
    private static boolean fill1 = true;
    private static boolean fill2 = true;
    private static boolean useBW = true;
    private boolean useLocal = true;
    private boolean listThresholds;
    private boolean convertToMask;
    private String method;
    private String background;
    private static boolean staticUseLocal = true;
    private static boolean staticListThresholds;
    private static String staticMethod;
    private static String staticBackground;
    private ImagePlus imp;
    private String impTitle;
    private Vector choices;

    public Thresholder(ImagePlus imp){
        this.method = methods[0];
        this.background = backgrounds[0];
        this.imp = imp;
        impTitle = imp.getTitle();
    }

    public void run(String arg) {

        this.convertToMask = arg.equals("mask");
        if (arg.equals("skip") || this.convertToMask) {
            this.showLegacyDialog = false;
        }

        if (imp.getStackSize() == 1) {
            if (!this.convertToMask && imp.getProcessor().isBinary()) {
                this.setThreshold(imp);
            } else {
                Undo.setup(6, imp);
                this.applyThreshold(imp, false);
            }
        } else {
            this.convertStack(imp);
        }
    }

    public void run(double minThres, double maxThres) {
        this.maxThreshold = maxThres;
        this.minThreshold = minThres;

        if (imp.getStackSize() == 1) {
            if (!this.convertToMask && imp.getProcessor().isBinary()) {
                this.setThreshold(imp);
            } else {
                Undo.setup(6, imp);
                this.applyThreshold(imp, false);
            }
        } else {
            this.convertStack(imp);
        }
    }

    public ImagePlus getImagePlus() {
        return imp;
    }

    private void setThreshold(ImagePlus imp) {
        ImageProcessor ip = imp.getProcessor();
        int threshold = ip.isInvertedLut() ? 255 : 0;
        if (Prefs.blackBackground) {
            threshold = ip.isInvertedLut() ? 0 : 255;
        }

        ip.setThreshold((double)threshold, (double)threshold, 2);
    }

    void convertStack(ImagePlus imp) {
        if (imp.getStack().isVirtual()) {
            IJ.error("Thresholder", "This command does not work with virtual stacks.\nUse Image>Duplicate to convert to a normal stack.");
        } else {
            this.showLegacyDialog = false;
            boolean thresholdSet = imp.isThreshold();
            this.imp = imp;
            impTitle = imp.getTitle();
            if (!IJ.isMacro()) {
                this.method = staticMethod;
                this.background = staticBackground;
                this.useLocal = staticUseLocal;
                this.listThresholds = staticListThresholds;
                if (!thresholdSet) {
                    this.updateThreshold(imp);
                }
            } else {
                String macroOptions = Macro.getOptions();
                if (macroOptions != null && macroOptions.indexOf("slice ") != -1) {
                    Macro.setOptions(macroOptions.replaceAll("slice ", "only "));
                }

                this.useLocal = false;
            }

            boolean saveBlackBackground = Prefs.blackBackground;
            boolean oneSlice = false;

            /** No GUI needed*/
//            GenericDialog gd = new GenericDialog("Convert Stack to Binary");
//            gd.addChoice("Method:", methods, this.method);
//            gd.addChoice("Background:", backgrounds, this.background);
//            gd.addCheckbox("Calculate threshold for each image", this.useLocal);
//            gd.addCheckbox("Only convert current image", oneSlice);
//            gd.addCheckbox("Black background (of binary masks)", Prefs.blackBackground);
//            gd.addCheckbox("List thresholds", this.listThresholds);
//            this.choices = gd.getChoices();
//            if (this.choices != null) {
//                ((Choice)this.choices.elementAt(0)).addItemListener(this);
//                ((Choice)this.choices.elementAt(1)).addItemListener(this);
//            }
//
//            gd.showDialog();

            this.imp = null;
            this.method = "Default";
            this.background = "Default";
            this.useLocal = true;
            oneSlice = false;
            Prefs.blackBackground = false;
            this.listThresholds = false;




            if (true) {
//                this.imp = null;
//                this.method = gd.getNextChoice();
//                this.background = gd.getNextChoice();
//                this.useLocal = gd.getNextBoolean();
//                oneSlice = gd.getNextBoolean();
//                Prefs.blackBackground = gd.getNextBoolean();
//                this.listThresholds = gd.getNextBoolean();
                if (!IJ.isMacro()) {
                    staticMethod = this.method;
                    staticBackground = this.background;
                    staticUseLocal = this.useLocal;
                    staticListThresholds = this.listThresholds;
                }

                if (oneSlice) {
                    this.useLocal = false;
                    this.listThresholds = false;
                    if (oneSlice && imp.getBitDepth() != 8) {
                        IJ.error("Thresholder", "8-bit stack required to process a single slice.");
                        return;
                    }
                }


                Undo.reset();
                if (this.useLocal) {
                    this.convertStackToBinary(imp);
                } else {
                    this.applyThreshold(imp, oneSlice);
                }

                Prefs.blackBackground = saveBlackBackground;
                if (thresholdSet) {
                    if (imp.getProcessor().getLutUpdateMode() != 2) {
                        imp.getProcessor().resetThreshold();
                    }

                    imp.updateAndDraw();
                }
                this.imp = imp;
            }
        }
    }

    private void applyThreshold(ImagePlus imp, boolean oneSlice) {
        imp.deleteRoi();
        ImageProcessor ip = imp.getProcessor();
        ip.resetBinaryThreshold();
        int type = imp.getType();
        if (type != 1 && type != 2) {
            if (imp.lock()) {
                double saveMinThreshold = ip.getMinThreshold();
                double saveMaxThreshold = ip.getMaxThreshold();
                this.autoThreshold = saveMinThreshold == -808080.0D;
                boolean useBlackAndWhite = false;
                boolean fill1 = true;
                boolean fill2 = true;
                String options = Macro.getOptions();
                boolean modernMacro = options != null && !options.contains("thresholded") && !options.contains("remaining");
                if (this.autoThreshold || modernMacro || IJ.macroRunning() && options == null) {
                    this.showLegacyDialog = false;
                }

                int fcolor = 255;
                int bcolor = 0;
                int i;
                if (this.showLegacyDialog) {
                    GenericDialog gd = new GenericDialog("Make Binary");
                    gd.addCheckbox("Thresholded pixels to foreground color", fill1);
                    gd.addCheckbox("Remaining pixels to background color", fill2);
                    gd.addMessage("");
                    gd.addCheckbox("Black foreground, white background", useBW);
                    gd.showDialog();
                    if (gd.wasCanceled()) {
                        imp.unlock();
                        return;
                    }

                    fill1 = gd.getNextBoolean();
                    fill2 = gd.getNextBoolean();
                    useBW = useBlackAndWhite = gd.getNextBoolean();
                    i = ip.getPixel(0, 0);
                    if (useBlackAndWhite) {
                        ip.setColor(Color.black);
                    } else {
                        ip.setColor(Toolbar.getForegroundColor());
                    }

                    ip.drawPixel(0, 0);
                    fcolor = ip.getPixel(0, 0);
                    if (useBlackAndWhite) {
                        ip.setColor(Color.white);
                    } else {
                        ip.setColor(Toolbar.getBackgroundColor());
                    }

                    ip.drawPixel(0, 0);
                    bcolor = ip.getPixel(0, 0);
                    ip.setColor(Toolbar.getForegroundColor());
                    ip.putPixel(0, 0, i);
                } else {
                    this.convertToMask = true;
                }

                if (type == 4) {
                    ImageProcessor ip2 = this.updateColorThresholdedImage(imp);
                    if (ip2 != null) {
                        imp.setProcessor(ip2);
                        this.autoThreshold = false;
                        saveMinThreshold = 255.0D;
                        saveMaxThreshold = 255.0D;
                        type = 0;
                    }
                }

                if (type != 0) {
                    this.convertToByte(imp);
                }

                ip = imp.getProcessor();
                if (this.autoThreshold) {
                    this.autoThreshold(ip);
                } else {
                    if (Recorder.record && !Recorder.scriptMode() && (!IJ.isMacro() || Recorder.recordInMacros)) {
                        Recorder.record("//setThreshold", (int)saveMinThreshold, (int)saveMaxThreshold);
                    }

                    this.minThreshold = saveMinThreshold;
                    this.maxThreshold = saveMaxThreshold;
                }

                if (this.convertToMask && ip.isColorLut()) {
                    ip.setColorModel(ip.getDefaultColorModel());
                }

                ip.resetThreshold();
                if (IJ.debugMode) {
                    IJ.log("Thresholder (apply): " + this.minThreshold + "-" + this.maxThreshold + " " + fcolor + " " + bcolor + " " + fill1 + " " + fill2);
                }

                int[] lut = new int[256];

                for(i = 0; i < 256; ++i) {
                    if ((double)i >= this.minThreshold && (double)i <= this.maxThreshold) {
                        lut[i] = fill1 ? fcolor : (byte)i;
                    } else {
                        lut[i] = fill2 ? bcolor : (byte)i;
                    }
                }

                if (imp.getStackSize() > 1 && !oneSlice) {
                    (new StackProcessor(imp.getStack(), ip)).applyTable(lut);
                } else {
                    ip.applyTable(lut);
                }

                if (this.convertToMask && !oneSlice) {
                    boolean invertedLut = imp.isInvertedLut();
                    if (invertedLut && Prefs.blackBackground || !invertedLut && !Prefs.blackBackground) {
                        ip.invertLut();
                        if (IJ.debugMode) {
                            IJ.log("Thresholder (inverting lut)");
                        }
                    }
                }

                if (fill1 && fill2 && (fcolor == 0 && bcolor == 255 || fcolor == 255 && bcolor == 0)) {
                    imp.getProcessor().setThreshold((double)fcolor, (double)fcolor, 2);
                    if (IJ.debugMode) {
                        IJ.log("Thresholder: " + fcolor + "-" + fcolor + " (" + (Prefs.blackBackground ? "black" : "white") + " background)");
                    }
                }

                imp.updateAndRepaintWindow();
                imp.unlock();
            }
        } else {
            this.applyShortOrFloatThreshold(imp);
        }
    }

    private ImageProcessor updateColorThresholdedImage(ImagePlus imp) {
        Object mask = imp.getProperty("Mask");
        if (mask != null && mask instanceof ByteProcessor) {
            ImageProcessor maskIP = (ImageProcessor)mask;
            if (maskIP.getWidth() == imp.getWidth() && maskIP.getHeight() == imp.getHeight()) {
                Object originalImage = imp.getProperty("OriginalImage");
                if (originalImage != null && originalImage instanceof ImagePlus) {
                    ImagePlus imp2 = (ImagePlus)originalImage;
                    if (imp2.getBitDepth() == 24 && imp2.getWidth() == imp.getWidth() && imp2.getHeight() == imp.getHeight()) {
                        imp.setProcessor(imp2.getProcessor());
                        Undo.setup(6, imp);
                    }
                }

                return maskIP;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private void applyShortOrFloatThreshold(ImagePlus imp) {
        if (imp.lock()) {
            int width = imp.getWidth();
            int height = imp.getHeight();
            int size = width * height;
            boolean isFloat = imp.getType() == 2;
            int currentSlice = imp.getCurrentSlice();
            int nSlices = imp.getStackSize();
            ImageStack stack1 = imp.getStack();
            ImageStack stack2 = new ImageStack(width, height);
            ImageProcessor ip = imp.getProcessor();
            float t1 = (float)ip.getMinThreshold();
            float t2 = (float)ip.getMaxThreshold();
            if ((double)t1 == -808080.0D) {
                double min = ip.getMin();
                double max = ip.getMax();
                ip = ip.convertToByte(true);
                this.autoThreshold(ip);
                t1 = (float)(min + (max - min) * (this.minThreshold / 255.0D));
                t2 = (float)(min + (max - min) * (this.maxThreshold / 255.0D));
            }

            IJ.showStatus("Converting to mask");

            for(int i = 1; i <= nSlices; ++i) {
                IJ.showProgress(i, nSlices);
                String label = stack1.getSliceLabel(i);
                ImageProcessor ip1 = stack1.getProcessor(i);
                ImageProcessor ip2 = new ByteProcessor(width, height);

                for(int j = 0; j < size; ++j) {
                    float value = ip1.getf(j);
                    if (value >= t1 && value <= t2) {
                        ip2.set(j, 255);
                    } else {
                        ip2.set(j, 0);
                    }
                }

                stack2.addSlice(label, ip2);
            }

            imp.setStack((String)null, stack2);
            ImageStack stack = imp.getStack();
            stack.setColorModel(LookUpTable.createGrayscaleColorModel(!Prefs.blackBackground));
            imp.setStack((String)null, stack);
            if (imp.isComposite()) {
                CompositeImage ci = (CompositeImage)imp;
                ci.setMode(3);
                ci.resetDisplayRanges();
                ci.updateAndDraw();
            }

            imp.getProcessor().setThreshold(255.0D, 255.0D, 2);
            if (IJ.debugMode) {
                IJ.log("Thresholder16: 255-255 (" + (Prefs.blackBackground ? "black" : "white") + " background)");
            }

            IJ.showStatus("");
            imp.unlock();
        }
    }

    void convertStackToBinary(ImagePlus imp) {
        int nSlices = imp.getStackSize();
        double[] minValues = this.listThresholds ? new double[nSlices] : null;
        double[] maxValues = this.listThresholds ? new double[nSlices] : null;
        int bitDepth = imp.getBitDepth();
        ImageStack stack;
        if (bitDepth != 8) {
            IJ.showStatus("Converting to byte");
            stack = imp.getStack();
            ImageStack stack2 = new ImageStack(imp.getWidth(), imp.getHeight());

            for(int i = 1; i <= nSlices; ++i) {
                IJ.showProgress(i, nSlices);
                String label = stack.getSliceLabel(i);
                ImageProcessor ip = stack.getProcessor(i);
                ip.resetMinAndMax();
                if (this.listThresholds) {
                    minValues[i - 1] = ip.getMin();
                    maxValues[i - 1] = ip.getMax();
                }

                stack2.addSlice(label, ip.convertToByte(true));
            }

            imp.setStack((String)null, stack2);
        }

        stack = imp.getStack();
        IJ.showStatus("Auto-thresholding");
        if (this.listThresholds) {
            IJ.log("Thresholding method: " + this.method);
        }

        for(int i = 1; i <= nSlices; ++i) {
            IJ.showProgress(i, nSlices);
            ImageProcessor ip = stack.getProcessor(i);
            if (this.method.equals("Default") && this.background.equals("Default")) {
                ip.setAutoThreshold(1, 2);
            } else {
                ip.setAutoThreshold(this.method, !this.background.equals("Light"), 2);
            }

            this.minThreshold = ip.getMinThreshold();
            this.maxThreshold = ip.getMaxThreshold();
            if (this.listThresholds) {
                double t1 = this.minThreshold;
                double t2 = this.maxThreshold;
                if (bitDepth != 8) {
                    t1 = minValues[i - 1] + t1 / 255.0D * (maxValues[i - 1] - minValues[i - 1]);
                    t2 = minValues[i - 1] + t2 / 255.0D * (maxValues[i - 1] - minValues[i - 1]);
                }

                int digits = bitDepth == 32 ? 2 : 0;
                IJ.log("  " + i + ": " + IJ.d2s(t1, digits) + "-" + IJ.d2s(t2, digits));
            }

            int[] lut = new int[256];

            for(int j = 0; j < 256; ++j) {
                if ((double)j >= this.minThreshold && (double)j <= this.maxThreshold) {
                    lut[j] = -1;
                } else {
                    lut[j] = 0;
                }
            }

            ip.applyTable(lut);
        }

        stack.setColorModel(LookUpTable.createGrayscaleColorModel(!Prefs.blackBackground));
        imp.setStack(this.impTitle, stack);
        imp.getProcessor().setThreshold(255.0D, 255.0D, 2);
//        if (imp.isComposite()) {
//            CompositeImage ci = (CompositeImage)imp;
//            ci.setMode(3);
//            ci.resetDisplayRanges();
//            ci.updateAndDraw();
//        }
//
//        IJ.showStatus("");
    }

    void convertToByte(ImagePlus imp) {
        int currentSlice = imp.getCurrentSlice();
        ImageStack stack1 = imp.getStack();
        ImageStack stack2 = imp.createEmptyStack();
        int nSlices = imp.getStackSize();

        for(int i = 1; i <= nSlices; ++i) {
            String label = stack1.getSliceLabel(i);
            ImageProcessor ip = stack1.getProcessor(i);
            ip.setMinAndMax(0.0D, 255.0D);
            stack2.addSlice(label, ip.convertToByte(true));
        }

        imp.setStack((String)null, stack2);
        imp.setSlice(currentSlice);
        imp.setCalibration(imp.getCalibration());
    }

    public static ByteProcessor createMask(ImagePlus imp) {
        ImageProcessor ip = imp.getProcessor();
        if (ip instanceof ColorProcessor) {
            throw new IllegalArgumentException("Non-RGB image requires");
        } else if (ip.getMinThreshold() == -808080.0D) {
            throw new IllegalArgumentException("Image must be thresholded");
        } else {
            return ip.createMask();
        }
    }

    void autoThreshold(ImageProcessor ip) {
        ip.setAutoThreshold(1, 2);
        this.minThreshold = ip.getMinThreshold();
        this.maxThreshold = ip.getMaxThreshold();
        if (IJ.debugMode) {
            IJ.log("Thresholder (auto): " + this.minThreshold + "-" + this.maxThreshold);
        }

    }

    public static void setMethod(String method) {
        staticMethod = method;
    }

    public static void setBackground(String background) {
        staticBackground = background;
    }

    public void itemStateChanged(ItemEvent e) {
        if (this.imp != null) {
            Choice choice = (Choice)e.getSource();
            if (choice == this.choices.elementAt(0)) {
                this.method = choice.getSelectedItem();
            } else {
                this.background = choice.getSelectedItem();
            }

            this.updateThreshold(this.imp);
        }
    }

    private void updateThreshold(ImagePlus imp) {
        ImageProcessor ip = imp.getProcessor();
        if (this.method.equals("Default") && this.background.equals("Default")) {
            ip.setAutoThreshold(1, 0);
        } else {
            ip.setAutoThreshold(this.method, !this.background.equals("Light"), 0);
        }

        imp.updateAndDraw();
    }

    static {
        staticMethod = methods[0];
        staticBackground = backgrounds[0];
    }
}

