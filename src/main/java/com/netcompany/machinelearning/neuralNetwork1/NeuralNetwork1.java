package com.netcompany.machinelearning.neuralNetwork1;


import com.netcompany.machinelearning.preprocessing.Preprocessing;
import com.netcompany.machinelearning.preprocessing.PreprocessingFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeuralNetwork1 {

    private static Logger log = LoggerFactory.getLogger(NeuralNetwork1.class);

    public static void main(String[] args) throws Exception {

        // ###############################################################
        // OPPGAVE 1: PREPROSSESERING -> NORMALISERE OG FLAT UT
        // ###############################################################
        //TODO: Lag mindre datasett for utviklingsøyemed, slik at en slipper å laste inn hele MNIST hver gang en vil teste koden sin

        Boolean lesInnHeleDatasettet = false;
        Preprocessing preprocessing = Preprocessing.create(lesInnHeleDatasettet);

        // a) Normaliser bildene
        double[][][] trainingImages = normalize(preprocessing.getTrainingImages());
        double[][][] testbilder = normalize(preprocessing.getTestImages());

        // b) Flat ut bildene
        double[][] trainingFlatImages = flatMapImage(trainingImages);
        double[][] testFlatBilder = flatMapImage(testbilder);

        // ###############################################################
        // OPPGAVE 2: NEVRALT NETTVERK -> BYGG NETTVERK
        // ###############################################################

        NevraltNettverkBygger nevraltNettverkBygger = new NevraltNettverkBygger();
        nevraltNettverkBygger = nevraltNettverkBygger.leggTilLag(784, 32);
        nevraltNettverkBygger = nevraltNettverkBygger.leggTilLag(32, 10);

        NevraltNettverk nevraltNettverk = nevraltNettverkBygger.bygg();


        System.out.println("Trener..");
        nevraltNettverk.tren(trainingFlatImages, preprocessing.getTrainingLabels());

        System.out.println("Ferdig trent. Evaluerer...");
        nevraltNettverk.evaluer(testFlatBilder, preprocessing.getTestLabels());

        System.out.println("FERDIG");
        // Oppgave a)
//        final int numRows = 28;
//        final int numColumns = 28;
//        int outputNum = 10; // number of output classes
//        int batchSize = 2000; // batch size for each epoch
//        int rngSeed = 123; // random number seed for reproducibility
//        int numEpochs = 2; // number of epochs to perform
//
//        //Get the DataSetIterators:
//        log.info("Fetching training data");
//        DataSetIterator mnistTrain = new MnistDataSetIterator(batchSize, true, rngSeed);
//        log.info("Fetching test data");
//        DataSetIterator mnistTest = new MnistDataSetIterator(batchSize, false, rngSeed);
//
//
//        log.info("Build model....");
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
//        log.info("Train model....");
//        for( int i=0; i<numEpochs; i++ ){
//            model.fit(mnistTrain);
//        }
//
//
//        log.info("Evaluate model....");
//        Evaluation eval = new Evaluation(outputNum); //create an evaluation object with 10 possible classes
//        while(mnistTest.hasNext()){
//            DataSet next = mnistTest.next();
//            INDArray output = model.output(next.getFeatureMatrix()); //get the networks prediction
//            eval.eval(next.getLabels(), output); //check the prediction against the true class
//        }

        //log.info(eval.stats());
        //log.info("****************Example finished********************");




    }

    private static double[][][] normalize(int[][][] trainingImages) {
        int numberOfImages = trainingImages.length;
        int width = PreprocessingFactory.width;
        int height = PreprocessingFactory.height;

        double [][][] normalizedImages = new double[numberOfImages][width][height];

        double MAX_PIXEL_VERDI = 255.0; // FORDI VI VET DET :)

        for (int iCounter=0; iCounter < numberOfImages; iCounter++) {
            for (int row=0; row < width; row++) {
                for (int col=0; col < height; col++) {
                    normalizedImages[iCounter][row][col] = trainingImages[iCounter][row][col] / MAX_PIXEL_VERDI;
                }
            }
        }
        return normalizedImages;
    }

    private static double[][] flatMapImage(double[][][] images) {
        int numberOfImages = images.length;
        double[][] flatImages = new double[numberOfImages][images[0].length*images[0][0].length];

        for (int i = 0; i < numberOfImages; i++) {
            flatImages[i] = flatImage(images[i]);
        }
        return flatImages;
    }

    private static double[] flatImage(double[][] image) {
        int counter = 0;
        double[] flatImage = new double[image.length * image[0].length];
        for (int col = 0; col < image.length; col++) {
            for (int row = 0; row < image[col].length; row++) {
                flatImage[counter] = image[col][row];
                counter++;
            }
        }
        return flatImage;
    }
}
