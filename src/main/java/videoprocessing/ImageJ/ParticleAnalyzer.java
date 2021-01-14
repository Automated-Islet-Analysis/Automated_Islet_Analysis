/**
 * Class to analyse particles, used in this project to find area of the islets
 *
 * @author ImageJ develpers
 *
 * Modified by Team Automated analysis of "islet in eye", Bioengineering department, Imperial College London
 *
 * Last modified: 11/01/2021
 */


package videoprocessing.ImageJ;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.LookUpTable;
import ij.Macro;
import ij.Prefs;
import ij.Undo;
import ij.WindowManager;
import ij.gui.GenericDialog;
import ij.gui.ImageWindow;
import ij.gui.Overlay;
import ij.gui.PolygonRoi;
import ij.gui.ProgressBar;
import ij.gui.Roi;
import ij.gui.Wand;
import ij.macro.Interpreter;
import ij.measure.Calibration;
import ij.measure.Measurements;
import ij.measure.ResultsTable;
import ij.plugin.Colors;
import ij.plugin.filter.Analyzer;
import ij.plugin.filter.PlugInFilter;
import ij.plugin.frame.Recorder;
import ij.plugin.frame.RoiManager;
import ij.process.ByteProcessor;
import ij.process.ByteStatistics;
import ij.process.ColorProcessor;
import ij.process.ColorStatistics;
import ij.process.FloatProcessor;
import ij.process.FloatStatistics;
import ij.process.FloodFiller;
import ij.process.ImageProcessor;
import ij.process.ImageStatistics;
import ij.process.PolygonFiller;
import ij.process.ShortProcessor;
import ij.process.ShortStatistics;
import ij.text.TextPanel;
import ij.text.TextWindow;
import ij.util.Tools;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.util.Properties;

public class ParticleAnalyzer implements PlugInFilter, Measurements {
    public static final int SHOW_RESULTS = 1;
    public static final int SHOW_SUMMARY = 2;
    public static final int SHOW_OUTLINES = 4;
    public static final int EXCLUDE_EDGE_PARTICLES = 8;
    public static final int SHOW_ROI_MASKS = 16;
    public static final int SHOW_PROGRESS = 32;
    public static final int CLEAR_WORKSHEET = 64;
    public static final int RECORD_STARTS = 128;
    public static final int DISPLAY_SUMMARY = 256;
    public static final int SHOW_NONE = 512;
    public static final int INCLUDE_HOLES = 1024;
    public static final int ADD_TO_MANAGER = 2048;
    public static final int SHOW_MASKS = 4096;
    public static final int FOUR_CONNECTED = 8192;
    public static final int IN_SITU_SHOW = 16384;
    public static final int SHOW_OVERLAY_OUTLINES = 32768;
    public static final int SHOW_OVERLAY_MASKS = 65536;
    static final String OPTIONS = "ap.options";
    static final int BYTE = 0;
    static final int SHORT = 1;
    static final int FLOAT = 2;
    static final int RGB = 3;
    static final double DEFAULT_MIN_SIZE = 0.0D;
    static final double DEFAULT_MAX_SIZE = 1.0D / 0.0;
    private static double staticMinSize = 0.0D;
    private static double staticMaxSize = 1.0D / 0.0;
    private static boolean pixelUnits;
    private static int staticOptions = Prefs.getInt("ap.options", 64);
    private static String[] showStrings = new String[]{"Nothing", "Outlines", "Bare Outlines", "Ellipses", "Masks", "Count Masks", "Overlay", "Overlay Masks"};
    private static double staticMinCircularity = 0.0D;
    private static double staticMaxCircularity = 1.0D;
    protected static final int NOTHING = 0;
    protected static final int OUTLINES = 1;
    protected static final int BARE_OUTLINES = 2;
    protected static final int ELLIPSES = 3;
    protected static final int MASKS = 4;
    protected static final int ROI_MASKS = 5;
    protected static final int OVERLAY_OUTLINES = 6;
    protected static final int OVERLAY_MASKS = 7;
    protected static int staticShowChoice;
    protected ImagePlus imp;
    protected ResultsTable rt;
    protected Analyzer analyzer;
    protected int slice;
    protected boolean processStack;
    protected boolean showResults;
    protected boolean excludeEdgeParticles;
    protected boolean showSizeDistribution;
    protected boolean resetCounter;
    protected boolean showProgress;
    protected boolean recordStarts;
    protected boolean displaySummary;
    protected boolean floodFill;
    protected boolean addToManager;
    protected boolean inSituShow;
    private boolean showResultsTable;
    private boolean showSummaryTable;
    private double level1;
    private double level2;
    private double minSize;
    private double maxSize;
    private double minCircularity;
    private double maxCircularity;
    private int showChoice;
    private int options;
    private int measurements;
    private Calibration calibration;
    private String arg;
    private double fillColor;
    private boolean thresholdingLUT;
    private ImageProcessor drawIP;
    private int width;
    private int height;
    private boolean canceled;
    private ImageStack outlines;
    private IndexColorModel customLut;
    private int particleCount;
    private int maxParticleCount;
    private int totalCount;
    private ResultsTable summaryTable;
    private Wand wand;
    private int imageType;
    private int imageType2;
    private boolean roiNeedsImage;
    private int minX;
    private int maxX;
    private int minY;
    private int maxY;
    private ImagePlus redirectImp;
    private ImageProcessor redirectIP;
    private PolygonFiller pf;
    private Roi saveRoi;
    private int saveSlice;
    private int beginningCount;
    private Rectangle r;
    private ImageProcessor mask;
    private double totalArea;
    private FloodFiller ff;
    private Polygon polygon;
    private RoiManager roiManager;
    private static RoiManager staticRoiManager;
    private static ResultsTable staticResultsTable;
    private static ResultsTable staticSummaryTable;
    private ImagePlus outputImage;
    private boolean hideOutputImage;
    private int roiType;
    private int wandMode;
    private Overlay overlay;
    boolean blackBackground;
    private static int defaultFontSize = 9;
    private static int nextFontSize;
    private static Color defaultFontColor;
    private static Color nextFontColor;
    private static int nextLineWidth;
    private int fontSize;
    private Color fontColor;
    private int lineWidth;
    private boolean noThreshold;
    private boolean calledByPlugin;
    private boolean hyperstack;
    int counter;

