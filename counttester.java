
import java.util.LinkedList;
import java.io.*;
import java.util.*;
public class counttester{

	public static void main(String[] args)
	{
		char[][] array = {{ 'O' , 'O' , 'X' , 'O' },
		{ 'X' , 'O' , 'X' , 'O' },
		{ 'X' , 'X' , 'X' , 'O' },
		{ 'O' , 'X' , 'O' , 'X' }};

		/*
		for (int i = 0; i < 4; i++){
			for (int j = 0; j < 4; j++){
				System.out.print(array[i][j] + " ");
			}
			System.out.println("");
		}
		*/
		
		System.out.println("O points: " + count(array, 'O'));
		System.out.println("X points: " + count(array, 'X'));
		
	}
	
	//method counts possible ways a symbol can win
	public static int count(char[][] array, char symbol)
	{
		int size = 4;
		int answer = 0;
		
		for (int i = 0; i < size; i++)
		{
			for (int j = 0; j < size; j++)
			{
				if (array[i][j] == symbol) //if symbol is found at target location
				{
					//checks if any immediate surrounding areas also have same symbol on them
					//then accumulates points
					
					//checking j current size so that j pointer stays in bounds
					if (j < size-1)		//as long as j is not == to last column index in array
					{
						if (array[i][j+1] == symbol) //two in a row is found
						{
							answer = answer + 2; //increment point total by two
							
							//if two in a row is found, check for three in a row
							if (j < size-2) //as long as j is not == to second to last column index in array
							{
								if (array[i][j+2] == symbol) //three in a row is found)
									answer = answer + 3; //increment point total by 3
							}
						}

					}
					
					//checking i current size so that i pointer stays in bounds
					if (i < size-1)		//as long as i is not == to last row index in array
					{
						if (array[i+1][j] == symbol) //two in a row is found
						{
							answer = answer + 2; //increment point total by two
							
							//if two in a row is found, check for three in a row
							if (i < size-2) //as long as i is not == to second to last row index in array
							{
								if (array[i+2][j] == symbol) //three in a row is found)
									answer = answer + 3; //increment point total by 3
							}
						}

					}
				}
			}
		}
			
		return answer;
	}
}