
# 
=======
# Automated analysis of islet in eye
**Version 1.0**

Diabetes affects 1 out of 8 adults in our modern society. Diabetes is caused by a deficit in Insulin secretion of pancreatic Beta-cells.

Understanding the electrical coupling of Beta-cells could help understand the causes of insulin deficits in diabetic patients and help cure diabetes.

This project automates the measurement of the electrical activity of Beta-cells from fluorescence microscopy recordings. It is a java desktop app that relies on image processing techniques such as registration, filtering, and thresholding to identify Beta-cells and measure their electrical activity (mean intensity in recordings) through time.

To achieve this, we provide 5 tools, (1) planar motion correction implemented with the SimpleElastix registration toolkit, (2) depth motion correction,(3) automated identification of beta cells, (4) manual selection of beta cells and (5) measurements of mean intensity of regions of interest in beta-cells.

The ouputs of the program that can be saved for further analysis are the motion corrected videos for both planar and depth motion, an image of the Islet where all the identified cells are shown with cell numbers, a .cvs with all the coordinates in pixels of the cells and a .cvs file with mean intensity measurements of a region of intrest (ROI) for each cell.

---
## Installation
This project is a java based software that can be build and run through IntelliJ or directly run from the command prompt.

### Requirements
- Windows 64-bit version
- 8GB of RAM recommended
- 1GB of free disk space

### 1. Download the main branch of this git repository

Download this git repository on your Windows pc/laptop.

### 2. Install java

This software was coded with the Java Development Kit(JDK) - 15.0.1 and this JDK is therefore recommendeded. However, this software was tested and should be compatible with JDK 11.0, 13.0 and 14.0. 

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
  - Open the command prompt
  - Navigate to your local repository of this project
  - Launch software with java -jar Automated_Analysis_Islet.jar
  
### Using user interface
  - Load Video_for_Demo that is located in the videos folder of the repository by clicking the "Upload video" button
  - Process demo video by pressing the "Process video" button and choosing the configuration and the actions to be performed
  - Visualise the result of the analysis in the data tab. Under data > regions of interest, you can also manually add regions of interest.
  - Compute the mean intensity of the regions of interest in Data > Results
  - Save the results from the different analysis and the mean intensity measurements using Save > Save All, and selecting an existing directory where you want to save the results.


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

