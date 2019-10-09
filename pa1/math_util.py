import numpy as np

def relu(x):
    return x * (x > 0)

def d_relu(x):
    return 1. * (x > 0)

def sigmoid(x):
    return 1 / (1+np.exp(-x))

def d_sigmoid(x):
    return sigmoid(x) * (1 - sigmoid(x))

def softmax(x):
    return np.exp(x) / np.sum(np.exp(x), axis=1, keepdims=True)