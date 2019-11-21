import java.util.*;

public class MinMaxAgent extends BaseAgent {

	public int player; //1 for player 1 and 2 for player 2
	public int opponent_player;
	private int m_depth;
	private boolean m_use_corner_score;

	public MinMaxAgent(int setPlayer, int depth, boolean m_use_corner_score)
	{
		super(setPlayer);
		m_depth = depth;
	}

	@Override
	public String getAgentName() {
		return new String("MinMaxAgent");
	}
	
	@Override
	public positionTicTacToe getPolicyFromState(List<positionTicTacToe> board, int player)
	{
		long start = System.currentTimeMillis();

		//TODO: this is where you are going to implement your AI algorithm to win the game. The default is an AI randomly choose any available move.
		// positionTicTacToe myNextMove;

		positionTicTacToe myNextMove = new positionTicTacToe(0, 0, 0);
		Double max_value = -1000000.0;
		Double alpha = -1000000.0;
		Double beta = 1000000.0;

		for (int pos = 0; pos < board.size(); pos++) {
			if (board.get(pos).state != 0) continue; // marked position
			board.get(pos).state = player;
			// Double cur_value = minMax(board, 4, false);
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

		System.out.println(myNextMove.x + " " + myNextMove.y + " " + myNextMove.z);

		long end = System.currentTimeMillis();

		System.out.println("Time taken: " + (float)(end-start) + "ms.\n---------------------");

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
		
		if ((depth == 0) || (GameUtil.isEnded(board) != 0)) {
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
