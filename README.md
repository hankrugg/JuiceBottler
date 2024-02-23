# Juice Bottler
A multi-threaded simulation of an orange juice bottling operation!
## Overview
This program simulates the tasks fetching, peeling, squeezing, bottling, and processing oranges to create bottles of orange juice. 
The program uses threads for each worker responsible for each task. Each plant has a set of workers and the plant operation has a set of plants.
Running the ```PlantOperation.java``` file will run the whole program and print when and how an orange has been modified.
When the program stops, it will tell you statistics on how well your operation went. 
The program is not meant to interact with the user as is, but can be modified to increase production by adding more plants, more workers, or different types of workers.
## To Run
The repository includes a ```build.xml``` file which can run the file by the command ```ant run```.
