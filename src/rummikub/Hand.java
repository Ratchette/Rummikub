package rummikub;

import java.util.ArrayList;
import java.util.Collections;

public class Hand extends Set{

	// **********************************************************
    //						Constructors							
    // **********************************************************
	
	/**
	 * No argument constructor
	 */
	public Hand(){
		super();
	}

	/**
	 * copy constructor
	 */
	public Hand(Hand hand) throws Exception{
		super(hand.getTiles());
	}
	
	/**
	 * Create a new hand with the tiles provided
	 * @param tiles
	 * 			The array of tiles in your hand
	 * @throws Exception
	 */
	public Hand(ArrayList<Tile> tiles) throws Exception{
		super(tiles);
	}
	

	/**
	 * Re-creates a set that was converted into a string
	 * @param raw
	 * 			The string that contains a set
	 * 
	 * NOTE: this function should only be called on strings created using
	 * 			this classes' toString method
	 */
	public Hand(String raw) throws Exception{
		super(raw);
	}
	
	// **********************************************************
    //					Getters and Setters							
    // **********************************************************
	
	/**
	 * Get a tiles from your hand
	 * @param index
	 * 			The position of the tile you wish to retreive
	 * 	NOTE: positions start at 0, not 1
	 * 
	 * @return The Tile at index index
	 */
	private Tile getTile(int index){
		return this.getTiles().get(index);
	}
	
	// **********************************************************
    //							Sorting							
    // **********************************************************
	
	/**
	 * Sorts the tiles by number
	 */
	public void sortByNumber() throws Exception{
		// FIXME breaking encapsulation here
		ArrayList<Tile> tiles;
		
		tiles = this.getTiles();
		Collections.sort(tiles);
		this.setTiles(tiles);
	}
	
	
	/**
	 * Separate the hand into a series of subhands by number
	 * @return
	 */
	public ArrayList<Hand> separateByNumber() throws Exception{
		ArrayList<Hand> tilesByNumber;
		
		this.sortByNumber();
		tilesByNumber = new ArrayList<Hand>();
		for(int i=0; i<Tile.JOKER; i++)
			tilesByNumber.add(new Hand());
		
		for(int i=0; i<this.getNumTiles(); i++)
			tilesByNumber.get(this.getTile(i).number).addTile(this.getTile(i));
		
		return tilesByNumber;
	}

	/**
	 * Sorts the tiles by colour first, then number
	 */
	public void sortByColour() throws Exception{
		ArrayList<Hand> colouredSets;
		// FIXME breaking the encapsulation here
		ArrayList<Tile> tiles;	
		
		colouredSets = this.separateByColour();
		tiles = new ArrayList<Tile>();
		
		for(int i=0; i<4; i++){
			colouredSets.get(i).sortByNumber();
			tiles.addAll(colouredSets.get(i).getTiles());
		}
		
		this.setTiles(tiles);
	}
	
	/**
	 * Separate the tiles based on colour into four sets
	 * 
	 * @return an ArrayList that contains 4 sets
	 * 			each set contains all the tiles of one colour
	 * 
	 * @throws Exception
	 */
	private ArrayList<Hand> separateByColour() throws Exception{
		ArrayList<Hand> colouredSets = new ArrayList<Hand>();
		Tile currentTile;
		int numTiles;
		
		for(int i=0; i<4; i++)
			colouredSets.add(new Hand());
		
		numTiles = this.getNumTiles();
		for(int i=0; i<numTiles; i++){
			currentTile = this.getTile(i);
			
			if(currentTile.colour == Tile.RED)
				colouredSets.get(0).addTile(currentTile);
			else if(currentTile.colour == Tile.ORANGE)
				colouredSets.get(1).addTile(currentTile);
			else if(currentTile.colour == Tile.BLUE)
				colouredSets.get(2).addTile(currentTile);
			else
				colouredSets.get(3).addTile(currentTile);
		}
		
		return colouredSets;
	}
	
