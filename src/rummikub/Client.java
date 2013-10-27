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
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/**
 * A class that models a Rummikub client. It is driven by the GUI
 * 
 * @author jwiner
 */
public class Client extends SwingWorker<Void, String> {
	private Socket mySocket;
	private BufferedReader inbox;
	private PrintWriter outbox;

	private int playerNum;
	private Gui display;

	// **********************************************************
	// 					Initialization Methods
	// **********************************************************

	/**
	 * Initializes the private variables and tries to connect to the server at
	 * address ip
	 * 
	 * @param myGui
	 *            The Gui that displays all information relayed by this client
	 * @param serverIP
	 *            the IP address of the server
	 */
	public Client(Gui myGui, String serverIP) {
		mySocket = null;
		inbox = null;
		outbox = null;

		display = myGui;
		playerNum = -1;

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
			String errorMessage = "Could not connect to the server at "
					+ serverIP;
			printStatus(errorMessage);
			JOptionPane.showMessageDialog(display, errorMessage
					+ "\nPlease ensure that you have started "
					+ "the server before starting a client",
					"Opponent Not Found", JOptionPane.ERROR_MESSAGE);
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
	 * Update the Gui according to the message received
	 * 
	 * @param message
	 *            the message
	 */
	private void decodeMessage(String message) {
		try {
			printStatus("Updating GUI with message: " + message);
			display.updateGui(message);
		} catch (Exception e) {
			printStatus("Failed to update the GUI");
		}
	}

	/**
	 * Prints the current status of the server This function should be called at
	 * least once from every other function in the server
	 * 
	 * @param requester
	 *            Where (the server or which client) the message originated from
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
	
	/**
	 * The "main" function of the client thread. It communicates with the server
	 * on the Gui's behalf.
	 */
	@Override
	protected Void doInBackground() {
		String message;

		try {
			while (true) {
				message = inbox.readLine();
				printStatus("Recieved message: " + message);
				publish(message);
			}
		}

		catch (Exception e) {
			printStatus("The server disconnected");
		}

		return null;
	}

	/**
	 * Update the Gui based on the input that was just received
	 * 
	 * @param messages
	 *            A list of messages to process
	 */
	@Override
	protected void process(java.util.List<String> messages) {
		String message;

		message = messages.remove(0);
	}

	/**
	 * Closes the sockets and the streams before exiting the thread
	 */
	@Override
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
}
