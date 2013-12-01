package rummikub;

import java.util.ArrayList;

public abstract class Set {
	private ArrayList<Tile> tiles;
	
	// **********************************************************
    // 						Constructors
    // **********************************************************
	
	/**
	 * Creates an empty set
	 */
	public Set(){
		tiles = new ArrayList<Tile>();

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
		this.tiles = new ArrayList<Tile>(tiles);
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