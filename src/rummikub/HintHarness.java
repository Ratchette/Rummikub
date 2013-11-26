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
			System.out.println("Hand: " + hand.toString());
			System.out.println(game.displayGame());
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

/**
[ b1 o1 x1 ]
[ x4 x5 x6 x7 ]
[ b5 b6 b7 ]
[ b3 b4 b5 b6 ]
[ o3 o4 o5 o6 ]
[ r4 r5 r6 r7 ]
[ r1 r2 r3 ]
[ b4 r4 x4 ]
[ x8 x9 x10 x11 ]
[ x7 x8 x9 ]
[ b8 b9 b10 b11 b12 ]
[ r7 r8 r9 r10 ]
[ b13 o13 x13 ]
[ b13 o13 r13 x13 ]
[ b12 o12 r12 x12 ]
[ o11 r11 x11 ]
[ b10 o10 r10 ]

 */


