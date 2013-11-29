package server;

import java.awt.Point;

import common.ClientMessageListener;
import common.Color;
import common.ServerMessageListener;

/**
 * Listens for client events, and performs the appropriate server-side actions
 * when they occur. This includes logging in, joining/creating boards, drawing
 * lines, and cleaning up when a session closes.
 * 
 * There is one session controller for each client connection.
 * This is NOT thread-safe.
 * When clientClose() is called for the first time, unregister the listener
 * if one is registered, remove the current username from the Authentication
 * Backend, and call serverClose() on the ServerMessageListener.
 * None of the methods should throw exceptions. Instead, call clientClose() if
 * something bad happens.
 */
public class SessionHandler implements ClientMessageListener{

	/**
	 * Construct with the given parameters
	 * @param auth
	 * @param boards //check in about boards. The googledoc said the second parameter should be
	 * BoardCollection boards
	 */
	public SessionHandler(AuthenticationBackend auth, WhiteboardMap boards){
		
	}
	
	/**
	 * Use the given listener object to send messages to the client.
	 * @param l: the listener object
	 */
	public void setServerMessageListener(ServerMessageListener l){
		
	}
	
	@Override
	public void login(String username) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void connectToBoard(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void newBoard() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disconnectFromBoard() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawLine(Point p1, Point p2, Color color) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clientClose() {
		// TODO Auto-generated method stub
		
	}
}