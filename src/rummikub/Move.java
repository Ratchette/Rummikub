package rummikub;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

// FIXME - this was thrown in at the last second
public class Move {
	public Hand handBefore;
	public Hand handAfter;
	public Hand usedTiles;
	
	public ArrayList<Meld> boardBefore;
	public ArrayList<Meld> boardAfter;
	
	public HashMap<Tile, ArrayList<Position>> positionsBefore;
	public HashMap<Tile, ArrayList<Position>> positionsAfter;
	public HashMap<Position, Position> movement;
	
	
	public Move(Hand startHand, Hand endHand, ArrayList<Meld> startBoard, ArrayList<Meld> endBoard) throws Exception{
		handBefore = startHand;
		handBefore.sortByColour();
		
		handAfter = endHand;
		handAfter.sortByColour();
		
		boardBefore = startBoard;
		Collections.sort(boardBefore);
		boardAfter = endBoard;
		Collections.sort(boardAfter);
		
		calculateUsedTiles();
		removeUntouchedMelds();
		
		System.out.println("\n--------------------------------------------------------\n");
		System.out.println("Starting Tiles");
		displayMelds(boardBefore);
		
		System.out.println("\nEnding Tiles");
		displayMelds(boardAfter);
		
		calculatePositions();
		displayMoves();
	}

	private void calculateUsedTiles() throws Exception{
		ArrayList<Tile> tilesBefore;
		ArrayList<Tile> tilesAfter;
		
		tilesBefore = handBefore.getTiles();
		tilesAfter = handAfter.getTiles();
		
		for (Tile tile : tilesAfter)
			tilesBefore.remove(tile);
		
		usedTiles = new Hand(new ArrayList<Tile>(tilesBefore));
	}
	
	private void removeUntouchedMelds(){
		ArrayList<Meld> startingMelds = new ArrayList<Meld>(boardBefore);
		
		for(Meld meld : startingMelds){
			if(boardAfter.contains(meld)){
				boardBefore.remove(meld);
				boardAfter.remove(meld);
			}
		}
	}
	
	private void calculatePositions() throws Exception{
		HashMap<Tile, ArrayList<Position>> startPositions = new HashMap<Tile, ArrayList<Position>>();
		HashMap<Tile, ArrayList<Position>> endPositions = new HashMap<Tile, ArrayList<Position>>();
		ArrayList<Tile> tiles;
		Tile tile;
		
		// find the position of all the tiles before the move
		for(int i=0; i<boardBefore.size(); i++){
			tiles = boardBefore.get(i).getTiles();
			
			for(int j=0; j<tiles.size(); j++){
				tile = tiles.get(j);
				
				if(!startPositions.containsKey(tile))
					startPositions.put(tile, new ArrayList<Position>());
				
				startPositions.get(tile).add(new Position(i+1, j+1));
			}
		}
		positionsBefore = new HashMap<Tile, ArrayList<Position>>(startPositions);
		
		// find the position of all the tiles after the move
		for(int i=0; i<boardAfter.size(); i++){
			tiles = boardAfter.get(i).getTiles();
			
			for(int j=0; j<tiles.size(); j++){
				tile = tiles.get(j);
				
				if(!endPositions.containsKey(tile))
					endPositions.put(tile, new ArrayList<Position>());
				
				endPositions.get(tile).add(new Position(i+1, j+1));
			}
		}
		positionsAfter = new HashMap<Tile, ArrayList<Position>>(endPositions);
		
		// Create the before and after mapping of positions
		movement = new HashMap<Position, Position>();
		Position start, end;
		for(Tile key : positionsAfter.keySet()){
			for(int j=0; j<endPositions.get(key).size(); j++){
				start = new Position(-1, -1);
				end = endPositions.get(key).remove(0);
				
				if(startPositions.containsKey(key))
					start = startPositions.get(key).remove(0);
				
				if(end.equals(start))
					continue;
				
				movement.put(start, end);
			}
		}
	}
	
	private void displayMelds(ArrayList<Meld> melds){
		for(int i=0; i<melds.size(); i++){
			System.out.println("\t" + (i+1) + ") " + melds.get(i).toString());
		}
	}
	
	private void displayMoves(){
		ArrayList<Tile> tiles;
		Tile tile;
		Position key;
		
		for(int i=0; i<boardBefore.size(); i++){
			tiles = boardBefore.get(i).getTiles();
			System.out.println("\nFor meld " + boardBefore.get(i).toString());
			
			for(int j=0; j<tiles.size(); j++){
				tile = tiles.get(j);
				key = new Position(i+1, j+1);
				
				if(movement.containsKey(key)){
					System.out.println("Move [ " + tile.toString() + " ] from " + key.toString()
							+ " to " + movement.get(key).toString());
					movement.remove(key);
				}
			}
		}
		
		System.out.println("\nFrom your hand");
		for(Position start : movement.keySet()){
			key = movement.get(start);
			tile = boardAfter.get(key.getMeldNum()-1).getTiles().get(key.getPositionNum()-1);
			
			System.out.println("Move [ " + tile + " ] from your hand to " 
					+ key.toString());
		}
	}

}