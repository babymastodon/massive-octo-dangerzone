package server;

import java.util.List;

import common.ServerMessageListener;
import common.Whiteboard;

/**
 * An object containing a final Whiteboard object, a list of ServerMessageListeners
 * listening on the whiteboard, and the ID number of the whiteboard.
 * 
 *
 * Thread safety:
 *      It is NOT thread-safe. Clients should lock this object before accessing
 *      or modifying any of its attributes.
 *
 */
public class WhiteboardStruct {
    private Whiteboard board;
    private List<ServerMessageListener> listeners;
    private List<String> users;
    private int id;
    /**
     * Contruct with the given variables
     * @param board: the whiteboard that has all the drawing data
     * @param users: list of users connected to the board
     * @param listeners: list of listeners for the board
     * @param id: the id number of the board.
     */
    public WhiteboardStruct(Whiteboard board, List<String> users, List<ServerMessageListener> listeners, int id){
        this.board = board;
        this.users = users;
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
     * @return a reference to the *mutable* list of usernames
     */
    public List<String> getUsers(){
        return this.users;
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
