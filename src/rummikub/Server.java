/**
 * Description: CIS*4900 - A Rummikub framework
 * @author Jennfier Winer
 * 
 * @since 2013-10-24
 * Created: October 24, 2013
 * Last Modified: November 28, 2013
 */

package rummikub;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * NOTES:
 * 	All of my players are numbered starting at 1, not 0. All player information must be decremented by one before being used as an index
 */

/**
 * A class that models a Rummikub server that keeps track of 
 * the game and all players. Its two primary functions are to 
 * validate the move that each player makes, then relay it to 
 * all other players. 
 * 
 * @author jwiner
 */
public class Server extends Thread{
	public static final String SERVER_IP = "localhost";
	public static final int PORT_NUM = 4900;

    private static ServerSocket serverSocket;  
    private Socket[] clientSocket;
    private PrintWriter[] outbox;	// messages from server to client
    private BufferedReader[] inbox;	// messages from client to server
    
    private Pool pool;
    private Hand[] hands;
    private Boolean[] initialMelds;
    private GameInfo game;
    
	private int turn;
	private int round;
	
	// FIXME Calculate the total playtime 
	private Date startTime;
    
    // **********************************************************
    // 						Constructors
    // **********************************************************
    
    /**
     * Constructor for the server
     * @param numPlayers The number of players per game
     */
    public Server(int numPlayers){
    	serverSocket = null;
        clientSocket = new Socket[numPlayers];
        inbox =  new BufferedReader[numPlayers];
        outbox = new PrintWriter[numPlayers];
        
        pool = null;
        hands = null;
    	game = null;
    	
    	printStatus("Initialization complete");
    }
	
    // **********************************************************
    // 					Main + User input Validation
    // **********************************************************
    /**
     * Print the correct way to run this program to the command line
     */
    public static void printUsage(){
		System.out.println("\nProper syntax is:");
		System.out.println("Server.java <num_players>");
		System.out.println("\tnum_players = The number of players in this game. Possible values are 2, 3, or 4");
    }
    
    
    /**
     * Validates the arguments used to run this program
     * @param args The command line arguments that the user entered
     * @return The number of players per game of rummikub (a number between 2 - 4)
     * 			If the user input was not a number between 2 and 4, then it returns -1
     */
    private static int validateArguments(String[] args){
		if(args.length < 1){
			System.out.println("Too few arguments.");
			System.out.println("The first argument to this function must be the number of players that this server will support.");
		}
		else if(!args[0].matches("^\\d$")){
			System.out.println("The first argument " + args[0] + " is invalid");
			System.out.println("The first argument to this function must be an integer that represents the number of players that this server will support.");
		}
		else if(Integer.parseInt(args[0]) < 2 || Integer.parseInt(args[0]) > 4){
			System.out.println("The first argument " + args[0] + " is invalid");
			System.out.println("This game can only support between 2-4 players");
		}
		else
			return Integer.parseInt(args[0]);
		
		return -1;
	}
    
	/**
	 * Runs the server as a thread
	 * @param args command line arguments sent to main
	 */
    public static void main(String[] args) {
		int numPlayers;
		
		numPlayers = validateArguments(args);
		if(numPlayers == -1){
			printUsage();
			return;
		}
		
		(new Server(numPlayers)).start();
	} 
    
    /**
     * The heart of the server. 
     * NOTE: this function should never be called directly, it should be invoked
     * 		indirectly through the start of the thread.
     * 
     *  for more information, please see: 
     *  	http://docs.oracle.com/javase/tutorial/essential/concurrency/
     */
    @Override
    public void run(){
        String message;
    	
        acceptClients();
        startGame();
                
        while(!game.isGameOver()){
        	updateTurn();
        	printStatus("It is now " + GameInfo.indexToPlayerName(this.turn) + "'s turn");
        	printStatus("Tiles in pool: " + pool.remainingTiles());
        	
        	// receive move
        	System.out.println(game.displayGame());
        	outbox[turn].println(game.toString());
        	
            try{
                message = inbox[turn].readLine();
                interpretMessage(message);
            }
            
            catch(Exception e){
//                e.printStackTrace();
                printStatus("A client has disconnected. Relaying game termination to all clients");
                this.turn = GameInfo.DISCONNECT;
                break;
            }
        }
        
        if(game.isGameOver())
        	broadcastFinalScores();
        
        this.disconnectClients();
    }
    
    /**
     * Prints the current status of the server
     * This function should be called at least once from every other function in the server
     * @param message	What is going on
     */
	private void printStatus(String message){
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		System.out.println("\t" + date + " >> [[ Server ]] " + message);
	}  
    
    // **********************************************************
    // 						Start Game
    // **********************************************************
    
    /**
     * Configure and connect clients to sockets
     */
    private void acceptClients(){
    	String clientGrammer;
    	clientGrammer = "clients";
    	
    	printStatus("Now ready to accept " + Integer.toString(clientSocket.length) + " clients");
    	
        try{
            serverSocket = new ServerSocket(PORT_NUM);	
            
            for(int i=0; i<clientSocket.length; i++){
            	if(i == 1)
            		clientGrammer = "client";
                printStatus("Waiting for " + Integer.toString(clientSocket.length - i)
                				+ " more " + clientGrammer);
                
            	clientSocket[i] = serverSocket.accept();
            	inbox[i] = new BufferedReader(new InputStreamReader(clientSocket[i].getInputStream()));
                outbox[i] = new PrintWriter(clientSocket[i].getOutputStream(), true);
                outbox[i].println(i);

                printStatus("Accepted client " + Integer.toString(i+1));
            }
        }
        
        catch(Exception e){
        	printStatus("Error accepting clients");
            e.printStackTrace(); 
        }
    }
    
