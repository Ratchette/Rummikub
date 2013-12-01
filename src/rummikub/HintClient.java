package rummikub;

import java.util.ArrayList;
import java.util.Scanner;

public class HintClient extends Thread{
	private final int playerIndex;
	
	private Scanner keyboard;
	
	private GameInfo game;
	private Hand hand;
	private boolean initialMeld;
	
	
	public HintClient(boolean initialMeldMade){
		String userInput;
		
		keyboard = new Scanner(System.in);
		
		game = new GameInfo(2);
		initialMeld = initialMeldMade;
		
		hand = null;
		while(hand == null){
			System.out.print("Please enter your hand: ");
			userInput = keyboard.nextLine();
			
			try {
				hand = new Hand(userInput);
				
				if(hand.getNumTiles() != GameInfo.HAND_SIZE && !initialMeld)
					throw new Exception();
				
			} catch (Exception e) {
				hand = null;
				System.out.println("\t[ INVALID HAND ]: " + userInput);
				System.out.println("");
			}
		}
		
		// TODO Change to something more meaningful?
		playerIndex = GameInfo.PLAYER1;
	}
	
	
	public void run(){
//		String message;
//		ArrayList<Meld> play, trivialPlay;
//		Boolean playMade;
//		
//			
//		while (hand.getNumTiles() > 0) {
//			playMade = false;
//			
//			System.out.print("Enter the current state of the board: ");
//			message = keyboard.nextLine().trim();
//			if(message.endsWith("]"))
//				message = message + ", ";
//				
//			message = message + hand.getNumTiles() + " 14";
//			
//			game = new GameInfo(message);
//			
//			if(initialMeld){
//				// find if you have any plays to make in your hand
//				trivialPlay = hand.getMeldsFromHand();
//				if(trivialPlay.size() > 0){
//					game.addMelds(trivialPlay);
//					game.setHand(playerNum, hand.getNumTiles());
//					playMade = true;
//				}
//				
//				play = game.getAdjacentPlay(hand, playerNum);
//				
//				if(play != null){
//					playMade = true;
//				}
//				else if (trivialPlay.size() > 0)
//					play = trivialPlay;
//			}
//			
//			else{
//				play = hand.getInitialMeld();
//				
//				if(play != null){
//					game.addMelds(play);
//					game.setHand(playerNum, hand.getNumTiles());
//					initialMeld = true;
//					playMade = true;
//				}
//				
//			}
//			
//			// there is no play to make
//			if(!playMade){
//				System.out.println("Could not make a meld");
//				System.out.print("Please enter new tile or hand: ");
//				
//				message = keyboard.nextLine().trim();
//				if(message.startsWith("["))
//					hand = new Hand(message);
//				else
//					hand.addTile(new Tile(message));
//				
//				game.setHand(playerNum, hand.getNumTiles());
//			}
//			else{
//				// you can make a move
//				for(int i=0; i<play.size(); i++){
//					System.out.println("Play: " + play.get(i).toString());
//				}
////				System.out.println("Play : ");
////				System.out.println(game.displayGame());
//				System.out.println();
//			}
//			System.out.println("Hand: " + hand.toString());
//			System.out.println(game.displayGame());
//		}
	}

	public static void main(String[] args){
		HintClient player;
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
}


