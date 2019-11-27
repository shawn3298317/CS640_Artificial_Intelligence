import java.util.*;

public class MinMaxAgentOpt extends BaseAgent {

	public int player; //1 for player 1 and 2 for player 2
	public int opponent_player;
	private int m_depth;
	private int m_depth1;
	private int m_depth2;
	private int depth_increase_thres;
	private boolean m_use_pot_win_score;
	private int steps_taken;


	public MinMaxAgentOpt(int setPlayer)
	{
		super(setPlayer);
		init(setPlayer, 4, true);
	}

	public MinMaxAgentOpt(int setPlayer, int depth, boolean use_pot_win_score)
	{
		super(setPlayer);
		init(setPlayer, depth, use_pot_win_score);
	}

	public void init(int setPlayer, int depth, boolean use_pot_win_score){
		this.steps_taken = 0;
		this.m_depth1 = depth;
		this.m_depth2 = this.m_depth1 + 1;
		this.m_depth = this.m_depth1;

		this.depth_increase_thres = 7;

		this.m_use_pot_win_score = use_pot_win_score;
		this.player = setPlayer;
		this.opponent_player = (player == 2) ? 1 : 2;
	}

	@Override
	public String getAgentName() {
		return new String("MinMaxAgentOpt");
	}
	
	@Override
	public positionTicTacToe getPolicyFromState(List<positionTicTacToe> board, int player)
	{
		//TODO: this is where you are going to implement your AI algorithm to win the game. The default is an AI randomly choose any available move.
		// positionTicTacToe myNextMove;

		positionTicTacToe myNextMove = new positionTicTacToe(0, 0, 0);
		Double max_value = -1000000.0;
		Double alpha = -1000000.0;
		Double beta = 1000000.0;

		if(this.steps_taken == depth_increase_thres){
			this.m_depth = this.m_depth2;
		}
//		System.out.println("minimaxAgentOpt | d=" + this.m_depth);

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
		this.steps_taken++;

//		System.out.println(myNextMove.x + " " + myNextMove.y + " " + myNextMove.z);
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

		// System.out.println("Player:" + this.player + " opponent:" + this.opponent_player);
		// Has complete line(s)
		Double terminal_score = 0.0;
		int winner = GameUtil.isEnded(state);
		if (winner == player) {
			terminal_score = 250.0;
		} else if (winner == opponent_player) {
			terminal_score = -250.0;
		}

		// Possible un-blocked line(s)
		Double max_potential_win_line_score = 0.0;
		// Possible un-blocked line(s) [for opponent]
		Double min_potential_win_line_score = 0.0;

		if (m_use_pot_win_score == true) {
			max_potential_win_line_score = GameUtil.getPotentialWinLineCount(state, this.player);
			min_potential_win_line_score = -1.0 * GameUtil.getPotentialWinLineCount(state, this.opponent_player);
		}
		

		// // Corner Bonus
		// Double corner_score = 0.0;
		// int num_corners = 0;
		
		// for (int i = 0; i < GameUtil.cornerIndex.size(); i++) {
		// 	int corner_pos = GameUtil.cornerIndex.get(i);
		// 	if (state.get(corner_pos).state == player) {
		// 		num_corners += 1;
		// 	}
		// }
		// if (m_use_corner_score)
		// 	corner_score = num_corners * 1.0;
		// else
		// 	corner_score = 0.0;

		Double heuristic_score = terminal_score + max_potential_win_line_score + min_potential_win_line_score;
		// System.out.println("Score:" + heuristic_score + " term=" + terminal_score + " max=" + max_potential_win_line_score + " min=" + min_potential_win_line_score);
		return heuristic_score;
	}
}
