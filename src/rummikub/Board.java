package rummikub;

import java.util.ArrayList;

public class Board {
	private ArrayList<Set> pool;
	private ArrayList<Set> melds;
	
	public Board(){
		pool = new ArrayList<Set>();
		melds = new ArrayList<Set>();
	}
}
