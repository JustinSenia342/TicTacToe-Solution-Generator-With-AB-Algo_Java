/*
 * Name: Justin Senia
 * E-Number: E00851822
 * Date: 10/20/2017
 * Class: COSC 461
 * Project: #2
 */
import java.util.LinkedList;
import java.io.*;
import java.util.*;

//This program is a Tic-Tac-Toe game, utilizing alpha-beta pruning,
//depth limits and minmax heuristics to improve run time
public class TicTacAB{
	
	private final char EMPTY = ' ';		//empty slot
	private final char COMPUTER = 'X';	//computer
	private final char PLAYER	= 'O';	//player
	private final int MIN = 0;			//min level
	private final int MAX = 1;			//max level
	private final int LIMIT = 6;		//depth limit
	
	//keeps track of current board scores for display use
	private int ScoreOx2 = 0;
	private int ScoreOx3 = 0;
	private int ScoreXx2 = 0;
	private int ScoreXx3 = 0;
	private int ScoreO = 0;
	private int ScoreX = 0;
	
	//used to add variability to children to add a small chance of
	//increasing or decreasing a child board's value to incentivize
	//not always choosing a predictable path when the state space 
	//search cannot see far enough ahead to determine victory or
	//loss paths and the majority of board values are simililar in nature.
	//this also prevents player from having ample time to setup a
	//board as long as they place tiles far enough away from each other.
	private Random rn = new Random();
	
	//Board class (inner class)
	private class Board
	{
		private char[][] array;			//board array
		
		//Constructor of Board class
		private Board(int size)
		{
			array = new char[size][size]; 	//create array
											//fill with empty slots
			for (int i = 0; i < size; i++)
				for (int j = 0; j < size; j++)
					array[i][j] = EMPTY;
		}
	}
	
	private Board board;				//game board
	private int size;					//size of board
	
	//constructor of TicTacAB class
	public TicTacAB(int size)
	{
		this.board = new Board(size);	//create game board
		this.size = size;				//assign game board size
	}
	
	//method plays game
	public void play()
	{	
		gameBoardDisplay(board);		//initially display board 
		
		while (true)					//computer and player take turns
		{
			board = playerMove(board);	//player move
			
			if (playerWin(board))		//if player wins then game over
			{
				System.out.println("");
				System.out.println("****************************");
				System.out.println("*        Player Wins       *");
				System.out.println("****************************");
				System.out.println("");
				break;
			}
			
			if (draw(board))			//if draw game over
			{
				System.out.println("");
				System.out.println("****************************");
				System.out.println("*           DRAW           *");
				System.out.println("****************************");
				System.out.println("");
				break;
			}
			
			board = computerMove(board);	//computer move
			
			if (computerWin(board))			//if computer wins then game over
			{
				System.out.println("");
				System.out.println("****************************");
				System.out.println("*      Computer Wins       *");
				System.out.println("****************************");
				System.out.println("");
				break;
			}
			
			if (draw(board))			//if draw game over
			{
				System.out.println("");
				System.out.println("****************************");
				System.out.println("*           DRAW           *");
				System.out.println("****************************");
				System.out.println("");
				break;
			}
		}
	}
	
	//method performs player move
	private Board playerMove(Board board)
	{
		System.out.print("Player move: ");			//prompt player
		Scanner scanner = new Scanner(System.in);	//read move
		int i = scanner.nextInt();
		int j = scanner.nextInt();
		
		board.array[i][j] = PLAYER;					//place player symbol
		
		//display board
		gameBoardDisplay(board);
		
		return board;								//return updated board
	}
	
