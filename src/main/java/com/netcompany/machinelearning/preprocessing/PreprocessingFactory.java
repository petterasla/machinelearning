package com.netcompany.machinelearning.preprocessing;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class PreprocessingFactory {
    private static String BASE_DIR = System.getProperty("user.dir");
    private static String DATA_DIR = BASE_DIR + "/src/main/java/com/netcompany/machinelearning/data/";
    public static String TRAINING_DIR = DATA_DIR + "mnist_png/training";
    public static String TEST_DIR = DATA_DIR + "mnist_png/testing";

    public static final int height = 28;
    public static final int width = 28;

    public static int getTotalNumberOfImages(String datasetPath) {
        int numberOfImages = 0;
        File f = new File(datasetPath);
        ArrayList<String> folderPaths = new ArrayList<String>(Arrays.asList(f.list()));
        for (final String folder : folderPaths) {
            ArrayList<String> imagePaths = getImagePaths(datasetPath + "/" + folder);
            numberOfImages += imagePaths.size();
        }
        return numberOfImages;
    }

    private static ArrayList<String> getImagePaths(String imagePath) {
        File f = new File(imagePath);
        return new ArrayList<String>(Arrays.asList(f.list()));
    }
}