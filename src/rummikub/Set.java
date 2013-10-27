package rummikub;

import java.util.ArrayList;

public class Set {
	private ArrayList<Tile> tiles;
	private boolean run;
	
	// creates the deck
	public Set() throws Exception{
		tiles = new ArrayList<Tile>();
		
		for(int i=0; i<13; i++){
			for(char colour : Tile.getColours())
				tiles.add(new Tile(colour, i));
		}
		
		tiles.add(new Tile(Tile.Colour.RED,   Tile.JOKER));
		tiles.add(new Tile(Tile.Colour.BLACK, Tile.JOKER));
	}
	
	public Set(ArrayList<Tile> tiles) throws Exception{
		if(!validateSet(tiles))
			throw new Exception("The tiles do not form a valid set");
		
		this.tiles = new ArrayList<Tile>(tiles);
	}
	
	public Set(Set copy){
		this.run = copy.isRun();
		this.tiles = copy.getTiles();
	}
	
	boolean validateSet(ArrayList<Tile> tiles){
		if(tiles.size() < 3)
			return false;
		
		return validateRun(tiles) || validateGroup(tiles);
	}
	
	
	
	boolean validateRun(ArrayList<Tile> tiles){
		Tile currentTile;
		Tile.Colour runColour;
		int previousNum;
		
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
		ArrayList<Tile.Colour> usedColours;
		int groupNumber;
		
		if(tiles.size() > 4)
			return false;
		
		groupNumber = tiles.get(0).number;
		usedColours = new ArrayList<Tile.Colour>();
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
	
	
	
	
	boolean isGroup(){
		return !this.run;
	}
	
	boolean isRun(){
		return this.run;
	}

	
	public ArrayList<Tile> getTiles() {
		return new ArrayList<Tile>(tiles);
	}

	public void setTiles(ArrayList<Tile> tiles) {
		this.tiles = tiles;
	}
}
