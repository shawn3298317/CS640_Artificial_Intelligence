'''
Results of at least 5 different values of number of neurons in your hiddenlayer. (with other values fixed)
Results of at least 5 different values of regularization (with other values fixed)
Results of at least 5 different values of learning rate (with other values fixed)
Results of at least 3 different values of epoches. (with other values fixed)
'''

from math_util import *
from util import TASK_REGRESSION, TASK_BINARY_CLASS, TASK_MULTI_CLASS
from main import main as nnMain
from Logging import Logging

def test(args):
	return nnMain(args) # TODO: should plot from here and returns metric. main needs to implement this as well!

def find_best_test_val(args, key, test_vals):
	for tv in test_vals:
		args[key] = tv
		Logging.info("find_best_test_val | Modifying {}={}".format(key, tv))
		metrics = test(args)

def main():
	args = {
		"NNodes": 50,
		"activate": relu,
		"deltaActivate": d_relu,
		"learningRate": 0.1,
		"epochs": 50,
		"regLambda": 0.1,
		"batchSize": 20,
		"task": TASK_MULTI_CLASS
		# "task": "regression"
	}

	find_best_test_val(args, 'NNodes', [1,2,3,4,5])
	find_best_test_val(args, 'regLambda', [1,2,3,4,5])
	find_best_test_val(args, 'learningRate', [1,2,3,4,5])
	find_best_test_val(args, 'epochs', [1,2,3])

if __name__ == "__main__":
	main()
