package rummikub;

import java.util.ArrayList;
import java.util.Scanner;

public class HintHarness {
	private GameInfo game;
	private Hand hand;
	private boolean initialMeld;
	private static final int playerNum = GameInfo.PLAYER1;
	
	private Scanner keyboard;
	
	public HintHarness(boolean initialMeldMade) throws Exception{
		String encodedHand = "";
		
		keyboard = new Scanner(System.in);
		
		initialMeld = initialMeldMade;
		game = new GameInfo(2);
		
		System.out.print("Please enter your hand: ");
		while(!encodedHand.trim().startsWith("[")) 
			encodedHand = keyboard.nextLine();
		
		hand = new Hand(encodedHand);
		System.out.println();
	}
	
	public void run() throws Exception{
		String message;
		ArrayList<Meld> play, trivialPlay;
		Boolean playMade;
		
			
		while (hand.getNumTiles() > 0) {
			playMade = false;
			
			System.out.print("Enter the current state of the board: ");
			message = keyboard.nextLine().trim();
			if(message.endsWith("]"))
				message = message + ", ";
				
			message = message + hand.getNumTiles() + " 14";
			
			game = new GameInfo(message);
			System.out.println("Hand: " + hand.toString());
			System.out.println(game.displayGame());
			
			if(initialMeld){
				// find if you have any plays to make in your hand
				trivialPlay = hand.getMeldsFromHand();
				if(trivialPlay.size() > 0){
					game.addMelds(trivialPlay);
					game.setHand(playerNum, hand.getNumTiles());
					playMade = true;
				}
				
				play = game.getAdjacentPlay(hand, playerNum);
				
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
					game.setHand(playerNum, hand.getNumTiles());
					initialMeld = true;
					playMade = true;
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
				
				game.setHand(playerNum, hand.getNumTiles());
			}
			else{
				// you can make a move
				for(int i=0; i<play.size(); i++){
					System.out.println("Play: " + play.get(i).toString());
				}
//				System.out.println("Play : ");
//				System.out.println(game.displayGame());
				System.out.println();
			}
			
		}
	}
	
	public static void main(){
		try {
//			HintHarness player = new HintHarness();
//			player.run();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
