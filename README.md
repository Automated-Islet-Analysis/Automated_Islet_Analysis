
# 
=======
# Automated analysis of islet in eye
**Version 0.1**

Diabetes affects 1 out of 8 adults in our modern society. Diabetes is caused by a deficit in Insulin secretion of pancreatic Beta-cells.

Understanding the electrical coupling of Beta-cells could help understand the causes of insulin deficits in diabetic patients and help cure diabetes.

This project automates the measurement of the electrical activity of Beta-cells from fluorescence microscopy recordings. It is a java desktop app that relies on image processing techniques such as registration, filtering, and thresholding to identify Beta-cells and measure their electrical activity (mean intensity in recordings) through time.

To achieve this, we provide 5 tools, Planar motion correction performed with the SimpleElastix registration toolkit, depth motion correction, automated search of beta cells, manual selection of beta cells and measurements of mean intensity.

The ouput files that can be saved for further anaylis are the motion corrected videos for both planar and depth motion, an image of the Islet with all the identified cells are shown, a .cvs with all the coordinates of cells and a .cvs file with mean intensity measurements of a region of intrest (ROI) for each cell.

---
## Installation
This is a java based software that can be build and run through IntelliJ and relies on the java ImageJ library and SimpleElastix toolkit on top of common java libraries.

### Requirements
Windows 64-bit version

(min 3GB of free disk space + size of file + size of cmake + SimpleElastix)

### 1. Download this git repository, main branch

Download git repository on your pc by downloading it as a zip and extracting it, using the just doanloaded git command from the command line, or any git GUI Clients.

### 2. Install java

This software was coded with the Java Development Kit(JDK) - 15.0.1.

#### Download jdk-15.0.1 from: https://www.oracle.com/java/technologies/javase-jdk15-downloads.html
  - Select Windows x64 Installer
  - Accept the License Agreement
  - Press green 'Download jdk-15.0.1_windows-x64_bin.exe' button and follow the instructions without changing default configurations


#### Run the executable (jdk-15.0.1_windows-x64_bin.exe) by double-clicking on it and follow the instructions

A full description of the installation process can be found at https://bit.ly/2L0bHaY

### 3. Install IntelliJ

IntelliJ Ultimate is not free however you can get free Educational licenses if applicable. 
IntelliJ Community is free on the other hand. 
Both are compatible with this project.

#### Download IntelliJ from: https://www.jetbrains.com/idea/download/#section=windows
  - Select the Ultimate or Community version to your preference.
  - Download .exe file by clicking the download button

#### Run the just downloaded executable by double-clicking it and follow the instructions without changing the default settings 

A full description of the installation process can be found at: https://www.jetbrains.com/help/idea/installation-guide.html#silent

---
## Demo

### Launching user interface
  - Open IntelliJ
  - Open this project with File > Open and then select this project
  - Set the Project SDK with File > Project Structure > Project, set 15, check that 15's path corresponds to the JDK that was installed earlier. You can see the path by clicking on edit.
  - Build project by clicking on gradle on the top right of in IntelliJ then selecting Tasks > build > build
  - Run project by clicking on gradle on the top right of in IntelliJ then selecting Tasks > application > run

### Using user interface
  - Load Video_for_Demo that is located in the video folder of the project by pressing load button
  - Analyse demo video by pressing the analyse button and chooseing the configuration and the actions to be performed
  - Visualise the result of the analysis in the data tab. Under data > ROIs, you can also add ROIs manually
  - Compute the mean intensity of the ROIs in Data > Results
  - Save the results from the different analysis and the mean intensity measurements


---
### Contributors
- Sacha Maire <sacha.maire18@imperial.ac.uk>
- Marta Masramon <marta.masramon-munoz18@imperial.ac.uk>
- Fabio Oliva <fabio.oliva18@imperial.ac.uk>
- Gaspard Oliviers <gaspard.oliviers18@imperial.ac.uk>
- Livia Soro <livia.soro18@imperial.ac.uk>

Correspondance at <email@imperial.ac.uk>

---

## License & copyright
© Team Automated analysis of "islet in eye", Bioengineering department, Imperial College London

