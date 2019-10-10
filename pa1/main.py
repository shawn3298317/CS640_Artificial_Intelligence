import numpy as np
import matplotlib.pyplot as plt
from util import *
from nn import *
from math_util import *
from Logging import Logging

def main(args):
    Logging.info("HyperParams:")
    for k, v in args.items():
        Logging.info("\t%s: %s" % (k, v))

    # Data prep.
    X, Y = getData("data/DataFor640/dataset1/", "LinearX.csv", "LinearY.csv")
    # X, Y = getData("data/DataFor640/dataset1/", "NonLinearX.csv", "NonLinearY.csv")
    # X, Y = getData("data/DataFor640/dataset2/", "Digit_x.csv", "Digit_y.csv")
    train_ind, test_ind = splitData(X, Y, 5)
    X_train, Y_train = X[train_ind], Y[train_ind]
    X_test, Y_test = X[test_ind], Y[test_ind]
    Logging.info("X train shape: {}".format(X_train.shape))
    Logging.info("Y train shape: {}".format(Y_train.shape))
    Logging.info("X test shape: {}".format(X_test.shape))
    Logging.info("Y test shape: {}".format(Y_test.shape))
    model = train(X_train, Y_train, args)
    if Y_train.shape[1] == 2:
        plotDecisionBoundary(model, X_train, Y_train)
    y_predict = model.predict(X_test)

    metrics = getPerformanceScores(Y_test, y_predict)
    Logging.info("Confusion metric: \n{}".format(metrics["CM"]))
    Logging.info("Accuracy: {:.4f}".format(metrics["accuracy"]))
    Logging.info("Precision: {}".format(["%.3f" % f for f in metrics["precision"]]))
    Logging.info("Recall: {}".format(["%.3f" % f for f in metrics["recall"]]))
    Logging.info("F1: {}".format(["%.3f" % f for f in metrics["f1"]]))
    if Y_test.shape[1] == 2: # plot ROC curve only for binary classification dataset
        get_plot_ROC_2(model,X_test,Y_test)
    test_cost = model.getCost(Y_test, model.forward(X_test))
    Logging.info("Test Loss: %.3f" % test_cost)
    plt.show()

    return metrics

if __name__ == "__main__":

    # Hyper param settings
    args = {
        "NNodes": 100,
        "activate": relu,
        "deltaActivate": d_relu,
        "learningRate": 0.015,
        "epochs": 200,
        "regLambda": 0.1,
        "batchSize": 20,
        "task": TASK_MULTI_CLASS
    }
    main(args)

    