	// **********************************************************
    //					Run and Group Generation
    // **********************************************************

	/**
	 * Remove all redundant tiles from your hand
	 */
	private void removeRedundancies(){
		ArrayList<Tile> tiles;
		
		tiles = new ArrayList<Tile>();
		for(Tile tile : this.getTiles()){
			if(!tiles.contains(tile))
				tiles.add(tile);
		}
		
		this.setTiles(tiles);
	}
	
	/**
	 * Find all of the possible groups this current hand can make
	 * @return All groups that can be produced
	 * 			these groups are NOT mutually exclusive
	 * @throws Exception
	 */
	public ArrayList<Meld> getGroups() throws Exception{
		ArrayList<Hand> tilesByNumber;
		ArrayList<Meld> groups;
		
		groups = new ArrayList<Meld>();
		tilesByNumber = separateByNumber();
		
		for(Hand currentSet : tilesByNumber){
			currentSet.removeRedundancies();
			groups.addAll(currentSet.getSubGroups());
		}
		
		return groups;
	}
	
	
	/**
	 * Separate all tiles of the same number into groups
	 * @throws Exception
	 */
	private ArrayList<Meld> getSubGroups() throws Exception{
		ArrayList<Meld> groups;
		ArrayList<Tile> temp_tiles;
		
		groups = new ArrayList<Meld>();
		
		if(this.getNumTiles() > 2)
			groups.add(new Meld(this.getTiles()));
		
		if(this.getNumTiles() > 3){
			for(Tile removedTile : this.getTiles()){
				temp_tiles = new ArrayList<Tile>(this.getTiles());
				temp_tiles.remove(removedTile);
				groups.add(new Meld(temp_tiles, false));
			}
		}
		
		return groups;
	}
	
	
	/**
	 * Find all possible runs that this hand can make
	 * @return all runs that can be produced
	 * 			these runs are NOT Mutually exclusive
	 * @throws Exception
	 */
	public ArrayList<Meld> getRuns() throws Exception{
		ArrayList<Hand> colouredSets;
		ArrayList<Meld> runs;
		ArrayList<Tile> possibleRun;
		
		runs = new ArrayList<Meld>();
		colouredSets = separateByColour();
		
		for(Hand hand : colouredSets){
			hand.removeRedundancies();
			hand.sortByNumber();
			
			for(int j=0; j<hand.getNumTiles(); j++){
				possibleRun = new ArrayList<Tile>();
				possibleRun.add(hand.getTile(j));
				
				for(int k=j+1; k<hand.getNumTiles(); k++){
					if(hand.getTile(k-1).number == hand.getTile(k).number -1)
						possibleRun.add(hand.getTile(k));
					else 
						break;
					
					if(possibleRun.size() > 2)
						runs.add(new Meld(possibleRun, true));
				}
			}
		}
		
		return runs;
	}
	
	
	
	// **********************************************************
    //					Meld Generation
    // **********************************************************

	public ArrayList<Meld> getInitialMeld() throws Exception{
		ArrayList<Meld> melds;
		int score;
		
		melds = getGroups();
		melds.addAll(getRuns());
		
		melds = findLargestSubset(new ArrayList<Tile>(this.getTiles()), new ArrayList<Tile>(), melds);
		
		if(melds == null)
			return null;
		
		score = 0; 
		for(int i=0; i<melds.size(); i++)
			score = score + melds.get(i).getScore();
		
		if(score < 30)
			return null;
		
		// FIXME move this
		for(Set set : melds)
			for(Tile usedTile : set.getTiles())
				this.removeTile(usedTile);

		return melds;
	}
	