    public ParticleAnalyzer(ResultsTable rt, double minSize, double maxSize, double minCirc, double maxCirc) {
        this.showResultsTable = false;
        this.showSummaryTable = false;
        this.maxParticleCount = 0;
        this.wandMode = 1;
        this.fontSize = nextFontSize;
        this.fontColor = nextFontColor;
        this.lineWidth = nextLineWidth;
        this.counter = 0;
        this.options = options;
        this.measurements = 0;
        this.rt = rt;
        if (this.rt == null) {
            this.rt = new ResultsTable();
        }

        this.minSize = minSize;
        this.maxSize = maxSize;
        this.minCircularity = minCirc;
        this.maxCircularity = maxCirc;
        this.slice = 1;
        this.options = staticOptions;


        this.options &= -2;     // change option for equivalent of only selecting "Exclude on edges"
        this.options &= -9;
        this.options &= -65;
        this.options &= -1025;
        this.options |= 256;
        this.options &= -129;
        this.options &= -2049;
        this.options &= -16385;


        staticOptions = this.options;
        this.options |= 32;


        if ((options & 16) != 0) {
            this.showChoice = 5;
        }

        if ((options & 13) != 0) { // changed literal
            this.showChoice = 6;
        }

        if ((options & 65536) != 0) {
            this.showChoice = 7;
        }

        if ((options & 4) != 0) {
            this.showChoice = 1;
        }

        if ((options & 4096) != 0) {
            this.showChoice = 4;
        }

        if ((options & 512) != 0) {
            this.showChoice = 0;
        }

        if ((options & 8192) != 0) {
            this.wandMode = 4;
            options |= 1024;
        }

        nextFontSize = defaultFontSize;
        nextFontColor = defaultFontColor;
        nextLineWidth = 1;
        this.calledByPlugin = true;
    }

//    public ParticleAnalyzer(int options, int measurements, ResultsTable rt, double minSize, double maxSize) {
//        this(options, measurements, rt, minSize, maxSize, 0.0D, 1.0D);
//    }

    public ParticleAnalyzer() {
        this.showResultsTable = false;
        this.showSummaryTable = false;
        this.maxParticleCount = 1;
        this.wandMode = 1;
        this.fontSize = nextFontSize;
        this.fontColor = nextFontColor;
        this.lineWidth = nextLineWidth;
        this.counter = 0;
        this.slice = 1;
    }

    public int setup(String arg, ImagePlus imp) {
        this.arg = arg;
        this.imp = imp;
        IJ.register(ij.plugin.filter.ParticleAnalyzer.class);
        if (imp == null) {
            IJ.noImage();
            return 4096;
        } else if (imp.getBitDepth() == 24 && !this.isThresholdedRGB(imp)) {
            IJ.error("Particle Analyzer", "RGB images must be thresholded using\nImage>Adjust>Color Threshold.");
            return 4096;
        } else if (!this.showDialog()) {
            return 4096;
        } else {
            int baseFlags = 415;
            int flags = IJ.setupDialog(imp, baseFlags);
            this.processStack = (flags & 32) != 0;
            this.slice = 0;
            this.saveRoi = imp.getRoi();
            this.saveSlice = imp.getCurrentSlice();
            if (this.saveRoi != null && this.saveRoi.getType() != 0 && this.saveRoi.isArea()) {
                this.polygon = this.saveRoi.getPolygon();
            }

            imp.startTiming();
            nextFontSize = defaultFontSize;
            nextFontColor = defaultFontColor;
            nextLineWidth = 1;
            return flags;
        }
    }

    public void run(ImageProcessor ip) {
        if (!this.canceled) {
            ++this.slice;
            if (this.imp.getStackSize() > 1 && this.processStack) {
                this.imp.setSlice(this.slice);
            }

            if (this.imp.getType() == 4) {
                ip = (ImageProcessor)this.imp.getProperty("Mask");
                ip.setThreshold(255.0D, 255.0D, 2);
                ip.setRoi(this.imp.getRoi());
            }

            if (!this.analyze(this.imp, ip)) {
                this.canceled = true;
            }

            if (this.slice == this.imp.getStackSize()) {
                this.imp.updateAndDraw();
                if (this.saveRoi != null) {
                    this.imp.setRoi(this.saveRoi);
                }

                if (this.processStack) {
                    this.imp.setSlice(this.saveSlice);
                }
            }

        }
    }

