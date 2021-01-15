/**
 * Processor to find cells in video.
 *
 * @author Team Automated analysis of "islet in eye", Bioengineering department, Imperial College London
 *
 * Last modified: 11/01/2021
 */

package videoprocessing.processor;

import videoprocessing.Cell;
import videoprocessing.Video;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.LinkedList;

public class CellFinder extends Processor {
    // Holds size/side of cell in pixels
    private int cellSize;

    // Constructor
    public CellFinder(Video video, int cellSize) {
        super(video);
        this.cellSize=cellSize;
    }

    // Find cells from all the frames of the video
    @Override
    public ProcessorError run(){
        // Variables for search of cells  after filtering
        // Number of peaks=cells to be found per frame
        final int nPeaksPerFrame = 15;
        // Minimal distance between two cells on individual frame
        final int disBtwPeaksFrame = 50;
        // Minimal distance between cells in overall video
        final int distBtwPeaks = 30;

        // Variables of circular kernel for filtering
        // radius of the inner circle that has value -2 and inner radius of torus with value 0
        final double rMinusTwo = 7.5;
        // outer radius of torus that has value = 0
        final double rZero = 8.5;
        // side of square filter, needs to be uneven
        final int sizeFilter = 31;

        // Variable that stores all the cells found = nPeaksPerFrame x number of frames
        LinkedList<int[]> coor = new LinkedList<>();
        // Variable that stores all cells from coor that are at least distBtwPeaks apart
        LinkedList<int[]> coorUnique = new LinkedList<>();

        // Create kernel, circular kernel with -2 circle in middle, a torus with value 0 around it and 1's for all other values of the kernel
        // Initialise kernel
        float[] kernel = new float[sizeFilter*sizeFilter];

        // Array that stores distances from center(ie. (16,16) for 31x31) of the kernel
        double[][] dist = new double[sizeFilter][sizeFilter];
        // Find the distances from the center
        for (int i = 0; i<sizeFilter; i++){
            for (int j =0; j<sizeFilter; j++){
                dist[i][j] = Math.sqrt(Math.pow(i-Math.ceil(sizeFilter/2),2) + Math.pow(j-Math.ceil(sizeFilter/2),2));
            }
        }

        // Give correct value to kernel element depending on the distance to the center
        int filtSum = 0;
        for (int i = 0; i<sizeFilter; i++){
            for (int j = 0; j<sizeFilter; j++){
                if (dist[i][j]<rMinusTwo) kernel[i*sizeFilter + j]=-2.f;
                else if(dist[i][j]<rZero) kernel[i*sizeFilter + j]=0.f;
                else kernel[i*sizeFilter + j]=1.f ;
                filtSum = (int) (filtSum +kernel[i*sizeFilter + j]);
            }
        }
        // Normalise filter to avoid that convolution yields results over 256
        for (int i = 0; i<sizeFilter; i++) {
            for (int j = 0; j < sizeFilter; j++) {
                kernel[i*sizeFilter + j] = kernel[i*sizeFilter + j]/filtSum;
            }
        }

        // Loop over each image and find the nPeaksPerFrame highest peaks
        int width = video.getIjFrames().get(0).getWidth();
        int height = video.getIjFrames().get(0).getHeight();
        if(video.getIdxFramesInFocus().size()==0) return ProcessorError.PROCESSOR_NO_FRAME_IN_FOCUS_ERROR;
        for (int i:video.getIdxFramesInFocus()){ //
            // Image to BufferImage for quicker 2D convolution
            BufferedImage bI;
            bI = video.getIjFrames().get(i).getBufferedImage();
            // Initialise output buffer image for convolution
            BufferedImage bIOut=new BufferedImage(width,height,BufferedImage.TYPE_BYTE_GRAY);

            // Apply circular kernel
            Kernel kernel1 = new Kernel(sizeFilter,sizeFilter,kernel);
            ConvolveOp convolveOp = new ConvolveOp(kernel1, ConvolveOp.EDGE_NO_OP,null);
            convolveOp.filter(bI,bIOut);

            // Find nPeaksPerFrame maxima per frame
            for (int z = 0; z< nPeaksPerFrame; z++){
                // Find maximum
                /* Reference 1 - taken from https://imagejdocu.tudor.lu/plugin/analysis/find_min_max/start*/
                double max = Double.NEGATIVE_INFINITY;
                int xMax = 0;
                int yMax = 0;
                for(int y = 0; y < height; ++y) {
                    for(int x = 0; x < width; ++x) {
                        if (bIOut.getRGB(x,y) > max) {
                            max = bIOut.getRGB(x,y);
                            xMax = x;
                            yMax = y;
                        }
                    }
                }
                /* end of reference 1 */

                int[] c=new int[3];
                c[0]=xMax;
                c[1]=yMax;
                c[2]=i;
                // Add coordinate of the maximum
                coor.add(c);
                // Set the pixel values in a square around the last identified maximum to 0
                // so that next maximum is different from all the already identified maxima
                for (int p = -disBtwPeaksFrame; p<= disBtwPeaksFrame; p++){
                    for (int q = -disBtwPeaksFrame; q<= disBtwPeaksFrame; q++){
                        if((yMax+p>0) &&(xMax+q>0) &&(xMax+q<width-sizeFilter) &&(yMax+p<height-sizeFilter))
                            bIOut.setRGB(xMax+q,yMax+p,0);
                    }
                }
            }
        }

        // Keep only the peaks that are at least distBtwPeaks apart
        coorUnique.add(coor.get(0));
        int[] cellCoor = new int[2];
        cellCoor[0]=coor.get(0)[0];
        cellCoor[1]=coor.get(0)[1];
        addCell(cellCoor,coor.get(0)[2]);
        for (int i=1;i<coor.size();i++){
            double minDist=Math.sqrt(Math.pow(coor.get(i)[0]-coorUnique.get(0)[0],2) + Math.pow(coor.get(i)[1]-coorUnique.get(0)[1],2));
            for(int j=1;j<coorUnique.size();j++){
                double distance = Math.sqrt(Math.pow(coor.get(i)[0]-coorUnique.get(j)[0],2) + Math.pow(coor.get(i)[1]-coorUnique.get(j)[1],2));
                if(distance<minDist) minDist=distance;
            }
            if(minDist>distBtwPeaks){
                int[] cellCoor1 = new int[2];
                cellCoor1[0]=coor.get(i)[0];
                cellCoor1[1]=coor.get(i)[1];
                coorUnique.add(coor.get(i));
                if(cellCoor1[0]-cellSize<0 || cellCoor1[1]-cellSize<0 || cellCoor1[0]+cellSize> video.getWidth() || cellCoor1[1]+cellSize> video.getWidth())
                    return ProcessorError.PROCESSOR_CELL_OUT_OF_FRAME_ERROR;
                else
                    addCell(cellCoor1,coor.get(i)[2]);
            }
        }
        return ProcessorError.PROCESSOR_SUCCESS;
    }

    // Add identified cell to video
    private void addCell(int[] cellCoor, int frameNum){
        // Create a Cell
        Cell cell = new Cell(cellCoor,video.getCells().size()+1,frameNum, cellSize);
        // Add cell to video
        video.addCell(cell);
    }

}
