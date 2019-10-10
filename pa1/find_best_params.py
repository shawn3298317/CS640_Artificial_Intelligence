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

def find_best_test_val(args, key, test_vals, output):
	output.append("Changing {}".format(key))
	for tv in test_vals:
		args[key] = tv
		output.append("Set {} to {}".format(key, tv))
		Logging.info("find_best_test_val | Modifying {}={}".format(key, tv))
		metric = test(args)

		output.append(args)
		output.append(metric)
		output.append("")
	output.append("====================")
	return output

def main():

	output = []

	args = {
		"NNodes": 100,
		"activate": relu,
		"deltaActivate": d_relu,
		"learningRate": 0.015,
		"epochs": 200,
		"regLambda": 0.1,
		"batchSize": 20,
		"task": TASK_MULTI_CLASS
		# "task": "regression"
	}

	output = find_best_test_val(args, 'NNodes', [50,100,150,200], output)
	output = find_best_test_val(args, 'regLambda', [0.0, 0.1, 0.5, 1, 1.5], output)
	output = find_best_test_val(args, 'learningRate', [0.015, 0.03, 0.15, 0.3, 1.5], output)
	output = find_best_test_val(args, 'epochs', [100, 200, 300], output)
	for log in output:
		Logging.superInfo(log)

if __name__ == "__main__":
	main()