    public boolean showDialog() {
        Calibration cal = this.imp != null ? this.imp.getCalibration() : new Calibration();
        double unitSquared = cal.pixelWidth * cal.pixelHeight;
        if (pixelUnits) {
            unitSquared = 1.0D;
        }

        if (Macro.getOptions() != null) {
            boolean oldMacro = this.updateMacroOptions();
            if (oldMacro) {
                unitSquared = 1.0D;
            }

            staticMinSize = 0.0D;
            staticMaxSize = 1.0D / 0.0;
            staticMinCircularity = 0.0D;
            staticMaxCircularity = 1.0D;
            staticShowChoice = 0;
        }

        GenericDialog gd = new GenericDialog("Analyze Particles");
        this.minSize = staticMinSize;
        this.maxSize = staticMaxSize;
        this.minCircularity = staticMinCircularity;
        this.maxCircularity = staticMaxCircularity;
        this.showChoice = staticShowChoice;
        if (this.maxSize == 999999.0D) {
            this.maxSize = 1.0D / 0.0;
        }

        this.options = staticOptions;
        String unit = cal.getUnit();
        boolean scaled = cal.scaled();
        String units = unit + "^2";
        int places = 0;
        double cmin = this.minSize * unitSquared;
        if ((double)((int)cmin) != cmin) {
            places = 2;
        }

        double cmax = this.maxSize * unitSquared;
        if ((double)((int)cmax) != cmax && cmax != 1.0D / 0.0) {
            places = 2;
        }

        String minStr = ResultsTable.d2s(cmin, places);
        if (minStr.indexOf("-") != -1) {
            for(int i = places; i <= 6; ++i) {
                minStr = ResultsTable.d2s(cmin, i);
                if (minStr.indexOf("-") == -1) {
                    break;
                }
            }
        }

        String maxStr = ResultsTable.d2s(cmax, places);
        if (maxStr.indexOf("-") != -1) {
            for(int i = places; i <= 6; ++i) {
                maxStr = ResultsTable.d2s(cmax, i);
                if (maxStr.indexOf("-") == -1) {
                    break;
                }
            }
        }

        if (scaled) {
            gd.setInsets(5, 0, 0);
        }

        gd.addStringField("Size (" + units + "):", minStr + "-" + maxStr, 12);
        if (scaled) {
            gd.setInsets(0, 40, 5);
            gd.addCheckbox("Pixel units", pixelUnits);
        }

        gd.addStringField("Circularity:", IJ.d2s(this.minCircularity) + "-" + IJ.d2s(this.maxCircularity), 12);
        gd.addChoice("Show:", showStrings, showStrings[this.showChoice]);
        String[] labels = new String[8];
        boolean[] states = new boolean[8];
        labels[0] = "Display results";
        states[0] = (this.options & 1) != 0;
        labels[1] = "Exclude on edges";
        states[1] = (this.options & 8) != 0;
        labels[2] = "Clear results";
        states[2] = (this.options & 64) != 0;
        labels[3] = "Include holes";
        states[3] = (this.options & 1024) != 0;
        labels[4] = "Summarize";
        states[4] = (this.options & 256) != 0;
        labels[5] = "Record starts";
        states[5] = false;
        labels[6] = "Add to Manager";
        states[6] = (this.options & 2048) != 0;
        labels[7] = "In_situ Show";
        states[7] = (this.options & 16384) != 0;
        gd.addCheckboxGroup(4, 2, labels, states);
        gd.addHelp("http://imagej.nih.gov/ij/docs/menus/analyze.html#ap");
        gd.showDialog();
        if (gd.wasCanceled()) {
            return false;
        } else {
            gd.setSmartRecording(this.minSize == 0.0D && this.maxSize == 1.0D / 0.0);
            String size = gd.getNextString();
            if (scaled) {
                pixelUnits = gd.getNextBoolean();
            }

            if (pixelUnits) {
                unitSquared = 1.0D;
            } else {
                unitSquared = cal.pixelWidth * cal.pixelHeight;
            }

            String[] minAndMax = Tools.split(size, " -");
            double mins = minAndMax.length >= 1 ? gd.parseDouble(minAndMax[0]) : 0.0D;
            double maxs = minAndMax.length == 2 ? gd.parseDouble(minAndMax[1]) : 0.0D / 0.0;
            this.minSize = Double.isNaN(mins) ? 0.0D : mins / unitSquared;
            this.maxSize = Double.isNaN(maxs) ? 1.0D / 0.0 : maxs / unitSquared;
            if (this.minSize < 0.0D) {
                this.minSize = 0.0D;
            }

            if (this.maxSize < this.minSize) {
                this.maxSize = 1.0D / 0.0;
            }

            staticMinSize = this.minSize;
            staticMaxSize = this.maxSize;
            gd.setSmartRecording(this.minCircularity == 0.0D && this.maxCircularity == 1.0D);
            minAndMax = Tools.split(gd.getNextString(), " -");
            double minc = minAndMax.length >= 1 ? gd.parseDouble(minAndMax[0]) : 0.0D;
            double maxc = minAndMax.length == 2 ? gd.parseDouble(minAndMax[1]) : 0.0D / 0.0;
            this.minCircularity = Double.isNaN(minc) ? 0.0D : minc;
            this.maxCircularity = Double.isNaN(maxc) ? 1.0D : maxc;
            if (this.minCircularity < 0.0D) {
                this.minCircularity = 0.0D;
            }

            if (this.minCircularity > this.maxCircularity && this.maxCircularity == 1.0D) {
                this.minCircularity = 0.0D;
            }

            if (this.minCircularity > this.maxCircularity) {
                this.minCircularity = this.maxCircularity;
            }

            if (this.maxCircularity < this.minCircularity) {
                this.maxCircularity = this.minCircularity;
            }

            if (this.minCircularity == 1.0D && this.maxCircularity == 1.0D) {
                this.minCircularity = 0.0D;
            }

            staticMinCircularity = this.minCircularity;
            staticMaxCircularity = this.maxCircularity;
            if (gd.invalidNumber()) {
                IJ.error("Bins invalid.");
                this.canceled = true;
                return false;
            } else {
                gd.setSmartRecording(this.showChoice == 0);
                this.showChoice = gd.getNextChoiceIndex();
                gd.setSmartRecording(false);
                staticShowChoice = this.showChoice;
                if (gd.getNextBoolean()) {
                    this.options |= 1;
                } else {
                    this.options &= -2;
                }

                if (gd.getNextBoolean()) {
                    this.options |= 8;
                } else {
                    this.options &= -9;
                }

                if (gd.getNextBoolean()) {
                    this.options |= 64;
                } else {
                    this.options &= -65;
                }

                if (gd.getNextBoolean()) {
                    this.options |= 1024;
                } else {
                    this.options &= -1025;
                }

                if (gd.getNextBoolean()) {
                    this.options |= 256;
                } else {
                    this.options &= -257;
                }

                if (gd.getNextBoolean()) {
                    this.options |= 128;
                } else {
                    this.options &= -129;
                }

                if (gd.getNextBoolean()) {
                    this.options |= 2048;
                } else {
                    this.options &= -2049;
                }

                if (gd.getNextBoolean()) {
                    this.options |= 16384;
                } else {
                    this.options &= -16385;
                }

                staticOptions = this.options;
                this.options |= 32;
                if ((this.options & 256) != 0) {
                    Analyzer.setMeasurements(Analyzer.getMeasurements() | 1);
                }

                return true;
            }
        }
    }

    private boolean isThresholdedRGB(ImagePlus imp) {
        Object obj = imp.getProperty("Mask");
        if (obj != null && obj instanceof ImageProcessor) {
            ImageProcessor mask = (ImageProcessor)obj;
            return mask.getWidth() == imp.getWidth() && mask.getHeight() == imp.getHeight();
        } else {
            return false;
        }
    }

    boolean updateMacroOptions() {
        String options = Macro.getOptions();
        options = options.replace("show=[Overlay Outlines]", "show=Overlay");
        Macro.setOptions(options);
        int index = options.indexOf("maximum=");
        if (index == -1) {
            return false;
        } else {
            index += 8;

            int len;
            for(len = options.length(); index < len - 1 && options.charAt(index) != ' '; ++index) {
            }

            if (index == len - 1) {
                return false;
            } else {
                int min = (int)Tools.parseDouble(Macro.getValue(options, "minimum", "1"));
                int max = (int)Tools.parseDouble(Macro.getValue(options, "maximum", "999999"));
                options = "size=" + min + "-" + max + options.substring(index, len);
                Macro.setOptions(options);
                return true;
            }
        }
    }

