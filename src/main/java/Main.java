/**
 * This project automates the measurement of the electrical activity of Beta-cells from fluorescence microscopy recordings.
 * It is a java desktop app that relies on image processing techniques
 * such as registration, filtering, and thresholding to identify Beta-cells
 * and measure their electrical activity (mean intensity in recordings) through time.
 *
 * To achieve this, we provide 5 tools, Planar motion correction performed with the SimpleElastix registration toolkit,
 * depth motion correction, automated search of beta cells, manual selection of beta cells and measurements of mean intensity.
 *
 * The controller class is the main class that controls the user interface and stored the VideoProcessor in use of the analysis.
 *
 * @author Team Automated analysis of "islet in eye", Bioengineering department, Imperial College London
 *
 * Last modified: 11/01/2021
 */


import UI.Controller;

import java.awt.*;

public class Main extends Frame {

    public static void main(final String[] args) {
       new Controller();
    }
}

