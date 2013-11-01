package rummikub;

import java.util.ArrayList;
import java.util.Collections;

public class Set {
	private ArrayList<Tile> tiles;
	
	// **********************************************************
    // 						Constructors
    // **********************************************************
	
	/**
	 * Create a shuffled deck of tiles 
	 * 
	 * @throws Exception
	 * 				If the tile that we are trying to create is invalid (internal error)
	 */
	public Set() throws Exception{
		tiles = new ArrayList<Tile>();
		
		for(int i=0; i<13; i++){
			for(char colour : Tile.getColours().toCharArray()){
				tiles.add(new Tile(colour, i+1));
				tiles.add(new Tile(colour, i+1));
			}
		}
		
		// TODO implement jokers
//		tiles.add(new Tile(Tile.RED,   Tile.JOKER));
//		tiles.add(new Tile(Tile.BLACK, Tile.JOKER));
		
		Collections.shuffle(tiles);
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
	}
	
	/**
	 * copy constructor
	 * @param copy
	 * 			the set that you wish to duplicate
	 */
	public Set(Set copy){
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
	// TODO Test this funciton
	public Set(String raw) throws Exception{
		String[] tokens;
		
		tiles = new ArrayList<Tile>();
		// TODO Make this regex better
		tokens = raw.split("\\s+|\\[|\\]");
		
		for(String tile : tokens)
			if(tile.trim().length() > 0)
				tiles.add(new Tile(tile));
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
	
	public int getNumTiles(){
		return tiles.size();
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
	public void sortByColour(){
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
		return this.getTiles() == otherTile.getTiles();
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