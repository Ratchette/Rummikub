package rummikub;

import java.util.ArrayList;
import java.util.Collections;

public class Hand {
	private ArrayList<Tile> tiles;

	public Hand(ArrayList<Tile> tiles){
		this.tiles = tiles;
	}
	
	public ArrayList<Tile> getTiles() {
		return tiles;
	}

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
	
	
	
	
	@Override
	public boolean equals (Object other){
		if(other == null)
			return false;

		if (!(other instanceof Hand)) 
			return false;

		Hand otherTile = (Hand) other;
		return this.getTiles().equals(otherTile.getTiles());
	}
	
	// TODO Test this function
	@Override
	public String toString(){
		String encoding = "";
		
		encoding = encoding + "{ ";
		for(int i=0; i<tiles.size(); i++)
			encoding = encoding + tiles.get(i).toString() + " ";
		encoding = encoding + "}";
		
		return encoding;
	}
}
