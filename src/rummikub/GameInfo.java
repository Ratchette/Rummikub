package rummikub;

public class GameInfo {
	public static final int GAMEOVER = -1;
	public static final int PLAYER1 = 0;
	public static final int PLAYER2 = 1;
	public static final int PLAYER3 = 2;
	public static final int PLAYER4 = 3;
	
	private Board board;
	private boolean myTurn;
	private boolean doneInitialMeld;
	
	private Integer[] handSize;
	private Set myHand;
	
	public static int getPlayer(int playerIndex){
		switch(playerIndex){
			case PLAYER1: return 1;
			case PLAYER2: return 2;
			case PLAYER3: return 3;
			case PLAYER4: return 4;
			default: return -1;
		}
	}



	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public void changeTurn(){
		myTurn = myTurn ? false : true;
	}
}
