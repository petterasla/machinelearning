package com.netcompany.machinelearning.preprocessing;


import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class Preprocessing {

    private int[][][] trainingImages;
    private int[] trainingLabels;
    private int[][][] testImages;
    private int[] testLabels;

    public static void main(String[] args) {
        Preprocessing preprocessing = Preprocessing.create();
    }

    private Preprocessing() {
        System.out.println("This may take a minute.\n\nLoading training set and labels.");

        trainingImages = new int
                [PreprocessingFactory.getTotalNumberOfImages(PreprocessingFactory.TRAINING_DIR)]
                [PreprocessingFactory.height]
                [PreprocessingFactory.height];

        trainingLabels = new int
                [PreprocessingFactory.getTotalNumberOfImages(PreprocessingFactory.TRAINING_DIR)];

        readImagesToArray(trainingImages, PreprocessingFactory.TRAINING_DIR);
        readImageLabels(trainingLabels, PreprocessingFactory.TRAINING_DIR);

        System.out.println("Total number of training images: " + getTrainingImages().length);
        System.out.println();
        System.out.println("Loading test set");

        testImages = new int
                [PreprocessingFactory.getTotalNumberOfImages(PreprocessingFactory.TEST_DIR)]
                [PreprocessingFactory.height]
                [PreprocessingFactory.height];

        testLabels = new int
                [PreprocessingFactory.getTotalNumberOfImages(PreprocessingFactory.TEST_DIR)];

        readImagesToArray(testImages, PreprocessingFactory.TEST_DIR);
        readImageLabels(testLabels, PreprocessingFactory.TEST_DIR);

        System.out.println("Total number of testing images: " + getTestImages().length);
        System.out.println("\nFinished loading images");

    }

    /**
     * Oppretter et preprosseseringsobject som leser inn trenings- og testbilder samt fasit til disse.
     *
     * Hent dataene ved hjelp av getters
     *
     */
    public static Preprocessing create()  {
        return new Preprocessing();
    }

    /**
     *
     * @param imagePath stien til ett bilde
     * @return en to-dimensjonal int array representasjon av bilde med verdier innenfor 0-255.
     */
    private int[][] getImageAsArray(String imagePath){
        try {
            BufferedImage image = ImageIO.read(new File(imagePath));
            WritableRaster raster = image.getRaster();
            DataBufferByte data   = (DataBufferByte) raster.getDataBuffer();

            int w = image.getWidth();
            int h = image.getHeight();

            int[][] array = new int[w][h];
            for (int row = 0; row < w; row++) {
                for (int col = 0; col < h; col++) {
                    data.getElemFloat(row);
                    array[row][col] = raster.getSample(row, col, 0);
                }

            }
            return array;
        } catch (Exception e) {
            System.out.println("Something went wrong while converting the image to 8-bit.\nImage path was:" + imagePath);
        }
        return null;
    }

    /**
     *
     * @param images Et array med hvilke data som skal lagres. F.eks trening
     * @param datasetPath Stien til datasettet.
     */
    private void readImagesToArray(int[][][] images, String datasetPath) {
        int counter = 0;
        File f = new File(datasetPath);
        ArrayList<String> folderPaths = new ArrayList<String>(Arrays.asList(f.list()));
        for (final String folder : folderPaths) {
            if (folder.equalsIgnoreCase(".DS_STORE")) continue;
            String imageFolderPath = datasetPath + "/" + folder;
            ArrayList<String> imagePaths = getImagePaths(imageFolderPath);
            for (final String imagePath : imagePaths) {
                images[counter] = getImageAsArray(imageFolderPath + "/" + imagePath);
            }
        }
    }

    private ArrayList<String> getImagePaths(String imagePath) {
        File f = new File(imagePath);
        return new ArrayList<String>(Arrays.asList(f.list()));
    }

    private void readImageLabels(int[] labels, String datasetPath) {
        int counter = 0;
        File f = new File(datasetPath);
        ArrayList<String> imagePaths;
        ArrayList<String> folderPaths = new ArrayList<String>(Arrays.asList(f.list()));
        for (final String folder : folderPaths) {
            if (folder.equalsIgnoreCase(".DS_STORE")) continue;
            imagePaths = getImagePaths(datasetPath + "/" + folder);
            for (final String imagePath : imagePaths) {
                labels[counter] = Integer.parseInt(folder);
                counter++;
            }
        }
    }

    public int[][][] getTrainingImages() {
        return trainingImages;
    }
    public int[][][] getTestImages() {return testImages;}
    public int[] getTrainingLabels() {return trainingLabels;}
    public int[] getTestLabels() {return testLabels;}

}