    public boolean analyze(ImagePlus imp) {
        return this.analyze(imp, imp.getProcessor());
    }

    public boolean analyze(ImagePlus imp, ImageProcessor ip) {
        if (this.imp == null) {
            this.imp = imp;
        }

        this.showResults = (this.options & 1) != 0;
        this.excludeEdgeParticles = (this.options & 8) != 0;
        this.resetCounter = (this.options & 64) != 0;
        this.showProgress = (this.options & 32) != 0;
        this.floodFill = (this.options & 1024) == 0;
        this.recordStarts = (this.options & 128) != 0;
        this.addToManager = (this.options & 2048) != 0;
        if (staticRoiManager != null) {
            this.addToManager = true;
            this.roiManager = staticRoiManager;
            staticRoiManager = null;
        }

        this.hyperstack = imp.isHyperStack();
        if (staticResultsTable != null) {
            this.rt = staticResultsTable;
            staticResultsTable = null;
            this.showResultsTable = false;
        }

        if (staticSummaryTable != null) {
            this.summaryTable = staticSummaryTable;
            staticSummaryTable = null;
            this.showSummaryTable = false;
        }

        this.displaySummary = (this.options & 256) != 0 || (this.options & 2) != 0;
        this.inSituShow = (this.options & 16384) != 0;
        this.outputImage = null;
        ip.snapshot();
        ip.setProgressBar((ProgressBar)null);
        if (Analyzer.isRedirectImage()) {
            this.redirectImp = Analyzer.getRedirectImage(imp);
            if (this.redirectImp == null) {
                return false;
            }

            int depth = this.redirectImp.getStackSize();
            if (depth > 1 && depth == imp.getStackSize()) {
                ImageStack redirectStack = this.redirectImp.getStack();
                this.redirectIP = redirectStack.getProcessor(imp.getCurrentSlice());
            } else {
                this.redirectIP = this.redirectImp.getProcessor();
            }
        } else if (imp.getType() == 4) {
            ImagePlus original = (ImagePlus)imp.getProperty("OriginalImage");
            if (original != null && original.getWidth() == imp.getWidth() && original.getHeight() == imp.getHeight()) {
                this.redirectImp = original;
                this.redirectIP = original.getProcessor();
            }
        }

        if (!this.setThresholdLevels(imp, ip)) {
            return false;
        } else {
            this.width = ip.getWidth();
            this.height = ip.getHeight();
            if (this.showChoice != 0 && this.showChoice != 6 && this.showChoice != 7) {
                this.blackBackground = Prefs.blackBackground && this.inSituShow;
                if (this.slice == 1) {
                    this.outlines = new ImageStack(this.width, this.height);
                }

                if (this.showChoice == 5) {
                    this.drawIP = new ShortProcessor(this.width, this.height);
                } else {
                    this.drawIP = new ByteProcessor(this.width, this.height);
                }

                this.drawIP.setLineWidth(this.lineWidth);
                if (this.showChoice != 5) {
                    if (this.showChoice == 4 && !this.blackBackground) {
                        this.drawIP.invertLut();
                    } else if (this.showChoice == 1) {
                        if (!this.inSituShow) {
                            if (this.customLut == null) {
                                this.makeCustomLut();
                            }

                            this.drawIP.setColorModel(this.customLut);
                        }

                        this.drawIP.setFont(new Font("SansSerif", 0, this.fontSize));
                        if (this.fontSize > 12 && this.inSituShow) {
                            this.drawIP.setAntialiasedText(true);
                        }
                    }
                }

                this.outlines.addSlice((String)null, this.drawIP);
                if (this.showChoice != 5 && !this.blackBackground) {
                    this.drawIP.setColor(Color.white);
                    this.drawIP.fill();
                    this.drawIP.setColor(Color.black);
                } else {
                    this.drawIP.setColor(Color.black);
                    this.drawIP.fill();
                    this.drawIP.setColor(Color.white);
                }
            }

            this.calibration = this.redirectImp != null ? this.redirectImp.getCalibration() : imp.getCalibration();
            if (this.measurements == 0) {
                this.measurements = Analyzer.getMeasurements();
            }

            this.measurements &= -257;
            if (this.rt == null) {
                Frame table = WindowManager.getFrame("Results");
                if (!this.showResults && table != null) {
                    this.rt = new ResultsTable();
                    if (this.resetCounter && table instanceof TextWindow) {
                        IJ.run("Clear Results");
                        ((TextWindow)table).close();
                        this.rt = Analyzer.getResultsTable();
                    }
                } else {
                    this.rt = Analyzer.getResultsTable();
                }
            }

            this.analyzer = new Analyzer(imp, this.measurements, this.rt);
            if (this.resetCounter && this.slice == 1 && this.rt.size() > 0 && !Analyzer.resetCounter()) {
                return false;
            } else {
                this.beginningCount = Analyzer.getCounter();
                byte[] pixels = null;
                if (ip instanceof ByteProcessor) {
                    pixels = (byte[])((byte[])ip.getPixels());
                }

                if (this.r == null) {
                    this.r = ip.getRoi();
                    this.mask = ip.getMask();
                    if (this.displaySummary) {
                        if (this.mask != null) {
                            this.totalArea = ImageStatistics.getStatistics(ip, 1, this.calibration).area;
                        } else {
                            this.totalArea = (double)this.r.width * this.calibration.pixelWidth * (double)this.r.height * this.calibration.pixelHeight;
                        }
                    }
                }

                this.minX = this.r.x;
                this.maxX = this.r.x + this.r.width;
                this.minY = this.r.y;
                this.maxY = this.r.y + this.r.height;
                if ((this.r.width < this.width || this.r.height < this.height || this.mask != null) && !this.eraseOutsideRoi(ip, this.r, this.mask)) {
                    return false;
                } else {
                    int inc = Math.max(this.r.height / 25, 1);
                    int mi = 0;
                    ImageWindow win = imp.getWindow();
                    if (win != null) {
                        win.running = true;
                    }

                    if (this.showChoice == 3) {
                        this.measurements |= 2048;
                    }

                    this.roiNeedsImage = (this.measurements & 128) != 0 || (this.measurements & 8192) != 0 || (this.measurements & 16384) != 0;
                    this.particleCount = 0;
                    this.wand = new Wand(ip);
                    this.pf = new PolygonFiller();
                    if (this.floodFill) {
                        ImageProcessor ipf = ip.duplicate();
                        ipf.setValue(this.fillColor);
                        this.ff = new FloodFiller(ipf);
                    }

                    this.roiType = Wand.allPoints() ? 3 : 4;
                    boolean done = false;

                    for(int y = this.r.y; y < this.r.y + this.r.height; ++y) {
                        int offset = y * this.width;

                        for(int x = this.r.x; x < this.r.x + this.r.width; ++x) {
                            double value;
                            if (pixels != null) {
                                value = (double)(pixels[offset + x] & 255);
                            } else if (this.imageType == 1) {
                                value = (double)ip.getPixel(x, y);
                            } else {
                                value = (double)ip.getPixelValue(x, y);
                            }

                            if (value >= this.level1 && value <= this.level2 && !done) {
                                this.analyzeParticle(x, y, imp, ip);
                                done = this.level1 == 0.0D && this.level2 == 255.0D && imp.getBitDepth() == 8;
                            }
                        }

                        if (this.showProgress && y % inc == 0) {
                            IJ.showProgress((double)(y - this.r.y) / (double)this.r.height);
                        }

                        if (win != null) {
                            this.canceled = !win.running;
                        }

                        if (this.canceled) {
                            Macro.abort();
                            break;
                        }
                    }

                    if (this.showProgress) {
                        IJ.showProgress(1.0D);
                    }

                    if (this.showResults && this.showResultsTable && this.rt.size() > 0) {
                        this.rt.updateResults();
                    }

                    imp.deleteRoi();
                    ip.resetRoi();
                    ip.reset();
                    if (this.displaySummary) {
                        this.updateSliceSummary();
                    }

                    if (this.addToManager && this.roiManager != null) {
                        this.roiManager.setEditMode(imp, true);
                    }

                    this.maxParticleCount = this.particleCount > this.maxParticleCount ? this.particleCount : this.maxParticleCount;
                    this.totalCount += this.particleCount;
                    if (!this.canceled) {
                        this.showResults();
                    }

                    return true;
                }
            }
        }
    }

