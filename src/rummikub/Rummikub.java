package rummikub;

public class Rummikub {
	public static void main(String[] args) {
		int numPlayers;
		
		numPlayers = parseArguments(args);
		if(numPlayers == -1)
			return;
		
		(new Server(numPlayers)).start();
		Gui player1 = new Gui("localhost");
		player1.setVisible(true);
		
		Gui player2 = new Gui("localhost");
		player2.setVisible(true);
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
