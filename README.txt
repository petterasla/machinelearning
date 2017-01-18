Data:
    Legg til data under com/netcompany/machinelearning/data/
    Den fulle pathen skal da se slik ut:
    "nc-maskinlæring/src/main/java/com/netcompany/machinelearning/data/mnist_png/testing og training"
    
    Her skal det ligge 60,000 treningsbilder og 10,000 testbilder
    
Oppgaver:
    1 - Preprossesering:
            - Start med å kalle Preprocessing.create() for å laste inn alle bilder
              i ett tredimensjonal int array int[][][]. 
            - Man vil ha tilgang til treningsbildene via getTrainingImages()
              og testbildene via getTestImages().
            - Print bilde i consoloen via PreprocessingFactory.printImage(int[][] image)
            
            - Oppgave: Normaliser 8-bits bildene til -1 (eller 0) og 1. 
    