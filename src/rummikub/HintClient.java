package rummikub;

import java.util.ArrayList;
import java.util.Scanner;

public class HintClient extends Thread{
	private final int playerIndex;
	
	private Scanner keyboard;
	
	private GameInfo game;
	private Hand hand;
	private boolean initialMeld;
	private int round;

	// **********************************************************
	// 					Constructors
	// **********************************************************
	
	public HintClient(boolean initialMeldMade){
		game = null;
		hand = null;
		initialMeld = initialMeldMade;
		round = 0;
		
		// TODO Change to something more meaningful?
		playerIndex = GameInfo.PLAYER1;
		keyboard = new Scanner(System.in);
	}
	
	// **********************************************************
    // 				Main + User input validation
    // **********************************************************
	
	public static void main(String[] args){
		boolean initial = false;
		
		if(args[0].equalsIgnoreCase("true"))
			initial = true;
		else if(args[0].equalsIgnoreCase("false"))
			initial = false;
		else{
			System.out.println("Invalid usage of this program.Proper syntax is:");
			System.out.println("\nProper syntax of this program is ");
			System.out.println("\tHintClient.java <initial meld played>");
			System.out.println("\tinitial meld played defaults to false");
			System.exit(1);
		}
		
		(new HintClient(initial)).run();
	}
	
	// **********************************************************
    // 						Starting a Game
    // **********************************************************
	
	/**
	 * Initialize a new game
	 * @throws Exception
	 */
	private void startGame(){ 
		String userInput;
		
		game = new GameInfo(2);
		
		while(hand == null){
			System.out.print("Please enter your initial hand: ");
			userInput = keyboard.nextLine();
			
			try {
				hand = new Hand(userInput);
				
			} catch (Exception e) {
				hand = null;
				System.out.println("\t[ INVALID HAND ]: " + userInput);
				System.out.println("");
			}
		}
		System.out.println("You entered: " + hand.toString());
	}
	
	// **********************************************************
    // 						Playing a game
    // **********************************************************
	
	public void run(){
		String message;
		ArrayList<Meld> play, trivialPlay;
		Boolean playMade;
		
		try{
			startGame();
			
		while (hand.getNumTiles() > 0) {
			updateRound();
			playMade = false;
			
			System.out.print("Enter the current state of the board: ");
			message = keyboard.nextLine().trim();
			if(message.endsWith("]"))
				message = message + ", ";
			message = message + hand.getNumTiles() + " 14";
			game = new GameInfo(message);
			
			
			System.out.println("Current Hand : " + hand.toString());
			System.out.println(game.displayGame());
			
			if(initialMeld){
				// find if you have any plays to make in your hand
				trivialPlay = hand.getMeldsFromHand();
				if(trivialPlay.size() > 0){
					game.addMelds(trivialPlay);
					game.setHand(playerIndex, hand.getNumTiles());
					playMade = true;
					
					for(int i=0; i<trivialPlay.size(); i++){
						System.out.println("Play: " + trivialPlay.get(i).toString());
					}
				}
				
				play = game.getAdjacentPlay(hand, playerIndex);
				
				if(play != null){
					playMade = true;
				}
				else if (trivialPlay.size() > 0)
					play = trivialPlay;
			}
			
			else{
				play = hand.getInitialMeld();
				
				if(play != null){
					game.addMelds(play);
					game.setHand(playerIndex, hand.getNumTiles());
					initialMeld = true;
					playMade = true;
					
					for(int i=0; i<play.size(); i++){
						System.out.println("Play: " + play.get(i).toString());
					}
				}
			}
			
			// there is no play to make
			if(!playMade){
				System.out.println("Could not make a meld");
				System.out.print("Please enter new tile or hand: ");
				
				message = keyboard.nextLine().trim();
				if(message.startsWith("["))
					hand = new Hand(message);
				else
					hand.addTile(new Tile(message));
				
				game.setHand(playerIndex, hand.getNumTiles());
			}
			else{
//				// you can make a move
//				
////				System.out.println("Play : ");
////				System.out.println(game.displayGame());
//				System.out.println();
			}
			
			System.out.println("========================================================\n");
			System.out.println("The game is now: ");
			System.out.println(game.displayGame());
			System.out.println("Hand : " + hand.toString());
			System.out.println("Score: " + hand.getScore());
		}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	// **********************************************************
	//					Ending a Game
	// **********************************************************
	private void updateRound(){
		round++;
		
    	System.out.println("\n\n========================================================");
    	System.out.printf( "------------------      Round %2d      ------------------\n", round);
    	System.out.println("========================================================\n");
	}
	
}


