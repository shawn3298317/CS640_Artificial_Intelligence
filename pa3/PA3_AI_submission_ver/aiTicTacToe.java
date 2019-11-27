import java.util.*;

class aiTicTacToe {
	public int player; //1 for player 1 and 2 for player 2
	public int opponent_player;
	private int m_depth;
	private int m_depth1;
	private int m_depth2;
	private int depth_increase_thres;
	private boolean m_use_pot_win_score;
	private int steps_taken;


	public aiTicTacToe(int setPlayer)
	{
		init(setPlayer, 4, true);
	}

	public aiTicTacToe(int setPlayer, int depth, boolean use_pot_win_score)
	{
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

	public String getAgentName() {
		return new String("MinMaxAgentOpt");
	}


	public void resetAgent() {

	}


	protected int getStateOfPositionFromBoard(positionTicTacToe position, List<positionTicTacToe> board)
	{
		//a helper function to get state of a certain position in the Tic-Tac-Toe board by given position TicTacToe
		int index = GameUtil.positionToIndex(position.x, position.y, position.z);
		return board.get(index).state;
	}

	// To port the methods from original file.
	public positionTicTacToe myAIAlgorithm(List<positionTicTacToe> board, int player){
		return getPolicyFromState(board, player);
	}
	
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


	public boolean makeMove(positionTicTacToe position, int player, List<positionTicTacToe> targetBoard)
	{
		//make move on Tic-Tac-Toe board, given position and player 
		//player 1 = 1, player 2 = 2
		
		//brute force (obviously not a wise way though)
		for(int i=0;i<targetBoard.size();i++)
		{
			if(targetBoard.get(i).x==position.x && targetBoard.get(i).y==position.y && targetBoard.get(i).z==position.z) //if this is the position
			{
				if(targetBoard.get(i).state==0)
				{
					targetBoard.get(i).state = player;
					return true;
				}
				else
				{
					System.out.println("Error: this is not a valid move.");
				}
			}
			
		}
		return false;
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


	static class GameUtil {
		static int positionToIndex(int x, int y, int z) {
			return (x*16 + y*4 + z);
		}

		static positionTicTacToe indexToPosition(int index) {
			return new positionTicTacToe(index/16, (index/4)%4, index%4);
		}

		static List<Integer> cornerIndex = new ArrayList<Integer>() {{
			add(GameUtil.positionToIndex(0, 0, 0));
			add(GameUtil.positionToIndex(0, 3, 0));
			add(GameUtil.positionToIndex(3, 0, 0));
			add(GameUtil.positionToIndex(3, 3, 0));
			add(GameUtil.positionToIndex(0, 0, 3));
			add(GameUtil.positionToIndex(0, 3, 3));
			add(GameUtil.positionToIndex(3, 0, 3));
			add(GameUtil.positionToIndex(3, 3, 3));
		}};

		static int isEnded(List<positionTicTacToe> board) {
			GamePattern pattern = new GamePattern(-1);

			// straight line checks
			if (GameUtil.hasXWinLines(board, pattern)) {
		//		 System.out.println("hasXWinLines" + pattern.getValue());
				return pattern.getValue();
			}
			if (GameUtil.hasYWinLines(board, pattern)) {
		//		 System.out.println("hasYWinLines" + pattern.getValue());
				return pattern.getValue();
			}
			if (GameUtil.hasZWinLines(board, pattern)) {
		//		 System.out.println("hasZWinLines" + pattern.getValue());
				return pattern.getValue();
			}

			// plane diagonal line checks
			if (GameUtil.hasXZWinLines(board, pattern)) {
		//		 System.out.println("hasXYWinLines" + pattern.getValue());
				return pattern.getValue();
			}
			if (GameUtil.hasYZWinLines(board, pattern)) {
		//		 System.out.println("hasYZWinLines" + pattern.getValue());
				return pattern.getValue();
			}
			if (GameUtil.hasXYWinLines(board, pattern)) {
		//		 System.out.println("hasXYWinLines" + pattern.getValue());
				return pattern.getValue();
			}

			// cubic diagonal line checks
			if (GameUtil.hasXYZWinLines(board, pattern)) {
		//		 System.out.println("hasXYZWinLines" + pattern.getValue());
				return pattern.getValue();
			}

			if (GameUtil.hasEmptySlots(board)) {
				return 0; // Continue
			} else {
				return -1; // Call Draw
			}

		}


		static boolean hasXWinLines(List<positionTicTacToe> board, GamePattern pattern) {

			for(int i = 0; i<4; i++) {	
				for(int j = 0; j<4;j++)
				{
					int cur_pattern = board.get(GameUtil.positionToIndex(0, i, j)).state;
					if (cur_pattern == board.get(GameUtil.positionToIndex(1, i, j)).state &&
						cur_pattern == board.get(GameUtil.positionToIndex(2, i, j)).state &&
						cur_pattern == board.get(GameUtil.positionToIndex(3, i, j)).state) {
						pattern.setValue(cur_pattern);
						return true;
					}
				}
			}
			return false;
		}



		static Double getPotentialWinLineCount(List<positionTicTacToe> board, int player) {

			Double score = 0.0;
			// List<Integer> total_counts = new ArrayList<Integer>();
			int mark_counts = 0;
			
			// X axis counts
			for(int i = 0; i<4; i++) {	
				for(int j = 0; j<4;j++)
				{
					mark_counts = 0;	
					for (int n = 0; n < 4; n++) {
						if (board.get(GameUtil.positionToIndex(n, i, j)).state == player) {
							mark_counts += 1;
						} else if (board.get(GameUtil.positionToIndex(n, i, j)).state == 0) {
							continue;
						} else {
							mark_counts = 0;
							break;
						}
					}

					// total_counts.add(new Integer(mark_counts));
					score += (1 << mark_counts);
				}
			}

			// Y axis counts
			for(int i = 0; i<4; i++) {	
				for(int j = 0; j<4;j++)
				{
					mark_counts = 0;
					for (int n = 0; n < 4; n++) {
						if (board.get(GameUtil.positionToIndex(i, n, j)).state == player) {
							mark_counts += 1;
						} else if (board.get(GameUtil.positionToIndex(i, n, j)).state == 0) {
							continue;
						} else {
							mark_counts = 0;
							break;
						}
					}
					score += (1 << mark_counts);
					// total_counts.add(new Integer(mark_counts));
					// total_counts.add(mark_counts);
				}
			}

			// Z axis counts
			for(int i = 0; i<4; i++) {	
				for(int j = 0; j<4;j++)
				{
					mark_counts = 0;
					for (int n = 0; n < 4; n++) {
						if (board.get(GameUtil.positionToIndex(i, j, n)).state == player) {
							mark_counts += 1;
						} else if (board.get(GameUtil.positionToIndex(i, j, n)).state == 0) {
							continue;
						} else {
							mark_counts = 0;
							break;
						}
					}
					score += (1 << mark_counts);
					// total_counts.add(new Integer(mark_counts));
					// total_counts.add(mark_counts);
				}
			}

			// XY (anti)diagonal counts
			for(int i = 0; i<4; i++) {
				// diagonal
				mark_counts = 0;
				for (int n = 0; n < 4; n++) {
					if (board.get(GameUtil.positionToIndex(n, n, i)).state == player) {
						mark_counts += 1;
					} else if (board.get(GameUtil.positionToIndex(n, n, i)).state == 0) {
						continue;
					} else {
						mark_counts = 0;
						break;
					}
				}
				score += (1 << mark_counts);
				// total_counts.add(mark_counts);
			}

			for(int i = 0; i<4; i++) {
				// anti-diagonal
				mark_counts = 0;
				for (int n = 0; n < 4; n++) {
					if (board.get(GameUtil.positionToIndex(n, 3-n, i)).state == player) {
						mark_counts += 1;
					} else if (board.get(GameUtil.positionToIndex(n, 3-n, i)).state == 0) {
						continue;
					} else {
						mark_counts = 0;
						break;
					}
				}
				score += (1 << mark_counts);
				// total_counts.add(mark_counts);
			}

			// YZ (anti)diagonal counts
			for(int i = 0; i<4; i++) {
				// diagonal
				mark_counts = 0;
				for (int n = 0; n < 4; n++) {
					if (board.get(GameUtil.positionToIndex(i, n, n)).state == player) {
						mark_counts += 1;
					} else if (board.get(GameUtil.positionToIndex(i, n, n)).state == 0) {
						continue;
					} else {
						mark_counts = 0;
						break;
					}
				}
				score += (1 << mark_counts);
				// total_counts.add(mark_counts);
			}

			for(int i = 0; i<4; i++) {
				// anti-diagonal
				mark_counts = 0;
				for (int n = 0; n < 4; n++) {
					if (board.get(GameUtil.positionToIndex(i, n, 3-n)).state == player) {
						mark_counts += 1;
					} else if (board.get(GameUtil.positionToIndex(i, n, 3-n)).state == 0) {
						continue;
					} else {
						mark_counts = 0;
						break;
					}
				}
				score += (1 << mark_counts);
				// total_counts.add(mark_counts);
			}
			// XZ (anti)diagonal counts
			for(int i = 0; i<4; i++) {
				// diagonal
				mark_counts = 0;
				for (int n = 0; n < 4; n++) {
					if (board.get(GameUtil.positionToIndex(n, i, n)).state == player) {
						mark_counts += 1;
					} else if (board.get(GameUtil.positionToIndex(n, i, n)).state == 0) {
						continue;
					} else {
						mark_counts = 0;
						break;
					}
				}
				score += (1 << mark_counts);
				// total_counts.add(mark_counts);
			}

			for(int i = 0; i<4; i++) {
				// anti-diagonal
				mark_counts = 0;
				for (int n = 0; n < 4; n++) {
					if (board.get(GameUtil.positionToIndex(n, i, 3-n)).state == player) {
						mark_counts += 1;
					} else if (board.get(GameUtil.positionToIndex(n, i, 3-n)).state == 0) {
						continue;
					} else {
						mark_counts = 0;
						break;
					}
				}
				score += (1 << mark_counts);
				// total_counts.add(mark_counts);
			}
			// XYZ (anti)diagonal counts
			mark_counts = 0;
			for (int n = 0; n < 4; n++) {
				if (board.get(GameUtil.positionToIndex(n, n, n)).state == player) {
					mark_counts += 1;
				} else if (board.get(GameUtil.positionToIndex(n, n, n)).state == 0) {
					continue;
				} else {
					mark_counts = 0;
					break;
				}
			}
			score += (1 << mark_counts);
			// total_counts.add(mark_counts);

			mark_counts = 0;
			for (int n = 0; n < 4; n++) {
				if (board.get(GameUtil.positionToIndex(n, n, 3-n)).state == player) {
					mark_counts += 1;
				} else if (board.get(GameUtil.positionToIndex(n, n, 3-n)).state == 0) {
					continue;
				} else {
					mark_counts = 0;
					break;
				}
			}
			score += (1 << mark_counts);
			// total_counts.add(mark_counts);
			
			mark_counts = 0;
			for (int n = 0; n < 4; n++) {
				if (board.get(GameUtil.positionToIndex(3-n, n, n)).state == player) {
					mark_counts += 1;
				} else if (board.get(GameUtil.positionToIndex(3-n, n, n)).state == 0) {
					continue;
				} else {
					mark_counts = 0;
					break;
				}
			}
			score += (1 << mark_counts);
			// total_counts.add(mark_counts);

			mark_counts = 0;
			for (int n = 0; n < 4; n++) {
				if (board.get(GameUtil.positionToIndex(n, 3-n, n)).state == player) {
					mark_counts += 1;
				} else if (board.get(GameUtil.positionToIndex(n, 3-n, n)).state == 0) {
					continue;
				} else {
					mark_counts = 0;
					break;
				}
			}
			score += (1 << mark_counts);
			// total_counts.add(mark_counts);

			return score;
		}

		static boolean hasYWinLines(List<positionTicTacToe> board, GamePattern pattern) {

			// System.out.println("hasYWinLines");

			for(int i = 0; i<4; i++) {	
				for(int j = 0; j<4;j++)
				{
					int cur_pattern = board.get(GameUtil.positionToIndex(i, 0, j)).state;
					if (cur_pattern == board.get(GameUtil.positionToIndex(i, 1, j)).state &&
						cur_pattern == board.get(GameUtil.positionToIndex(i, 2, j)).state &&
						cur_pattern == board.get(GameUtil.positionToIndex(i, 3, j)).state) {
						pattern.setValue(cur_pattern);
						return true;
					}
				}
			}

			return false;
		}

		static boolean hasZWinLines(List<positionTicTacToe> board, GamePattern pattern) {

			for(int i = 0; i<4; i++) {	
				for(int j = 0; j<4;j++)
				{
					int cur_pattern = board.get(GameUtil.positionToIndex(i, j, 0)).state;
					if (cur_pattern == board.get(GameUtil.positionToIndex(i, j, 1)).state &&
						cur_pattern == board.get(GameUtil.positionToIndex(i, j, 2)).state &&
						cur_pattern == board.get(GameUtil.positionToIndex(i, j, 3)).state) {
						pattern.setValue(cur_pattern);
						return true;
					}
				}
			}

			return false;
		}

		static boolean hasXYWinLines(List<positionTicTacToe> board, GamePattern pattern) {

			for(int i = 0; i<4; i++) {
				// diagonal
				int cur_pattern = board.get(GameUtil.positionToIndex(0, 0, i)).state;
				if (cur_pattern == board.get(GameUtil.positionToIndex(1, 1, i)).state &&
					cur_pattern == board.get(GameUtil.positionToIndex(2, 2, i)).state &&
					cur_pattern == board.get(GameUtil.positionToIndex(3, 3, i)).state) {
					pattern.setValue(cur_pattern);
					return true;
				}

				// anti-diagonal
				cur_pattern = board.get(GameUtil.positionToIndex(0, 3, i)).state;
				if (cur_pattern == board.get(GameUtil.positionToIndex(1, 2, i)).state &&
					cur_pattern == board.get(GameUtil.positionToIndex(2, 1, i)).state &&
					cur_pattern == board.get(GameUtil.positionToIndex(3, 0, i)).state) {
					pattern.setValue(cur_pattern);
					return true;
				}
			}

			return false;
		}

		static boolean hasXZWinLines(List<positionTicTacToe> board, GamePattern pattern) {

			for(int i = 0; i<4; i++) {
				// diagonal
				int cur_pattern = board.get(GameUtil.positionToIndex(0, i, 0)).state;
				if (cur_pattern == board.get(GameUtil.positionToIndex(1, i, 1)).state &&
					cur_pattern == board.get(GameUtil.positionToIndex(2, i, 2)).state &&
					cur_pattern == board.get(GameUtil.positionToIndex(3, i, 3)).state) {
					pattern.setValue(cur_pattern);
					return true;
				}

				// anti-diagonal
				cur_pattern = board.get(GameUtil.positionToIndex(0, i, 3)).state;
				if (cur_pattern == board.get(GameUtil.positionToIndex(1, i, 2)).state &&
					cur_pattern == board.get(GameUtil.positionToIndex(2, i, 1)).state &&
					cur_pattern == board.get(GameUtil.positionToIndex(3, i, 0)).state) {
					pattern.setValue(cur_pattern);
					return true;
				}
			}

			return false;
		}

		static boolean hasYZWinLines(List<positionTicTacToe> board, GamePattern pattern) {

			for(int i = 0; i<4; i++) {
				// diagonal
				int cur_pattern = board.get(GameUtil.positionToIndex(i, 0, 0)).state;
				if (cur_pattern == board.get(GameUtil.positionToIndex(i, 1, 1)).state &&
					cur_pattern == board.get(GameUtil.positionToIndex(i, 2, 2)).state &&
					cur_pattern == board.get(GameUtil.positionToIndex(i, 3, 3)).state) {
					pattern.setValue(cur_pattern);
					return true;
				}

				// anti-diagonal
				cur_pattern = board.get(GameUtil.positionToIndex(i, 0, 3)).state;
				if (cur_pattern == board.get(GameUtil.positionToIndex(i, 1, 2)).state &&
					cur_pattern == board.get(GameUtil.positionToIndex(i, 2, 1)).state &&
					cur_pattern == board.get(GameUtil.positionToIndex(i, 3, 0)).state) {
					pattern.setValue(cur_pattern);
					return true;
				}
			}

			return false;
		}

		static boolean hasXYZWinLines(List<positionTicTacToe> board, GamePattern pattern) {

			int cur_pattern = board.get(GameUtil.positionToIndex(0, 0, 0)).state;
			if (cur_pattern == board.get(GameUtil.positionToIndex(1, 1, 1)).state &&
				cur_pattern == board.get(GameUtil.positionToIndex(2, 2, 2)).state &&
				cur_pattern == board.get(GameUtil.positionToIndex(3, 3, 3)).state) {
				pattern.setValue(cur_pattern);
				return true;
			}

			cur_pattern = board.get(GameUtil.positionToIndex(0, 0, 3)).state;
			if (cur_pattern == board.get(GameUtil.positionToIndex(1, 1, 2)).state &&
				cur_pattern == board.get(GameUtil.positionToIndex(2, 2, 1)).state &&
				cur_pattern == board.get(GameUtil.positionToIndex(3, 3, 0)).state) {
				pattern.setValue(cur_pattern);
				return true;
			}

			cur_pattern = board.get(GameUtil.positionToIndex(3, 0, 0)).state;
			if (cur_pattern == board.get(GameUtil.positionToIndex(2, 1, 1)).state &&
				cur_pattern == board.get(GameUtil.positionToIndex(1, 2, 2)).state &&
				cur_pattern == board.get(GameUtil.positionToIndex(0, 3, 3)).state) {
				pattern.setValue(cur_pattern);
				return true;
			}

			cur_pattern = board.get(GameUtil.positionToIndex(0, 3, 0)).state;
			if (cur_pattern == board.get(GameUtil.positionToIndex(1, 2, 1)).state &&
				cur_pattern == board.get(GameUtil.positionToIndex(2, 1, 2)).state &&
				cur_pattern == board.get(GameUtil.positionToIndex(3, 0, 3)).state) {
				pattern.setValue(cur_pattern);
				return true;
			}

			return false;
		}

		static boolean hasEmptySlots(List<positionTicTacToe> board) {
			
			for (int i=0;i<board.size();i++) {
				if (board.get(i).state == 0) {
					// System.out.println("hasEmptySlots");
					return true;
				}
			}
			// System.out.println("hasNoEmptySlots!!");
			return false;
		}
	}

	static class GamePattern {
		private int m_int;

		GamePattern(int i) {
			m_int = i;
		}

		public int getValue() {
			return m_int;
		}

		public void setValue(int i) {
			m_int = i;
		}
	}

}


