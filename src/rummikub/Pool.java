package rummikub;

import java.util.ArrayList;
import java.util.Collections;

public class Pool extends Set{
	public static final boolean INCLUDE_JOKERS = false;
	
	// **********************************************************
    // 						Constructors
    // **********************************************************	
	
	/**
	 * Create a shuffled pool of tiles 
	 * 
	 * @throws Exception
	 * 					If a tile that we are trying to create is invalid (internal error)
	 */
	public Pool() throws Exception{
		super();
		
		for(int i=0; i<13; i++){
			for(char colour : Tile.COLOURS){
				addTile(new Tile(colour, i+1));
				addTile(new Tile(colour, i+1));
			}
		}
		
		// TODO implement jokers
		if(INCLUDE_JOKERS){
			addTile(new Tile(Tile.RED,   Tile.JOKER));
			addTile(new Tile(Tile.BLACK, Tile.JOKER));
		}
		
		ArrayList<Tile> tiles = getTiles();
		Collections.shuffle(tiles);
		setTiles(tiles);
	}
	
	/**
	 * Get the number of tiles that remain int the pool
	 * @return The number of tiles that remain in the pool
	 */
	public int numTilesRemaining(){
		return this.getNumTiles();
	}
	
	/**
	 * Retrieves one hand from the top of the randomized deck
	 * @return an array of 14 tiles 
	 */
	public Hand getHand() throws Exception{
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		
		for(int i=0; i<GameInfo.HAND_SIZE; i++)
			tiles.add(this.drawTile()); 
			
		return new Hand(tiles);
	}
	
	/**
	 * Draws one tile from the front of the deck
	 * @return one random Tile
	 */
	public Tile drawTile(){
		ArrayList<Tile> tempPool;
		Tile next_tile;
		
		if(numTilesRemaining() == 0)
			return null;
		
		tempPool = this.getTiles();
		next_tile = tempPool.remove(0);
		this.setTiles(tempPool);
		
		return next_tile;
	}
	
//	@Override
//	public String toString(){
//		ArrayList<Tile> pool; 
//		String encoding = "";
//		
//		pool = remaining_tiles.getTiles();
//		encoding = encoding + "( ";
//		for(int i=0; i<pool.size();i++)
//			encoding = encoding + pool.get(i).toString() + " ";
//		encoding = encoding + ")";
//		
//		return encoding;
//	}
}
