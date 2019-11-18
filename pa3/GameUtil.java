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
		// System.out.println("hasXWinLines" + pattern.getValue());
		return pattern.getValue();
	}
	if (GameUtil.hasYWinLines(board, pattern)) {
		// System.out.println("hasYWinLines" + pattern.getValue());
		return pattern.getValue();
	}
	if (GameUtil.hasZWinLines(board, pattern)) {
		// System.out.println("hasZWinLines" + pattern.getValue());
		return pattern.getValue();
	}

	// plane diagonal line checks
	if (GameUtil.hasXZWinLines(board, pattern)) {
		// System.out.println("hasXYWinLines" + pattern.getValue());
		return pattern.getValue();
	}
	if (GameUtil.hasYZWinLines(board, pattern)) {
		// System.out.println("hasYZWinLines" + pattern.getValue());
		return pattern.getValue();
	}
	if (GameUtil.hasXYWinLines(board, pattern)) {
		// System.out.println("hasXYWinLines" + pattern.getValue());
		return pattern.getValue();
	}

	// cubic diagonal line checks
	if (GameUtil.hasXYZWinLines(board, pattern)) {
		// System.out.println("hasXYZWinLines" + pattern.getValue());
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