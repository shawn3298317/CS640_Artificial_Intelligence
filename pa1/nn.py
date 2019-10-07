import numpy as np
import os, sys
import datetime

from util import *



# Utils - TODO: import Utils
class Utils:
    @staticmethod
    def data_iterator(X, Y, batch_size):
        for i in range(X.shape[0]//batch_size):
            yield(X[i*batch_size: (i+1)*batch_size], Y[i*batch_size: (i+1)*batch_size])

# Logging - TODO: import logging
class Logging:
    @staticmethod
    def log(s, level):
        timestamp = datetime.datetime.now()
        if level == 'DEBUG':
            if LOG_LEVEL in ['DEBUG', 'INFO', 'WARNING']:
                print("{} | {} | {}".format(level, timestamp, s))
        elif level == 'INFO':
            if LOG_LEVEL in ['INFO', 'WARNING']:
                print("{} | {} | {}".format(level, timestamp, s))
        elif level == 'WARNING':
            if LOG_LEVEL in ['WARNING']:
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

LOG_LEVEL = 'DEBUG' # Possible inputs: DEBUG, INFO, WARNING

class NeuralNetwork:
    def __init__(self, NNodes, activate, deltaActivate):
        self.NNodes = NNodes # the number of nodes in the hidden layer
        self.activate = activate # a function used to activate
        self.deltaActivate = deltaActivate # the derivative of activate

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
                self.backpropagate(x_batch)

        pass

    def initialize_param(self):
        self.W_1 = np.random.uniform(-1, 1, (self.NNodes, self.input_dim))
        self.W_2 = np.random.uniform(-1, 1, (self.NNodes, self.output_dim))
        self.b_0 = np.random.uniform(-1, 1, (self.NNodes, 1))
        self.b_1 = np.random.uniform(-1, 1, (self.output_dim, 1))
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
        return YPredict

    def forward(self, X):
        # Perform matrix multiplication and activation twice (one for each layer).
        # (hint: add a bias term before multiplication)
        
        # input layer to 1st hidden layer
        self.a_1 = np.matmul(self.W_1, X.T) + np.repeat(self.b_0, self.batch_size, axis=1) # W1^T * X + B_0
        Logging.debug("a_1: %s" % repr(self.a_1.shape))
        
        # 1st hidden layer - activation
        self.h_1 = self.activate(self.a_1)
        Logging.debug("h_1: %s" % repr(self.h_1.shape))
        
        # output layer
        self.a_2 = np.matmul(self.h_1.T, self.W_2) + np.repeat(self.b_1, self.batch_size, axis=1)
        Logging.debug("a_2: %s" % repr(self.a_2.shape))
        
        self.h_2 = self.activate(self.a_2)
        Logging.debug("h_2: %s" % repr(self.h_2.shape))

    def backpropagate(self, X):
        # Compute loss / cost using the getCost function.



        # Compute gradient for each layer.



        # Update weight matrices.
        
        pass

    def getCost(self, YTrue, YPredict):
        # Compute loss / cost in terms of crossentropy.
        # (hint: your regularization term should appear here)
        pass

def relu(x):
    return x * (x > 0)
    
def d_relu(x):
    return 1. * (x > 0)

def sigmoid(x):
    1 / (1+np.exp(-x))

def d_sigmoid(x):
    sigmoid(x) * (1 - sigmoid(x))

        

# TODO: should be in train.py
def main():
    # Data Processing
    X, Y = getData("data/DataFor640/dataset1/", "LinearX.csv", "LinearY.csv")   # 400x2
    train_ind, test_ind = splitData(X, Y, 5)
    X_train, Y_train = X[train_ind], Y[train_ind]
    X_test, Y_test = X[test_ind], Y[test_ind]
    
    
    # Parameters
    NNodes, activate, deltaActivate, learningRate, epochs, regLambda = 3, relu, d_relu, 0.01, 1, 0 # TODO: change to argv.
    
    # Neural Network Execution
    nn = NeuralNetwork(NNodes, activate, deltaActivate)
    nn.fit(X, Y, learningRate, epochs, regLambda)

if __name__ == "__main__":
    main()
    
    
    
    
    
    
    
    
    
    
    
    
    
