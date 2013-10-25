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
import java.net.Socket;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import Assignment1.Gui;
import Assignment1.Server;

/**
 * A class that models a Rummikub client.
 * 
 * @author jwiner
 */
public class Client {
	    private Socket mySocket;
	    private DataInputStream dataIn;
	    private DataOutputStream dataOut;
	    private String serverIP;
	    
	    private Gui display;
	    
	    /**
	     * Initializes the private variables and tries to connect to the server at address ip
	     * 
	     * @param myGui The Gui that displays all information relayed by this client
	     * @param ip = the IP address of the server
	     */
	    public Client(Gui myGui, String ip){
	        display = myGui;
	        serverIP = ip;
	        
	        try{
	            mySocket = new Socket(serverIP, Server.portNum);
	            dataIn = new DataInputStream(mySocket.getInputStream());
	            dataOut = new DataOutputStream(mySocket.getOutputStream()); 
	        }
	        
	        catch(Exception e){
	            String errorMessage = "Could not connect to the server at " + serverIP
	                    + "\nPlease ensure that you have started a new game before you try to join one!";
	            
	            JOptionPane.showMessageDialog(display, errorMessage, "Opponent Not Found",JOptionPane.ERROR_MESSAGE);
	            System.exit(0);
	        }
	    }
	    
	    
	    
	    /**
	     * The "main" function of the client thread. It communicates with the server
	     * on the Gui's behalf.
	     */
	    @Override
	    protected Void doInBackground(){
	        int message;
	        
	        try{
	            while(true){
	                message = dataIn.readInt();
	                publish(message);

	                if(message < 10)
	                    break;
	            }
	        }
	            
	        catch(Exception e){
	            // You have been disconnected from the server for some reason
	        }
	        
	        return null;
	    }
	    
	    /**
	     * Update the Gui based on the input that was just received 
	     * 
	     * @param messages A list of messages to process
	     */
	    @Override
	    protected void process(java.util.List<Integer> messages){
	        int message;
	        
	        message = messages.get(0);
	        messages.remove(0);
	        decodeMessage(message);
	    }
	    
	    /**
	     * Closes the sockets and the streams before exiting the thread
	     */
	    @Override
	    protected void done(){
	        try{
	            dataIn.close();
	            dataOut.close();
	            mySocket.close();
	        }
	        catch(Exception e){
	            e.printStackTrace();
	        }
	    }
	    
	    

	    /**
	     * Sends a message to the server
	     * 
	     * @param message the message that you wish to send
	     */
	    protected void sendMessage(int message){
	        try{
	            dataOut.writeInt(message);
	        }
	        catch(Exception e){
	            // You are no longer connected to the server
	            decodeMessage(-1);
	        }
	    }

	    /**
	     * Update the Gui according to the message received
	     * 
	     * @param message the message that the 
	     */
	    private void decodeMessage(int message){
	        try{
	            if (0 < message && message < 4){
	                int gameOver = message;
	                
	                message = dataIn.readInt();
	                display.updateGui(message);
	                display.updateGui(gameOver);
	            }
	            
	            else{
	                display.updateGui(message);
	            }
	        }
	        catch(Exception e){
	        }
	    }
	
	
	public static void main(String[] args) {
		System.out.println("Hello World");

	}

}
