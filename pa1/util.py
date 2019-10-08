import os, sys
import math
import random
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt

def getData(data_dir, fn_x, fn_y):
    '''
    Returns
    -------
    X : numpy matrix
        Input data samples.
    Y : numpy array
        Input data labels.
    '''
    # TO-DO for this part:
    # Use your preferred method to read the csv files.
    # Write your codes here:
    x_path = data_dir + fn_x
    y_path = data_dir + fn_y
    print("Loading X from %s...\nLoading Y from %s..." % (x_path, y_path))
    X = pd.read_csv(x_path, sep=",", header=None).values
    Y = pd.read_csv(y_path, sep=",", header=None).values

    # Hint: use print(X.shape) to check if your results are valid.
    return X, Y

def splitData(X, Y, K = 5):
    '''
    Returns
    -------
    result : List[[train, test]]
        "train" is a list of indices corresponding to the training samples in the data.
        "test" is a list of indices corresponding to the testing samples in the data.
        For example, if the first list in the result is [[0, 1, 2, 3], [4]], then the 4th
        sample in the data is used for testing while the 0th, 1st, 2nd, and 3rd samples
        are for training.
    '''

    # Make sure you shuffle each train list.
    assert(X.shape[0] == Y.shape[0])
    shuffled_ind = list(range(X.shape[0]))
    random.shuffle(shuffled_ind)

    N = math.floor(X.shape[0] / K * (K-1))
    train_ind, test_ind = shuffled_ind[:N], shuffled_ind[N:]
    print("Training set size: %i %s\nTesting set size: %i %s" % (len(train_ind), repr(train_ind[:5]), len(test_ind), repr(test_ind[:5])))

    return (train_ind, test_ind)

def plotDecisionBoundary(model, X, Y):
    """
    Plot the decision boundary given by model.
    Parameters
    ----------
    model : model, whose parameters are used to plot the decision boundary.
    X : input data
    Y : input labels
    """
    x1_array, x2_array = np.meshgrid(np.arange(-4, 4, 0.01), np.arange(-4, 4, 0.01))
    grid_coordinates = np.c_[x1_array.ravel(), x2_array.ravel()]
    if model:
        Z = model.predict(grid_coordinates)
        Z = Z.reshape(x1_array.shape)
        plt.contourf(x1_array, x2_array, Z, cmap=plt.cm.bwr)
    plt.scatter(X[:, 0], X[:, 1], c=Y[:, 0], s=5, cmap=plt.cm.bwr)
    plt.show()

def train(XTrain, YTrain, args):
    """
    This function is used for the training phase.
    Parameters
    ----------
    XTrain : numpy matrix
        The matrix containing samples features (not indices) for training.
    YTrain : numpy array
        The array containing labels for training.
    args : List
        The list of parameters to set up the NN model.
    Returns
    -------
    NN : NeuralNetwork object
        This should be the trained NN object.
    """
    # 1. Initializes a network object with given args.


    # 2. Train the model with the function "fit".
    # (hint: use the plotDecisionBoundary function to visualize after training)


    # 3. Return the model.

    pass

def test(XTest, model):
    """
    This function is used for the testing phase.
    Parameters
    ----------
    XTest : numpy matrix
        The matrix containing samples features (not indices) for testing.
    model : NeuralNetwork object
        This should be a trained NN model.
    Returns
    -------
    YPredict : numpy array
        The predictions of X.
    """
    pass

def getConfusionMatrix(YTrue, YPredict):
    """
    Computes the confusion matrix.
    Parameters
    ----------
    YTrue : numpy array
        This array contains the ground truth.
    YPredict : numpy array
        This array contains the predictions.
    Returns
    CM : square numpy matrix
        The confusion matrix as shown below
                    Predicted
                    1 0
                    _ _
        Actual  1|
                0|
    """
    tp = 0
    fn = 0
    fp = 0
    tn = 0
    for i in range(YTrue.shape[0]):
        if (YPredict[i] == YTrue[i] == 1):
            tp += 1
        if (YPredict[i] == YTrue[i] == 0):
            tn += 1
        if (YPredict[i] == 1 and YTrue[i] == 0):
            fp += 1
        if (YPredict[i] == 0 and YTrue[i] == 1):
            fn += 1
    return np.matrix([[tp, fn], [fp, tn]])

def getAccuracy(cm):
    """
    Computes the accuracy .
    Parameters
    ----------
    CM : square numpy matrix
        The confusion matrix.
    Returns
    accuracy : float
        The accuracy
    """
    n = cm.shape[0]
    numerator=0
    for i in range(n):
        numerator += cm.item(i, i)
    accuracy = numerator / np.sum(cm)
    return accuracy

def getPrecision(cm):
    """
    Computes the precision
    Parameters
    ----------
    CM : square numpy matrix
        The confusion matrix.
    Returns
    precision : float
        The precision
    """
    # assuming only a 2X2 matrix
    numerator = cm.item(0, 0)
    precision = numerator / np.sum(cm.T[0])
    return precision

def getRecall(cm):
    """
    Computes the recall
    Parameters
    ----------
    CM : square numpy matrix
        The confusion matrix.
    Returns
    recall : float
        The recall
    """
    # assuming only a 2X2 matrix
    numerator = cm.item(0, 0)
    recall = numerator / np.sum(cm[0])
    return recall

def getF1(precision,recall):
    """
    Computes the f1
    Parameters
    ----------
    precision : float
        Precision of the model
    recall : float
        Recall of the model
    Returns
    f1 : float
        The f1
    """
    return 2*recall*precision/(precision+recall)

def getPerformanceScores(YTrue, YPredict):
    """
    Computes the accuracy, precision, recall, f1 score.
    Parameters
    ----------
    YTrue : numpy array
        This array contains the ground truth.
    YPredict : numpy array
        This array contains the predictions.
    Returns
    {"CM" : numpy matrix,
    "accuracy" : float,
    "precision" : float,
    "recall" : float,
    "f1" : float}
        This should be a dictionary.
    """
    cm=getConfusionMatrix(YTrue, YPredict)
    # accuracy has the leading elements / total number of elements
    accuracy=getAccuracy(cm)
    precision=getPrecision(cm)
    recall=getRecall(cm)
    f1=getF1(recall,precision)

    return {"CM" : cm, "accuracy" : accuracy, "precision" : precision, "recall" : recall,  "f1" : f1}