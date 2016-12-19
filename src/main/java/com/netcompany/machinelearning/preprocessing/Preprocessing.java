package com.netcompany.machinelearning.preprocessing;


import com.netcompany.machinelearning.data.FetchMNISTData;
import org.datavec.image.mnist.MnistFetcher;
import org.deeplearning4j.datasets.fetchers.MnistDataFetcher;

import java.io.File;
import java.io.IOException;

public class Preprocessing {


    public static void main(String[] args) {
        Preprocessing preprocessing = new Preprocessing();

        File file = preprocessing.getMnistData();
    }

    private File getMnistData() {

        FetchMNISTData mnistFetcher = new FetchMNISTData();

        try {
            File file = mnistFetcher.downloadAndUntar();
            return file;
        } catch (IOException e) {
            System.out.println("Couldnt fetch the MNIST data");
        }
        return null;

    }

}