    public double sliceArea(){
        int slices = this.imp.getStackSize();
        float[] areas = this.rt.getColumn(0);
        if (areas == null) {
            areas = new float[0];
        }

        double sum = 0.0D;
        int start = areas.length - this.particleCount;
        if (start >= 0) {
            int places;
            for(places = start; places < areas.length; ++places) {
                sum += (double)areas[places];
            }
        }
        return sum;
    }

    void updateSliceSummary() {
        int slices = this.imp.getStackSize();
        Frame frame;
        TextWindow tw;
        ResultsTable table;
        if (slices == 1) {
            frame = WindowManager.getFrame("Summary");
            if (frame != null && frame instanceof TextWindow) {
                tw = (TextWindow)frame;
                table = tw.getTextPanel().getResultsTable();
                if (table != null) {
                    this.summaryTable = table;
                }
            }
        } else {
            frame = WindowManager.getFrame("Summary of " + this.imp.getTitle());
            if (frame != null && frame instanceof TextWindow) {
                tw = (TextWindow)frame;
                table = tw.getTextPanel().getResultsTable();
                if (table != null) {
                    this.summaryTable = table;
                }
            }
        }

        if (this.summaryTable == null) {
            this.summaryTable = new ResultsTable();
        }

        float[] areas = this.rt.getColumn(0);
        if (areas == null) {
            areas = new float[0];
        }

        String label = this.imp.getTitle();
        if (slices > 1) {
            if (this.processStack) {
                label = this.imp.getStack().getShortSliceLabel(this.slice);
            } else {
                label = this.imp.getStack().getShortSliceLabel(this.imp.getCurrentSlice());
            }

            label = label != null && !label.equals("") ? label : "" + this.slice;
        }

        this.summaryTable.incrementCounter();
        this.summaryTable.addValue("Slice", label);
        double sum = 0.0D;
        int start = areas.length - this.particleCount;
        if (start >= 0) {
            int places;
            for(places = start; places < areas.length; ++places) {
                sum += (double)areas[places];
            }

            places = Analyzer.getPrecision();
            Calibration cal = this.imp.getCalibration();
            this.summaryTable.addValue("Count", (double)this.particleCount);
            this.summaryTable.addValue("Total Area", sum);
            this.summaryTable.addValue("Average Size", sum / (double)this.particleCount);
            this.summaryTable.addValue("%Area", sum * 100.0D / this.totalArea);
            this.addMeans(areas.length > 0 ? start : -1);
            String title = slices == 1 ? "Summary" : "Summary of " + this.imp.getTitle();
            if (this.showSummaryTable) {
                this.summaryTable.show(title);
            }

        }
    }

    void addMeans(int start) {
        if ((this.measurements & 2) != 0) {
            this.addMean(1, start);
        }

        if ((this.measurements & 8) != 0) {
            this.addMean(3, start);
        }

        if ((this.measurements & 128) != 0) {
            this.addMean(10, start);
        }

        if ((this.measurements & 2048) != 0) {
            this.addMean(15, start);
            this.addMean(16, start);
            this.addMean(17, start);
        }

        if ((this.measurements & 8192) != 0) {
            this.addMean(18, start);
            this.addMean(35, start);
        }

        if ((this.measurements & 16384) != 0) {
            this.addMean(19, start);
            this.addMean(29, start);
            this.addMean(30, start);
            this.addMean(31, start);
            this.addMean(32, start);
        }

        if ((this.measurements & 13) != 0) { // chaged literal '耀'
            this.addMean(20, start);
        }

        if ((this.measurements & 65536) != 0) {
            this.addMean(21, start);
        }

        if ((this.measurements & 131072) != 0) {
            this.addMean(22, start);
        }

        if ((this.measurements & 262144) != 0) {
            this.addMean(23, start);
        }

    }

