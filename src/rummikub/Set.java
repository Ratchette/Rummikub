package rummikub;

import java.util.ArrayList;
import java.util.Collections;

public class Set {
	private ArrayList<Tile> tiles;
	public final Boolean isRun;
	
	// **********************************************************
    // 						Constructors
    // **********************************************************
	
	/**
	 * Creates an empty set
	 */
	public Set(){
		tiles = new ArrayList<Tile>();
		isRun = null;
	}
	
	/**
	 * Create a shuffled deck of tiles 
	 * 
	 * @param includeJokers
	 * 			true = will generate a deck with jokers
	 * 			false = will generate a deck WITHOUT jokers
	 * 
	 * @throws Exception
	 * 				If the tile that we are trying to create is invalid (internal error)
	 */
	public Set(Boolean includeJokers) throws Exception{
		tiles = new ArrayList<Tile>();
		
		for(int i=0; i<13; i++){
			for(char colour : Tile.getColours().toCharArray()){
				tiles.add(new Tile(colour, i+1));
				tiles.add(new Tile(colour, i+1));
			}
		}
		
		// TODO implement jokers
		if(includeJokers){
			tiles.add(new Tile(Tile.RED,   Tile.JOKER));
			tiles.add(new Tile(Tile.BLACK, Tile.JOKER));
		}
		
		Collections.shuffle(tiles);
		this.isRun = null;
	}
	
	/**
	 * Generate a run or 
	 * @param tiles
	 * 			the set that you wish to duplicate
	 * @throws Exception
	 * 			If the array of tiles do not form valid run or group
	 */
	public Set(ArrayList<Tile> tiles) throws Exception{
		this.tiles = new ArrayList<Tile>(tiles);
		this.isRun = null;
	}
	
	public Set(ArrayList<Tile> tiles, boolean run){
		this.tiles = new ArrayList<Tile>(tiles);
		this.isRun = run;
	}
	
	/**
	 * copy constructor
	 * @param copy
	 * 			the set that you wish to duplicate
	 */
	public Set(Set copy){
		this.tiles = copy.getTiles();
		this.isRun = copy.isRun;
	}

	/**
	 * Re-creates a set that was converted into a string
	 * @param raw
	 * 			The string that contains a set
	 * 
	 * NOTE: this function should only be called on strings created using
	 * 			this classes' toString method
	 */
	// TODO Test this funciton
	public Set(String raw) throws Exception{
		String[] tokens;
		
		tiles = new ArrayList<Tile>();
		// TODO Make this regex better
		tokens = raw.split("\\s+|\\[|\\]");
		
		for(String tile : tokens)
			if(tile.trim().length() > 0)
				tiles.add(new Tile(tile));
		
		if(validateRun(tiles)){
			isRun = true;
		}
		else if(validateGroup(tiles)){
			isRun = false;
		}
		else
			isRun = null;
	}
	
	// **********************************************************
    // 						Validation
    // **********************************************************
	
	/**
	 * Check if the tiles form a valid set or run
	 * @param tiles
	 * 			the tiles that you wish to validate
	 * @return
	 * 			true if the tiles form a set or run </br>
	 * 			false otherwise 
	 */
	boolean validateSet(ArrayList<Tile> tiles){
		return true;
	}
	
	boolean validateRun(ArrayList<Tile> tiles){
        Tile currentTile;
        char runColour;
        int previousNum;
        
        Collections.sort(tiles);
        runColour = tiles.get(0).colour;
        previousNum = tiles.get(0).number;
        
        for(int i=1; i<tiles.size(); i++){
                currentTile = tiles.get(i);
                
                if(currentTile.colour != runColour)
                        return false;
                
                if(currentTile.number == previousNum + 1)
                        previousNum++;
                else
                        return false;
        }
        
        return true;
	}
	
	boolean validateGroup(ArrayList<Tile> tiles){
        Tile currentTile;
        ArrayList<Character> usedColours;
        int groupNumber;
        
        if(tiles.size() > 4)
                return false;
        
        groupNumber = tiles.get(0).number;
        usedColours = new ArrayList<Character>();
        usedColours.add(tiles.get(0).colour);
        
        for(int i=1; i<tiles.size(); i++){
                currentTile = tiles.get(i);
                
                if(currentTile.number != groupNumber)
                        return false;
                
                if(usedColours.contains(currentTile.colour))
                        return false;
                
                usedColours.add(currentTile.colour);
        }
        
        return true;
	}
	
	// **********************************************************
    // 					Getters and Setters
    // **********************************************************
	
	/**
	 * Getter for the tiles array
	 * @return
	 * 			The array of tiles
	 */
	public ArrayList<Tile> getTiles() {
		return new ArrayList<Tile>(tiles);
	}

	/**
	 * Setter for the tiles array
	 * @param tiles
	 */
	public void setTiles(ArrayList<Tile> tiles) {
		this.tiles = tiles;
	} 
	
	public void addTile(Tile tile) throws Exception{
		tiles.add(new Tile(tile));
	}
	
