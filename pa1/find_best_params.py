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

def find_best_test_val(args, changed_arg, test_vals):
	Logging.superInfo("Changing {}".format(changed_arg))
	for tv in test_vals:
		args[changed_arg] = tv
		Logging.superInfo("Set {} to {}".format(changed_arg, tv))
		Logging.info("find_best_test_val | Modifying {}={}".format(changed_arg, tv))
		metric = test(args)
		metric['precision'] = np.mean(np.mean(metric['precision']))
		metric['recall'] = np.mean(np.mean(metric['recall']))
		metric['f1'] = np.mean(np.mean(metric['f1']))

		Logging.superInfo(args)
		Logging.superInfo(metric)

		Logging.superInfo("")
		Logging.superInfo("==l==================")

def main():
	args = {
		"NNodes": 100,
		"activate": relu,
		"deltaActivate": d_relu,
		"learningRate": 0.015,
		"epochs": 200,
		"regLambda": 0.1,
		"batchSize": 20,
		"task": TASK_MULTI_CLASS,
		"k_fold": 4
	}

	find_best_test_val(args, 'NNodes', [1, 10, 50, 100, 200])
	find_best_test_val(args, 'regLambda', [0.0, 0.1, 0.5, 1, 1.5])
	find_best_test_val(args, 'learningRate', [0.000015, 0.00015, 0.0015, 0.015, 0.15, 1.5, 15])
	find_best_test_val(args, 'epochs', [1, 10, 100, 300, 500])

if __name__ == "__main__":
	main()
