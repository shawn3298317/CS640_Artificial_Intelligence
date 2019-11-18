import java.util.*;

public class MinMaxAgent extends BaseAgent {

	public int player; //1 for player 1 and 2 for player 2

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
		positionTicTacToe myNextMove = new positionTicTacToe(0,0,0);

		do {
			Random rand = new Random();
			int x = rand.nextInt(4);
			int y = rand.nextInt(4);
			int z = rand.nextInt(4);
			myNextMove = new positionTicTacToe(x,y,z);
		} while (getStateOfPositionFromBoard(myNextMove, board)!=0);

		return myNextMove;

	}

	protected Double getValueFromStateAction(List<positionTicTacToe> state, positionTicTacToe action) {

		// Has complete line(s)

		// Number of almost complete lines

		return 0.0;
	}
}
