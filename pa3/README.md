# 4x4x4 Tic-Tac-Toe AI

## TODO:
1. Re-run the tests for ybwc.
2. Implement dynamic increment as game progresses because the overall time doesn't matter. Rather, the max time per move matters.
3. If time limit is hit, then just randomly run a move. Or should we dedicate one thread to do a less depth answer?

## Agents to Implement
- Random Agents (baseline)
- Alpha-Beta Min-max Agent (Value functions??)
- (can skip) Monte-Carlo Agent (Value functions??)
- RL Agent?
- // DQN Agent?


# Multi-Threading Analysis
 - Directly doing multi-threading without YBWC is pretty much doing minimax without alphabeta pruning.

# Extra Info
n8 = No multi-threading. Just using regular alpha-beta algorithm.
ybwc8_ri2_px2 = Using YBWC algorithm for multi-threading alpha-beta pruning. 'ri' refers to remain_iter variable, meaning how many branches to loop sequentially to find the alpha/beta value to pass to the remaining children, which are multi-threaded. 'px' refers to the calculation of fixed thread pool is equal to # of process times 2.


