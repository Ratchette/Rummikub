package rummikub;

import java.util.ArrayList;

public class GameInfo {
	public static final int GAMEOVER = -1;
	public static final int PLAYER1 = 0;
	public static final int PLAYER2 = 1;
	public static final int PLAYER3 = 2;
	public static final int PLAYER4 = 3;
	
	public static final int HAND_SIZE = 14;
	
	private ArrayList<Set> board;
	private Pool pool;

	private Hand myHand;
	private int turn;
	
//	private Integer[] handSize;
	
	public static int getPlayer(int playerIndex){
		switch(playerIndex){
			case PLAYER1: return 1;
			case PLAYER2: return 2;
			case PLAYER3: return 3;
			case PLAYER4: return 4;
			default: return -1;
		}
	}

	public GameInfo() throws Exception{
		pool = new Pool();
		System.out.println(pool.toString());
	}

	public ArrayList<Tile> getHand(){
		ArrayList<Tile> hand = new ArrayList<Tile>();
		
		for(int i=0; i<HAND_SIZE; i++)
			hand.add(pool.pickupTile()); 
			
		return hand;
	}
	
	
}
