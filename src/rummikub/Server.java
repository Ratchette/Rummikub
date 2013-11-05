/**
 * Description: CIS*4900 - A Rummikub framework
 * @author Jennfier Winer
 * 
 * @since 2013-10-24
 * Created: October 24, 2013
 * Last Modified: October 24, 2013
 */

package rummikub;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

/**
 * NOTES:
 * 	All of my players are numbered starting at 1, not 0. All player information must be decremented by one before being used as an index
 */

/**
 * A class that models a Rummikub server.
 * The server performs the game setup as well as the validation of moves
 * 
 * @author jwiner
 */
public class Server extends Thread{
	public static final int portNum = 4900;

    private static ServerSocket serverSocket;  
    private Socket[] clientSocket;
    private PrintWriter[] outbox;	// messages from server to client
    private BufferedReader[] inbox;	// messages from client to server
    
    private Pool pool;
    private Set[] hands;
    private Boolean[] initialMelds;
    private GameInfo game;
    
	private int turn;
    
    // **********************************************************
    // 						Server Methods 
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
    
    /**
     * Prints the current status of the server
     * This function should be called at least once from every other function in the server
     * @param message	What is going on
     */
	public void printStatus(String message){
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		System.out.println("\t" + date + " >> [[ Server ]] " + message);
	}
   
	
    // **********************************************************
    // 					Communication Methods
    // **********************************************************
	