	public ArrayList<Meld> findLargestSubset(ArrayList<Tile> remainingHand, ArrayList<Tile> mandatoryTiles, ArrayList<Meld> possibleSets) throws Exception{
		ArrayList<ArrayList<Meld>> allValidSubsets;
		ArrayList<Meld> subsets;
		ArrayList<Tile> currentSet, currentHand, mandatoryCopy;

		allValidSubsets = new ArrayList<ArrayList<Meld>>();
		if(remainingHand.size() < 3)
			return null;

		while(possibleSets.size() > 0){
			mandatoryCopy = new ArrayList<Tile>(mandatoryTiles);
			currentHand = new ArrayList<Tile>(remainingHand);
			currentSet = possibleSets.remove(0).getTiles();
			
			for(int j=0; j<currentSet.size(); j++){
				
				if(currentHand.contains(currentSet.get(j))){
					currentHand.remove(currentSet.get(j));
					mandatoryCopy.remove(currentSet.get(j));
				}
				else {
					currentHand = null;
					break;
				}
			}
				
			if(currentHand == null)
				continue;
			
			subsets = findLargestSubset(new ArrayList<Tile>(currentHand), mandatoryCopy, new ArrayList<Meld>(possibleSets));
			
			if(subsets == null)
				subsets = new ArrayList<Meld>();
			
			subsets.add(new Meld(currentSet));
			allValidSubsets.add(subsets);
		}
		
		return findLargestSubset(allValidSubsets, mandatoryTiles);
	}
	
	
	private ArrayList<Meld> findLargestSubset(ArrayList<ArrayList<Meld>> allValidSubsets, ArrayList<Tile> mandatoryTiles){
		ArrayList<Meld> currentSubset;
		ArrayList<Tile> usedTiles;
		int largest_index;
		int score, largest_score;
		
		largest_index = -1;
		largest_score = 0;
		
		for(int i=0; i<allValidSubsets.size(); i++){
			currentSubset = allValidSubsets.get(i);
			score = 0;			
			
			for(int j=0; j<currentSubset.size(); j++)
				score = score + currentSubset.get(j).getScore();
			
			if(mandatoryTiles != null){
				usedTiles = new ArrayList<Tile>();
				for(Meld set : currentSubset)
					usedTiles.addAll(set.getTiles());
				
				for(Tile mandatoryTile : mandatoryTiles){
					if(usedTiles.contains(mandatoryTile))
						usedTiles.remove(mandatoryTile);
					else{
						score = -1;
					}
				}
			}
			
			if(score > largest_score){
				largest_score = score;
				largest_index = i;
			}
		}
		
		if(largest_score > 0)
			return allValidSubsets.get(largest_index);
		else
			return null;
	}
	
	public ArrayList<Meld> getMeldsFromHand() throws Exception{
		ArrayList<Meld> melds;
		
		melds = getGroups();
		melds.addAll(getRuns());
		
		melds = findLargestSubset(new ArrayList<Tile>(this.getTiles()), new ArrayList<Tile>(), melds);
		
		if(melds == null)
			return new ArrayList<Meld>();
		
		for(Meld set : melds)
			for(Tile tile : set.getTiles())
				this.removeTile(tile);
		
		return melds;
	}

	public ArrayList<Tile> getAdjacentInHand(Tile original){
		ArrayList<Tile> similarTiles, allTiles;
		boolean addedTiles;
		
		allTiles = new ArrayList<Tile>(this.getTiles());
		similarTiles = new ArrayList<Tile>();
		
		allTiles.remove(original);
		addedTiles = true;
		
		while(addedTiles){
			addedTiles = false;
			
			// for every tile we are interested in finding a match to
			for(Tile comaprison : similarTiles){
				
				// compare to all remaining tiles in your hand 
				for(Tile tile : allTiles){
					if(comaprison.isAdjacent(tile)){
						similarTiles.add(tile);
						allTiles.remove(tile);
						addedTiles = true;
					}
				}
			}
		}
		
		return similarTiles;
	}
	
	// **********************************************************
    //							Common
    // **********************************************************
	
	
	@Override
	public boolean equals (Object other){
		if(other == null)
			return false;

		if (!(other instanceof Hand)) 
			return false;

		Hand otherTile = (Hand) other;
		return this.getTiles().equals(otherTile.getTiles());
	}
}

