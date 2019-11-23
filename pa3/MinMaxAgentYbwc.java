import java.util.*;
import java.util.concurrent.*;

public class MinMaxAgentOpt extends BaseAgent {

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
	private ExecutorService executorService;

	public MinMaxAgentOpt(int setPlayer, int depth, boolean m_use_corner_score)
	{
		super(setPlayer);
		m_depth = depth;
	}

	@Override
	public String getAgentName() {
		return new String("MinMaxAgentOpt");
	}
	
	@Override
	public positionTicTacToe getPolicyFromState(List<positionTicTacToe> board, int player)
	{
		long start = System.currentTimeMillis();
        List<FutureStorage> list = new ArrayList<FutureStorage>();
        this.executorService = Executors.newFixedThreadPool(2);
		int processors = Runtime.getRuntime().availableProcessors();
		System.out.println("num of processors avail = " + processors);
//        System.out.println("board");
//		System.out.println(board);

		//TODO: this is where you are going to implement your AI algorithm to win the game. The default is an AI randomly choose any available move.
		// positionTicTacToe myNextMove;

		positionTicTacToe myNextMove = new positionTicTacToe(0, 0, 0);
		Double max_value = -1000000.0;
		Double alpha = -1000000.0;
		Double beta = 1000000.0;

		int futureCount = 0;
		for (int pos = 0; pos < board.size(); pos++) {
			// Double cur_value = minMax(board, 4, false);
			if (board.get(pos).state != 0) continue; // marked position

			List<positionTicTacToe> board_clone = new ArrayList<positionTicTacToe>();
			for(int j=0; j<board.size(); j++){
				board_clone.add(board.get(j).create_copy());
			};

//			List<positionTicTacToe> board_clone = new ArrayList<positionTicTacToe>(board);
//			System.out.println("board_clone1");
//			System.out.println(board_clone);

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
//					System.out.println("\tPlayer makes move. Player = " + player);
//					System.out.println("\t" + board);

					cur_value = minMaxAlphaBeta(this.board_clone, this.m_depth-1, this.alpha, this.beta, false);
//					System.out.println("m_depth-1=" + (this.m_depth-1) + ",alpha=" + this.alpha + ", beta=" + this.beta);
					return cur_value;
				}
			};

			minMaxCallable minMax = new minMaxCallable(board_clone, pos, player, alpha, beta, m_depth);

//			Callable<Double> minMax = new Callable<Double>() {
//				@Override
//				public Double call() {
//					Double cur_value = 0.0;
//					cur_value = minMaxAlphaBeta(board_clone, m_depth-1, alpha, beta, false);
//					System.out.println("min_max | task finished");
//					return cur_value;
//				}
//			};

			Future<Double> f = executorService.submit(minMax);
			// System.out.println("Added pos:" + pos);
			list.add(new FutureStorage(f, pos));
//			board.get(pos).state = 0; // back tracking
		}

//		System.out.println("Added " + list.size() + " tasks to executorService");

		for(FutureStorage futStorage : list) {
			try {
				Future<Double> fut = futStorage.fut;
				int pos = futStorage.pos;

				//print the return value of Future, notice the output delay in console
				// because Future.get() waits for task to get completed
				Double cur_value = fut.get();

				if (cur_value > max_value) {
					max_value = cur_value;
					myNextMove = GameUtil.indexToPosition(pos);
//					System.out.println("cur > max. Set next move to pos(" + pos + ")");
				}
				alpha = Math.max(alpha, max_value);
				if (alpha >= beta)
					break;
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		};

		executorService.shutdownNow();
		System.out.println("Executor service is shutdown: " + executorService.isShutdown());

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
