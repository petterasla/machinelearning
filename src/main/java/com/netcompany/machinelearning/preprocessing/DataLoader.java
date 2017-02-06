package com.netcompany.machinelearning.preprocessing;

import com.google.common.collect.Lists;
import com.twelvemonkeys.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class DataLoader {

    private final Logger LOGG = LoggerFactory.getLogger(this.getClass());
    private static final double ANDEL_AV_DATA = 0.05;

    private final Datasett treningsdata;
    private final Datasett testdata;

    public DataLoader(final Boolean lesInnHeleDatasettet) {
        if (lesInnHeleDatasettet) {
            LOGG.info("Laster alle trenings- og testdata. Dette kan ta en stund...");

            treningsdata = readImages(1, DataHelper.TRAINING_DIR);
            testdata = readImages(1, DataHelper.TEST_DIR);

            LOGG.info("Total number of testing images: " + getTestImages().length);
            LOGG.info("Finished loading images");
        } else {
            LOGG.info(String.format("Laster inn subset (%.0f%%) av trenings- og testdata.", ANDEL_AV_DATA * 100));

            treningsdata = readImages(ANDEL_AV_DATA, DataHelper.TRAINING_DIR);
            testdata = readImages(ANDEL_AV_DATA, DataHelper.TEST_DIR);

            LOGG.info("Innlasting av subsett ferdig");
        }
    }

    private Datasett readImages(final double partToLoad, final String datasetPath) {
        final List<int[][]> bilder = Lists.newArrayList();
        final List<Integer> fasit = Lists.newArrayList();

        File f = new File(datasetPath);
        Validate.isTrue(f.exists(), String.format("%s finnes ikke", datasetPath));

        final ArrayList<String> folderPaths = Lists.newArrayList(f.list());
        for (final String folder : folderPaths) {
            int antallPerKlasse = 0;
            if (folder.equalsIgnoreCase(".DS_STORE")) continue;
            final String imageFolderPath = datasetPath + File.separator + folder;
            final ArrayList<String> imagePaths = getImagePaths(imageFolderPath);
            for (final String imagePath : imagePaths) {
                if (antallPerKlasse++ > imagePaths.size() * partToLoad) {
                    break;
                }
                bilder.add(getImageAsArray(imageFolderPath + File.separator + imagePath));
                fasit.add(Integer.parseInt(folder));
            }
        }
        final int[][][] bildeArray = new int[bilder.size()][DataHelper.height][DataHelper.width];
        final int[] fasitArray = fasit.stream().mapToInt(Integer::intValue).toArray();
        bilder.toArray(bildeArray);
        return new Datasett(bildeArray, fasitArray);
    }

    /**
     * @param imagePath stien til ett bilde
     * @return en to-dimensjonal int array representasjon av bilde med verdier innenfor 0-255.
     */
    private int[][] getImageAsArray(final String imagePath) {
        try {
            final BufferedImage image = ImageIO.read(new File(imagePath));
            final WritableRaster raster = image.getRaster();
            final DataBufferByte data = (DataBufferByte) raster.getDataBuffer();

            final int w = image.getWidth();
            final int h = image.getHeight();

            final int[][] array = new int[w][h];
            for (int row = 0; row < w; row++) {
                for (int col = 0; col < h; col++) {
                    data.getElemFloat(row);
                    array[row][col] = raster.getSample(row, col, 0);
                }

            }
            return array;
        } catch (final Exception e) {
            LOGG.info("Something went wrong while converting the image to 8-bit.\nImage path was:" + imagePath);
        }
        return null;
    }

    /**
     * @param images      Et array med hvilke data som skal lagres. F.eks trening
     * @param datasetPath Stien til datasettet.
     */
    private void readImagesToArray(final int[][][] images, final String datasetPath) {
        int counter = 0;
        final File f = new File(datasetPath);
        final ArrayList<String> folderPaths = new ArrayList<>(Arrays.asList(f.list()));
        for (final String folder : folderPaths) {
            if (folder.equalsIgnoreCase(".DS_STORE")) continue;
            final String imageFolderPath = datasetPath + "/" + folder;
            final ArrayList<String> imagePaths = getImagePaths(imageFolderPath);
            for (final String imagePath : imagePaths) {
                images[counter++] = getImageAsArray(imageFolderPath + "/" + imagePath);
            }
        }
    }

    private ArrayList<String> getImagePaths(final String imagePath) {
        final File f = new File(imagePath);
        return new ArrayList<>(Arrays.asList(f.list()));
    }

    public int[][][] getTrainingImages() {
        return treningsdata.getBilder();
    }

    public int[][][] getTestImages() {
        return testdata.getBilder();
    }

    public int[] getTrainingLabels() {
        return treningsdata.getFasit();
    }

    public int[] getTestLabels() {
        return testdata.getFasit();
    }

    /**
     * Intern klasse som brukes til å holde features (bildedata) og fasit (klasse) for datasettene.
     */
    private final class Datasett {

        final int[][][] bilder;
        final int[] fasit;

        private Datasett(final int[][][] bilder, final int[] fasit) {
            this.bilder = bilder;
            this.fasit = fasit;
        }

        private int[][][] getBilder() {
            return bilder;
        }

        private int[] getFasit() {
            return fasit;
        }
    }

}
