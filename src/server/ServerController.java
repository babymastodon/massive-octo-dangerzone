package server;

import java.net.ServerSocket;

/**
 * Listens for incoming client connections. For each new client
 * connection, it constructs a new ServerSocketHandler, a new SessionController,
 * and connects them together by adding them to each other as listeners.
 * 
 * This is NOT thread-safe.
 */
public class ServerController {
	/**
	 * Construct a ServerController that listens for new connections on
	 * the given server socket.
	 * @param s: the socket that will connect the server parts.
	 */
	public ServerController(ServerSocket s){
		
	}
	
	/**
	 * Execute the main loop.
	 */
	public void run(){
		
	}
}