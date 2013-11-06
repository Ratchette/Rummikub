package rummikub;

import java.util.ArrayList;
import java.util.HashMap;

public class GameInfo {
	public static final int GAMEOVER = -1;
	public static final int PLAYER1 = 0;
	public static final int PLAYER2 = 1;
	public static final int PLAYER3 = 2;
	public static final int PLAYER4 = 3;
	
	public static final int HAND_SIZE = 7;
	
	private ArrayList<Set> board;
	private Integer[] handSize;
	
	public GameInfo(int numPlayers) throws Exception{
    	this.board = new ArrayList<Set>();
    	
    	this.handSize = new Integer[numPlayers];
    	for(int i=0; i<handSize.length; i++)
    		handSize[i] = HAND_SIZE;
	}
	
	public GameInfo(String encodedGame) throws Exception{
		String[] tokens;
		int i;
		
		tokens = encodedGame.split(",");
		
		this.board = new ArrayList<Set>();
		
		for(i=0; i<tokens.length; i++){
			tokens[i] = tokens[i].trim();
			
			if(tokens[i].startsWith("["))
				board.add(new Set(tokens[i]));
			else
				break;
		}
		
		tokens = tokens[i].split(" ");
		this.handSize = new Integer[tokens.length];
		
		for(i=0; i<handSize.length; i++)
			handSize[i] = Integer.parseInt(tokens[i]);
	}
	

	public void addTile(int player){
		handSize[player]++;
	}

	public void setHand(int player, int size){
		handSize[player] = size;
	}
	
	public boolean isGameOver(){
		for(int i=0; i<handSize.length; i++)
			if(handSize[i] < 1)
				return true;
		
		
		return false;
	}
	
//	public ArrayList<Set> getHumanBasedMove(Set hand, int playerNum) throws Exception{
//		HashMap<Tile, ArrayList<Integer>> endTiles, freeTiles;
//		ArrayList<Set> melds;
//		
//		melds = hand.getInitialMeld();
//		
//		endTiles = getEndTiles();
//		
//		
//		return null;
//	}
//	
//	private HashMap<Tile, ArrayList<Integer>> getFreeTiles(){
//		HashMap<Tile, ArrayList<Integer>> tiles = new HashMap<Tile, ArrayList<Integer>>();
//		
//		for(Set set : board){
//			if(set.isRun){
//				
//			}
//			else{
//				if(set.getNumTiles() < 4)
//			}
//		}
//		
//		
//	}
//	
//	private HashMap<Tile, ArrayList<Integer>> getEndTiles(){
//		HashMap<Tile, ArrayList<Integer>> tiles = new HashMap<Tile, ArrayList<Integer>>();
//		
//		for(Set set : board){
//			if(set.isRun){
//				
//			}
//			else{
//				if()
//			}
//		}
//	}
	
	// combine hand with deck and find all valid moves
	public ArrayList<Set> getBruteForceMove(Set hand, int playerNum) throws Exception{
		ArrayList<Set> melds;
		ArrayList<Tile> originalTiles, usedTiles;
		Set allTiles;
		
		// add all tiles on the board and in your hand to one set
		originalTiles = new ArrayList<Tile>();
		originalTiles.addAll(hand.getTiles());
		for(int i=0; i<board.size(); i++)
			originalTiles.addAll(board.get(i).getTiles());
		
		// generate all possible runs + groups
		allTiles = new Set(originalTiles);
		melds = allTiles.getGroups();
		melds.addAll(allTiles.getRuns());
		
		// get the mutually exclusive subset with the largest score
		melds = allTiles.findLargestSubset(new ArrayList<Tile>(allTiles.getTiles()), melds);

		// Create a list of tiles from the original board
		originalTiles = new ArrayList<Tile>();
		for(int i=0; i<board.size(); i++)
			originalTiles.addAll(board.get(i).getTiles());
		
		// make a list of all tiles used in the new melds
		usedTiles = new ArrayList<Tile>();
		for(Set set : melds)
			usedTiles.addAll(set.getTiles());
		
		// remove tiles from the original board from the tiles that you used
		for(Tile tile : originalTiles){
			if(usedTiles.contains(tile))
				usedTiles.remove(tile);
		}
		
		// no tiles were moved from your hand to the board
		if(usedTiles.size() == 0)
			return null;
		
		// remove the tiles that you played from your hand
		for(Tile tile : usedTiles){
			hand.removeTile(tile);
			handSize[playerNum]--;
		}
		
		return melds;
	}
	
	public void addMelds(ArrayList<Set> newMelds){
		board.addAll(newMelds);
	} 	
	
	public void setMelds(ArrayList<Set> melds){
		ArrayList<Tile> oldTiles, newTiles;
		
		oldTiles = new ArrayList<Tile>();
		for(Set set : board)
			oldTiles.addAll(set.getTiles());
		
		newTiles = new ArrayList<Tile>();
		for(Set set: melds)
			newTiles.addAll(set.getTiles());

		for(Tile tile : oldTiles)
			newTiles.remove(tile);
		
		board = new ArrayList<Set>(melds);
	}
	
	@Override
	public String toString(){
		String encoding;
		
		encoding = "";
		
		for(int i=0; i<board.size(); i++)
			encoding = encoding + board.get(i).toString() + ", ";
		
		for(int i=0; i<handSize.length; i++)
			encoding = encoding + handSize[i] + " ";
		
		return encoding;
	}
	
	
	public String displayGame(){
		String game;
		
		game = "\n--------------------------------------------------------\n";
		
		game = game + "Hand Sizes: ";
		for(int i=0; i<handSize.length; i++)
			game = game + handSize[i] + " ";
		
		game = game + "\nBoard:\n";
		for(Set set : board)
			game = game + "\t" + set.toString() + "\n";
		
		game = game + "--------------------------------------------------------\n";
		
		return game;
	}
}
