package rummikub;

import java.util.ArrayList;

/**
 * This file runs a full game with 2 clients
 * @author jennifer
 *
 */
public class Rummikub {
	
	public static void main(String[] args) {
		String serverArgs[] = {"2"};
		String clientArgs[] = {Server.SERVER_IP, Server.PORT_NUM + ""};
		
		Server.main(serverArgs);
		RAIclient.main(clientArgs);
		RAIclient.main(clientArgs);
	}
}
