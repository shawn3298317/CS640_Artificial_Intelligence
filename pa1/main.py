import numpy as np
import matplotlib.pyplot as plt
from util import *
from nn import *
from math_util import *
from Logging import Logging

if __name__ == "__main__":

    # Hyper param settings
    args = {
        "NNodes": 3,
        "activate": relu,
        "deltaActivate": d_relu,
        "learningRate": 0.015,
        "epochs": 5,
        "regLambda": 0.01,
        "batchSize": 20,
        "task": TASK_MULTI_CLASS
        # "task": "regression"
    }
    Logging.info("HyperParams:")
    for k, v in args.items():
        Logging.info("\t%s: %s" % (k, v))

    # Data prep.
    # X, Y = getData("data/DataFor640/dataset1/", "LinearX.csv", "LinearY.csv")
    # X, Y = getData("data/DataFor640/dataset1/", "NonLinearX.csv", "NonLinearY.csv")
    X, Y = getData("data/DataFor640/dataset2/", "Digit_x.csv", "Digit_y.csv")
    train_ind, test_ind = splitData(X, Y, 2)
    X_train, Y_train = X[train_ind], Y[train_ind]
    X_test, Y_test = X[test_ind], Y[test_ind]
    Logging.info("X train shape: {}".format(X_train.shape))
    Logging.info("Y train shape: {}".format(Y_train.shape))
    Logging.info("X test shape: {}".format(X_test.shape))
    Logging.info("Y test shape: {}".format(Y_test.shape))
    model = train(X_train, Y_train, args)
    test_labels = test(X_test, model)
    if args["task"] != "regression":
        metrics = getPerformanceScores(Y_test, test_labels)
        Logging.info("Performance metrics: {}".format(metrics))
        plt = get_plot_ROC(model,X_test,Y_test)
        if plt:
            plt.show()
    else:
        y_predict = model.predict(X_test)
        test_cost = model.getCost(Y_test, y_predict)
        Logging.info("Test Loss: %.3f" % test_cost)