	//method determines computer move
	private Board computerMove(Board board)
	{												//generate children of board
		LinkedList<Board> children = generate(board, COMPUTER);
		
		int maxIndex = 0;
		int maxValue = minmax(children.get(0), MIN, 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
		
		for (int i = 1; i < children.size(); i++)	//find child with largest minmax value
		{
			int currentValue = minmax(children.get(i), MIN, 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
			
			if (currentValue > maxValue)
			{
				maxIndex = i;
				maxValue = currentValue;
			}
		}
		
		Board result = children.get(maxIndex);	//choose child as next move
		
		System.out.println("Computer Move: ");
		
		//print next move
		gameBoardDisplay(result);
		
		return result;
	}
	
	//method computes minmax value of board
	private int minmax(Board board, int level, int depth, int alpha, int beta)
	{
		if (computerWin(board) || playerWin(board) || draw(board) || depth >= LIMIT)
			return evaluate(board);		//if board leaf
		
		else if (level == MAX)			//if board is at max level
		{
			int maxValue = Integer.MIN_VALUE;
			
			LinkedList<Board> children = generate(board, COMPUTER);
								//generate children of board
			for (int i = 0; i < children.size(); i++)
			{
				int currentValue = minmax(children.get(i), MIN, depth+1, alpha, beta);
								//find minmax values of children
				if (currentValue > maxValue)
					maxValue = currentValue;
								//if maximum exceeds beta then stop
					if (maxValue >= beta)
						return maxValue;
								//if maximum exceeds alpha then update alpha
					if (maxValue > alpha)
						alpha = maxValue;
			}
			
			return maxValue;	//return maximum value
		}
		else					//if board is at min level
		{
			int minValue = Integer.MAX_VALUE;
			
			LinkedList<Board> children = generate(board, PLAYER);
								//generate children of board
			for (int i = 0; i < children.size(); i++)
			{
				int currentValue = minmax(children.get(i), MAX, depth+1, alpha, beta);
								//find the minimum of minmax values of children
				if (currentValue < minValue)
					minValue = currentValue;
								//if minimum is less than alpha stop
				if (minValue <= alpha)
					return minValue;
								//if minimum is less than beta, update beta
				if (minValue < beta)
					beta = minValue;
			}
			
			return minValue;	//return minimum value
		}
	}
	
	//method generates children of a board using a symbol
	private LinkedList<Board> generate(Board board, char symbol)
	{
		LinkedList<Board> children = new LinkedList<Board>();
												//empty list of children
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)		//go through board
				if (board.array[i][j] == EMPTY)
				{								//if slot is empty
					Board child = copy(board);	//put the symbol and
					child.array[i][j] = symbol; //create child board
					children.addLast(child);
				}
				
		return children;						//return list of children
	}
	
	//method checks whether computer wins
	private boolean computerWin(Board board)
	{
		return full(board) && count(board, COMPUTER) > count(board, PLAYER);	//checks if computer wins
	}
	
	//method checks whether player wins
	private boolean playerWin(Board board)
	{
		return full(board) && count(board, PLAYER) > count(board, COMPUTER);	//checks if player wins
	}
	
	//method checks whether board is a draw
	private boolean draw(Board board)
	{									//check if board is full and
		return full(board) && !computerWin(board) && !playerWin(board);
										//neither computer nor player wins
	}
	
	
	//method checks whether a board is full
	private boolean full(Board board)
	{
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j ++)
				if (board.array[i][j] == EMPTY)
					return false;
				
		return true;
	}
	
	//Method evaluates a board
	private int evaluate(Board board)
	{
		if (computerWin(board))		//score is 4*size if computer wins
			return 4*size;
		else if (playerWin(board))	//score is -4*size if player wins
			return -4*size;
		else if (draw(board))		//score is 3*size if draw
			return 3*size;			
		else if (rn.nextInt(10) + 1 == 1) //adding variability to make AI less predictable
			return 1 + count(board, COMPUTER) - count(board, PLAYER);
		else
			return count(board, COMPUTER) - count(board, PLAYER);
	}								//score is difference between computer and
									//player winnings for partial board
									
