package rummikub;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RAIclient extends Thread{
	private Client client;
	private int playerIndex;	
	
	private GameInfo game;
	private Hand hand;
	private boolean initialMeld;
	private int round;
	
	// **********************************************************
	// 					Constructors
	// **********************************************************
	
	/**
	 * Initializes a AI client
	 */
	public RAIclient(String serverIP, int port){
		try {
			client = new Client();
			playerIndex = client.connect(serverIP, port);
			printStatus("Connected to Rummikub server at " + serverIP);
			
			game = null;
			hand = null;
			initialMeld = false;
			round = 0;
			
		} catch (Exception e) {
			System.out.println("[FATAL ERROR] Could not connect to the server at " + serverIP);
			System.out.println("Please ensure that you have started the server before the client");
			
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	// **********************************************************
    // 				Main + User input validation
    // **********************************************************

	/**
	 * Performs user input validation, then runs the AI client as a thread
	 * @param args command line arguments sent to main
	 */
	public static void main(String[] args){
		String serverIP;
		int port;
		
		serverIP = Server.SERVER_IP;
		port = Server.PORT_NUM;
		
		if(args.length > 0)
			serverIP = args[0];
		if(args.length > 1)
			port = Integer.parseInt(args[1]);
		if(args.length > 2){
			System.out.println("Invalid command line options: " + args.toString());
			System.out.println("\nProper syntax of this program is ");
			System.out.println("\tRAIclient.java <Server IP> <port number>");
			System.out.println("\tServer IP defaults to " + Server.SERVER_IP);
			System.out.println("\tport number defaults to " + Server.PORT_NUM);
			System.exit(-1);
		}
		
		(new RAIclient(serverIP, port)).start();
	}
	
	/**
	 * The "main" function of the client thread.
	 */
	@Override
	public void run() {
		ArrayList<Meld> play, trivialPlay;
		Boolean playMade;
		
		try {
			startNewGame();
			
			while (!game.isGameOver()) {
				updateRound();
				
				playMade = false;
				
				System.out.println(game.displayGame());
				printStatus("Current Hand : " + hand.toString());
				
				if(initialMeld){
					trivialPlay = hand.getMeldsFromHand();
					
					if(trivialPlay.size() > 0){
						game.addMelds(trivialPlay);
						game.setHand(playerIndex, hand.getNumTiles());
						playMade = true;
					}
					
					play = game.getAdjacentPlay(hand, playerIndex);
					if(play != null){
						playMade = true;
					}
					else if (trivialPlay.size() > 0)
						play = trivialPlay;
				}
				
				else{
					play = hand.getInitialMeld();
					
					if(play != null){
						game.addMelds(play);
						game.setHand(playerIndex, hand.getNumTiles());
						initialMeld = true;
						playMade = true;
					}
				}
				
				if(!playMade){
					printStatus("Could not make a meld");
					drawTile();
				}
				else{
					playMelds(play);
				}
				
				// Wait for it to be your turn again 
				game = new GameInfo(client.getMessage());
			}
		}

		catch (Exception e) {
			printStatus("The server disconnected me?");
			e.printStackTrace();
		}
		
		this.endGame();
	}
	
	/**
	 * Prints the current status of the client
	 * 
	 * @param message
	 *            What is going on in the client
	 */
	private void printStatus(String message) {
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		System.out.println("\t" + date + " >> [ " + GameInfo.indexToPlayerName(playerIndex)+ " ] " + message);
	}
	
	// **********************************************************
    // 						Starting a Game
    // **********************************************************
	
	/**
	 * Initialize a new game
	 * @throws Exception
	 */
	private void startNewGame() throws Exception{
		hand = new Hand(client.getMessage());
		// TESTING
//		hand = new Set("[ o5 o6 o7 o8 o9 b6 x6 ]");
		System.out.println();
		printStatus("A new game has begun");
		printStatus("My Hand : " + hand.toString());
		
		game = new GameInfo(client.getMessage());
	}

	// **********************************************************
    // 						Playing a game
    // **********************************************************
	
    /**
     * Keeps track of how many rounds have passed so far
     */
	private void updateRound(){
		round++;
		
    	System.out.println("\n\n========================================================");
    	System.out.printf( "------------------      Round %2d      ------------------\n", round);
    	System.out.println("========================================================\n");
	}
	
	/**
	 * You chose to draw a new tile from the deck
	 * @throws Exception
	 */
	private void drawTile() throws Exception{
		client.sendMessage("draw");
		
		hand.addTile(new Tile(client.getMessage()));
		hand.sortByColour();
		
		printStatus("My new hand is: " + hand.toString());
	}
	
	/**
	 * Send a list of all of your moves to the server
	 * 
	 * @param melds
	 * @throws Exception
	 */
	private void playMelds(ArrayList<Meld> melds) throws Exception{
		for(Meld meld : melds)
			printStatus("Play: " + meld.toString());
		
		client.sendMessage(game.toString());
		hand.sortByColour();
	}
	
	
	// **********************************************************
	//					Ending a Game
	// **********************************************************
	
	// FIXME - move this section into the client class?
	
	/**
	 * Closes the sockets and the streams before exiting the thread
	 */
	private void endGame() {
		String finalScores;
		
		try{
			if(game.isGameOver()){
				client.sendMessage("" + hand.getScore());
				finalScores = client.getMessage();
				
				System.out.println("\n********************************************************\n");
				
				if(hand.getNumTiles() == 0)
					printStatus("YOU WIN!!!");
				else
					printStatus("YOU LOSE");
				
				displayFinalScores(finalScores.trim());
			}
		}
		catch(Exception e){
			printStatus("[ ERROR ] - Disconnected from the server during game over scoring");
		};
		
		client.disconnect();
		printStatus("Disconnected from the server");
	}
	
	/**
	 * print out the final scores of all players
	 * 
	 * @param results the results at the end of the game
	 */
	private void displayFinalScores(String results){
		String score[];
		
		score = results.split(",");
		
		System.out.println();
		printStatus("My Score: " + score[playerIndex]);
		printStatus("My hand : " + hand.toString());
		
		System.out.println();
		printStatus("Opponent Scores: ");
		for(int i=0; i<score.length; i++)
			if(i != playerIndex)
				printStatus("\t[ " + GameInfo.indexToPlayerName(i) + " ] : " + score[i]);
		System.out.println();
	}
	
}
