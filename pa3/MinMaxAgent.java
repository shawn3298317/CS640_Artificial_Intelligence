import java.util.*;

public class MinMaxAgent extends BaseAgent {

	public int player; //1 for player 1 and 2 for player 2
	public int opponent_player;

	public MinMaxAgent(int setPlayer)
	{
		super(setPlayer);
	}

	@Override
	public String getAgentName() {
		return new String("MinMaxAgent");
	}
	
	@Override
	public positionTicTacToe getPolicyFromState(List<positionTicTacToe> board, int player)
	{

		//TODO: this is where you are going to implement your AI algorithm to win the game. The default is an AI randomly choose any available move.
		// positionTicTacToe myNextMove;

		positionTicTacToe myNextMove = new positionTicTacToe(0, 0, 0);
		Double max_value = -1000000.0;

		for (int pos = 0; pos < board.size(); pos++) {
			if (board.get(pos).state != 0) continue; // marked position
			board.get(pos).state = player;
			Double cur_value = minMax(board, 2, false);
			if (cur_value > max_value) {
				max_value = cur_value;
				myNextMove = GameUtil.indexToPosition(pos);
			}
			// back tracking
			board.get(pos).state = 0;
		}

		System.out.println(myNextMove.x + " " + myNextMove.y + " " + myNextMove.z);
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

	private positionTicTacToe minMaxAlphaBeta(List<positionTicTacToe> board, int depth, boolean maximizer) {
		return new positionTicTacToe(0, 0, 0);
	}

	protected Double getValueFromState(List<positionTicTacToe> state) {

		// Has complete line(s)
		Double terminal_score = 0.0;
		int winner = GameUtil.isEnded(state);
		if (winner == player) {
			terminal_score = 50.0;
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
		corner_score = num_corners * 1.0;

		return corner_score;
	}
}
