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

public class TicTacABTester
{

    //Main method for testing
    public static void main(String[] args) throws IOException, NumberFormatException
    {
      //creating buffered reader for getting user input
      java.io.BufferedReader keyIn = new BufferedReader(new InputStreamReader(System.in));

	  //message welcoming to the program/giving instructions
      System.out.println("*****************************************");
      System.out.println("*      Tic-Tac-Toe Multiscore game      *");
      System.out.println("*      (Using multiple Heuristics)      *");
      System.out.println("*****************************************");
      System.out.println("*   Please an even # board-size start   *");
      System.out.println("* or type quit to terminate the program *");
      System.out.println("*****************************************");

      //start a loop that continues querying for input as long as user
      //does not enter "quit" (without the quotes)
      while (true)
      {
        System.out.print("Please make your entry now: ");
        String userIn = ""; //used for file entry and/or quitting
		
        userIn = keyIn.readLine(); //reading user input

        if (userIn.equalsIgnoreCase("quit")) //if user typed quit, stops program
          break;
        else
		{
			//try
		//	{
				//getting board size from user input
				int boardSize = Integer.parseInt(userIn);
				System.out.println(" ");

				//creating new TicTacAB object and calling play() Method
				TicTacAB tAB = new TicTacAB(boardSize);
				tAB.play();

			//}
		//	catch (IOException e) //catches if there were any fileIO exceptions
		//	{
		//		;
		//	}
		}
	  }
    }
}
