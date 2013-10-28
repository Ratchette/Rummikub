package rummikub;

import java.util.ArrayList;
import java.util.Collections;

public class Pool{
	ArrayList<Tile> tiles; 
	
	/**
	 * Create a shuffled deck of tiles 
	 * @throws Exception
	 * 				If the tile that we are trying to create is invalid
	 */
	public Pool() throws Exception{
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
	
	public int remainingTiles(){
		return tiles.size();
	}
	
	public Tile pickupTile(){
		if(remainingTiles() == 0)
			return null;
		
		return tiles.remove(0);
	}
	
	// TODO Test this function
	@Override
	public String toString(){
		String encoding = "";
		
		encoding = encoding + "( ";
		for(int i=0; i<tiles.size(); i++)
			encoding = encoding + tiles.get(i).toString() + " ";
		encoding = encoding + ")";
		
		return encoding;
	}
}
