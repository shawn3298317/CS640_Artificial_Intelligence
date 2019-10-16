import os, sys
import math
import random
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt

from nn import NeuralNetwork
from math_util import *
from Logging import Logging

TASK_BINARY_CLASS = "binary_class"
TASK_MULTI_CLASS = "multi_class"
TASK_REGRESSION = "regression"

def convert_to_labels(y):
    ans = []
    for row in y:
        ans.append(np.argmax(row))
    return np.array(ans)

def getData(data_dir, fn_x, fn_y):
    '''
    Returns
    -------
    X : numpy matrix
        Input data samples.
    Y : numpy matrix
        Input one-hot labels.
    '''
    # TO-DO for this part:
    # Use your preferred method to read the csv files.
    # Write your codes here:
    x_path = data_dir + fn_x
    y_path = data_dir + fn_y
    Logging.info("Loading X from %s..." % x_path)
    Logging.info("Loading Y from %s..." % y_path)
    X = pd.read_csv(x_path, sep=",", header=None).values
    Y = pd.read_csv(y_path, sep=",", header=None)
    K = len(Y[0].unique())
    Y = np.array([np.eye(K)[int(y[0])] for y in Y.values])
    # Hint: use print(X.shape) to check if your results are valid.
    return X, Y

def normalize(X):
    X = (X - np.mean(X, axis=1, keepdims=True)) / np.std(X, axis=1, keepdims=True)
    return X

def splitData(X, Y, k_fold, K = 5):
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

    # N = math.floor(X.shape[0] / K * (K-1))
    N = math.floor(X.shape[0] / K)
    train_ind = shuffled_ind[:k_fold*N] + shuffled_ind[(k_fold+1)*N:]
    test_ind = shuffled_ind[k_fold*N: (k_fold+1)*N]

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
    plt.figure()
    x1_array, x2_array = np.meshgrid(np.arange(-4, 4, 0.01), np.arange(-4, 4, 0.01))
    grid_coordinates = np.c_[x1_array.ravel(), x2_array.ravel()]
    if model:
        Z = model.predict(grid_coordinates)
        Z = Z.reshape(x1_array.shape)
        plt.contourf(x1_array, x2_array, Z, cmap=plt.cm.bwr)
    plt.scatter(X[:, 0], X[:, 1], c=(1 - Y[:, 0]), s=5, cmap=plt.cm.bwr)


def train(XTrain, YTrain, args):
    """
    This function is used for the training phase.
    Parameters
    ----------
    XTrain : numpy matrix
        The matrix containing samples features (not indices) for training.
    YTrain : numpy matrix
        The array containing labels for training.
    args : List
        The list of parameters to set up the NN model.
    Returns
    -------
    NN : NeuralNetwork object
        This should be the trained NN object.
    """
    # 1. Initializes a network object with given args.
    nn = NeuralNetwork(args["NNodes"], args["activate"], args["deltaActivate"], args["task"])
    
    # 2. Train the model with the function "fit".
    # (hint: use the plotDecisionBoundary function to visualize after training)
    # Parameters TODO: arguments or script
    # Neural Network Execution
    nn.fit(XTrain, YTrain, args["learningRate"], args["epochs"], args["regLambda"], args["batchSize"])

    # 3. Return the model.
    return nn

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
    YPredict : numpy matrix
        The predictions of X.
    """
    # test_labels = model.predict(XTest)
    return model.forward(XTest)

def getConfusionMatrix(YTrue, YPredict):
    """
    Computes the confusion matrix.
    Parameters
    ----------
    YTrue : numpy matrix
        This array contains the ground truth.
    YPredict : numpy matrix
        This array contains the predictions.
    Returns
    CM : square numpy matrix
        The confusion matrix as shown below
                    Predicted
                    2 1 0
                    _ _ _
        Actual  2|
                1|
                0|
    """
    YTrue = convert_to_labels(YTrue)
    #YPredict = convert_to_labels(YPredict)

    num_classes = np.unique(YTrue).shape[0]
    cm = np.zeros((num_classes, num_classes))
    for i in range(YTrue.shape[0]):
        cm[YTrue[i]][YPredict[i]] = cm[YTrue[i]][YPredict[i]] + 1
    return np.matrix(cm)

    # tp = 0
    # fn = 0
    # fp = 0
    # tn = 0
    # for i in range(YTrue.shape[0]):
    #     if (YPredict[i] == YTrue[i] == 1):
    #         tp += 1
    #     if (YPredict[i] == YTrue[i] == 0):
    #         tn += 1
    #     if (YPredict[i] == 1 and YTrue[i] == 0):
    #         fp += 1
    #     if (YPredict[i] == 0 and YTrue[i] == 1):
    #         fn += 1
    # return np.matrix([[tp, fn], [fp, tn]])


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
    precision : numpy array
        The class wise precision
    """
    n = cm.shape[0]
    numerator=0
    precisions=[]
    for i in range(n):
        numerator = cm.item(i, i)
        if(np.sum(cm.T[i])==0):
            precisions.append(0)
        else:
            precision = numerator/np.sum(cm.T[i])
            precisions.append(precision)
    return np.array(precisions)

def getRecall(cm):
    """
    Computes the recall
    Parameters
    ----------
    CM : square numpy matrix
        The confusion matrix.
    Returns
    recall : numpy array
        The class wise recall
    """
    n = cm.shape[0]
    numerator=0
    recalls=[]
    for i in range(n):
        numerator = cm.item(i, i)
        if(np.sum(cm[i])==0):
            recalls.append(0)
        else:
            recall = numerator/np.sum(cm[i])
            recalls.append(recall)
    return np.array(recalls)

