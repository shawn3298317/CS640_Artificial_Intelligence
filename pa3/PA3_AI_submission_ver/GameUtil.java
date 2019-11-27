import java.util.*;

public class GameUtil {

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