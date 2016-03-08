#!/usr/bin/python

# Author: Yihong Zhou (yzhou8)
#         Mengwen Li (mli2)

import sys
import getopt
import numpy as np
import matplotlib.pyplot as plt
from random import randint

# the number of nodes used in the hidden layer.
hidNodeNum = 5
# the percentage for holdout data.
holdOutPerc = 0.2
# weight has length 3 * hidNodeNum, [0: hidNodeNum] for the first input,
# [hidNodeNum: 2 * hidNodeNum] for the second input,
# [2 * hidNodeNum: 3 * hidNodeNum] for the hidden nodes output.
weight = []
# step size.
alpha = 0.15


# Print out the usage information.
def usage():
    print "python ann.py <filename> [h <number of hidden nodes> | p <holdout percentage>]"


# Define a sigmoid function.
def sigmoid(z):
    return 1 / (1 + np.exp(-z))


# Define the derivitive of sigmoid function.
def dsigmoid(z):
    return sigmoid(z) * (1 - sigmoid(z))


# Define a 3-layer neural network.
def ann3(x, y):
    # x * weight as input for hidden nodes.
    hidInputX = np.dot(x, weight[0: hidNodeNum])
    # y * wieght as input for hidden nodes.
    hidInputY = np.dot(y, weight[hidNodeNum: 2 * hidNodeNum])
    # sum them together and input to the hidden nodes.
    hidInput = np.add(hidInputX, hidInputY)
    # use sigmoid function to get outputs.
    hidOutput = sigmoid(hidInput)
    # outputs from hidden nodes multiply by corresponding weights as input for output.
    outInput = hidOutput * weight[2 * hidNodeNum: 3 * hidNodeNum]
    # use sigmoid function to get output
    output = sigmoid(np.sum(outInput))
    return output


# Function used to assign random value to weights.
def randWeight():
    global weight
    # random values for weight.
    for i in range(0, 3 * hidNodeNum):
        weight.append(randint(-10, 10))


# Use back propagation to train our neural net.
def backPropLearning(trainingSet):
    global alpha
    randWeight()
    # repeat 180 times.
    for t in range(0, 180):
        for exp in trainingSet:
            '''
            for each node i in input layer do
                ai <- xi
            '''
            a1 = []
            for i in range(0, 2):
                a1.append(exp[i])
            '''
            for l = 2 to L do
                for each node j in layer l do
                    inj <- sigma(wi,j*ai)
                    aj <- g(inj)
            '''
            in2 = np.add(np.dot(a1[0], weight[0: hidNodeNum]), np.dot(a1[1], weight[hidNodeNum: 2 * hidNodeNum]))
            a2 = sigmoid(in2)
            in3 = np.sum(a2 * weight[2 * hidNodeNum: 3 * hidNodeNum])
            a3 = sigmoid(in3)
            '''
            Propagate deltas backward from output layer to input layer.
            for each node j in the output layer do
                delta[j] <- g'(inj) * (yj - aj)
            '''
            delta3 = dsigmoid(in3) * (exp[2] - a3)
            delta2 = np.dot(delta3, dsigmoid(in2) * (weight[2 * hidNodeNum: 3 * hidNodeNum]))
            delta1 = []
            delta1.append(dsigmoid(a1[0]) * np.sum(weight[0: hidNodeNum] * delta2))
            delta1.append(dsigmoid(a1[1]) * np.sum(weight[hidNodeNum: 2 * hidNodeNum] * delta2))
            '''
            Update every weight in network using deltas.
            for each weight wi,j in network do
                wi,j <- wi,j + alpha * ai * delta[j]
            '''
            weight[0: hidNodeNum] = weight[0: hidNodeNum] + np.dot(alpha * a1[0], delta2)
            weight[hidNodeNum: hidNodeNum * 2] = weight[hidNodeNum: hidNodeNum * 2] + np.dot(alpha * a1[1], delta2)
            weight[hidNodeNum * 2: hidNodeNum * 3] = weight[hidNodeNum * 2: hidNodeNum * 3] + np.dot(alpha * delta3, a2)


# function to classify validation set and report error rate.
def classify(validationSet):
    wrongCnt = 0
    totalCnt = 0
    rtnSet = []
    for val in validationSet:
        row = []
        row.append(val[0])
        row.append(val[1])
        rtn = float(round(ann3(val[0], val[1])))
        if rtn != val[2]:
            wrongCnt += 1
        totalCnt += 1
        row.append(rtn)
        rtnSet.append(row)
    print "Error rate is ", float(float(wrongCnt) / float(totalCnt))
    # return set is used for plotting when test.
    return rtnSet


# Plot a given data set. 1 as red and 0 as blue.
def plotSet(inputSet):
    x = []
    y = []
    colors = []
    for i in range(0, len(inputSet)):
        x.append(inputSet[i][0])
        y.append(inputSet[i][1])
        if inputSet[i][2] == float(1):
            colors.append('r')
        else:
            colors.append('b')
    plt.scatter(x, y, c=colors)
    plt.show()


def main():
    global hidNodeNum
    global holdOutPerc
    # Parse the command line arguments to determine hidden node number, hold out percentage and input file.
    tempArg = []
    for i in range(len(sys.argv)):
        if sys.argv[i] == '-h' or sys.argv[i] == '-p':
            print "command line argument error"
            exit(-1)
        if sys.argv[i] == 'h':
            tempArg.append('-h')
        elif sys.argv[i] == 'p':
            tempArg.append('-p')
        else:
            tempArg.append(sys.argv[i])
    try:
        opts, args = getopt.gnu_getopt(tempArg[1:], "h:p:")
    except getopt.GetoptError as err:
        print(err)
        usage()
        sys.exit(-1)
    for o, a in opts:
        if o == '-h':
            try:
                hidNodeNum = int(a)
            except ValueError:
                print "Need an integer for h option."
                exit(-1)
        if o == '-p':
            try:
                holdOutPerc = float(a)
            except ValueError:
                print "Need an float for p option."
                exit(-1)
    if len(args) != 1:
        print "Need one input file name."
        usage()
        exit(-1)
    fileName = args[0]

    # Read in the data file.
    fileData = []
    fp = open(fileName, 'r')
    for line in fp:
        # print line
        lineData = line.split()
        # print lineData
        for i in range(0, len(lineData)):
            lineData[i] = float(lineData[i])
        fileData.append(lineData)
    fp.close()
    trainingSet = []
    validationSet = []
    # Split the data into training set and validation set according to hold out percentage.
    for i in range(0, len(fileData)):
        if i < int(len(fileData) * (1 - holdOutPerc)):
            trainingSet.append(fileData[i])
        else:
            validationSet.append(fileData[i])
    # for i in range(0, len(fileData)):
    #     if i < int(len(fileData) * holdOutPerc):
    #         validationSet.append(fileData[i])
    #     else:
    #         trainingSet.append(fileData[i])

    # plotSet(validationSet)
    # Use back propogation to train our neural network.
    backPropLearning(trainingSet)
    # Use the neural network to classify the validation set and calculate error rate.
    classify(validationSet)
    # plotSet(classify(validationSet))


if __name__ == "__main__":
    main()

