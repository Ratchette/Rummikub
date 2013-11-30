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
    // 					Communication Methods
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
     * Closes the sockets and streams of all clients
     */
    private void disconnectClients() {
    	// FIXME broadcast to all players who won
    	
        try{
        	printStatus("Game Over! [ " + GameInfo.indexToPlayerName(this.turn) + " ] wins");
            /* This ensures that clients have enough time to potentially read 
             * a Game over message in addition to a "move made" message before
             * the stream is closed and an irritating exception is thrown */
            Thread.sleep(1000);
            serverSocket.close();
            printStatus("Server Socket Closed");
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
        for(int i=0; i<clientSocket.length; i++){
	        try{
	            inbox[i].close();
	            outbox[i].close();
	            clientSocket[i].close();
	        }
	        catch(Exception e){
	        	printStatus("[ Player " + Integer.toString(i+1)
	        			+ " ] disconnected before I could terminate connection");
	        }
        }
    }
   
    /**
     * Interprets the message sent from a client. 
     */
    private void decodeMessage(String message) throws Exception{
        if(message.equalsIgnoreCase("draw"))
        	drawTile();
    	else
    		playMeld(message);
    }
    
    // **********************************************************
    // 					Gameplay Methods (server thread)
    // **********************************************************
    
    @Override
    public void run(){
        String message;
    	
        acceptClients();
        startGame();
                
        while(turn >= GameInfo.PLAYER1 && turn < hands.length){
        	
        	updateRound();
        	printStatus("It is now " + GameInfo.indexToPlayerName(this.turn) + "'s turn");
        	printStatus("Sent Current state of game");
        	printStatus("Tiles in pool: " + pool.remainingTiles());
        	
        	// receive move
        	System.out.println(game.displayGame());
        	outbox[turn].println(game.toString());
        	
            try{
                message = inbox[turn].readLine();
                decodeMessage(message);
                
                turn = game.getNextPlayer(this.turn);
            }
            
            catch(Exception e){
//                e.printStackTrace();
                printStatus("A client has disconnected. Relaying game termination to all clients");
                this.turn = GameInfo.DISCONNECT;
                break;
            }
        }
        
        this.disconnectClients();
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
	    	
	    	this.turn = GameInfo.PLAYER1;
	    	this.round = 0;
    	}
    	catch(Exception e){
    		System.out.println("FATAL ERROR: unable to create a new game");
    		System.out.println(e.getMessage());
    		e.printStackTrace();
    		System.exit(-1);
    	}
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
    }
    
    /**
     * Keeps track of how many rounds have passed so far
     */
	private void updateRound(){
    	System.out.println("\n\n========================================================");
    	
    	if(turn == GameInfo.PLAYER1){
        	round++;
        	System.out.printf( "------------------      Round %2d      ------------------\n", round);
    	}
    	
    	System.out.println("========================================================\n");
	}
    
	// **********************************************************
    // 					Main Methods
    // **********************************************************
	
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
     * Validates the arguments used to run this program
     * @param args The command line arguments that the user entered
     * @return The number of players per game of rummikub (a number between 2 - 4)
     * 			If the user input was not a number between 2 and 4, then it returns -1
     * 			
     */
    public static int validateArguments(String[] args){
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
     * Print the correct way to run this program to the command line
     */
    public static void printUsage(){
		System.out.println("\nProper syntax is:");
		System.out.println("Server.java <num_players>");
		System.out.println("\tnum_players = The number of players in this game. Possible values are 2, 3, or 4");
    }
    
    /**
     * Prints the current status of the server
     * This function should be called at least once from every other function in the server
     * @param message	What is going on
     */
	public void printStatus(String message){
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		System.out.println("\t" + date + " >> [[ Server ]] " + message);
	}    
    
}

