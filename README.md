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

min 2GB of free RAM, Min 4GB of RAM per core?

(min 3GB of frre disk space + size of file + size of cmake + SimpleElastix)

### 1. Download git
Git command is needed when CMake is used.

Download git from: https://git-scm.com/download/win

Run downloaded executable file by double-clicking on it.

### 2. Download this git repository, main branch

Download git repository on your pc by downloading it as a zip and extracting it, using the just doanloaded git command from the command line, or any git GUI Clients.

### 3. Install java

This software was coded with the Java Development Kit(JDK) - 15.0.1.

#### Download jdk-15.0.1 from: https://www.oracle.com/java/technologies/javase-jdk15-downloads.html
  - Select Windows x64 Installer
  - Accept the License Agreement
  - Press green 'Download jdk-15.0.1_windows-x64_bin.exe' button and follow the instructions without changing default configurations


#### Run the executable (jdk-15.0.1_windows-x64_bin.exe) by double-clicking on it and follow the instructions

#### Add java bin folder to the PATH Environment Variable
  - Open 'Edit the system environment variables' (windows key and type environment variables)
  - Select 'Environment Variables...' in the bottom right corner
  - Double-click the 'Path' variable in 'System variables'
  - add path of the bin folder of JDK, the default path is 'C:\Program Files\Java\jdk-15\bin'

A full description of the installation process can be found at https://bit.ly/2L0bHaY

### 4. Install IntelliJ

IntelliJ Ultimate is not free however you can get free Educational licenses if applicable. 
IntelliJ Community is free on the other hand. 
Both are compatible with this project.

#### Download IntelliJ from: https://www.jetbrains.com/idea/download/#section=windows
  - Select the Ultimate or Community version to your preference.
  - Download .exe file by clicking the download button

#### Run the just downloaded executable by double-clicking it and follow the instructions without changing the default settings 

A full description of the installation process can be found at: https://www.jetbrains.com/help/idea/installation-guide.html#silent

### 5. Install SimpleElastix

SimpleElastix requires compiling the C++ SimpleElastix project and needs therefore Visual Studio and CMake. Out software is compatible with Visual Studio 2019 and CMake-3.19. 

#### Download SimpleElastix from github at: https://github.com/SuperElastix/SimpleElastix

#### Download CMake-3.19 from: https://cmake.org/download/
  - Download Windows win64-x64 ZIP
  - Unzip to your path of preference

#### Download Visual Studio 2019 from: https://visualstudio.microsoft.com/downloads/
  - Select version of preference and an installer will automatically download
  - Run the executable
  - Make sure to select the downloading of Desktop development with C++
  - Launch install and downloading
  
#### Build SimpleElastix project
  - Open CMake GUI from the bin folder where you extracted CMake
  - Create a 'build' directory for CMake. Make sure the path the that directory is short, eg. a directory called 'buildSimpleElastix' at the root of your harddrive.  
  - Add the path of the SuperBuild directory of downloaded SimpleElastix folder to 'Where is the source code:'
  - Add the path to the directory created for CMake, eg 'C:/buildSimpleElastix' to 'Where to build the binaries'
  - Press 'Configure' button, a pop-up should open and display 'Visual Studio xx 2019' for 'Specify the generator for this project' and 'Use default native compilers' should be selected. Press 'Finish'. This process can output warning but they can be disregarded.
  - Unselect all the files except 'WRAP_JAVA'. This will shorthen the compilation time.
  - Press the Generate button
  
  - Open Visual Studio, select File -> Open Project/Solution -> Open and choose SuperBuildSimpleITK.sln which is located in the folder where the binary files where created with CMake, eg. C:/buildSimpleElastix
  - Build the ALL_BUILD project after checking that "Release" build type is selected and x64 is selected too. Building the project might take a few hours(around 2 hours).

A full description of the installation process can be found at: https://simpleelastix.readthedocs.io/GettingStarted.html 

### 6. Setting up dependencies

#### Setting up SimpleElastix library
  - Add .jar file of SimpleElastix to the libs folder of the git repository of this project. The file is called 'simpleitk-2.0.0rc2.dev908-g8244e.jar' and is located under .../buildSimpleElastix/SimpleITK-build/Wrapping/Java
  - Add the .dll file of SimpleElastix to the bin folder of your JDK installation. The file is called 'SimpleITKJava.dll' and is located under .../buildSimpleElastix/SimpleITK-build/Wrapping/Java/lib/Release
  
#### Setting up ImageJ library(needed?)


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
