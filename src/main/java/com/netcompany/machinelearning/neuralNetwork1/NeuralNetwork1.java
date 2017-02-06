package com.netcompany.machinelearning.neuralNetwork1;


import com.netcompany.machinelearning.preprocessing.DataLoader;
import com.netcompany.machinelearning.preprocessing.DataHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeuralNetwork1 {

    private static final Logger LOGG = LoggerFactory.getLogger(NeuralNetwork1.class);

    public static void main(final String[] args) throws Exception {

        // ###############################################################
        // OPPGAVE 1: PREPROSSESERING -> NORMALISERE OG FLAT UT
        // ###############################################################

        final Boolean lesInnHeleDatasettet = false;
        final DataLoader dataLoader = new DataLoader(lesInnHeleDatasettet);

        //TODO: Flate ut først, deretter normalisere
        // a) Normaliser bildene
        final double[][][] trainingImages = normalize(dataLoader.getTrainingImages());
        final double[][][] testbilder = normalize(dataLoader.getTestImages());

        // b) Flat ut bildene
        final double[][] trainingFlatImages = flatMapImage(trainingImages);
        final double[][] testFlatBilder = flatMapImage(testbilder);

        // ###############################################################
        // OPPGAVE 2: NEVRALT NETTVERK -> BYGG NETTVERK
        // ###############################################################

        final NevraltNettverk nevraltNettverk =
                new NevraltNettverkBygger()
                        .leggTilLag(784, 100)
                        .leggTilLag(100, 10)
                        .bygg();

        nevraltNettverk.setAntallEpoker(50);
        nevraltNettverk.setBatchStorrelse(50);

        LOGG.info("Trener..");
        nevraltNettverk.tren(trainingFlatImages, dataLoader.getTrainingLabels());

        LOGG.info("Ferdig trent. Evaluerer...");
        nevraltNettverk.evaluer(testFlatBilder, dataLoader.getTestLabels());

        LOGG.info("FERDIG");
        // Oppgave a)
//        final int numRows = 28;
//        final int numColumns = 28;
//        int outputNum = 10; // number of output classes
//        int batchSize = 2000; // batch size for each epoch
//        int rngSeed = 123; // random number seed for reproducibility
//        int numEpochs = 2; // number of epochs to perform
//
//        //Get the DataSetIterators:
//        LOGG.info("Fetching training data");
//        DataSetIterator mnistTrain = new MnistDataSetIterator(batchSize, true, rngSeed);
//        LOGG.info("Fetching test data");
//        DataSetIterator mnistTest = new MnistDataSetIterator(batchSize, false, rngSeed);
//
//
//        LOGG.info("Build model....");
//        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
//                .seed(rngSeed) //include a random seed for reproducibility
//                // use stochastic gradient descent as an optimization algorithm
//                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
//                .iterations(1)
//                .learningRate(0.006) //specify the learning rate
//                .updater(Updater.NESTEROVS).momentum(0.9) //specify the rate of change of the learning rate.
//                .regularization(true).l2(1e-4)
//                .list()
//                .layer(0, new DenseLayer.Builder() //create the first, input layer with xavier initialization
//                        .nIn(numRows * numColumns)
//                        .nOut(1000)
//                        .activation("relu")
//                        .weightInit(WeightInit.XAVIER)
//                        .build())
//                .layer(1, new OutputLayer.Builder() //create hidden layer
//                        .nIn(1000)
//                        .nOut(outputNum)
//                        .activation("softmax")
//                        .weightInit(WeightInit.XAVIER)
//                        .build())
//                .pretrain(false).backprop(true) //use backpropagation to adjust weights
//                .build();
//
//        MultiLayerNetwork model = new MultiLayerNetwork(conf);
//        model.init();
//        //print the score with every 1 iteration
//        model.setListeners(new ScoreIterationListener(1));
//
//        LOGG.info("Train model....");
//        for( int i=0; i<numEpochs; i++ ){
//            model.fit(mnistTrain);
//        }
//
//
//        LOGG.info("Evaluate model....");
//        Evaluation eval = new Evaluation(outputNum); //create an evaluation object with 10 possible classes
//        while(mnistTest.hasNext()){
//            DataSet next = mnistTest.next();
//            INDArray output = model.output(next.getFeatureMatrix()); //get the networks prediction
//            eval.eval(next.getLabels(), output); //check the prediction against the true class
//        }

        //LOGG.info(eval.stats());
        //LOGG.info("****************Example finished********************");
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