    private void addMean(int column, int start) {
        double value = 0.0D / 0.0;
        if (start != -1) {
            float[] c = column >= 0 ? this.rt.getColumn(column) : null;
            if (c != null) {
                ImageProcessor ip = new FloatProcessor(c.length, 1, c, (ColorModel)null);
                if (ip == null) {
                    return;
                }

                ip.setRoi(start, 0, ip.getWidth() - start, 1);
                ip = ip.crop();
                ImageStatistics stats = new FloatStatistics(ip);
                if (stats == null) {
                    return;
                }

                value = stats.mean;
            }
        }

        this.summaryTable.addValue(ResultsTable.getDefaultHeading(column), value);
    }

    boolean eraseOutsideRoi(ImageProcessor ip, Rectangle r, ImageProcessor mask) {
        int width = ip.getWidth();
        int height = ip.getHeight();
        ip.setRoi(r);
        if (this.excludeEdgeParticles && this.polygon != null) {
            ImageStatistics stats = ImageStatistics.getStatistics(ip, 16, (Calibration)null);
            if (this.fillColor >= stats.min && this.fillColor <= stats.max) {
                double replaceColor = this.level1 - 1.0D;
                int y;
                if (replaceColor < 0.0D || replaceColor == this.fillColor) {
                    replaceColor = this.level2 + 1.0D;
                    y = this.imageType == 0 ? 255 : '\uffff';
                    if (replaceColor > (double)y || replaceColor == this.fillColor) {
                        IJ.error("Particle Analyzer", "Unable to remove edge particles");
                        return false;
                    }
                }

                for(y = this.minY; y < this.maxY; ++y) {
                    for(int x = this.minX; x < this.maxX; ++x) {
                        int v = ip.getPixel(x, y);
                        if ((double)v == this.fillColor) {
                            ip.putPixel(x, y, (int)replaceColor);
                        }
                    }
                }
            }
        }

        ip.setValue(this.fillColor);
        if (mask != null) {
            mask = mask.duplicate();
            mask.invert();
            ip.fill(mask);
        }

        ip.setRoi(0, 0, r.x, height);
        ip.fill();
        ip.setRoi(r.x, 0, r.width, r.y);
        ip.fill();
        ip.setRoi(r.x, r.y + r.height, r.width, height - (r.y + r.height));
        ip.fill();
        ip.setRoi(r.x + r.width, 0, width - (r.x + r.width), height);
        ip.fill();
        ip.resetRoi();
        return true;
    }

    boolean setThresholdLevels(ImagePlus imp, ImageProcessor ip) {
        double t1 = ip.getMinThreshold();
        double t2 = ip.getMaxThreshold();
        boolean invertedLut = imp.isInvertedLut();
        boolean byteImage = ip instanceof ByteProcessor;
        if (ip instanceof ShortProcessor) {
            this.imageType = 1;
        } else if (ip instanceof FloatProcessor) {
            this.imageType = 2;
        } else {
            this.imageType = 0;
        }

        if (t1 == -808080.0D) {
            this.noThreshold = true;
            ImageStatistics stats = imp.getStatistics();
            if (this.imageType != 0 || stats.histogram[0] + stats.histogram[255] != stats.pixelCount) {
                IJ.error("Particle Analyzer", "A thresholded image or 8-bit binary image is\nrequired. Threshold levels can be set using\nthe Image->Adjust->Threshold tool.");
                this.canceled = true;
                return false;
            }

            boolean threshold255 = invertedLut;
            if (Prefs.blackBackground) {
                threshold255 = !invertedLut;
            }

            if (threshold255) {
                this.level1 = 255.0D;
                this.level2 = 255.0D;
                this.fillColor = 64.0D;
            } else {
                this.level1 = 0.0D;
                this.level2 = 0.0D;
                this.fillColor = 192.0D;
            }
        } else {
            this.level1 = t1;
            this.level2 = t2;
            if (this.imageType == 0) {
                if (this.level1 > 0.0D) {
                    this.fillColor = 0.0D;
                } else if (this.level2 < 255.0D) {
                    this.fillColor = 255.0D;
                }
            } else if (this.imageType == 1) {
                if (this.level1 > 0.0D) {
                    this.fillColor = 0.0D;
                } else if (this.level2 < 65535.0D) {
                    this.fillColor = 65535.0D;
                }
            } else {
                if (this.imageType != 2) {
                    return false;
                }

                this.fillColor = -3.4028234663852886E38D;
            }
        }

        this.imageType2 = this.imageType;
        if (this.redirectIP != null) {
            if (this.redirectIP instanceof ShortProcessor) {
                this.imageType2 = 1;
            } else if (this.redirectIP instanceof FloatProcessor) {
                this.imageType2 = 2;
            } else if (this.redirectIP instanceof ColorProcessor) {
                this.imageType2 = 3;
            } else {
                this.imageType2 = 0;
            }
        }

        return true;
    }

