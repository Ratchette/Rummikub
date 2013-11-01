package rummikub;

import java.util.ArrayList;
import java.util.Collections;

public class Set {
	private ArrayList<Tile> tiles;
	private boolean run;
	private boolean group;
	
	// **********************************************************
    // 						Constructors
    // **********************************************************
	
	/**
	 * Generate a run or 
	 * @param tiles
	 * 			the set that you wish to duplicate
	 * @throws Exception
	 * 			If the array of tiles do not form valid run or group
	 */
	public Set(ArrayList<Tile> tiles) throws Exception{
		if(!validateSet(tiles))
			throw new Exception("The tiles do not form a valid set");
		
		this.tiles = new ArrayList<Tile>(tiles);
		this.run = validateRun(tiles);
		this.group = validateGroup(tiles);
	}
	
	/**
	 * copy constructor
	 * @param copy
	 * 			the set that you wish to duplicate
	 */
	public Set(Set copy){
		this.run = copy.isRun();
		this.group = copy.isGroup();
		this.tiles = copy.getTiles();
	}

	/**
	 * Re-creates a set that was converted into a string
	 * @param raw
	 * 			The string that contains a set
	 * 
	 * NOTE: this function should only be called on strings created using
	 * 			this classes' toString method
	 */
	public Set(String raw) throws Exception{
		ArrayList<Tile> parsedTiles;
		String[] tokens;
		
		parsedTiles = new ArrayList<Tile>();
		// TODO Make this regex better
		tokens = raw.split("\\s+|\\[|\\]");
		
		for(String tile : tokens)
			if(tile.trim().length() > 0)
				parsedTiles.add(new Tile(tile));
		
		if(!validateSet(tiles))
			throw new Exception("The tiles do not form a valid set");
		
		this.tiles = new ArrayList<Tile>(tiles);
		this.run = validateRun(tiles);
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
//		if(tiles.size() < 3)
//			return false;
//		
//		return validateRun(tiles) || validateGroup(tiles);
		return true;
	}
	
	/**
	 * Determine if the tiles form a run
	 * @param tiles
	 * 			The set of tiles that you wish to check
	 * @return
	 * 			true if the tiles form a run </br>
	 * 			false otherwise
	 */
	boolean validateRun(ArrayList<Tile> tiles){
		Tile currentTile;
		char runColour;
		int previousNum;
		
		sortByNumber(tiles);
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
	
	/**
	 * Determine if the tiles form a group
	 * @param tiles
	 * 			The set of tiles that you wish to check
	 * @return
	 * 			true if the tiles form a group </br>
	 * 			false otherwise
	 */
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
	 * Check if the set is a group
	 * @return
	 * 			true if the set is a group </br>
	 * 			false otherwise
	 */
	boolean isGroup(){
		return !this.run;
	}
	
	/**
	 * Check if the set is a run
	 * @return
	 * 			true if the set is a run </br>
	 * 			false otherwise
	 */
	boolean isRun(){
		return this.run;
	}

	
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
	
	// **********************************************************
    //							Sorting							
    // **********************************************************
	
	/**
	 * Sorts the tiles by number
	 * @param tiles
	 * 			The array of tiles that you wish to sort
	 */
	public void sortByNumber(ArrayList<Tile> tiles){
		Collections.sort(tiles);
	}
	
	
	/**
	 * Sorts the tiles by colour first, then number
	 * @param tiles
	 * 			The array of tiles that you wish to sort
	 * 
	 * NOTE: this function is a bit shoddy and should be rewritten
	 */
	public void sortByColour(ArrayList<Tile> tiles){
		ArrayList<ArrayList<Tile>> colouredSets = new ArrayList<ArrayList<Tile>>(4); 
		int numTiles;
		Tile currentTile;
		
		for(int i=0; i<4; i++)
			colouredSets.add(new ArrayList<Tile>());
		
		numTiles = tiles.size();
		for(int i=0; i<numTiles; i++){
			currentTile = tiles.remove(0);
			
			if(currentTile.colour == Tile.RED)
				colouredSets.get(0).add(currentTile);
			else if(currentTile.colour == Tile.ORANGE)
				colouredSets.get(1).add(currentTile);
			else if(currentTile.colour == Tile.BLUE)
				colouredSets.get(2).add(currentTile);
			else
				colouredSets.get(3).add(currentTile);
		}
		
		
		for(int i=0; i<4; i++){
			sortByNumber(colouredSets.get(i));
			tiles.addAll(colouredSets.get(i));
		}
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

		Set otherTile = (Set) other;
		return this.getTiles() == otherTile.getTiles()
				&& this.isRun() == otherTile.isRun();
	}
	
	// TODO Test this function
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
