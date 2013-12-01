package rummikub;

import java.util.ArrayList;
import java.util.Collections;

public class Meld extends Set implements Comparable<Meld>{
	public final boolean isRun;
	
	// **********************************************************
    // 						Constructors
    // **********************************************************
	
	/**
     * Generate a run or 
     * @param tiles
     *          the set that you wish to duplicate
     * @throws Exception
     *          If the array of tiles do not form valid run or group
     */
    public Meld(ArrayList<Tile> tiles) throws Exception{
        super(tiles);
        
        if(!validateRun(tiles) && !validateGroup(tiles))
        	throw new Exception(tiles.toString() + " is not a run or a group");
        
        this.isRun = validateRun(tiles);
    }
    
    public Meld(ArrayList<Tile> tiles, boolean run) throws Exception{
    	super(tiles);
    	
    	if(run && !validateRun(tiles))
			throw new Exception(tiles.toString() + " is not a valid run");
		else if(!run && !validateGroup(tiles))
			throw new Exception(tiles.toString() + " is not a valid Group");

            this.isRun = run;
    }
    
    /**
     * copy constructor
     * @param copy
     *                         the set that you wish to duplicate
     */
    public Meld(Meld copy) throws Exception{
    	super(copy.getTiles());
        this.isRun = copy.isRun;
    }
	
    public Meld(String raw) throws Exception{
    	super();
    	
    	ArrayList<Tile> tiles;
        String[] tokens;
        
        // FIXME this breaks encapsulation
        tiles = new ArrayList<Tile>();
        tokens = raw.split("\\s+|\\[|\\]"); // TODO Make this regex better
        
        for(String rawTile : tokens)
            if(rawTile.trim().length() > 0)
            	tiles.add(new Tile(rawTile));
        
        if(validateRun(tiles))
            isRun = true;
        else if(validateGroup(tiles))
            isRun = false;
        else
            throw new Exception(raw + " is not a run or a group");
        
        this.setTiles(tiles);
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
	boolean validateMeld(ArrayList<Tile> tiles){
		if(tiles.size() < 3)
			return false;
		
		return validateRun(tiles) || validateGroup(tiles);
	}
	
	/**
	 * Determine if the set of tiles is a run
	 * 
	 * @param tiles
	 * 			The tiles that we are considering together
	 * 
	 * @return 	true if all the tiles taken together form a run,
	 * 			false otherwise
	 */
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
	
	/**
	 * Determine if the set of tiles forms a group
	 * 
	 * @param tiles
	 * 			The tiles that we are considering together form a group
	 * 
	 * @return 	true if all the tiles taken together form a group,
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

	@Override
	public void addTile(Tile tile) throws Exception{
		ArrayList<Tile> run, group;
		
		if(isRun){
			run = this.getTiles();
			
			if(tile.colour != run.get(0).colour)
				throw new Exception(tile.toString() + " is not the same colour as the run " + this.toString());
			else if(tile.number == run.get(0).number - 1)
				run.add(0, new Tile(tile));
			else if(tile.number == run.get(run.size()-1).number + 1)
				run.add(new Tile(tile));
			else
				throw new Exception(tile.toString() + " is not a valid number for the run " + this.toString());
			
			this.setTiles(run);
		}
		
		else{
			group = this.getTiles();
			
			if(tile.number != group.get(0).number)
				throw new Exception(tile.toString() + " is not the same number as the group " + this.toString());
			
			for(Tile groupTile : this.getTiles())
				if(groupTile.colour == tile.colour)
					throw new Exception(tile.toString() + " is a colour that already exists in the group");
			
			group.add(new Tile(tile));
			this.setTiles(group);
		}
	}
	
	
	public int getNumAdjacent(Tile original){
		int numAdjacent;
		
		numAdjacent = 0;
		for(Tile tile : this.getTiles())
			if(original.isAdjacent(tile))	
				numAdjacent++;
		
		return numAdjacent;
	}
	
	@Override
	public int compareTo(Meld other) {
		if(this.isRun && !other.isRun)
			return -1;
		else if(!this.isRun && other.isRun)
			return 1;
		
		if(isRun)
			return compareRuns(other);
		else
			return compareGroups(other);
	}
	
	private int compareRuns(Meld other){
		ArrayList<Tile> myTiles;
		ArrayList<Tile> otherTiles;
		int difference;

		myTiles = this.getTiles();
		otherTiles = other.getTiles();
		
		// compare number
		difference = myTiles.get(0).number - otherTiles.get(0).number;
		if(difference != 0)
			return difference;
		
		// compare the colours
		difference = myTiles.get(0).colour - otherTiles.get(0).colour;
		if(difference != 0)
			return difference;
		
		// compare the lengths
		return myTiles.size() - otherTiles.size();
	}
	
	private int compareGroups(Meld other){
		ArrayList<Tile> myTiles;
		ArrayList<Tile> otherTiles;
		int difference;

		myTiles = this.getTiles();
		otherTiles = other.getTiles();
		
		// compare number
		difference = myTiles.get(0).number - otherTiles.get(0).number;
		if(difference != 0)
			return difference;
		
		// compare size
		difference = myTiles.size() - otherTiles.size();
		if(difference != 0)
			return difference;
		
		// lastly compare the colours
		for(int i=0; i<myTiles.size(); i++)
			if(myTiles.get(i).colour != otherTiles.get(i).colour)
				return myTiles.get(0).colour - otherTiles.get(0).colour;
		
		return 0;
	}
	
	@Override
	public boolean equals (Object other){
		if(other == null)
			return false;

		if (!(other instanceof Meld)) 
			return false;

		Meld otherMeld = (Meld) other;
		if(this.isRun != otherMeld.isRun)
			return false;
		if(this.getNumTiles() != otherMeld.getNumTiles())
			return false;
		
		ArrayList<Tile> myTiles = getTiles();
		ArrayList<Tile> otherTiles = otherMeld.getTiles();
		
		for(int i=0; i<myTiles.size(); i++)
			if(!myTiles.get(i).equals(otherTiles.get(i)))
				return false;
		
		return true;
	}
}