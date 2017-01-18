package com.netcompany.machinelearning.neuralNetwork;


import com.netcompany.machinelearning.preprocessing.Preprocessing;
import org.deeplearning4j.datasets.iterator.impl.MnistDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeuralNetwork {

    private static Logger log = LoggerFactory.getLogger(NeuralNetwork.class);

    public static void main(String[] args) throws Exception {

        //number of rows and columns in the input pictures
        final int numRows = 28;
        final int numColumns = 28;
        int outputNum = 10; // number of output classes
        int batchSize = 2000; // batch size for each epoch
        int rngSeed = 123; // random number seed for reproducibility
        int numEpochs = 2; // number of epochs to perform
        double learingRate = 0.005;     // How fast the algortithm will learn
        int iterations = 1;              // Number of iterations


        // ###############################################################
        // OPPGAVE 1: PREPROSSESERING -> NORMALISERE
        // ###############################################################

        Preprocessing preprocessing = Preprocessing.create();



        //Get the data:
        DataSetIterator mnistTrain = new MnistDataSetIterator(batchSize, true, rngSeed);
        DataSetIterator mnistTest = new MnistDataSetIterator(batchSize, false, rngSeed);


        // ###############################################################

        // Oppgave a)
        MultiLayerConfiguration configuration = new NeuralNetConfiguration.Builder()
                .seed(rngSeed)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .iterations(1)
                .learningRate(learingRate)
                .updater(Updater.NESTEROVS).momentum(0.9)
                .regularization(true).l2(1e-4)
                .list()
                .layer(0, new DenseLayer.Builder()
                        .nIn(numRows * numColumns)
                        .nOut(1000)
                        .activation("relu")
                        .weightInit(WeightInit.RELU)
                        .build())
            .layer(1, new OutputLayer.Builder(LossFunctions.LossFunction.MSE)
                .nIn(1000)
                .nOut(outputNum)
                .activation("softmax")
                .weightInit(WeightInit.XAVIER)
                .build())
            .pretrain(false).backprop(true)
            .build();


        MultiLayerNetwork model = new MultiLayerNetwork(configuration);
        model.init();
        //print the score with every 1 iteration
        model.setListeners(new ScoreIterationListener(1));

        log.info("Train model....");
        for( int i=0; i<numEpochs; i++ ){
            model.fit(mnistTrain);
        }

        log.info("Evaluate model....");
        Evaluation eval = new Evaluation(outputNum); //create an evaluation object with 10 possible classes
        while(mnistTest.hasNext()){
            DataSet next = mnistTest.next();
            INDArray output = model.output(next.getFeatureMatrix()); //get the networks prediction
            eval.eval(next.getLabels(), output); //check the prediction against the true class
        }

        log.info(eval.stats());
        log.info("****************Example finished********************");



    }
}
