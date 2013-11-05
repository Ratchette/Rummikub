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
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A class that models a Rummikub client.
 * 
 * @author jwiner
 */
//public class Client extends SwingWorker<Void, String> {
public class Client extends Thread{
	private Socket mySocket;
	private BufferedReader inbox;
	private PrintWriter outbox;

	private GameInfo game;
	private Set hand;
	private boolean initialMeld;
	private int playerNum;	// discover if you are player 1 through 4 (for display purposes only)

	// **********************************************************
	// 					Initialization Methods
	// **********************************************************

	/**
	 * Initializes the private variables and tries to connect to the server at
	 * address ip
	 * 
	 * @param serverIP
	 *            the IP address of the server
	 */
	public Client() {
		mySocket = null;
		inbox = null;
		outbox = null;

		playerNum = -1;	// -1 means that you are not connected to a server

		printStatus("Initialization Complete");
	}

	/**
	 * Connect to the server
	 * 
	 * @param serverIP
	 *            The IP address of the server
	 */
	public void connect(String serverIP) {
		try {
			mySocket = new Socket(serverIP, Server.portNum);
			outbox = new PrintWriter(mySocket.getOutputStream(), true);

			inbox = new BufferedReader(new InputStreamReader(
					mySocket.getInputStream()));
			playerNum = Integer.parseInt(inbox.readLine());	
			printStatus("Connected to Server");
		}

		catch (Exception e) {
			printStatus("Could not connect to the server at "
					+ serverIP + "\nPlease ensure that you have started "
					+ "the server before starting a client");
			System.exit(0);
		}
	}

	// **********************************************************
	// 					Communication Methods
	// **********************************************************

	/**
	 * Sends a message to the server
	 * 
	 * @param message
	 *            the message that you wish to send
	 */
	protected void sendMessage(String message) {
		try {
			printStatus("Sending message: " + message);
			outbox.println(message);
		} catch (Exception e) {
			printStatus("The server disconnected me");
			decodeMessage("no connection");
		}
	}

	/**
	 * Decipher the message and alert the user
	 * 
	 * @param message
	 *            the message received from the server
	 */
	private void decodeMessage(String message) {
		try {
			printStatus("Server says: " + message);
		} catch (Exception e) {
			printStatus("Failed to update the GUI [GUI NOT IMPLEMENTED YET]");
		}
	}

	/**
	 * Prints the current status of the server This function should be called at
	 * least once from every other function in the server
	 * 
	 * @param message
	 *            What is going on
	 */
	public void printStatus(String message) {
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new Date());
		System.out.println("\t" + date + " >> [ Client " + playerNum + " ] "
				+ message);
	}

	// **********************************************************
    // 						Gameplay Methods
    // **********************************************************
	

	private void startGame() throws Exception{
		String encodedHand;
		
		initialMeld = false;
		
		try{
			encodedHand = inbox.readLine();
			printStatus("Received message: " + encodedHand);
			hand = new Set(encodedHand);
//			hand = new Set("[ o5 o6 o7 o8 o9 b6 x6 ]");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * The "main" function of the client thread.
	 */
	@Override
	public void run() {
		String message;
		ArrayList<Set> play;
		
		connect("localhost");

		try {
			startGame();
			
			while (true) {
				// the server will send you the current state of the board and the number of cards in each players hand before it is your turn
				// YOU are responsible for checking if a player has won and disconnecting on your own!
				
				message = inbox.readLine();
				game = new GameInfo(message);
				
				System.out.println(game.displayGame());
				printStatus("Current Hand : " + hand.toString());
				
				if(!initialMeld){
					play = hand.getInitialMeld();
					
					if(play != null){
						game.addMelds(play);
						game.setHand(playerNum - 1, hand.getNumTiles());
						initialMeld = true;
					}
				}
				
				else{
					play = game.getMove(hand, playerNum - 1);
					
					if(play != null)
						game.setMelds(play);
				}
				
				// there is no play to make
				if(play == null){
					printStatus("Could not make a meld");
					drawTile();
				}
				else{
					playMeld(play);
				}
			}
		}

		catch (Exception e) {
			printStatus("The server disconnected");
			e.printStackTrace();
		}
		
		this.done();
	}

	
	private void drawTile() throws Exception{
		String encodedTile;
		
		outbox.println("draw");
		encodedTile = inbox.readLine();
		
		hand.addTile(new Tile(encodedTile));
		hand.sortByColour();
		printStatus("My new hand is: " + hand.toString());
	}
	
	private void playMeld(ArrayList<Set> melds) throws Exception{
		
		
		for(int i=0; i<melds.size(); i++){
			printStatus("Play: " + melds.get(i).toString());
			// FIXME - this no longer works!
//			hand.removeTiles(play.get(i));
		}
		outbox.println(game.toString());
	}
	
	/**
	 * Closes the sockets and the streams before exiting the thread
	 */
	protected void done() {
		try {
			inbox.close();
			outbox.close();
			mySocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			printStatus("Disconnected from the server");
		}
	}
	
	
	public static void main(String[] args){
		(new Client()).start();
	}
}
