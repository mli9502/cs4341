Author: Yihong Zhou (yzhou8), Mengwen Li (mli2)

We wrote this program in Java. The code is in "sourceFiles" folder.

To run the program directly from ".jar" file, use the following command:
java -jar project3_featureSelect.jar <input file name> <output file name>

To run the program in "./yourProgram input.csv output.csv" format on the CCC machine, use the following commands:
1. Run command "chmod +x *.sh" to give execution permission to our script.
2. Run command "./featureSelect.sh <input file name> <output file name>"

The program will check for extention of input and output files. If they are not ".csv" format, an error is reported.
The program also checks for number of command line arguments and whether the provided files can be opened, read and closed correctly.

To import the output file into Weka, open Weka first, then, click "Explorer", then, click "Open file..." and open the output ".csv" file.
To use our data set on J48 and MultilayerPerceptron, the data set should be convert to "Nominal" type first.
To do this, first click "Choose" under "Filter" under "Preprocess", then, select "NumericToNominal" under "unsupervised", then, click "Apply".


