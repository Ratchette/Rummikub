package rummikub;

import java.util.ArrayList;
import java.util.Collections;

public class GameInfo {
	public static final int PLAYER1 = 0;
	public static final int PLAYER2 = 1;
	public static final int PLAYER3 = 2;
	public static final int PLAYER4 = 3;
	
	// end game turn statuses
	public static final int GAMEOVER = -1;
	public static final int DISCONNECT = -2;
	
	public static final int HAND_SIZE = 14;
	public static final int SUBSET_SIZE = 35;
	
	private ArrayList<Meld> board;
	private Integer[] tilesInHand;
	
	// **********************************************************
    //						Constructors							
    // **********************************************************
	
	/**
	 * Create a new game
	 * @param numPlayers
	 * 			The number of players in a game
	 * @throws Exception
	 */
	public GameInfo(int numPlayers) {
    	this.board = new ArrayList<Meld>();
    	
    	this.tilesInHand = new Integer[numPlayers];
    	for(int i=0; i<tilesInHand.length; i++)
    		tilesInHand[i] = HAND_SIZE;
	}
	
	/**
	 * Copy constructor
	 * 
	 * @param encodedGame
	 * @throws Exception
	 */
	public GameInfo(String encodedGame) throws Exception{
		String[] tokens;
		int i;
		
		tokens = encodedGame.split(",");
		this.board = new ArrayList<Meld>();
		
		for(i=0; i<tokens.length; i++){
			tokens[i] = tokens[i].trim();
			
			if(tokens[i].startsWith("["))
				board.add(new Meld(tokens[i]));
			else
				break;
		}
		Collections.sort(board);
		
		tokens = tokens[i].split(" ");
		this.tilesInHand = new Integer[tokens.length];
		
		for(i=0; i<tilesInHand.length; i++)
			tilesInHand[i] = Integer.parseInt(tokens[i]);
	}

	
	// **********************************************************
    //					Getters and setters							
    // **********************************************************
	
	/**
	 * Incremenets the number of tiles a player has
	 * @param player the player who has added a tile to their hand
	 */
	public void addTile(int player){
		tilesInHand[player]++;
	}

	public void setHand(int player, int size){
		tilesInHand[player] = size;
	}
	
	public void addMelds(ArrayList<Meld> newMelds){
		board.addAll(newMelds);
	} 	
	
	public void setMelds(ArrayList<Meld> melds){
		ArrayList<Tile> oldTiles, newTiles;
		
		oldTiles = new ArrayList<Tile>();
		for(Meld meld : board)
			oldTiles.addAll(meld.getTiles());
		
		newTiles = new ArrayList<Tile>();
		for(Meld meld: melds)
			newTiles.addAll(meld.getTiles());

		for(Tile tile : oldTiles)
			newTiles.remove(tile);
		
		board = new ArrayList<Meld>(melds);
	}
	
	/**
	 * Determine if the game has been won yet
	 * @return true if any player has no cards remaining in their hand
	 */
	public boolean isGameOver(){
		for(int i=0; i<tilesInHand.length; i++)
			if(tilesInHand[i] < 1)
				return true;
		
		return false;
	}
	
	public int getWinner(){
		for(int i=0; i<tilesInHand.length; i++)
			if(tilesInHand[i] < 1)
				return i;
		
		return DISCONNECT;
	}
	
	public ArrayList<Meld> getBoard(){
		return new ArrayList<Meld>(board);
	}

	// **********************************************************
    //						Move creation							
    // **********************************************************

	// combine hand with deck and find all valid moves
	public ArrayList<Meld> getBruteForceMove(Hand hand, int playerNum) throws Exception{
		ArrayList<Meld> melds;
		ArrayList<Tile> originalTiles, usedTiles;
		Hand allTiles;
		
		// add all tiles on the board and in your hand to one set
		originalTiles = new ArrayList<Tile>();
		originalTiles.addAll(hand.getTiles());
		for(int i=0; i<board.size(); i++)
			originalTiles.addAll(board.get(i).getTiles());
		
		// generate all possible runs + groups
		allTiles = new Hand(originalTiles);
		melds = allTiles.getGroups();
		melds.addAll(allTiles.getRuns());
		
		// get the mutually exclusive subset with the largest score
		melds = allTiles.findLargestSubset(new ArrayList<Tile>(allTiles.getTiles()), new ArrayList<Tile>(), melds);

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
			tilesInHand[playerNum]--;
		}
		