	public void removeTiles(Set set) throws Exception{
		ArrayList<Tile> tilesToRemove;
		
		tilesToRemove = set.getTiles();
		for(Tile tile : tilesToRemove){
			removeTile(tile);
		}
	}
	
	public void removeTile(Tile tile) throws Exception{
		tiles.remove(tile);
	}
	
	public int getNumTiles(){
		return tiles.size();
	}
	
	// **********************************************************
    //							Sorting							
    // **********************************************************
	
	/**
	 * Sorts the tiles by number
	 */
	public void sortByNumber(){
		Collections.sort(tiles);
	}

	
	/**
	 * Sorts the tiles by colour first, then number
	 * 
	 * NOTE: this function is a bit shoddy and should be rewritten
	 */
	public void sortByColour() throws Exception{
		ArrayList<Set> colouredSets;
		colouredSets = separateByColour();
		
		tiles = new ArrayList<Tile>();
		for(int i=0; i<4; i++){
			colouredSets.get(i).sortByNumber();
			tiles.addAll(colouredSets.get(i).getTiles());
		}
	}
	
	private ArrayList<Set> separateByColour() throws Exception{
		ArrayList<Set> colouredSets = new ArrayList<Set>();
		ArrayList<Tile> duplicateHand;
		Tile currentTile;
		int numTiles;
		
		for(int i=0; i<4; i++)
			colouredSets.add(new Set());
		
		duplicateHand = new ArrayList<Tile>(tiles);
		numTiles = duplicateHand.size();
		for(int i=0; i<numTiles; i++){
			currentTile = duplicateHand.remove(0);
			
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
    //						Runs and Groups
    // **********************************************************

	public ArrayList<Set> getGroups() throws Exception{
		ArrayList<Set> tilesByNumber;
		ArrayList<Set> groups;
		// FIXME this is breaking the set encapsulation!!!
		ArrayList<Tile> tile_set;
		
		sortByNumber();
		groups = new ArrayList<Set>();
		tilesByNumber = new ArrayList<Set>();
		for(int i=0; i<Tile.JOKER; i++)
			tilesByNumber.add(new Set());
		
		for(int i=0; i<tiles.size(); i++)
			tilesByNumber.get(tiles.get(i).number).addTile(tiles.get(i));
		
		for(int i=1; i<tilesByNumber.size(); i++){
			if(tilesByNumber.get(i).getNumTiles() < 3)
				continue;
			
			tile_set = tilesByNumber.get(i).getTiles();
			for(int j=0; j<tile_set.size(); j++)
				for(int k=j+1; k<tile_set.size(); k++)
					if(tile_set.get(j).colour == tile_set.get(k).colour){
						tile_set.remove(k);
						k = k -1; // to compensate for the fact we shifted everything after k down one index
					}
			
			if(tile_set.size() < 3)
				continue;
			else if(tile_set.size() == 3)
				groups.add(new Set(tile_set, false));
			else {
				groups.add(new Set(tile_set, false));
				groups.addAll(separateSubGroups(tile_set));
			}
		}
		
		return groups;
	}
	
	private ArrayList<Set> separateSubGroups(ArrayList<Tile> tiles) throws Exception{
		ArrayList<Set> groups;
		ArrayList<Tile> temp_tiles;
		
		groups = new ArrayList<Set>();
		for(int i=0; i<tiles.size(); i++){
			temp_tiles = new ArrayList<Tile>(tiles);
			temp_tiles.remove(i);
			groups.add(new Set(temp_tiles, false));
		}
		
		return groups;
	}
	
	public ArrayList<Set> getRuns() throws Exception{
		ArrayList<Set> coloured_sets, runs;
		ArrayList<Tile> coloured_tiles, temp_set;
		
		runs = new ArrayList<Set>();
		coloured_sets = separateByColour();
				
		for(int i=0; i<4; i++){
			coloured_tiles = coloured_sets.get(i).getTiles();
			temp_set = new ArrayList<Tile>();	// redundant
			
			for(int j=0; j<coloured_tiles.size(); j++){
				temp_set = new ArrayList<Tile>();
				temp_set.add(coloured_tiles.get(j));
				
				for(int k=j+1; k<coloured_tiles.size(); k++){
					// if you contain two of the same tile in your hand
					if(coloured_tiles.get(k-1).number == coloured_tiles.get(k).number)
						continue;
					
					else if(coloured_tiles.get(k-1).number == coloured_tiles.get(k).number -1)
						temp_set.add(coloured_tiles.get(k));
					
					else
						break;
				}
				
				if(temp_set.size() > 2)
					runs.addAll(separateSubRuns(temp_set));
			}
		}
		
		return runs;
	}
	
	private ArrayList<Set> separateSubRuns(ArrayList<Tile> tiles) throws Exception{
		ArrayList<Set> runs;
		
		runs = new ArrayList<Set>();
		for(int i=tiles.size()-1; i > 1; i--){
			runs.add(new Set(tiles, true));
			tiles.remove(i);
		}
		
		return runs;
	}

	public ArrayList<Set> getInitialMeld() throws Exception{
		ArrayList<Set> melds;
		int score;
		
		melds = getGroups();
		melds.addAll(getRuns());
		
		melds = findLargestSubset(new ArrayList<Tile>(tiles), null, melds);
		
		if(melds == null)
			return null;
		
		score = 0; 
		for(int i=0; i<melds.size(); i++)
			score = score + melds.get(i).getScore();
		
		if(score < 30)
			return null;
		
		for(Set set : melds)
			for(Tile tile : set.getTiles())
				tiles.remove(tile);
		
		return melds;
	}
	

	public ArrayList<Set> findLargestSubset(ArrayList<Tile> remainingPool, ArrayList<Set> mandatorySets, ArrayList<Set> possibleSets) throws Exception{
		ArrayList<ArrayList<Set>> allValidSubsets;
		ArrayList<Set> subsets;
		ArrayList<Tile> currentSet, currentPool;

		allValidSubsets = new ArrayList<ArrayList<Set>>();
		if(remainingPool.size() < 3)
			return null;
		
		while(possibleSets.size() > 0){
			currentPool = new ArrayList<Tile>(remainingPool);
			currentSet = possibleSets.remove(0).getTiles();
			
			for(int j=0; j<currentSet.size(); j++){
				if(currentPool.contains(currentSet.get(j)))
					currentPool.remove(currentSet.get(j));
				else {
					currentPool = null;
					break;
				}
			}	
				
			if(currentPool == null){
				continue;
			}
			
			subsets = findLargestSubset(new ArrayList<Tile>(currentPool), mandatorySets, new ArrayList<Set>(possibleSets));
			
			if(subsets == null)
				subsets = new ArrayList<Set>();
			
			subsets.add(new Set(currentSet));
			allValidSubsets.add(subsets);
		}
		
		
		return findLargestSubset(allValidSubsets, mandatorySets);	
	}
	
	private ArrayList<Set> findLargestSubset(ArrayList<ArrayList<Set>> allValidSubsets, ArrayList<Set> mandatorySets){
		ArrayList<Set> currentSubset;
		ArrayList<Tile> copyTileset, mandatoryTiles;
		int largest_index;
		int score, largest_score;
		
		largest_index = -1;
		largest_score = -1;
		
		for(int i=0; i<allValidSubsets.size(); i++){
			currentSubset = allValidSubsets.get(i);
			score = 0;			
			
			for(int j=0; j<currentSubset.size(); j++){
				score = score + currentSubset.get(j).getScore();
			}
			
			if(mandatorySets != null){
				mandatoryTiles = new ArrayList<Tile>();
				for(Set set : mandatorySets)
					mandatoryTiles.addAll(set.getTiles());
				
				copyTileset = new ArrayList<Tile>();
				for(Set set : currentSubset)
					copyTileset.addAll(set.getTiles());
				
				for(Tile tile : mandatoryTiles){
					if(copyTileset.contains(tile))
						copyTileset.remove(tile);
					else
						continue;
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
	
	
	
	
	public ArrayList<Set> getMeldsFromHand() throws Exception{
		ArrayList<Set> melds;
		int score;
		
		melds = getGroups();
		melds.addAll(getRuns());
		
		melds = findLargestSubset(new ArrayList<Tile>(tiles), null, melds);
		
		if(melds == null)
			return new ArrayList<Set>();
		
		for(Set set : melds)
			for(Tile tile : set.getTiles())
				tiles.remove(tile);
		
		return melds;
	}
	
	
	
	public ArrayList<Tile> getAdjacentInHand(Tile original){
		ArrayList<Tile> similarTiles, allTiles;
		boolean addedTiles;
		
		allTiles = new ArrayList<Tile>(this.tiles);
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
	
	public int getNumAdjacent(Tile original){
		int numAdjacent;
		
		numAdjacent = 0;
		
		for(int i=0; i<tiles.size(); i++)				
			if(original.isAdjacent(tiles.get(i)))	
				numAdjacent++;
		
		return numAdjacent;
	}
	
	
	// **********************************************************
    //							Common							
    // **********************************************************

	
	@Override
	public boolean equals (Object other){
		if(other == null)
			return false;

		if (!(other instanceof Set)) 
			return false;

		Set otherSet = (Set) other;
		
		if(this.tiles.size() != otherSet.tiles.size())
			return false;
		
		for(int i=0; i<this.getNumTiles(); i++)
			if(!this.tiles.get(i).equals(otherSet.tiles.get(i)))
				return false;
		
		return true;
	}
	
	@Override
	public String toString(){
		String encoding = "";
		
		encoding = encoding + "[ ";
		for(int i=0; i<tiles.size(); i++)
			encoding = encoding + tiles.get(i).toString() + " ";
		encoding = encoding + "]";
		
		return encoding;
	}
	
	/**
	 * Get the sum of numbers of all tiles in the set
	 * @return
	 * 			The score of the set
	 */
	public int getScore(){
		int sum;
		
		sum = 0;
		for(int i=0; i<tiles.size(); i++){
			sum = sum + tiles.get(i).number;
		}
		
		return sum;
	}	
}