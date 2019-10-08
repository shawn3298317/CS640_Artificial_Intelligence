import numpy as np
import os, sys
import datetime

from util import *

LOG_LEVEL = 'INFO' # Possible inputs: DEBUG, INFO, WARNING

# Utils - TODO: import Utils
class Utils:
    @staticmethod
    def data_iterator(X, Y, batch_size):
        # print(X.shape[0]//batch_size)
        # input()
        for i in range(X.shape[0]//batch_size):
            yield(X[i*batch_size: (i+1)*batch_size], Y[i*batch_size: (i+1)*batch_size])

    @staticmethod
    def relu(x):
        return x * (x > 0)

    @staticmethod
    def d_relu(x):
        return 1. * (x > 0)

    @staticmethod
    def sigmoid(x):
        return 1 / (1+np.exp(-x))

    @staticmethod
    def d_sigmoid(x):
        return Utils.sigmoid(x) * (1 - Utils.sigmoid(x))

# Logging - TODO: import logging
class Logging:
    @staticmethod
    def log(s, level):
        timestamp = datetime.datetime.now()
        if LOG_LEVEL == 'DEBUG':
            if level in ['DEBUG', 'INFO', 'WARNING']:
                print("{} | {} | {}".format(level, timestamp, s))
        elif LOG_LEVEL == 'INFO':
            if level in ['INFO', 'WARNING']:
                print("{} | {} | {}".format(level, timestamp, s))
        elif LOG_LEVEL == 'WARNING':
            if level in ['WARNING']:
                print("{} | {} | {}".format(level, timestamp, s))

    @staticmethod
    def debug(s):
        Logging.log(s, 'DEBUG')

    @staticmethod
    def info(s):
        Logging.log(s, 'INFO')
        
    @staticmethod
    def warning(s):
        Logging.log(s, 'WARNING')

class NeuralNetwork:
    def __init__(self, NNodes, activate, deltaActivate):
        self.NNodes = NNodes # the number of nodes in the hidden layer
        self.activate = activate # a function used to activate
        self.deltaActivate = deltaActivate # the derivative of activate
        self.count = 0

    def fit(self, X, Y, learningRate, epochs, regLambda, batchSize=5):
        """
        This function is used to train the model.
        Parameters
        ----------
        X : numpy matrix
            The matrix containing sample features for training.
        Y : numpy array
            The array containing sample labels for training.
        Returns
        -------
        None
        """
        # Initialize your weight matrices first.
        # (hint: check the sizes of your weight matrices first!)

        self.batch_size = batchSize
        self.reg_lambda = regLambda
        self.lr = learningRate
        self.input_dim = X.shape[1]
        self.output_dim = Y.shape[1]

        self.initialize_param()
        
        # For each epoch, do
        for ep in range(epochs):
            Logging.debug("Starting %i epoch" % ep) # DEBUG, INFO, WARNING
            # For each training sample (X[i], Y[i]), do
            
            for x_batch, y_batch in Utils.data_iterator(X, Y, self.batch_size):
                
                Logging.debug("Start forward step!")
                # 1. Forward propagate once. Use the function "forward" here!
                self.forward(x_batch)

                Logging.debug("Back propagating")
                # 2. Backward progate once. Use the function "backpropagate" here!
                self.backpropagate(x_batch, y_batch)
            self.count = 0

    def initialize_param(self):
        self.W_1 = np.random.uniform(-1, 1, (self.NNodes, self.input_dim))
        self.W_2 = np.random.uniform(-1, 1, (self.NNodes, self.output_dim))
        self.b_0 = np.random.uniform(-1, 1, (self.NNodes, 1))
        self.b_1 = np.random.uniform(-1, 1, (self.output_dim, 1))
        # self.W_1 = np.ones((self.NNodes, self.input_dim))
        # self.W_2 = np.ones((self.NNodes, self.output_dim))
        # self.b_0 = 0.5 * np.ones((self.NNodes, 1))
        # self.b_1 = 0.5 * np.ones((self.output_dim, 1))

        Logging.debug("W1: %s" % repr(self.W_1.shape))
        Logging.debug("W2: %s" % repr(self.W_2.shape))
        Logging.debug("b_0: %s " % repr(self.b_0.shape))
        Logging.debug("b_1: %s " % repr(self.b_1.shape))

    def predict(self, X):
        """
        Predicts the labels for each sample in X.
        Parameters
        X : numpy matrix
            The matrix containing sample features for testing.
        Returns
        -------
        YPredict : numpy array
            The predictions of X.
        ----------
        """
        self.forward(X)
        YPredict = np.where(self.y_hat > 0.5, 1, 0)

        return YPredict

    def forward(self, X):
        # Perform matrix multiplication and activation twice (one for each layer).
        # (hint: add a bias term before multiplication)
        
        # input layer to 1st hidden layer
        self.a_1 = (np.matmul(self.W_1, X.T) + self.b_0).T # W1^T * X + B_0 | W1=num_nodes_hidden x 2(input nodes), X = BTx2, a1 = BT x num_nodes_hidden
        Logging.debug("a_1: %s" % repr(self.a_1.shape))
        
        # 1st hidden layer - activation
        self.h_1 = self.activate(self.a_1)  # h1 = BT x num_nodes_hidden
        Logging.debug("h_1: %s" % repr(self.h_1.shape))
        
        # output layer
        self.a_2 = np.matmul(self.h_1, self.W_2) + self.b_1 # a2 = BT x 1, h1= BT x num_nodes_hidden, W2 = num_nodes_hidden x 1, b1 = BTx1
        #TODO: np.repeat(self.b_1, self.batch_size, axis=1)
        Logging.debug("a_2: %s" % repr(self.a_2.shape))
        
        self.y_hat = Utils.sigmoid(self.a_2) # = BT x 1
        Logging.debug("y_hat: %s" % repr(self.y_hat.shape))

    def backpropagate(self, X, Y):
        # Compute loss / cost using the getCost function.
        self.loss = self.getCost(Y, self.y_hat)  # Y = BT x 1, y_hat = BT x 1
        Logging.info("Loss: {} | count = {}".format(self.loss, self.count))
        self.count += 1

        # Compute gradient for each layer.
        g = (self.y_hat - Y) / Utils.d_sigmoid(self.a_2)    # BTx1 / BTx1 = BT x 1
        Logging.debug("g_loss: {}, {}".format(g, g.shape))
        
        # TODO: for loop for variable hidden layers
        
        # Hidden Layer
        g = g * Utils.d_sigmoid(self.a_2)   # g = BT x 1
        Logging.debug("g_a2: {}, {}".format(g, g.shape))
        
        grad_b_1 = np.sum(g, axis=0, keepdims=True) # TODO: include lambda  # g = BT x 1
        grad_W_2 = np.matmul(self.h_1.T, g) # TODO: include lambda  # g = BT x 1, h1 = BT x num_nodes_hidden, grad_w2 = num_nodes_hidden x 1
        Logging.debug("grad_b_1: {}, {}".format(grad_b_1, grad_b_1.shape))
        Logging.debug("grad_W_2: {}, {}".format(grad_W_2, grad_W_2.shape))

        g = np.matmul(g, self.W_2.T) # g=BT x 1, w2 = #num_nodes_hidden x 1, g(result) = BT x num_nodes_hidden
        Logging.debug("g_h1: {}, {}".format(g, g.shape))

        # Input Layer
        g = g * self.deltaActivate(self.a_1)   # g = BT x num_nodes_hidden, a_1 = BT x num_nodes_hidden
        Logging.debug("g_a1: {}, {}".format(g, g.shape)) # BT x num_nodes_hidden
        
        grad_b_0 = np.sum(g, axis=0, keepdims=True).T # TODO: include lambda  # g = BT x num_nodes_hidden, b0 = BT x num_nodes_hidden
        grad_W_1 = np.matmul(g.T, X) # TODO: include lambda  # g = BTxnum_nodes_hidden, X = BTx2, grad_w1 = num_nodes_hidden x2
        
        Logging.debug("grad_b_0: {}, {}".format(grad_b_0, grad_b_0.shape))
        Logging.debug("grad_W_1: {}, {}".format(grad_W_1, grad_W_1.shape))

        # Update weight matrices w/t stochastic gradient descent
        self.W_2 -= self.lr * grad_W_2 / self.batch_size
        self.b_1 -= self.lr * grad_b_1 / self.batch_size
        self.W_1 -= self.lr * grad_W_1 / self.batch_size
        self.b_0 -= self.lr * grad_b_0 / self.batch_size

        Logging.debug("Updated W_2: {}, {}".format(self.W_2, self.W_2.shape))
        Logging.debug("Updated b_1: {}, {}".format(self.b_1, self.b_1.shape))
        Logging.debug("Updated W_1: {}, {}".format(self.W_2, self.W_2.shape))
        Logging.debug("Updated b_0: {}, {}".format(self.b_0, self.b_0.shape))

    def getCost(self, YTrue, YPredict):
        # Compute loss / cost in terms of crossentropy.
        # (hint: your regularization term should appear here)
        return np.mean(-1 * (YTrue * np.log(YPredict) + (1 - YTrue) * np.log(1 - YPredict)), axis = 0)

# TODO: should be in train.py
def main():
    # Data Processing
    X, Y = getData("data/DataFor640/dataset1/", "LinearX.csv", "LinearY.csv")   # 400x2
    train_ind, test_ind = splitData(X, Y, 5)
    X_train, Y_train = X[train_ind], Y[train_ind]
    X_test, Y_test = X[test_ind], Y[test_ind]
    
    
    # Parameters TODO: arguments or script
    NNodes = 30
    activate, deltaActivate = Utils.sigmoid, Utils.d_sigmoid#Utils.relu, Utils.d_relu
    learningRate = 0.5
    epochs = 2
    regLambda = 0
    batchSize = 50
    
    # Neural Network Execution
    nn = NeuralNetwork(NNodes, activate, deltaActivate)
    nn.fit(X_train, Y_train, learningRate, epochs, regLambda, batchSize)

    plotDecisionBoundary(nn, X, Y)
    Y_predict=test(X_test,nn)
    metrics=getPerformanceScores(Y_test, Y_predict)
if __name__ == "__main__":
    main()
    
    
    
    
    
    
    
    
    
    
    
    
    
