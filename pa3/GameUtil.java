import java.util.*;

public class GameUtil {

static int positionToIndex(int x, int y, int z) {
	return (x*16 + y*4 + z);
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
			return true;
		}
	}
	return false;
}

}