def getF1(precisions,recalls):
    """
    Computes the f1
    Parameters
    ----------
    precision : numpy array
        Class wise Precision of the model
    recall : numpy array
        Class wise Recall of the model
    Returns
    f1 : numpy array
        The class wise f1 score
    """
    f1s=[]
    for i in range(recalls.shape[0]):
        if(recalls[i]==0 and precisions[i]==0):
            f1s.append(0)
        else:
            f1s.append(2*recalls[i]*precisions[i]/(precisions[i]+recalls[i]))
    return f1s

def getPerformanceScores(YTrue, YPredict):
    """
    Computes the accuracy, precision, recall, f1 score.
    Parameters
    ----------
    YTrue : numpy matrix
        This array contains the ground truth.
    YPredict : numpy matrix
        This array contains the predictions.
    Returns
    {"CM" : numpy matrix,
    "accuracy" : float,
    "precision" : numpy array,
    "recall" : numpy array,
    "f1" : numpy array}
        This should be a dictionary.
    """
    cm = getConfusionMatrix(YTrue, YPredict)
    # accuracy has the leading elements / total number of elements
    accuracy = getAccuracy(cm)
    precision = getPrecision(cm)
    recall = getRecall(cm)
    f1 = getF1(recall,precision)

    return {"CM" : cm, "accuracy" : accuracy, "precision" : precision, "recall" : recall,  "f1" : f1}

def getFPR(cm):
    """
    Computes the recall
    Parameters
    ----------
    CM : square numpy matrix
        The confusion matrix.
    Returns
    FPR : numpy array
        The numpy array FPR
    """
    n = cm.shape[0]
    numerator=0
    fprs=[]
    for i in range(n):
        numerator = np.sum(cm.T[i])-cm.item(i, i)
        denominator=np.sum(cm)-np.sum(cm[i])
        if(denominator==0):
            fprs.append(0)
        else:
            fpr = numerator/denominator
            fprs.append(fpr)
    return np.array(fprs)

def get_TPR_FPR(YTest,YPred):
    """
    Returns the TPR, FPR.
    Parameters
    ----------
    YTest : numpy matrix
        This array contains the ground truth.
    YPred : numpy matrix
        This array contains the predictions.

    Returns
    TPR : float
        True positive rate
    FPR : float
        False positive rate
    """
    cm = getConfusionMatrix(YTest, YPred)
    TPRs = getRecall(cm)
    FPRs = getFPR(cm)
    return TPRs, FPRs

    # cm=getConfusionMatrix(YTest, YPred)
    # TPR=getRecall(cm)
    # FPR=getFPR(cm)
    # return TPR, FPR


def get_plot_ROC(model, XTest, YTest):
    """
    Plots the ROC curve.
    Parameters
    ----------
    model : NeuralNetwork object
        This should be a trained NN model.
    XTest : numpy matrix
        The matrix containing samples features (not indices) for testing.
    YTest : numpy matrix
        This array contains the ground truth.
    Returns
    plt : matplot lib object
        The ROC plot
    """

    # for each threshold
    thresholds = np.arange(0, 1.01, 0.01)
    fprs = []
    tprs = []
    # get TPR, FPR
    for threshold in thresholds:
        YPred = model.predict(XTest, threshold)
        tpr, fpr = get_TPR_FPR(YTest, YPred)
        # Logging.info("ROC test: threshold {}, tpr {}, fpr {}".format(threshold, tpr, fpr))
        tprs.append(tpr)
        fprs.append(fpr)
    # add to X,Y
    fprs = np.matrix(fprs)
    tprs = np.matrix(tprs)

    plts = []
    count = 0

    fprs = fprs.T
    tprs = tprs.T

    # Logging.debug("fprs={}".format(fprs))
    # Logging.debug("tprs={}".format(tprs))

    for i in range(fprs.shape[0]):
        plt.plot(fprs[i], tprs[i])
        plt.title("ROC Curve for class " + str(count))
        plt.xlabel("FPR")
        plt.ylabel("TPR")
        plt.xlim(-0.05, 1.05)
        plt.ylim(-0.05, 1.05)
        count += 1
        plts.append(plt)
    return plts

def get_plot_ROC_2(model, XTest, YTest):
    """
    Plots the ROC curve.
    Parameters
    ----------
    model : NeuralNetwork object
        This should be a trained NN model.
    XTest : numpy matrix
        The matrix containing samples features (not indices) for testing.
    YTest : numpy matrix
        This array contains the ground truth.
    Returns
    plt : matplot lib object
        The ROC plot
    """
    # for each threshold
    thresholds = np.arange(0.01,1.01,0.01)
    fprs = []
    tprs = []
    # get TPR, FPR
    for threshold in thresholds:
        YPred = model.predict(XTest,threshold)
        tpr, fpr = get_TPR_FPR(YTest,YPred)
        Logging.debug("ROC test: threshold {}, tpr {} ({}), fpr {} ({})".format(threshold, tpr[1], tpr.shape, fpr[1], fpr.shape))
        tprs.append(tpr[1])
        fprs.append(fpr[1])
    # add to X,Y
    # plt.plot([f[1] for f in fprs], [t[1] for t in tprs])
    plt.figure()
    plt.plot(fprs, tprs)
    plt.title("ROC Curve")
    plt.xlabel("FPR")
    plt.ylabel("TPR")
    plt.xlim(-0.05, 1.05)
    plt.ylim(-0.05, 1.05)
    # add to plot
