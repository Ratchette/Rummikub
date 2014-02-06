/**
 * Description: CIS*4900 - A Rummikub framework
 * @author Jennfier Winer
 * 
 * @since 2013-10-24
 * Created: October 24, 2013
 * Last Modified: February 6, 2014
 */

package rummikub;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * An abstract class that handles all communication between the clients
 * and the servers
 * 
 * @author jwiner
 */

// TODO After creating a GUI, use the following class definition instead. 
// public abstract class Client extends SwingWorker<Void, String> {

public class Client{
	private Socket mySocket;
	private BufferedReader inbox;
	private PrintWriter outbox;

	// **********************************************************
	// 					Constructors
	// **********************************************************

	/**
	 * Initializes a client
	 */
	public Client() {
		mySocket = null;
		inbox = null;
		outbox = null;
	}

	// **********************************************************
	// 					Communication Methods
	// **********************************************************
	
	/**
	 * Connect to the server
	 * 
	 * @param serverIP
	 *            The IP address of the server
	 * @throws IOException 
	 * 			  Could not create the sockets
	 * @throws UnknownHostException
	 * 			  Unable to find host serverIP 
	 */
	public int connect(String serverIP, int port) throws UnknownHostException, IOException{
		mySocket = new Socket(serverIP, port);
		
		outbox = new PrintWriter(mySocket.getOutputStream(), true);
		inbox = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
		
		return Integer.parseInt(inbox.readLine());
	}
	
	/**
	 * Disconnect from the server
	 */
	public void disconnect(){
		try {
			inbox.close();
			outbox.close();
			mySocket.close();
		} catch (IOException e) {
			// The socket was already closed
			e.printStackTrace();
		}
	}	

	/**
	 * Receive a message from the server
	 * 
	 * @return The message from the server
	 */
	public String getMessage() throws IOException {
		return inbox.readLine();
	}
	
	/**
	 * Send a message to the server
	 * 
	 * @param message
	 *            the message that you wish to send
	 */
	public void sendMessage(String message) throws IOException{
		outbox.println(message);
	}
//	printStatus("The server disconnected me");
//	disconnect();
	
	/**
	 * Decipher the message and alert the user
	 * 
	 * @param message
	 *            the message received from the server
	 */
	public void decodeMessage(String message) {
		try {
			// update the GUI based on that message
		} catch (Exception e) {
			System.out.println("Failed to update the GUI [GUI NOT IMPLEMENTED YET ???]");
		}
	}
}
