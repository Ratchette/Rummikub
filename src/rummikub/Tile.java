package rummikub;

public class Tile implements Comparable<Tile>{
	public static final char RED 	= 'r';
	public static final char ORANGE = 'g';
	public static final char BLUE 	= 'b';
	public static final char BLACK	= 'x';
	public static final char[] COLOURS = {RED, ORANGE, BLUE, BLACK};
	
	public static final int JOKER = 14;
	
	public final char colour;
	public final int  number;
	
	
	// **********************************************************
    // 						Constructors
    // **********************************************************
	
	/**
	 * Regular constructor
	 * @param myColour
	 * 			The colour of the new tile
	 * @param myNum
	 * 			The number of the new tile
	 * @throws Exception
	 * 			If the tile's number is not between 1 and 14 (14 = Joker),
	 * 			if the tile is not one of the four colours, or if the 
	 * 			Joker is not the colour red or black.
	 */
	public Tile(char myColour, int myNum) throws Exception{
		if(!validateColour(myColour))
			throw new Exception("Invalid colour [ " + myColour + " ]");
		if(!validateNumber(myNum))
			throw new Exception("Invalid number [" + myNum + " ]");
		if(myNum == Tile.JOKER && !validateJoker(myColour, myNum))
			throw new Exception("Invalid Joker colour [ " + myColour + " ]");
		
		this.colour = myColour;
		this.number = myNum;
	}
	
	/**
	 * Copy constructor
	 * @param copy
	 * 			The tile that you wish to copy
	 * @throws Exception
	 * 			If the tile's number is not between 1 and 14 (14 = Joker),
	 * 			if the tile is not one of the four colours, or if the 
	 * 			Joker is not the colour red or black.
	 */
	public Tile(Tile copy) throws Exception{
		if(copy == null)
			throw new Exception("null tile sent to copy constructor");
		
		if(!validateTile(copy))
			throw new Exception("Invalid Tile send into copy constructor");
		
		this.colour = copy.colour;
		this.number = copy.number;
	}
	
	/**
	 * Re-creates a tile that was converted into a string
	 * @param raw
	 * 			The string that contains a tile
	 * 
	 * NOTE: this function should only be called on strings created using
	 * 			this classes' toString method
	 */
	public Tile(String raw) throws Exception{
		this.colour = raw.charAt(0);
		// TODO - make sure that this line works as expected
		this.number = Integer.parseInt(raw.substring(1));
		
		if(!validateTile(this))
			throw new Exception("[ " + raw + " ] is not a valid tile");
	}

	
	// **********************************************************
    // 						Validation
    // **********************************************************
	
	/**
	 * Ensure that the tile is a valid rummikub piece
	 * @param tile
	 * 			The tile that you wish to check
	 * @return
	 * 			true if he piece's colour and number combination
	 * 			is valid </br>
	 * 			false otherwise
	 */
	public boolean validateTile(Tile tile){
		return validateColour(tile.colour) 
				&& validateNumber(tile.number)
				&& (tile.number == Tile.JOKER) ? validateJoker(tile.colour, tile.number) : true;
	}
	
	/**
	 * Determine if the tile has a valid number
	 * @param num
	 * 			true if he piece's number is between 1 and 13
	 * 			or if the piece is a joker </br>
	 * 			false otherwise
	 */
	private boolean validateNumber(int num){
		return (num > 0 && num < 14) || num == Tile.JOKER;
	}
	
	/**
	 * Determine if the tile is a valid colour
	 * @param colour
	 * 			the colour of the tile
	 * @return 
	 * 			true if the colour is one of the four valid
	 *			rummikub colours </br>
	 *			false otherwise
	 */
	private boolean validateColour(char colour){
		return colour == Tile.RED
				|| colour == Tile.ORANGE
				|| colour == Tile.BLUE
				|| colour == Tile.BLACK;
	}
	
	/**
	 * Check if the piece is a joker
	 * @param colour
	 * 			the colour of the piece
	 * @param num
	 * 			the number of the piece
	 * @return
	 * 			true if the piece is a joker </br>
	 * 			false otherwise
	 */
	private boolean validateJoker(char colour, int num){
		if(number != Tile.JOKER)
			return false;
		
		if(colour == Tile.RED || colour == Tile.BLACK)
			return true;
		
		return false;
	}
	
	// **********************************************************
    // 							Common
    // **********************************************************
	
	@Override
	public boolean equals (Object other){
		if(other == null)
			return false;

		if (!(other instanceof Tile)) 
			return false;

		Tile otherTile = (Tile) other;
		return this.colour == otherTile.colour
				&& this.number == otherTile.number;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + colour;
		result = prime * result + number;
		return result;
	}
	
	@Override
	public String toString(){
		return "" + this.colour + Integer.toString(this.number); 
	}
	
	// **********************************************************
    // 							Unsorted
    // **********************************************************
	
	@Override
	public int compareTo(Tile other) {
		int difference;
		
		// This sorts by number first, then on colour
		difference = (this.number*10 + this.colour) - (other.number*10 + other.colour);
		
		return difference;
	}
	
	public boolean isAdjacent(Tile other){
		if(this.colour == other.colour){
			if(Math.abs(this.number - other.number) == 1)
				return true;
		}
		else{
			if(this.number == other.number)
				return true;
		}
		
		return false;
	}
	
	/**
	 * Returns a list of all of the valid colour codes
	 * @return
	 * 			a string where each character is a colour code
	 */
	public static String getColours(){
		return "" + RED + ORANGE + BLUE + BLACK;
	}
}
