/**
 * Description: CIS*4900 - A Rummikub framework
 * @author Jennfier Winer
 * 
 * @since 2013-10-24
 * Created: October 24, 2013
 * Last Modified: October 24, 2013
 */

package rummikub;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A class that models a Rummikub server.
 * The server performs the game setup as well as the validation of moves
 * 
 * @author jwiner
 */
public class Server extends Thread{
	public static final int portNum = 4900;

    private static ServerSocket hostSocket;  
    private Socket[] clientSocket;
    private DataInputStream[] inbox;
    private DataOutputStream[] outbox;
    
    private Board board;
    private ArrayList<Tile> pool;
    private ArrayList<Set>[] hand;
    
    private int gameInProgress;
    private int currentTurn;
    
    
    // **********************************************************
    // 						Server Methods 
    // **********************************************************
    
    /**
     * Constructor for the server
     * @param numPlayers The number of players per game
     */
    public Server(int numPlayers){
    	board = null;
    	pool = null;
    	hand = null;
    	gameInProgress = 0;
    	currentTurn = -1;
    	
    	hostSocket = null;
        clientSocket = new Socket[numPlayers];
        inbox = new DataInputStream[numPlayers];
        outbox = new DataOutputStream[numPlayers];
    }
    
	public void printStatus(String requester, String message){
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		System.out.println("\t" + date + " >> [ " + requester + " ] " + message);
	}
   
	
    // **********************************************************
    // 					Communication Methods
    // **********************************************************
	
    /**
     * Configure and connect sockets
     */
    private void acceptClients(){
        try{
            hostSocket = new ServerSocket(portNum);
            
            
            // TODO Make this function accomodate between 2-4 players
            clientSocket[0] = hostSocket.accept();
            inbox[0] = new DataInputStream(clientSocket[0].getInputStream());
            outbox[0] = new DataOutputStream(clientSocket[0].getOutputStream());
            System.out.println("Accepted first Client");
            
            clientSocket[1] = hostSocket.accept();
            inbox[1] = new DataInputStream(clientSocket[1].getInputStream());
            outbox[1] = new DataOutputStream(clientSocket[1].getOutputStream());
            System.out.println("Accepted Second Client");
            
            // Tell whoever is player one that it is their turn to make a move
            outbox[0].writeInt(50);
        }
        
        catch(Exception e){
        	
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
            Thread.sleep(5000);
            hostSocket.close();
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
	            // Player 1 must have already disconnected
	        }
        }
    }
    
    /**
     * Interprets the message sent from a client. If the move was valid, record it.
     * 
     * @param message The client's move encoded in an integer
     * 
     * @return An integer X (between 0 and 8) corresponds to a valid move made to square X. <br/>
     * An integer 3X (between 30 and 38) means that the client attempted 
     * to make a move to the previously occupied square X, and thus no changes were made to the game.
     */
    private int decodeMessage(int message){
        if( 0 <= message && message < 9){
            if(gameGrid[message/3][message%3] == -1)
                gameGrid[message/3][message%3] = currentTurn;
            else
                message =  message + 30;
            
            return message;
        }
        
        else{
            System.out.println("Unknown message " + message + " recieved from the client!!");
            return -5;
        }
    }

    /**
     * It sends a detailed message back to each client through the sockets 
     * about how to alter their Gui to match the current state of the game.
     * 
     * @param message The client's move encoded in an integer
     */
    private void sendMessages(int message){
        int winner = isGameOver();
        int currentClient;
        
        try{
            for(currentClient=0; currentClient<2; currentClient++){
                try{
                    // If the game has ended
                    if(winner != -1){
                        if(winner == 3)
                            outbox[currentClient].writeInt(3);
                        else if(winner == currentClient)
                            outbox[currentClient].writeInt(1);
                        else
                            outbox[currentClient].writeInt(2);     
                    }
                    
                    // If the move was valid, tell both clients what move was made and whose turn it is
                    if(0 <= message && message < 9){  
                        outbox[currentClient].writeInt(message + 10 + 10*((currentTurn + currentClient)%2));
                    }

                    // if the move was NOT valid, tell the client who played that move to try again
                    else if (30 <= message && message < 39){
                        if(currentTurn == currentClient)
                            outbox[currentClient].writeInt(message);
                    }

                    // If someone disconnected, tell their partner that the game has ended
                    else if (message == -1 && currentTurn != currentClient){
                        outbox[currentClient].writeInt(-1);
                    }
                    else
                        System.out.println("Unknown message " + message);
                }
                catch(Exception e){
                    System.out.println("Partner " + (currentClient+1) + " disconnected");
                    outbox[(currentClient+1)%2].writeInt(-1);
                }
            }
        }
        catch(Exception e){
            System.out.println("Both Parties have disconnected???");
        }
    }
    
    // **********************************************************
    // 					Gameplay Methods
    // **********************************************************
    
    @Override
    public void run(){
        int decodedMessage;
        
        acceptClients();
        while(!isGameOver()){
            try{
            	// wait for the next player to send me a move
                decodedMessage = decodeMessage(inbox[currentTurn].readInt());

                // TODO interpret the move
                
                // TODO respond back to the client
                //sendMessages(decodedMessage);
                
                if(decodedMessage < 30)
                    currentTurn = (currentTurn + 1) % hand.length;
            }
            
            catch(Exception e){
                // one of the clients has disconnected, so inform the others that the game is over
                sendMessages(-1);
                break;
            }
        }
        
        this.disconnectClients();
    }
    

    
    
    
    /**
     * Checks to see if the game has been completed yet
     * @return 0 = the game has not yet ended <br/>
     * Otherwise, the return value indicates which player has one (assuming the client indices start at 0) <br/>
     */
    private int isGameOver(){
        int i;
        
        for(i=0; i<hand.length; i++){
        	if (hand[i].size() == 0)
        		return i + 1;
        }
        return 0;
    }

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