    void analyzeParticle(int x, int y, ImagePlus imp, ImageProcessor ip) {
        ImageProcessor ip2 = this.redirectIP != null ? this.redirectIP : ip;
        this.wand.autoOutline(x, y, this.level1, this.level2, this.wandMode);
        if (this.wand.npoints == 0) {
            IJ.log("wand error: " + x + " " + y);
        } else {
            Roi roi = new PolygonRoi(this.wand.xpoints, this.wand.ypoints, this.wand.npoints, this.roiType);
            Rectangle r = roi.getBounds();
            if (r.width > 1 && r.height > 1) {
                PolygonRoi proi = (PolygonRoi)roi;
                this.pf.setPolygon(proi.getXCoordinates(), proi.getYCoordinates(), proi.getNCoordinates());
                ip2.setMask(this.pf.getMask(r.width, r.height));
                if (this.floodFill) {
                    this.ff.particleAnalyzerFill(x, y, this.level1, this.level2, ip2.getMask(), r);
                }
            }

            ip2.setRoi(r);
            ip.setValue(this.fillColor);
            ImageStatistics stats = this.getStatistics(ip2, this.measurements, this.calibration);
            boolean include = true;
            if (this.excludeEdgeParticles) {
                if (r.x == this.minX || r.y == this.minY || r.x + r.width == this.maxX || r.y + r.height == this.maxY) {
                    include = false;
                }

                if (this.polygon != null) {
                    Rectangle bounds = roi.getBounds();
                    int x1 = bounds.x + this.wand.xpoints[this.wand.npoints - 1];
                    int y1 = bounds.y + this.wand.ypoints[this.wand.npoints - 1];

                    for(int i = 0; i < this.wand.npoints; ++i) {
                        int x2 = bounds.x + this.wand.xpoints[i];
                        int y2 = bounds.y + this.wand.ypoints[i];
                        if (!this.polygon.contains(x2, y2)) {
                            include = false;
                            break;
                        }

                        if (x1 == x2 && (double)ip.getPixel(x1, y1 - 1) == this.fillColor || y1 == y2 && (double)ip.getPixel(x1 - 1, y1) == this.fillColor) {
                            include = false;
                            break;
                        }

                        x1 = x2;
                        y1 = y2;
                    }
                }
            }

            ImageProcessor mask = ip2.getMask();
            if (this.minCircularity > 0.0D || this.maxCircularity != 1.0D) {
                double perimeter = roi.getLength();
                double circularity = perimeter == 0.0D ? 0.0D : 12.566370614359172D * ((double)stats.pixelCount / (perimeter * perimeter));
                if (circularity > 1.0D && this.maxCircularity <= 1.0D) {
                    circularity = 1.0D;
                }

                if (circularity < this.minCircularity || circularity > this.maxCircularity) {
                    include = false;
                }
            }

            if ((double)stats.pixelCount >= this.minSize && (double)stats.pixelCount <= this.maxSize && include) {
                ++this.particleCount;
                if (this.roiNeedsImage) {
                    roi.setImage(imp);
                }

                stats.xstart = x;
                stats.ystart = y;
                this.saveResults(stats, roi);
                if (this.addToManager) {
                    this.addToRoiManager(roi, mask, this.particleCount);
                }

                if (this.showChoice != 0) {
                    this.drawParticle(this.drawIP, roi, stats, mask);
                }
            }

            if (this.redirectIP != null) {
                ip.setRoi(r);
            }

            ip.fill(mask);
        }
    }

    ImageStatistics getStatistics(ImageProcessor ip, int mOptions, Calibration cal) {
        switch(this.imageType2) {
            case 0:
                return new ByteStatistics(ip, mOptions, cal);
            case 1:
                return new ShortStatistics(ip, mOptions, cal);
            case 2:
                return new FloatStatistics(ip, mOptions, cal);
            case 3:
                return new ColorStatistics(ip, mOptions, cal);
            default:
                return null;
        }
    }

    protected void saveResults(ImageStatistics stats, Roi roi) {
        this.analyzer.saveResults(stats, roi);
        if (this.maxCircularity > 1.0D && this.rt.columnExists("Circ.") && this.rt.getValue("Circ.", this.rt.size() - 1) == 1.0D) {
            double perimeter = roi.getLength();
            double circularity = perimeter == 0.0D ? 0.0D : 12.566370614359172D * ((double)stats.pixelCount / (perimeter * perimeter));
            this.rt.addValue("Circ.", circularity);
        }

        if (this.recordStarts) {
            this.rt.addValue("XStart", (double)stats.xstart);
            this.rt.addValue("YStart", (double)stats.ystart);
        }

        if (this.showResultsTable && this.showResults) {
            this.rt.addResults();
        }

    }

    private void addToRoiManager(Roi roi, ImageProcessor mask, int particleNumber) {
        if (this.roiManager == null) {
            if (Macro.getOptions() != null && Interpreter.isBatchMode()) {
                this.roiManager = Interpreter.getBatchModeRoiManager();
            }

            if (this.roiManager == null) {
                Frame frame = WindowManager.getFrame("ROI Manager");
                if (frame == null) {
                    IJ.run("ROI Manager...");
                }

                frame = WindowManager.getFrame("ROI Manager");
                if (frame == null || !(frame instanceof RoiManager)) {
                    this.addToManager = false;
                    return;
                }

                this.roiManager = (RoiManager)frame;
            }

            if (this.resetCounter) {
                this.roiManager.runCommand("reset");
            }
        }

        if (this.imp.getStackSize() > 1) {
            int n = this.imp.getCurrentSlice();
            if (this.hyperstack) {
                int[] pos = this.imp.convertIndexToPosition(n);
                roi.setPosition(pos[0], pos[1], pos[2]);
            } else {
                roi.setPosition(n);
            }
        }

        if (this.lineWidth != 1) {
            roi.setStrokeWidth((float)this.lineWidth);
        }

        this.roiManager.add(this.imp, roi, particleNumber);
    }

    protected void drawParticle(ImageProcessor drawIP, Roi roi, ImageStatistics stats, ImageProcessor mask) {
        switch(this.showChoice) {
            case 1:
            case 2:
            case 6:
            case 7:
                this.drawOutline(drawIP, roi, mask, this.rt.size());
                break;
            case 3:
                this.drawEllipse(drawIP, stats, this.rt.size());
                break;
            case 4:
                this.drawFilledParticle(drawIP, roi, mask);
                break;
            case 5:
                this.drawRoiFilledParticle(drawIP, roi, mask, this.rt.size());
        }

    }

    void drawFilledParticle(ImageProcessor ip, Roi roi, ImageProcessor mask) {
        ip.setRoi(roi.getBounds());
        ip.fill(mask);
    }

    void drawOutline(ImageProcessor ip, Roi roi, ImageProcessor mask, int count) {
        int currentSlice;
        int[] pos;
        if (this.showChoice != 6 && this.showChoice != 7) {
            Rectangle r = roi.getBounds();
            currentSlice = ((PolygonRoi)roi).getNCoordinates();
            pos = ((PolygonRoi)roi).getXCoordinates();
            int[] yp = ((PolygonRoi)roi).getYCoordinates();
            int x = r.x;
            int y = r.y;
            if (!this.inSituShow) {
                ip.setValue(0.0D);
            }

            ip.moveTo(x + pos[0], y + yp[0]);

            for(int i = 1; i < currentSlice; ++i) {
                ip.lineTo(x + pos[i], y + yp[i]);
            }

            ip.lineTo(x + pos[0], y + yp[0]);
            if (this.showChoice != 2) {
                String s = ResultsTable.d2s((double)count, 0);
                ip.moveTo(r.x + r.width / 2 - ip.getStringWidth(s) / 2, r.y + r.height / 2 + this.fontSize / 2);
                if (!this.inSituShow) {
                    ip.setValue(1.0D);
                }

                ip.drawString(s);
            }
        } else {
            if (this.overlay == null) {
                this.overlay = new Overlay();
                this.overlay.drawLabels(true);
                this.overlay.setLabelFont(new Font("SansSerif", 0, this.fontSize));
            }

            Roi roi2 = (Roi)roi.clone();
            roi2.setStrokeColor(Color.cyan);
            if (this.lineWidth != 1) {
                roi2.setStrokeWidth((float)this.lineWidth);
            }

            if (this.showChoice == 7) {
                roi2.setFillColor(Color.cyan);
            }

            if (this.processStack || this.imp.getStackSize() > 1) {
                currentSlice = this.slice;
                if (!this.processStack) {
                    currentSlice = this.imp.getCurrentSlice();
                }

                if (this.hyperstack) {
                    pos = this.imp.convertIndexToPosition(currentSlice);
                    roi2.setPosition(pos[0], pos[1], pos[2]);
                } else {
                    roi2.setPosition(currentSlice);
                }
            }

            if (this.showResults) {
                roi2.setName("" + count);
            }

            this.overlay.add(roi2);
        }

    }

