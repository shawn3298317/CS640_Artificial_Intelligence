import java.util.*;

public class RandomAgent extends BaseAgent {

	public int player; //1 for player 1 and 2 for player 2
	public int opponent_player;

	public RandomAgent(int setPlayer)
	{
		super(setPlayer);
	}

	@Override
	public String getAgentName() {
		return new String("RandomAgent");
	}

	@Override
	public positionTicTacToe getPolicyFromState(List<positionTicTacToe> board, int player)
	{
		//TODO: this is where you are going to implement your AI algorithm to win the game. The default is an AI randomly choose any available move.
		positionTicTacToe myNextMove = new positionTicTacToe(0,0,0);

		int seed = 0;

		do {
			Random rand = new Random(seed++);
//			System.out.println("c2");
			int x = rand.nextInt(4);
			int y = rand.nextInt(4);
			int z = rand.nextInt(4);
//			System.out.println(x + ", " + y + ", " + z);
			myNextMove = new positionTicTacToe(x,y,z);
		} while (getStateOfPositionFromBoard(myNextMove, board)!=0);

		return myNextMove;

	}
}
