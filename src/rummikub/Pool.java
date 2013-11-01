package rummikub;

import java.util.ArrayList;

public class Pool {
	Set tiles;
	
	/**
	 * Create a shuffled pool of tiles 
	 * 
	 * @throws Exception
	 * 				If a tile that we are trying to create is invalid (internal error)
	 */
	public Pool() throws Exception{
		tiles = new Set(false);
	}
	
	/**
	 * Get the number of tiles that remain int the pool
	 * @return The number of tiles that remain in the pool
	 */
	public int remainingTiles(){
		return tiles.getNumTiles();
	}
	
	/**
	 * Retrieves one hand from the top of the randomized deck
	 * @return an array of 14 tiles 
	 */
	public ArrayList<Tile> getHand(){
		ArrayList<Tile> hand = new ArrayList<Tile>();
		
		for(int i=0; i<GameInfo.HAND_SIZE; i++)
			hand.add(this.drawTile()); 
			
		return hand;
	}
	
	/**
	 * Draws one tile from the front of the deck
	 * @return one random Tile
	 */
	public Tile drawTile(){
		ArrayList<Tile> tempPool;
		Tile next_tile;
		
		if(remainingTiles() == 0)
			return null;
		
		tempPool = tiles.getTiles();
		next_tile = tempPool.remove(0);
		tiles.setTiles(tempPool);
		
		return next_tile;
	}
	
	@Override
	public String toString(){
		ArrayList<Tile> pool; 
		String encoding = "";
		
		pool = tiles.getTiles();
		encoding = encoding + "( ";
		for(int i=0; i<pool.size();i++)
			encoding = encoding + pool.get(i).toString() + " ";
		encoding = encoding + ")";
		
		return encoding;
	}
}
