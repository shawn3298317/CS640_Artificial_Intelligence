import java.util.*;

public class RLAgent extends BaseAgent {

	public int player; //1 for player 1 and 2 for player 2
	
	private QTable m_qTable;

	// hyper params
	private int m_numEpisode = 500;
	private double m_gamma = 0.95;
	private double m_alpha = 0.5;
	private double m_epsilon = 0.1;


	public RLAgent(int setPlayer, String qTablePath, boolean isTrain)
	{
		super(setPlayer);
		if (!isTrain)
			m_qTable = loadQTable(qTablePath);
	}

	public QTable loadQTable(String qTablePath) {
		return new QTable();
	}

	public void setHyperParam(double alpha, double gamma, double epsilon, int numEpisode) {
		m_alpha = alpha;
		m_gamma = gamma;
		m_epsilon = epsilon;
		m_numEpisode = numEpisode;
	}

	@Override
	public String getAgentName() {
		return new String("RLAgent");
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

	/*
		TODO: Train agent with Q-Learning
		TODO: Enable training flow in runTicTacToe
	*/
}
