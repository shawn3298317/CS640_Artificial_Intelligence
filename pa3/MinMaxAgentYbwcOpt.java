import java.util.*;
import java.util.concurrent.*;

public class MinMaxAgentYbwcOpt extends BaseAgent {

	class FutureStorage{
		public Future<Double> fut;
		public int pos;

		public FutureStorage(Future<Double> fut, int pos){
			this.fut = fut;
			this.pos = pos;
		}
	}

	public int player; //1 for player 1 and 2 for player 2
	public int opponent_player;
	private int m_depth;
	private boolean m_use_corner_score;
	public ExecutorService executorService;
	private int remain_iter;
	private int steps_taken;
	private boolean dynamic_remain_iter;
	private int remain_iter_reduce_factor;
	private int threadPool;

	public MinMaxAgentYbwcOpt(int setPlayer, int depth, boolean m_use_corner_score)
	{
		super(setPlayer);
		int px = 2;
		this.m_depth = depth;
		int processors = Runtime.getRuntime().availableProcessors();
		this.threadPool = processors * px;
		this.steps_taken = 0;

		this.remain_iter = processors/2;
		this.dynamic_remain_iter = false;		// TODO: Not implemented yet.
		this.remain_iter_reduce_factor = 8;		// TODO: Not implemented yet. 64 moves. 64/8 = 8 (initial remain_iter)

		System.out.println("ri=" + this.remain_iter + ", px=" + px);
	}

	@Override
	public String getAgentName() {
		return new String("MinMaxAgentOpt");
	}
	
