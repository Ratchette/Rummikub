package rummikub;

import java.util.ArrayList;

public class Rummikub {
	public static void main(String[] args) {		
//		Server.main(args);
//		Client.main(null);
//		Client.main(null);
		
//							   [x11]
//				Related Sets : [ b11 o11 r11 ]
//				Related Sets : [ b10 b11 b12 b13 ]
		
//		Set testing;
//		ArrayList<Set> groups;
//		
//		try {
//			testing = new Set("[ x11 b11 o11 r11 b10 b11 b12 b13 ]");
//			groups = testing.getGroups();
//			for(Set set : groups)
//				System.out.println(set);
//			
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		HintHarness player;
		boolean initial = false;
		
		if(args[0].equalsIgnoreCase("true"))
			initial = true;
		else if(args[0].equalsIgnoreCase("false"))
			initial = false;
		else{
			System.out.println("Invalid input. The first arguemnt must be if you have made your initial meld yet");
			System.exit(1);
		}
		
		
		try {
			player = new HintHarness(initial);
			player.run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