		return melds;
	}
	
	public ArrayList<Meld> getAdjacentPlay(Hand hand, int playerNum) throws Exception{
		ArrayList<Meld> relatedSets;
		ArrayList<Tile> startingTiles;
		boolean playMade;
		
		// from brute force function!
		ArrayList<Meld> melds, playedMelds;
		ArrayList<Tile> tilesFromBoard, usedTiles;
		Hand allTiles;
		
		playedMelds = new ArrayList<Meld>();
		playMade = true;
		
		while(playMade){
			playMade = false;
			
			for(Tile startingTile : hand.getTiles()){
				startingTiles = new ArrayList<Tile>();
				startingTiles.add(startingTile);
				startingTiles.addAll(hand.getAdjacentInHand(startingTile));
				relatedSets = getRelatedSets(startingTiles, new ArrayList<Meld>(board));
				if(relatedSets.size() == 0)
					continue;
				
//				System.out.println("Related Tiles: " + startingTiles.toString());
//				for(int i=0; i<relatedSets.size(); i++)
//					System.out.println("Related Sets : " + relatedSets.get(i).toString());
//				System.out.println("");	
				
				// add all tiles on the board and in your hand to one set
				tilesFromBoard = new ArrayList<Tile>();
				for(int i=0; i<relatedSets.size(); i++){
					tilesFromBoard.addAll(relatedSets.get(i).getTiles());
					startingTiles.addAll(relatedSets.get(i).getTiles());
				}
				
				// generate all possible runs + groups
				allTiles = new Hand(startingTiles);
				melds = allTiles.getGroups();
				melds.addAll(allTiles.getRuns());
				
				// get the mutually exclusive subset with the largest score
				melds = allTiles.findLargestSubset(new ArrayList<Tile>(allTiles.getTiles()), tilesFromBoard, melds);
				if(melds == null)
					continue;
				
				// make a list of all tiles used in the new melds
				usedTiles = new ArrayList<Tile>();
				for(Set set : melds)
					usedTiles.addAll(set.getTiles());
				
				// remove tiles from the original board from the tiles that you used
				for(Tile tile : tilesFromBoard){
					if(usedTiles.contains(tile))
						usedTiles.remove(tile);
					else 
						throw new Exception("You lost tile " + tile.toString());
				}
				
				
				// FIXME move harness stuff
				Hand oldHand = new Hand(hand);
				ArrayList<Meld> oldBoard = new ArrayList<Meld>(board);
				
				
				// remove the tiles that you played from your hand
				for(Tile tile : usedTiles){
					hand.removeTile(tile);
					tilesInHand[playerNum]--;
				}
				
				if(usedTiles.size() > 0)
					playMade = true;
				
				// update the board with the melds that changed
				updateBoard(relatedSets, melds);
				
				// tiles were moved from your hand to the board
				if(usedTiles.size() != 0){
					playedMelds = melds;
					new Move(oldHand, new Hand(hand), oldBoard, new ArrayList<Meld>(board));
				}
			}
		}
			
		if(playedMelds.size() == 0){
//			System.out.println("Could not create a using tiles on the board");
			return null;
		}
		else
			System.out.println("\n--------------------------------------------------------\n");
		
		return playedMelds;
	}
	
	
	private void updateBoard(ArrayList<Meld> originalSets, ArrayList<Meld> melds){
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
	
	
	private ArrayList<Meld> getRelatedSets(ArrayList<Tile> originalTiles, ArrayList<Meld> board){
		ArrayList<Meld> adjacentSets;
		ArrayList<Tile> adjacentTiles;
		Integer[] numAdjacent;
		int indexOfMostMatches, maximumMatches;
		boolean addedTiles;
		
		adjacentSets = new ArrayList<Meld>();
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

	
	// **********************************************************
    //						Display information							
    // **********************************************************
	
	@Override
	public String toString(){
		String encoding;
		
		encoding = "";
		
		for(int i=0; i<board.size(); i++)
			encoding = encoding + board.get(i).toString() + ", ";
		
		for(int i=0; i<tilesInHand.length; i++)
			encoding = encoding + tilesInHand[i] + " ";
		
		return encoding;
	}
	
	
	
	public String displayGame(){
		String game;
		
		game = "\n--------------------------------------------------------\n";
		
		game = game + "Hand Sizes: ";
		for(int i=0; i<tilesInHand.length; i++)
			game = game + tilesInHand[i] + " ";
		
		game = game + "\nBoard:\n";
		for(Set set : board)
			game = game + "\t" + set.toString() + "\n";
		
		game = game + "--------------------------------------------------------\n";
		
		return game;
	}

	/**
	 * Translate an index into the name of a player
	 * 
	 * @param index the index of the player
	 * @return The player number if the index is one of the predefined
	 * 			numbers. "INVALID PLAYER" otherwise.
	 */
	public static String indexToPlayerName(int index){
		switch(index){
			case GameInfo.PLAYER1: 
				return "Player 1";
				
			case GameInfo.PLAYER2: 
				return "Player 2";
				
			case GameInfo.PLAYER3:
				return "Player 3";
				
			case GameInfo.PLAYER4: 
				return "Player 4";
				
			default: 
				return "INVALID PLAYER";
		}
	}
	
	/**
	 * Translate the name of a player to an index
	 * @param player The name of the player
	 * @return The player number - 1 if the player is 1, 2, 3, or 4
	 * 			-1 otherwise
	 */
	public static int playerToIndex(String player){
		if(player.trim().equalsIgnoreCase("Player 1"))
			return PLAYER1;
		else if(player.trim().equalsIgnoreCase("Player 2"))
			return PLAYER2;
		else if(player.trim().equalsIgnoreCase("Player 3"))
			return PLAYER3;
		else if(player.trim().equalsIgnoreCase("Player 4"))
			return PLAYER4;
		else
			return -1;
	}
	
	public int getNextPlayer(int current){
		return (current + 1) % tilesInHand.length;
	}
}

