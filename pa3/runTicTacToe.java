import java.util.*;

//TicTacToe-4x4x4
public class runTicTacToe {
	
	private List<List<positionTicTacToe>>  winningLines = new ArrayList<>(); 
	private List<positionTicTacToe> board = new ArrayList<>();
	private BaseAgent player1;
	private BaseAgent player2;

	public int result;
	public runTicTacToe()
	{
		//initialize winning lines
		// winningLines = initializeWinningLines();
		//initialzie board
		board = createTicTacToeBoard();
		
		//initialize AI players
		player1 = new RandomAgent(1);
		player2 = new RandomAgent(2);
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
	private int isEnded()
	{

		GamePattern pattern = new GamePattern(-1);

		// straight line checks
		if (GameUtil.hasXWinLines(board, pattern)) {
			System.out.println("hasXWinLines" + pattern.getValue());
			return pattern.getValue();
		}
		if (GameUtil.hasYWinLines(board, pattern)) {
			System.out.println("hasYWinLines" + pattern.getValue());
			return pattern.getValue();
		}
		if (GameUtil.hasZWinLines(board, pattern)) {
			System.out.println("hasZWinLines" + pattern.getValue());
			return pattern.getValue();
		}

		// plane diagonal line checks
		if (GameUtil.hasXZWinLines(board, pattern)) {
			System.out.println("hasXYWinLines" + pattern.getValue());
			return pattern.getValue();
		}
		if (GameUtil.hasYZWinLines(board, pattern)) {
			System.out.println("hasYZWinLines" + pattern.getValue());
			return pattern.getValue();
		}
		if (GameUtil.hasXYWinLines(board, pattern)) {
			System.out.println("hasXYWinLines" + pattern.getValue());
			return pattern.getValue();
		}

		// cubic diagonal line checks
		if (GameUtil.hasXYZWinLines(board, pattern)) {
			System.out.println("hasXYZWinLines" + pattern.getValue());
			return pattern.getValue();
		}

		if (GameUtil.hasEmptySlots(board)) {
			return 0; // Continue
		} else {
			return -1; // Call Draw
		}

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

	public void run()
	{

		Random rand = new Random();
		int turn = rand.nextInt(2)+1; //1 = player1's turn, 2 = player2's turn, who go first is randomized 
		
		while((result = isEnded())==0) //game loop
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
				System.out.println("Error!");
			}
		}
		
			//game is ended
		if(result==1)
		{
			//game ends, player 1 wins 
			System.out.println("Player:" + this.player1.getAgentName() + " Wins");
			printBoardTicTacToe(board);
		}
		else if(result==2)
		{
			//game ends, player 1 wins 
			System.out.println("Player:" + this.player2.getAgentName() + " Wins");
			printBoardTicTacToe(board);
		}
		else if(result==-1)
		{
			//game ends, it's a draw 
			System.out.println("This is a draw.");
			printBoardTicTacToe(board);
		}
		else
		{
			//exception occurs, stop
			System.out.println("Error!");
		}
		
	}
	

	
	//run the game once

	public static void main(String[] args) {

		//run game loop
		runTicTacToe rttt = new runTicTacToe();
		rttt.run();
	}
}