	@Override
	public positionTicTacToe getPolicyFromState(List<positionTicTacToe> board, int player)
	{
		long start = System.currentTimeMillis();
		this.executorService = Executors.newFixedThreadPool(threadPool);

        List<FutureStorage> list = new ArrayList<FutureStorage>();
		if(dynamic_remain_iter == true){
			this.remain_iter = ((4 * 4 * 4) - this.steps_taken) / this.remain_iter_reduce_factor;
		}

		positionTicTacToe myNextMove = new positionTicTacToe(0, 0, 0);
		Double max_value = -1000000.0;
		Double alpha = -1000000.0;
		Double beta = 1000000.0;

		int futureCount = 0;

		// Run alpha-beta on first <remain_iter> branch to find some (local) alpha-beta value to pass to child thread in future.
		// Improved: Do step1 using multi-threading way too!
		int pos = 0;
		int iter = 0;

		/*
		for(; pos<board.size() && iter < this.remain_iter; pos++){
			if (board.get(pos).state != 0) {
				continue; // marked position
			} else {
				iter++;
			}

			board.get(pos).state = player;
			Double cur_value = minMaxAlphaBeta(board, m_depth-1, alpha, beta, false);
			board.get(pos).state = 0; // back tracking
			if (cur_value > max_value) {
				max_value = cur_value;
				myNextMove = GameUtil.indexToPosition(pos);
			}
			alpha = Math.max(alpha, max_value);
			if (alpha >= beta)
				break;
		}
		*/

		// Multi-thread on remaining branches using alpha-beta value found from sequential run above.
		for (; pos<board.size() && iter < this.remain_iter; pos++) {
			// Double cur_value = minMax(board, 4, false);
			if (board.get(pos).state != 0){
				continue; // marked position
			} else {
				iter++;
			}

			List<positionTicTacToe> board_clone = new ArrayList<positionTicTacToe>();
			for(int j=0; j<board.size(); j++){
				board_clone.add(board.get(j).create_copy());
			};

			class minMaxCallable implements Callable<Double> {
				Double alpha;
				Double beta;
				List<positionTicTacToe> board_clone;
				int pos;
				int player;
				int m_depth;

				public minMaxCallable (List<positionTicTacToe> board_clone, int pos, int player, Double alpha, Double beta, int m_depth) {
					this.board_clone = board_clone;
					this.alpha = alpha;
					this.beta = beta;
					this.pos = pos;
					this.player = player;
					this.m_depth = m_depth;
				}

				@Override
				public Double call() throws Exception{
					Double cur_value = 0.0;
					this.board_clone.get(this.pos).state = this.player;
					cur_value = minMaxAlphaBeta(this.board_clone, this.m_depth-1, this.alpha, this.beta, false);
					return cur_value;
				}
			};
			minMaxCallable minMax = new minMaxCallable(board_clone, pos, player, alpha, beta, m_depth);
			Future<Double> f = executorService.submit(minMax);
			list.add(new FutureStorage(f, pos));
		}

		// Wait for all the child threads to be completed.
		for(FutureStorage futStorage : list) {
			try {
				Future<Double> fut = futStorage.fut;
				int futPos = futStorage.pos;

				//print the return value of Future, notice the output delay in console
				// because Future.get() waits for task to get completed
				Double cur_value = fut.get();

				if (cur_value > max_value) {
					max_value = cur_value;
					myNextMove = GameUtil.indexToPosition(futPos);
//					System.out.println("cur > max. Set next move to futPos(" + futPos + ")");
				}
				alpha = Math.max(alpha, max_value);
				if (alpha >= beta)
					break;
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		};
		list.clear();
		long step_1_done = System.currentTimeMillis();
		System.out.println("Time taken (debug): " + (float)(step_1_done-start) + "ms." +  "\n---------------------");

		// Multi-thread on remaining branches using alpha-beta value found from sequential run above.
		for (; pos < board.size(); pos++) {
			// Double cur_value = minMax(board, 4, false);
			if (board.get(pos).state != 0) continue; // marked position

			List<positionTicTacToe> board_clone = new ArrayList<positionTicTacToe>();
			for(int j=0; j<board.size(); j++){
				board_clone.add(board.get(j).create_copy());
			};

			class minMaxCallable implements Callable<Double> {
				Double alpha;
				Double beta;
				List<positionTicTacToe> board_clone;
				int pos;
				int player;
				int m_depth;

				public minMaxCallable (List<positionTicTacToe> board_clone, int pos, int player, Double alpha, Double beta, int m_depth) {
					this.board_clone = board_clone;
					this.alpha = alpha;
					this.beta = beta;
					this.pos = pos;
					this.player = player;
					this.m_depth = m_depth;
				}

				@Override
				public Double call() throws Exception{
					Double cur_value = 0.0;
					this.board_clone.get(this.pos).state = this.player;
					cur_value = minMaxAlphaBeta(this.board_clone, this.m_depth-1, this.alpha, this.beta, false);
					return cur_value;
				}
			};
			minMaxCallable minMax = new minMaxCallable(board_clone, pos, player, alpha, beta, m_depth);
			Future<Double> f = executorService.submit(minMax);
			list.add(new FutureStorage(f, pos));
		}

		// Wait for all the child threads to be completed.
		for(FutureStorage futStorage : list) {
			try {
				Future<Double> fut = futStorage.fut;
				int futPos = futStorage.pos;

				//print the return value of Future, notice the output delay in console
				// because Future.get() waits for task to get completed
				Double cur_value = fut.get();

				if (cur_value > max_value) {
					max_value = cur_value;
					myNextMove = GameUtil.indexToPosition(futPos);
//					System.out.println("cur > max. Set next move to futPos(" + futPos + ")");
				}
				alpha = Math.max(alpha, max_value);
				if (alpha >= beta)
					break;
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		};

		this.steps_taken++;
		this.executorService.shutdownNow();
		System.out.println("Next Move: " + myNextMove.x + " " + myNextMove.y + " " + myNextMove.z);

		long end = System.currentTimeMillis();
		System.out.println("Time taken: " + (float)(end-start) + "ms. myNextMove=" + myNextMove + "\n---------------------");

		return myNextMove;
	}

	private Double minMax(List<positionTicTacToe> board, int depth, boolean maximizer) {
		
		if ((depth == 0) || (GameUtil.isEnded(board) != 0)) {
			return getValueFromState(board);
		}

		if (maximizer == true) {
			Double max_value = -1000000.0;
			for (int pos = 0; pos < board.size(); pos++) {
				if (board.get(pos).state != 0) continue; // marked position
				board.get(pos).state = player;
				max_value = Math.max(max_value, minMax(board, depth - 1, false));
				// back tracking
				board.get(pos).state = 0;

			}
			return max_value;
		} else {
			Double min_value = 1000000.0;
			for (int pos = 0; pos < board.size(); pos++) {
				if (board.get(pos).state != 0) continue; // marked position
				board.get(pos).state = opponent_player;
				min_value = Math.min(min_value, minMax(board, depth - 1, true));
				board.get(pos).state = 0;
			}
			return min_value;
		}

	}

	private Double minMaxAlphaBeta(List<positionTicTacToe> board, int depth, Double alpha, Double beta, boolean maximizer) {
//		System.out.println("Running minMaxAlphaBeta. depth=" + depth + ",alpha=" + alpha);

		if ((depth == 0) || (GameUtil.isEnded(board) != 0)) {
//			System.out.println("GameUtil.isEnded(board)=" + GameUtil.isEnded(board));
			return getValueFromState(board);
		}

		if (maximizer == true) {
			Double max_value = -1000000.0;
			for (int pos = 0; pos < board.size(); pos++) {
				if (board.get(pos).state != 0) continue; // marked position
				board.get(pos).state = player;
				max_value = Math.max(max_value, minMaxAlphaBeta(board, depth - 1, alpha, beta, false));
				board.get(pos).state = 0; // back tracking

				alpha = Math.max(alpha, max_value);
				if (alpha >= beta)
					break;
			}
//			System.out.println("max val=" + max_value);
			return max_value;
		} else {
			Double min_value = 1000000.0;
			for (int pos = 0; pos < board.size(); pos++) {
				if (board.get(pos).state != 0) continue; // marked position
				board.get(pos).state = opponent_player;
				min_value = Math.min(min_value, minMaxAlphaBeta(board, depth - 1, alpha, beta, true));
				board.get(pos).state = 0; // back tracking

				beta = Math.min(beta, min_value);
				if (alpha >= beta)
					break;
			}
//			System.out.println("min val=" + min_value);
			return min_value;
		}

	}

	protected Double getValueFromState(List<positionTicTacToe> state) {

		// Has complete line(s)
		Double terminal_score = 0.0;
		int winner = GameUtil.isEnded(state);
		if (winner == player) {
			terminal_score = 20.0;
			return terminal_score;
		} else if (winner == opponent_player) {
			terminal_score = -10.0;
			return terminal_score;
		}

		// Corner Bonus
		Double corner_score = 0.0;
		int num_corners = 0;
		
		for (int i = 0; i < GameUtil.cornerIndex.size(); i++) {
			int corner_pos = GameUtil.cornerIndex.get(i);
			if (state.get(corner_pos).state == player) {
				num_corners += 1;
			}
		}
		if (m_use_corner_score)
			corner_score = num_corners * 1.0;
		else
			corner_score = 0.0;

		return corner_score;
	}
}