    /**
     * Configure and connect sockets
     */
    private void acceptClients(){
    	printStatus("Now ready to accept " + Integer.toString(clientSocket.length) + " clients");
    	
        try{
            serverSocket = new ServerSocket(portNum);	
            
            for(int i=0; i<clientSocket.length; i++){
                printStatus("Waiting for " + Integer.toString(clientSocket.length - i)
                				+ " more clients");
                
            	clientSocket[i] = serverSocket.accept();
            	inbox[i] = new BufferedReader(new InputStreamReader(clientSocket[i].getInputStream()));
                outbox[i] = new PrintWriter(clientSocket[i].getOutputStream(), true);
                outbox[i].println(i+1);

                printStatus("Accept client " + Integer.toString(i+1));
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
        try{
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
     * Interprets the message sent from a client. If the move was valid, record it.
     * 
     * @param message The client's move encoded in an integer
     * 
     */
    private int decodeMessage(String message){
//        if( 0 <= message && message < 9){
//            if(gameGrid[message/3][message%3] == -1)
//                gameGrid[message/3][message%3] = currentTurn;
//            else
//                message =  message + 30;
//            
//            return message;
//        }
//        
//        else{
//            System.out.println("Unknown message " + message + " recieved from the client!!");
//            return -5;
//        }
    	
    	printStatus("The method [ decodeMessage ] has not yet been implemented");
    	return -1;
    }

    /**
     * It sends a detailed message back to each client through the sockets 
     * about how to alter their Gui to match the current state of the game.
     * 
     * @param message The client's move encoded in an integer
     */
    private void sendMessages(int message){
//        int winner = isGameOver();
//        int currentClient;
//        
//        try{
//            for(currentClient=0; currentClient<2; currentClient++){
//                try{
//                    // If the game has ended
//                    if(winner != -1){
//                        if(winner == 3)
//                            outbox[currentClient].writeInt(3);
//                        else if(winner == currentClient)
//                            outbox[currentClient].writeInt(1);
//                        else
//                            outbox[currentClient].writeInt(2);     
//                    }
//                    
//                    // If the move was valid, tell both clients what move was made and whose turn it is
//                    if(0 <= message && message < 9){  
//                        outbox[currentClient].writeInt(message + 10 + 10*((currentTurn + currentClient)%2));
//                    }
//
//                    // if the move was NOT valid, tell the client who played that move to try again
//                    else if (30 <= message && message < 39){
//                        if(currentTurn == currentClient)
//                            outbox[currentClient].writeInt(message);
//                    }
//
//                    // If someone disconnected, tell their partner that the game has ended
//                    else if (message == -1 && currentTurn != currentClient){
//                        outbox[currentClient].writeInt(-1);
//                    }
//                    else
//                        System.out.println("Unknown message " + message);
//                }
//                catch(Exception e){
//                    System.out.println("Partner " + (currentClient+1) + " disconnected");
//                    outbox[(currentClient+1)%2].writeInt(-1);
//                }
//            }
//        }
//        catch(Exception e){
//            System.out.println("Both Parties have disconnected???");
//        }
    	
    	printStatus("The method [ server.sendMessages ] has not yet been implemented");
    }
    
    // **********************************************************
    // 					Gameplay Methods
    // **********************************************************
    
    
    
    @Override
    public void run(){
    	Scanner keyboard = new Scanner(System.in);
        String message;
    	
        acceptClients();
        startGame();
                
        while(!game.isGameOver()){
//        	printStatus("Press any button to continue .....");
//        	keyboard.nextLine();
        	
            try{
            	outbox[turn].println(game.toString());
            	System.out.println(game.displayGame());
    	        printStatus("Sent Current state of game to [ " + getCurrentPlayer() + " ]");
            	
                message = inbox[turn].readLine();
                
                // TODO interpret the move
                // TODO respond back to the client
                if(message.equalsIgnoreCase("draw")){
                	drawTile();
                }
            	else{
            		playMeld(message);	
            	}
 
                changeTurn();
                printStatus("It is now client " + getCurrentPlayer() + "'s turn");
            }
            
            catch(Exception e){
                printStatus("A client has disconnected. Relaying game termination to all clients");
                e.printStackTrace();
                sendMessages(-1);
                break;
            }
            
        }
        
        printStatus("Game Over! [ " + getCurrentPlayer() + " ] wins");
        this.disconnectClients();
    }
    
    private void startGame(){
    	int numPlayers;
    	
    	try{
    		numPlayers = clientSocket.length;
    		
    		this.pool = new Pool();
    		this.hands = new Set[numPlayers];
    		this.initialMelds = new Boolean[numPlayers];
	    	this.game = new GameInfo(numPlayers);    	
	    	
	    	for(int i=0; i<hands.length; i++){
	    		initialMelds[i] = false;
	    		
	    		hands[i] = new Set(pool.getHand());
	    		hands[i].sortByColour();
	    		outbox[i].println(hands[i].toString());
	    	}
	    	
	    	this.turn = GameInfo.PLAYER1;
    	}
    	catch(Exception e){
    		System.out.println(e.getMessage());
    		e.printStackTrace();
    	}
    }
    
    private void drawTile() throws Exception{
    	Tile nextTile;
    	
    	nextTile = pool.drawTile();
    	hands[turn].addTile(nextTile);
    	
    	printStatus("Sent a new tile [ " + nextTile.toString() + " ] to [ " + getCurrentPlayer() + " ]");
    	outbox[turn].println(nextTile.toString());
    	
    	game.addTile(turn);
    }
    
    private void playMeld(String message) throws Exception{
    	printStatus("Got message : from [ " + getCurrentPlayer() + " ]");
    	game = new GameInfo(message);
    }
    
	public void changeTurn(){
		this.turn = (turn + 1) % hands.length;
	}
	
	public String getCurrentPlayer(){
		switch(this.turn){
			case GameInfo.PLAYER1: 
				return "Player 1";
				
			case GameInfo.PLAYER2: 
				return "Player 2";
				
			case GameInfo.PLAYER3:
				if(hands.length > 2)
					return "Player 3";
				break;
				
			case GameInfo.PLAYER4: 
				if(hands.length > 3)
					return "Player 4";
				break;
				
			default: return "INVALID PLAYER";
		}
		
		return "INVALID PLAYER";
	}
    
    public static void main(String[] args) {
		int numPlayers;
		
		numPlayers = parseArguments(args);
		if(numPlayers == -1)
			return;
		
		(new Server(numPlayers)).start();
	}
    
    public static int parseArguments(String[] args){
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
}
