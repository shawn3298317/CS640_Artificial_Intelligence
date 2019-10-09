import numpy as np
import os, sys
import datetime

from math_util import *
from Logging import Logging

TASK_BINARY_CLASS = "binary_class"
TASK_MULTI_CLASS = "multi_class"
TASK_REGRESSION = "regression"

def data_iterator(X, Y, batch_size):
    for i in range(X.shape[0]//batch_size):
        yield(X[i*batch_size: (i+1)*batch_size], Y[i*batch_size: (i+1)*batch_size])

class NeuralNetwork:
    def __init__(self, NNodes, activate, deltaActivate, task):
        self.NNodes = NNodes # the number of nodes in the hidden layer
        self.activate = activate # a function used to activate
        self.deltaActivate = deltaActivate # the derivative of activate
        self.count = 0
        self.task = task
        self.print_every_n_step = 100

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
        self.current_epoch = 0
        self.current_step = 0

        # For each epoch, do
        while self.current_epoch < epochs:
            self.current_epoch += 1
            Logging.debug("Starting %i epoch" % (self.current_epoch)) # DEBUG, INFO, WARNING
            # For each training sample (X[i], Y[i]), do
            
            for x_batch, y_batch in data_iterator(X, Y, self.batch_size):
                self.current_step += 1    
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

    def predict(self, X, threshold=None):
        """
        Predicts the labels for each sample in X.
        Parameters
        X : numpy matrix
            The matrix containing sample features for testing.
        threshold : float
            The threshold for the predictions
        Returns
        -------
        YPredict : numpy array
            The predictions of X.
        ----------
        """
        y_hat = self.forward(X)

        if threshold:
            return np.where(self.y_hat[:, 1] > threshold, 1, 0)
        else:
            return np.argmax(y_hat, axis=1)

    def forward(self, X):
        # Perform matrix multiplication and activation twice (one for each layer).
        # (hint: add a bias term before multiplication)
        
        # input layer to 1st hidden layer
        Logging.debug("X: {}, W1: {}, b_0:{}".format(X.shape, self.W_1.shape, self.b_0.shape))
        self.a_1 = (np.matmul(self.W_1, X.T) + self.b_0).T # W1^T * X + B_0 | W1=num_nodes_hidden x 2(input nodes), X = BTx2, a1 = BT x num_nodes_hidden
        Logging.debug("a_1: %s" % repr(self.a_1.shape))
        
        # 1st hidden layer - activation
        self.h_1 = self.activate(self.a_1)  # h1 = BT x num_nodes_hidden
        Logging.debug("h_1: %s" % repr(self.h_1.shape))
        
        # output layer
        self.a_2 = np.matmul(self.h_1, self.W_2) + self.b_1.T # a2 = BT x 1, h1= BT x num_nodes_hidden, W2 = num_nodes_hidden x 1, b1 = BTx1
        #TODO: np.repeat(self.b_1, self.batch_size, axis=1)
        Logging.debug("a_2: %s" % repr(self.a_2.shape))
        
        if self.task == "regression":
            final_activation = relu
        else:
            final_activation = softmax

        self.y_hat = final_activation(self.a_2) # = BT x K
        Logging.debug("y_hat: %s" % repr(self.y_hat.shape))

        return self.y_hat

    def backpropagate(self, X, Y):
        # Compute loss / cost using the getCost function.
        self.loss = self.getCost(Y, self.y_hat)  # Y = BT x 1, y_hat = BT x 1
        if self.current_step % self.print_every_n_step == 0:
            Logging.info("Loss: {:.4f} | Ep: {}; Current_step = {}".format(self.loss, self.current_epoch, self.current_step))
        self.count += 1

        # Compute gradient for each layer.
        g = (self.y_hat - Y)
        Logging.debug("g_a2: {}".format(g.shape))

        # / d_sigmoid(self.a_2)    # BTx1 / BTx1 = BT x 1
        # Logging.debug("g_loss: {}".format(g.shape))
        
        # TODO: for loop for variable hidden layers

        # Hidden Layer
        #g = g * d_sigmoid(self.a_2)   # g = BT x 1
        
        grad_b_1 = np.sum(g, axis=0, keepdims=True).T + self.reg_lambda*self.b_1  # g = BT x 1
        Logging.debug("grad_b_1: {}".format(grad_b_1.shape))
        grad_W_2 = np.matmul(self.h_1.T, g) + self.reg_lambda*self.W_2 # TODO: include lambda  # g = BT x 1, h1 = BT x num_nodes_hidden, grad_w2 = num_nodes_hidden x 1
        Logging.debug("grad_W_2: {}".format(grad_W_2.shape))

        g = np.matmul(g, self.W_2.T) # g=BT x 1, w2 = #num_nodes_hidden x 1, g(result) = BT x num_nodes_hidden
        Logging.debug("g_h1: {}".format(g.shape))

        # Input Layer
        g = g * self.deltaActivate(self.a_1)   # g = BT x num_nodes_hidden, a_1 = BT x num_nodes_hidden
        Logging.debug("g_a1: {}".format(g.shape)) # BT x num_nodes_hidden
        
        grad_b_0 = np.sum(g, axis=0, keepdims=True).T + self.reg_lambda*self.b_0  # g = BT x num_nodes_hidden, b0 = BT x num_nodes_hidden
        grad_W_1 = np.matmul(g.T, X) + self.reg_lambda*self.W_1 # g = BT x num_nodes_hidden, X = BTx2, grad_w1 = num_nodes_hidden x2
        
        Logging.debug("grad_b_0: {}".format(grad_b_0.shape))
        Logging.debug("grad_W_1: {}".format(grad_W_1.shape))

        # Update weight matrices w/t stochastic gradient descent
        self.W_2 -= self.lr * grad_W_2 / self.batch_size
        self.b_1 -= self.lr * grad_b_1 / self.batch_size
        self.W_1 -= self.lr * grad_W_1 / self.batch_size
        self.b_0 -= self.lr * grad_b_0 / self.batch_size

        Logging.debug("Updated W_2: {}".format(self.W_2.shape))
        Logging.debug("Updated b_1: {}".format(self.b_1.shape))
        Logging.debug("Updated W_1: {}".format(self.W_2.shape))
        Logging.debug("Updated b_0: {}".format(self.b_0.shape))

    def getCost(self, YTrue, YPredict):
        # Compute loss / cost in terms of crossentropy.
        # (hint: your regularization term should appear here)

        # flatten all params
        thetas = np.concatenate([self.W_2.ravel(), self.b_1.ravel(), self.W_1.ravel(), self.b_0.ravel()]).ravel()
        Logging.debug("Theta shape: {}".format(thetas.shape))
        regular_term = self.reg_lambda * np.linalg.norm(thetas) / YTrue.shape[0]

        if self.task == "regression":
            return np.mean(np.linalg.norm(YTrue - YPredict, axis=1)) + regular_term
        else:
            # print(np.sum(YPredict, axis=0))
            return np.mean(-np.sum(YTrue*np.log(YPredict), axis=1)) + regular_term
            # return np.mean(-1 * (YTrue * np.log(YPredict) + (1 - YTrue) * np.log(1 - YPredict)), axis = 0) + regular_term


# TODO: should be in train.py
def main():
    # Data Processing
    X, Y = getData("data/DataFor640/dataset1/", "LinearX.csv", "LinearY.csv")   # 400x2
    # X, Y = getData("data/DataFor640/dataset1/", "NonLinearX.csv", "NonLinearY.csv")   # 400x2
    train_ind, test_ind = splitData(X, Y, 5)
    X_train, Y_train = X[train_ind], Y[train_ind]
    X_test, Y_test = X[test_ind], Y[test_ind]
    
    
    # Parameters TODO: arguments or script
    NNodes = 50
    # activate, deltaActivate = sigmoid, d_sigmoid#relu, d_relu
    activate, deltaActivate = relu, d_relu
    learningRate = 0.5
    epochs = 200
    regLambda = 0.001
    batchSize = 400
    
    # Neural Network Execution
    nn = NeuralNetwork(NNodes, activate, deltaActivate)
    nn.fit(X_train, Y_train, learningRate, epochs, regLambda, batchSize)

    plotDecisionBoundary(nn, X, Y)

if __name__ == "__main__":
    main()
    
    
    
    
    
    
    
    
    
    
    
    
    
