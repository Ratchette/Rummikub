package rummikub;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

public class Gui extends JFrame implements ActionListener{
	private Client myClient;
	private GameInfo myGame;
	
	// TESTING
	private Board myBoard;

	public Gui(String serverIP){
		myClient = new Client(this, serverIP);
		myClient.connect(serverIP);
		myClient.execute();
	}
	
	public void updateGui(String messgae){
		
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
	}
	
	// FIXME
	public void printBoard(){
		myBoard.printPool();
	}

}