    void drawEllipse(ImageProcessor ip, ImageStatistics stats, int count) {
        stats.drawEllipse(ip);
    }

    void drawRoiFilledParticle(ImageProcessor ip, Roi roi, ImageProcessor mask, int count) {
        int grayLevel = count < 65535 ? count : '\uffff';
        ip.setValue((double)grayLevel);
        ip.setRoi(roi.getBounds());
        ip.fill(mask);
    }

    void showResults() {
        int count = this.rt.size();
        boolean lastSlice = !this.processStack || this.slice == this.imp.getStackSize();
        String range;
        if ((this.showChoice == 6 || this.showChoice == 7) && this.overlay != null && count > 0 && (!this.processStack || this.slice == this.imp.getStackSize())) {
            if (this.processStack) {
                this.imp.setOverlay(this.overlay);
            } else {
                Overlay overlay0 = this.imp.getOverlay();
                if (overlay0 != null && this.imp.getStackSize() != 1) {
                    for(int i = 0; i < this.overlay.size(); ++i) {
                        overlay0.add(this.overlay.get(i));
                    }

                    this.imp.setOverlay(overlay0);
                } else {
                    this.imp.setOverlay(this.overlay);
                }
            }
        } else if (this.outlines != null && lastSlice) {
            String title = this.imp != null ? this.imp.getTitle() : "Outlines";
            if (this.showChoice == 4) {
                range = "Mask of ";
            } else if (this.showChoice == 5) {
                range = "Count Masks of ";
            } else {
                range = "Drawing of ";
            }

            this.outlines.update(this.drawIP);
            this.outputImage = new ImagePlus(range + title, this.outlines);
            this.outputImage.setCalibration(this.imp.getCalibration());
            if (this.inSituShow) {
                if (this.imp.getStackSize() == 1 && !Recorder.record) {
                    Undo.setup(6, this.imp);
                }

                ImageStack outputStack = this.outputImage.getStack();
                if (this.imp.getStackSize() > 1 && outputStack.getSize() == 1 && this.imp.getBitDepth() == 8) {
                    this.imp.setProcessor(outputStack.getProcessor(1));
                } else {
                    this.imp.setStack((String)null, outputStack);
                }
            } else if (!this.hideOutputImage) {
                this.outputImage.show();
            }
        }

        if (this.showResults && !this.processStack) {
            if (this.showResultsTable && this.rt.size() > 0) {
                TextPanel tp = IJ.getTextPanel();
                if (this.beginningCount > 0 && tp != null && tp.getLineCount() != count) {
                    this.rt.show("Results");
                }
            }

//            Analyzer.firstParticle = this.beginningCount;
//            Analyzer.lastParticle = Analyzer.getCounter() - 1;
        }
//        else {
//            Analyzer.lastParticle = 0;
//            Analyzer.firstParticle = 0;
//        }

        if (this.showResults && this.rt.size() == 0 && !IJ.isMacro() && !this.calledByPlugin && (!this.processStack || this.slice == this.imp.getStackSize())) {
            int digits = (double)((int)this.level1) == this.level1 && (double)((int)this.level2) == this.level2 ? 0 : 2;
            range = IJ.d2s(this.level1, digits) + "-" + IJ.d2s(this.level2, digits);
            String assummed = this.noThreshold ? "assumed" : "";
            IJ.showMessage("Particle Analyzer", "No particles were detected. The " + assummed + "\nthreshold (" + range + ") may not be correct.");
        }

    }

    public ImagePlus getOutputImage() {
        return this.outputImage;
    }

    public void setHideOutputImage(boolean hideOutputImage) {
        this.hideOutputImage = hideOutputImage;
    }

    public static void setFontSize(int size) {
        nextFontSize = size;
    }

    public static void setFontColor(String color) {
        nextFontColor = Colors.decode(color, defaultFontColor);
    }

    public static void setLineWidth(int width) {
        nextLineWidth = width;
    }

    public static void setRoiManager(RoiManager manager) {
        staticRoiManager = manager;
    }

    public static void setResultsTable(ResultsTable rt) {
        staticResultsTable = rt;
    }

    public static void setSummaryTable(ResultsTable rt) {
        staticSummaryTable = rt;
    }

    int getColumnID(String name) {
        int id = this.rt.getFreeColumn(name);
        if (id == -2) {
            id = this.rt.getColumnIndex(name);
        }

        return id;
    }

    void makeCustomLut() {
        IndexColorModel cm = (IndexColorModel)LookUpTable.createGrayscaleColorModel(false);
        byte[] reds = new byte[256];
        byte[] greens = new byte[256];
        byte[] blues = new byte[256];
        cm.getReds(reds);
        cm.getGreens(greens);
        cm.getBlues(blues);
        reds[1] = (byte)this.fontColor.getRed();
        greens[1] = (byte)this.fontColor.getGreen();
        blues[1] = (byte)this.fontColor.getBlue();
        this.customLut = new IndexColorModel(8, 256, reds, greens, blues);
    }

    public static void savePreferences(Properties prefs) {
        prefs.put("ap.options", Integer.toString(staticOptions));
    }

    static {
        nextFontSize = defaultFontSize;
        defaultFontColor = Color.red;
        nextFontColor = defaultFontColor;
        nextLineWidth = 1;
    }
}

