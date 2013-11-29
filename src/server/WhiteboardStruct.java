package server;

import java.util.List;

import common.ServerMessageListener;
import common.Whiteboard;

/**
 * An object containing a final Whiteboard object, a list of ServerMessageListeners
 * listening on the whiteboard, and the ID number of the whiteboard.
 * 
 * It is NOT thread-safe.
 */
public class WhiteboardStruct {
	private Whiteboard board;
	private List<ServerMessageListener> listeners;
	private int id;
	/**
	 * Contruct with the given variables
	 * @param board: the whiteboard that has all the drawing data
	 * @param listeners: list of listeners for the board
	 * @param id: the id number of the board.
	 */
	public WhiteboardStruct(Whiteboard board, List<ServerMessageListener> listeners, int id){
		this.board = board;
		this.listeners = listeners;
		this.id = id;
	}
	
	/**
	 * @return a reference to the *mutable* whiteboard
	 */
	public Whiteboard getWhiteboard(){
		return this.board;
	}
	
	/**
	 * @return a reference to the *mutable* list of listeners
	 */
	public List<ServerMessageListener> getListeners(){
		return this.listeners;
	}
	
	/**
	 * @return the integer ID
	 */
	public int getID(){
		return this.id;
	}
}