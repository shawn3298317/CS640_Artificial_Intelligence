import java.util.*;

//TicTacToe-4x4x4
public class runTicTacToe {
	
	private List<List<positionTicTacToe>>  winningLines = new ArrayList<>(); 
	private List<positionTicTacToe> board = new ArrayList<>();
	private BaseAgent player1;
	private BaseAgent player2;

	public runTicTacToe()
	{
		//initialize winning lines
		// winningLines = initializeWinningLines();
		//initialzie board
		board = createTicTacToeBoard();
		
		//initialize AI players
		initializePlayers();
	}
	public void initializePlayers() {
		// player1 = new RandomAgent(1);
		player1 = new MinMaxAgent(2, 4, false);
		player2 = new MinMaxAgent(2, 4, true);
		// player2 = new RandomAgent(2);
	}
	public void resetGame() {
		board = createTicTacToeBoard();
		player1.resetAgent();
		player2.resetAgent();
	}
	private List<positionTicTacToe> createTicTacToeBoard()
	{
		//create a 3-d 4x4x4 TicTacToe board and store it in a list
		List<positionTicTacToe> boardTicTacToe = new ArrayList<positionTicTacToe>();
		for(int i=0;i<4;i++)
			for(int j=0;j<4;j++)
				for(int k=0;k<4;k++)
					{
						boardTicTacToe.add(new positionTicTacToe(i,j,k,0)); //0 is state "not marked"
					}
		return boardTicTacToe;
	}
	
	private List<positionTicTacToe> deepCopyATicTacToeBoard(List<positionTicTacToe> board)
	{
		//deep copy of game boards
		List<positionTicTacToe> copiedBoard = new ArrayList<positionTicTacToe>();
		for(int i=0;i<board.size();i++)
		{
			copiedBoard.add(new positionTicTacToe(board.get(i).x,board.get(i).y,board.get(i).z,board.get(i).state));
		}
		return copiedBoard;
	}

	private int getStateOfPositionFromBoard(positionTicTacToe position, List<positionTicTacToe> targetBoard)
	{
		//a helper function to get state of a certain position in the Tic-Tac-Toe board by given position TicTacToe
		int index = position.x*16+position.y*4+position.z;
		return targetBoard.get(index).state;
	}
	
	
	public void printAllWinningLines()
	{
		//print all winning lines to help understand in what cases will any player win
		System.out.println(winningLines.size());
		for (int i=0;i<winningLines.size();i++)
		{
			System.out.println("Winning Line "+i+":");
			for (int j=0;j<winningLines.get(i).size();j++)
			{
				winningLines.get(i).get(j).printPosition();
			}
		}
	}
	public void printBoardTicTacToe(List<positionTicTacToe> targetBoard)
	{
		//print each position on the board, uncomment this for debugging if necessary
		/*
		System.out.println("board:");
		System.out.println("board slots: "+board.size());
		for (int i=0;i<board.size();i++)
		{
			board.get(i).printPosition();
		}
		*/
		
		//print in "graphical" display
		for (int i=0;i<4;i++)
		{
			System.out.println("level(z) "+i);
			for(int j=0;j<4;j++)
			{
				System.out.print("["); // boundary
				for(int k=0;k<4;k++)
				{
					if (getStateOfPositionFromBoard(new positionTicTacToe(j,k,i),targetBoard)==1)
					{
						System.out.print("X"); //print cross "X" for position marked by player 1
					}
					else if(getStateOfPositionFromBoard(new positionTicTacToe(j,k,i),targetBoard)==2)
					{
						System.out.print("O"); //print cross "O" for position marked by player 2
					}
					else if(getStateOfPositionFromBoard(new positionTicTacToe(j,k,i),targetBoard)==0)
					{
						System.out.print("_"); //print "_" if the position is not marked
					}
					if(k==3)
					{
						System.out.print("]"); // boundary
						System.out.println();
					}
					
					
				}

			}
			System.out.println();
		}
	}

	public int run(boolean verbose)
	{
		int result = 0;
		Random rand = new Random();
		int turn = rand.nextInt(2)+1; //1 = player1's turn, 2 = player2's turn, who go first is randomized 
		
		result = GameUtil.isEnded(board);
		while (result == 0) //game loop
		{
			if(turn == 1)
			{
				positionTicTacToe player1NextMove = player1.getPolicyFromState(board, 1); //1 stands for player 1
				if (player1.makeMove(player1NextMove, 1, board))
					turn = 2;
			}
			else if(turn == 2)
			{
				positionTicTacToe player2NextMove = player2.getPolicyFromState(board, 2); //2 stands for player 2
				if (player2.makeMove(player2NextMove, 2, board))
					turn = 1;
			}
			else 
			{
				//exception occurs, stop
				System.out.println("Error1!");
			}
			result = GameUtil.isEnded(board);
		}
		
		//game is ended
		if(result == 1)
		{
			//game ends, player 1 wins 
			if (verbose) {
				System.out.println("Player"+ result +":" + this.player1.getAgentName() + " Wins");
				printBoardTicTacToe(board);
			}
		}
		else if(result == 2)
		{
			//game ends, player 1 wins 
			if (verbose) {
				System.out.println("Player"+ result +":" + this.player2.getAgentName() + " Wins");
				printBoardTicTacToe(board);
			}
		}
		else if(result == -1)
		{
			//game ends, it's a draw 
			if (verbose) {
				System.out.println("This is a draw.");
				printBoardTicTacToe(board);
			}
				
		}
		else
		{
			//exception occurs, stop
			System.out.println("Error2! " + result);	
			if (verbose) {
				printBoardTicTacToe(board);	
			}
		}

		return result;
		
	}

	public void playNGames(int n, boolean verbose) {

		Double player1_won = .0;
		Double player2_won = .0;
		Double tie = .0;

		for (int i = 0; i < n; i++) {
			int result = this.run(verbose);
			if (result == 1)
				player1_won += 1.0;
			else if (result == 2)
				player2_won += 1.0;
			else
				tie += 1.0;
			this.resetGame();
		}

		System.out.println("Player1: " + player1.getAgentName() + " Win Rate = " + player1_won/n);
		System.out.println("Player2: " + player2.getAgentName() + " Win Rate = " + player2_won/n);
		System.out.println("Tie Rate: " + tie/n);	

	}

	
	//run the game once
	public static void main(String[] args) {

		runTicTacToe rttt = new runTicTacToe();

		int n = 1;
		boolean verbose = false;
		if (args.length > 0) {
			n = Integer.parseInt(args[0]);
		}
		if (args.length > 1) {
			verbose = (args[1].equals("--verbose"));
		}
		rttt.playNGames(n, verbose);
	}
}


