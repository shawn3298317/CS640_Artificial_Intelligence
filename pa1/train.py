import numpy as np
import matplotlib.pyplot as plt
from util import *
from nn import *

if __name__ == "__main__":

    # Data prep.
    X, Y = getData("data/DataFor640/dataset1/", "LinearX.csv", "LinearY.csv")
    plotDecisionBoundary(None, X, Y)
    train_ind, test_ind = splitData(X, Y, 5)
    X_train, Y_train = X[train_ind], Y[train_ind]
    X_test, Y_test = X[test_ind], Y[test_ind]
    print("train shape:", X_train.shape)
    print("train shape:", Y_train.shape)

