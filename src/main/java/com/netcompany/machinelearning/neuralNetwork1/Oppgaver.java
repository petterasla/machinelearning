package com.netcompany.machinelearning.neuralNetwork1;


import com.netcompany.machinelearning.preprocessing.DataLoader;
import com.netcompany.machinelearning.preprocessing.DataHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Oppgaver {

    private static final Logger LOGG = LoggerFactory.getLogger(Oppgaver.class);

    public static void main(final String[] args) throws Exception {

        // ###############################################################
        // OPPGAVE 1: PREPROSSESERING -> FLAT UT OG NORMALISERE
        // ###############################################################

        final Boolean lesInnHeleDatasettet = false;
        final DataLoader dataLoader = new DataLoader(lesInnHeleDatasettet);

        // a) Flat ut bildene
        final double[][][] trainingImages = normalize(dataLoader.getTrainingImages());
        final double[][][] testbilder = normalize(dataLoader.getTestImages());

        // b) Normaliser bildene
        final double[][] trainingFlatImages = flatMapImage(trainingImages);
        final double[][] testFlatBilder = flatMapImage(testbilder);

        // ###############################################################
        // OPPGAVE 2: NEVRALT NETTVERK -> BYGG NETTVERK OG KJØR
        // ###############################################################

        final NevraltNettverk nevraltNettverk =
                new NevraltNettverkBygger()
                        .leggTilLag(784, 100)
                        .leggTilLag(100, 10)
                        .bygg();

        nevraltNettverk.setAntallEpoker(550);
        nevraltNettverk.setBatchStorrelse(50);

        LOGG.info("Trener..");
        nevraltNettverk.tren(trainingFlatImages, dataLoader.getTrainingLabels());

        LOGG.info("Ferdig trent. Evaluerer...");
        nevraltNettverk.evaluer(testFlatBilder, dataLoader.getTestLabels());

        LOGG.info("FERDIG");

    }

    private static double[][][] normalize(final int[][][] trainingImages) {
        final int numberOfImages = trainingImages.length;
        final int width = DataHelper.width;
        final int height = DataHelper.height;

        final double[][][] normalizedImages = new double[numberOfImages][width][height];

        final double MAX_PIXEL_VERDI = 255.0; // FORDI VI VET DET :)

        for (int iCounter = 0; iCounter < numberOfImages; iCounter++) {
            for (int row = 0; row < width; row++) {
                for (int col = 0; col < height; col++) {
                    normalizedImages[iCounter][row][col] = (trainingImages[iCounter][row][col] / MAX_PIXEL_VERDI) * 2 - 1;
                }
            }
        }
        return normalizedImages;
    }

    private static double[][] flatMapImage(final double[][][] images) {
        final int numberOfImages = images.length;
        final double[][] flatImages = new double[numberOfImages][images[0].length * images[0][0].length];

        for (int i = 0; i < numberOfImages; i++) {
            flatImages[i] = flatImage(images[i]);
        }
        return flatImages;
    }

    private static double[] flatImage(final double[][] image) {
        int counter = 0;
        final double[] flatImage = new double[image.length * image[0].length];
        for (final double[] anImage : image) {
            for (final double anAnImage : anImage) {
                flatImage[counter] = anAnImage;
                counter++;
            }
        }
        return flatImage;
    }
}