	//method counts possible ways a symbol can win
	private int count(Board board, char symbol)
	{
		int answer = 0;
		
		//resetting counting global variables for re-use (for display purposes)
		if (symbol == 'O')
		{
			ScoreOx2 = 0;
			ScoreOx3 = 0;
			ScoreO = 0;
		}
		else if (symbol == 'X')
		{
			ScoreXx2 = 0;
			ScoreXx3 = 0;
			ScoreX = 0;
		}
		
		for (int i = 0; i < size; i++)
		{
			for (int j = 0; j < size; j++)
			{
				if (board.array[i][j] == symbol) //if symbol is found at target location
				{
					//checks if any immediate surrounding areas also have same symbol on them
					//then accumulates points
					
					//checking j current size so that j pointer stays in bounds
					if (j < size-1)		//as long as j is not == to last column index in array
					{
						if (board.array[i][j+1] == symbol) //two in a row is found
						{
							answer = answer + 2; //increment point total by two
							if (symbol == 'O') //if symbol being checked for is O, increment O's x2 found number
							{
								ScoreOx2 = ScoreOx2 + 1;
							}
							else if (symbol == 'X') //else if symbol is X, increment X's x2 found number
							{
								ScoreXx2 = ScoreXx2 + 1;
							}
							
							//if two in a row is found, check for three in a row
							if (j < size-2) //as long as j is not == to second to last column index in array
							{
								if (board.array[i][j+2] == symbol) //three in a row is found)
								{
									answer = answer + 3; //increment point total by 3
									if (symbol == 'O') //if symbol being checked for is O, increment O's x3 found number
									{
										ScoreOx3 = ScoreOx3 + 1;
									}
									else if (symbol == 'X') //else if symbol is X, increment X's x3 found number
									{
										ScoreXx3 = ScoreXx3 + 1;
									}
								}
							}
						}

					}
					
					//checking i current size so that i pointer stays in bounds
					if (i < size-1)		//as long as i is not == to last row index in array
					{
						if (board.array[i+1][j] == symbol) //two in a row is found
						{
							answer = answer + 2; //increment point total by two
							if (symbol == 'O') //if symbol being checked for is O, increment O's x2 found number
							{
								ScoreOx2 = ScoreOx2 + 1;
							}
							else if (symbol == 'X') //else if symbol is X, increment X's x2 found number
							{
								ScoreXx2 = ScoreXx2 + 1;
							}
							
							//if two in a row is found, check for three in a row
							if (i < size-2) //as long as i is not == to second to last row index in array
							{
								if (board.array[i+2][j] == symbol) //three in a row is found)
								{
									answer = answer + 3; //increment point total by 3
									if (symbol == 'O') //if symbol being checked for is O, increment O's x3 found number
									{
										ScoreOx3 = ScoreOx3 + 1;
									}
									else if (symbol == 'X') //else if symbol is X, increment X's x3 found number
									{
										ScoreXx3 = ScoreXx3 + 1;
									}
								}
							}
						}

					}
				}
			}
		}
			
			
		if (symbol == 'O')
		{
			ScoreO = answer;
		}
		else if (symbol == 'X')
		{
			ScoreX = answer;
		}
		
		return answer;
	}
	
	//method makes copy of board
	private Board copy(Board board)
	{
		Board result = new Board(size);
		
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				result.array[i][j] = board.array[i][j];
			
		return result;
	}
	
		//This method displays the gameboard
		public void gameBoardDisplay(Board board)
		{
			count(board, 'O');
			count(board, 'X');
			
			System.out.print("      *");			//top row space buffer
			for (int b = 0; b < size; b++)
			{
				System.out.print("****");			//fills top row with star border
			}
			System.out.println("");					//goes to next line
			
			System.out.print("      *");			//top grid guide space buffer
			for (int b = 0; b < size; b++)
			{
				System.out.print(" " + b + " *");	//prints top grid guide coordinate helper
			}
			
			//prints out O's current score
			System.out.print(" Current Score of O = 2 * " + ScoreOx2 + " + 3 * " + ScoreOx3 + " = " + ScoreO);
			System.out.println("");					//goes to next line
			
			System.out.print("      *");			//bottom of top row space buffer
			for (int b = 0; b < size; b++)
			{
				System.out.print("****");			//fills bottom of top row with star border
			}
			
			//prints out X's current score
			System.out.print(" Current Score of X = 2 * " + ScoreXx2 + " + 3 * " + ScoreXx3 + " = " + ScoreX);
			System.out.println("");					//goes to next line
			
			System.out.print("***** -");			//first game grid line buffer
			for (int b = 0; b < size; b++)
			{
				System.out.print("----");			//draws first horiz line of game grid
			}
			System.out.println("");					//goes to next line
			
			for (int i = 0; i < size; i++)
			{
				System.out.print("* " + i + " * |");	//prints side grid coordinate helper
				for (int b = 0; b < size; b++)
				{
					//fills all spaces with array values
					System.out.print(" " + board.array[i][b] + " |");	
				}
				System.out.println("");					//goes to next line
				
				System.out.print("***** -");			//bottom line buffer
				for (int b = 0; b < size; b++)
				{
					System.out.print("----");			//draws bottom horiz line of game grid
				}
				System.out.println("");
			}
			System.out.println("");
		}
		
}