    /**
     * Initialize a new game board
     */
    private void startGame(){
    	int numPlayers;
    	
    	try{
    		startTime = new Date();
    		numPlayers = clientSocket.length;
    		
    		this.pool = new Pool();
    		this.hands = new Hand[numPlayers];
    		this.initialMelds = new Boolean[numPlayers];
	    	this.game = new GameInfo(numPlayers);    	
	    	
	    	for(int i=0; i<hands.length; i++){
	    		initialMelds[i] = false;
	    		
	    		hands[i] = new Hand(pool.getHand());
	    		hands[i].sortByColour();
	    		outbox[i].println(hands[i].toString());
	    	}
	    	
	    	this.turn = GameInfo.PLAYER1 - 1;
	    	this.round = 0;
    	}
    	catch(Exception e){
    		System.out.println("FATAL ERROR: unable to create a new game");
    		System.out.println(e.getMessage());
    		e.printStackTrace();
    		System.exit(-1);
    	}
    }

    // **********************************************************
    // 						Gameplay
    // **********************************************************
    
    /**
     * Keeps track of how many rounds have passed so far
     */
	private void updateTurn(){
		turn = game.getNextPlayer(this.turn);
		
    	System.out.println("\n\n========================================================");
    	if(turn == GameInfo.PLAYER1){
        	round++;
        	System.out.printf( "------------------      Round %2d      ------------------\n", round);
    	}
    	System.out.println("========================================================\n");
	}
    
    /**
     * Interprets the message sent from a client. 
     */
    private void interpretMessage(String message) throws Exception{
        if(message.equalsIgnoreCase("draw"))
        	drawTile();
    	else
    		playMeld(message);
    }
    
    /**
     * The user chose to draw a tile
     * @throws Exception
     */
    private void drawTile() throws Exception{
    	Tile nextTile;
    	
    	nextTile = pool.drawTile();
    	hands[turn].addTile(nextTile);
    	game.addTile(turn);
    	
    	outbox[turn].println(nextTile.toString());
    	printStatus("No move made");
    	printStatus("Sent a new tile [ " + nextTile.toString() + " ] to [ " + GameInfo.indexToPlayerName(this.turn) + " ]");
    }
    
    /**
     * Add the move a player made to the board
     * 
     * @param message the play that they made
     * @throws Exception If any of the melds or tiles that they used in their 
     * 		play are invalid
     */
    private void playMeld(String message) throws Exception{
    	printStatus("Got a play from [ " + GameInfo.indexToPlayerName(this.turn) + " ]");
    	game = new GameInfo(message);
    	
    	// FIXME Validation missing
    	/**
    	 * Modify this method to validate the following
    	 */
    }
    
    // **********************************************************
    // 						End Game
    // **********************************************************
    
    /**
     * Calculate everyones final score then broadcast this to everyone
     */
	private void broadcastFinalScores(){
    	String stringScores;
    	int finalScore[];
    	
    	System.out.println();
    	printStatus("[ GAME OVER ] " + GameInfo.indexToPlayerName(turn) + " wins");
    	
		stringScores = "";
		finalScore = new int[hands.length];	// all elements are set to 0 by default in Java
		
		// Calculate the scores of each player 
    	for(int i=0; i<finalScore.length; i++){
    		outbox[i].println(game.toString());
    		
    		if(i != turn){
    			try {
					finalScore[i] = finalScore[i] + Integer.parseInt(inbox[i].readLine()) * -1;
				} catch (Exception e) {
					printStatus(GameInfo.indexToPlayerName(i) + " has already terminated their connection.");
				}
    			
    			finalScore[turn] = finalScore[turn] + finalScore[i] * -1;
    		}
    	}
    	
    	// Create a string that contains all scores
    	stringScores = "";
    	for(int i=0; i<finalScore.length; i++)
    		stringScores = stringScores + finalScore[i] + ",";
    	stringScores = stringScores.substring(0,stringScores.lastIndexOf(","));
    	
    	// Broadcast the scores to all players and display the final results in the server
    	for(int i=0; i<finalScore.length; i++){
    		if(i == turn)
    			printStatus("[ " + GameInfo.indexToPlayerName(i) + " ] : " + finalScore[i]);
    		else
    			printStatus("  " + GameInfo.indexToPlayerName(i) + "   : " + finalScore[i]);
    		
    		
    		outbox[i].println(stringScores);
    	}
    }
    
    /**
     * Closes the sockets and streams of all clients
     */
    private void disconnectClients() {
        for(int i=0; i<clientSocket.length; i++){
	        try{
	            inbox[i].close();
	            outbox[i].close();
	            clientSocket[i].close();
	        }
	        catch(Exception e){
	        	printStatus("[ " + GameInfo.indexToPlayerName(i) + " ] disconnected before I could terminate connection");
	        }
        }

        try {
			serverSocket.close();
		} catch (IOException e) {
			printStatus("[ ERROR ] the server socket is already closed?");
			e.printStackTrace();
		}
    }
}


