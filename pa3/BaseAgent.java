import java.util.*;

public class BaseAgent {

	public int player; //1 for player 1 and 2 for player 2

	public BaseAgent(int setPlayer)
	{
		player = setPlayer;
	}

	public String getAgentName() {
		return new String("BaseAgent");
	}

	protected int getStateOfPositionFromBoard(positionTicTacToe position, List<positionTicTacToe> board)
	{
		//a helper function to get state of a certain position in the Tic-Tac-Toe board by given position TicTacToe
		int index = GameUtil.positionToIndex(position.x, position.y, position.z);
		return board.get(index).state;
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

	public positionTicTacToe getPolicyFromState(List<positionTicTacToe> board, int player) {
		return new positionTicTacToe(0,0,0);
	}
}
