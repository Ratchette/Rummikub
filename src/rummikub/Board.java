package rummikub;

import java.util.ArrayList;

public class Board {
	private Set pool;
	private ArrayList<Set> melds;
	
	public Board() throws Exception{
		pool = new Set();
		setMelds(new ArrayList<Set>());
	}

	public ArrayList<Set> getMelds() {
		return melds;
	}

	public void setMelds(ArrayList<Set> melds) {
		this.melds = melds;
	}
	
	// testing 
	public void printPool(){
		System.out.println(pool.toString());
	}
	
}
