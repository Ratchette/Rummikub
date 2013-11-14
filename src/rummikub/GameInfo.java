package rummikub;

import java.util.ArrayList;
import java.util.HashMap;

public class GameInfo {
	public static final int GAMEOVER = -1;
	public static final int PLAYER1 = 0;
	public static final int PLAYER2 = 1;
	public static final int PLAYER3 = 2;
	public static final int PLAYER4 = 3;
	
	public static final int HAND_SIZE = 14;
	public static final int SUBSET_SIZE = 35;
	
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
		melds = allTiles.findLargestSubset(new ArrayList<Tile>(allTiles.getTiles()), null, melds);

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
	
	public ArrayList<Set> getAdjacentPlay(Set hand, int playerNum) throws Exception{
		ArrayList<Set> relatedSets;
		ArrayList<Tile> relatedTiles;
		
		// from brute force function!
		ArrayList<Set> melds, playedMelds;
		ArrayList<Tile> originalTiles, usedTiles;
		Set allTiles;
		
		playedMelds = new ArrayList<Set>();
		
		for(Tile startingTile : hand.getTiles()){
			relatedTiles = new ArrayList<Tile>();
			relatedTiles.add(startingTile);
			relatedTiles.addAll(hand.getAdjacentInHand(startingTile));
			relatedSets = getRelatedSets(relatedTiles, new ArrayList<Set>(board));
			if(relatedSets.size() == 0)
				continue;
			
			System.out.println("Related Tiles: " + relatedTiles.toString());
			for(int i=0; i<relatedSets.size(); i++)
				System.out.println("Related Sets : " + relatedSets.get(i).toString());
			System.out.println("");	
			
			// add all tiles on the board and in your hand to one set
			originalTiles = new ArrayList<Tile>();
			originalTiles.addAll(relatedTiles);
			for(int i=0; i<relatedSets.size(); i++)
				originalTiles.addAll(relatedSets.get(i).getTiles());
			
			// generate all possible runs + groups
			allTiles = new Set(originalTiles);
			melds = allTiles.getGroups();
			melds.addAll(allTiles.getRuns());
			
			// get the mutually exclusive subset with the largest score
			melds = allTiles.findLargestSubset(new ArrayList<Tile>(allTiles.getTiles()), relatedSets, melds);
			
			// Create a list of tiles from the original board
			originalTiles = new ArrayList<Tile>();
			for(int i=0; i<relatedSets.size(); i++)
				originalTiles.addAll(relatedSets.get(i).getTiles());
			
			// make a list of all tiles used in the new melds
			usedTiles = new ArrayList<Tile>();
			for(Set set : melds)
				usedTiles.addAll(set.getTiles());
			
			// remove tiles from the original board from the tiles that you used
			for(Tile tile : originalTiles){
				if(usedTiles.contains(tile))
					usedTiles.remove(tile);
			}
			
			// remove the tiles that you played from your hand
			for(Tile tile : usedTiles){
				hand.removeTile(tile);
				handSize[playerNum]--;
			}
			
			// update the board with the melds that changed
			updateBoard(relatedSets, melds);
			
			// tiles were moved from your hand to the board
			if(usedTiles.size() != 0)
				playedMelds.addAll(melds);
		}
		
		if(playedMelds.size() == 0){
			System.out.println("NO MOVE MADE");
			return null;
		}
		else{
			for(int i=0; i<playedMelds.size(); i++)
				System.out.println(playedMelds.get(i).toString());
		}
			
			
		return playedMelds;
	}
	
	
	private void updateBoard(ArrayList<Set> originalSets, ArrayList<Set> melds){
		for(int i=0; i<originalSets.size(); i++){
			for(int j=0; j<board.size(); j++){
				if(originalSets.get(i).equals(board.get(j))){
					board.remove(j);
					break;
				}
			}
		}
		
		board.addAll(melds);
	}
	
	
	private ArrayList<Set> getRelatedSets(ArrayList<Tile> originalTiles, ArrayList<Set> board){
		ArrayList<Set> adjacentSets;
		ArrayList<Tile> adjacentTiles;
		Integer[] numAdjacent;
		int indexOfMostMatches, maximumMatches;
		boolean addedTiles;
		
		adjacentSets = new ArrayList<Set>();
		adjacentTiles = new ArrayList<Tile>(originalTiles);
		addedTiles = true;
		
		while(addedTiles && adjacentTiles.size() < SUBSET_SIZE){
			addedTiles = false;
			numAdjacent = new Integer[board.size()];
			for(int i=0; i<board.size(); i++)
				numAdjacent[i] = new Integer(0);
			
			
			for(int i=0; i<adjacentTiles.size(); i++){ 
				for(int j=0; j<board.size(); j++){			
					numAdjacent[j] = numAdjacent[j] + board.get(j).getNumAdjacent(adjacentTiles.get(i));
				}
			}
			
			// find the set with the most matches
			maximumMatches = 0;
			indexOfMostMatches = -1;
			
			for(int j=0; j<board.size(); j++){
				if(numAdjacent[j]  > maximumMatches){
					maximumMatches = numAdjacent[j];
					indexOfMostMatches = j;
				}
			}
			
			// add all those tiles to the number of adjacent tiles
			if(maximumMatches > 0){
				if(adjacentTiles.size() + board.get(indexOfMostMatches).getNumTiles() > SUBSET_SIZE)
					break;
				
				adjacentTiles.addAll(board.get(indexOfMostMatches).getTiles());
				adjacentSets.add(board.get(indexOfMostMatches));
				
				board.remove(indexOfMostMatches);
				addedTiles = true;
			}
		}
		
		return adjacentSets;
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
