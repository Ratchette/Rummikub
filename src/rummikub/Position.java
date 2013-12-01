package rummikub;

// FIXME this class was shoddily put together
public class Position{
	private int meldNum;
	private int positionNum;
	
	public Position(){
		meldNum = -1;
		positionNum = -1;
	}
	
	public Position(int meld, int order){
		meldNum = meld;
		positionNum = order;
	}
	
	public Position(Position copy){
		meldNum = copy.meldNum;
		positionNum = copy.positionNum;
	}
	
	public void setMeldNum(int num){
		meldNum = num;
	}
	
	public int getMeldNum(){
		return meldNum;
	}
	
	
	public void setPositionNum(int num){
		positionNum = num;
	}
	
	public int getPositionNum(){
		return positionNum;
	}
	
	@Override
	public boolean equals (Object other){
		if(other == null)
			return false;

		if (!(other instanceof Position)) 
			return false;

		Position otherTile = (Position) other;
		return this.meldNum == otherTile.meldNum
				&& this.positionNum == otherTile.positionNum;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + meldNum;
		result = prime * result + positionNum;
		return result;
	}
	
	public String toString(){
		return "" + this.meldNum + ", " + this.positionNum;
	}
}