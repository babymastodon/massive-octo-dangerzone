package server;

import common.*;
import java.util.*;

/**
 * Keeps maps from id's to boards with listeners.
 * Is thread-safe: uses the monitor pattern by making all the methods
 * synchronized.
 */
public class WhiteboardMap {
    private ArrayList<WhiteboardStruct> structs;

    /**
     * create empty collection
     */
    public WhiteboardMap(){
        structs = new ArrayList<WhiteboardStruct>();
    }

    /**
     * create a new WhiteboardStruct, add it to the map, and return it.
     * @return the newly added WhiteboardStruct
     */
    public synchronized WhiteboardStruct newBoard(){
        // the id of the struct is its position in the ArrayList
        int id = structs.size();
        Whiteboard board = new Whiteboard();
        List<ServerMessageListener> listeners = new ArrayList<ServerMessageListener>();

        WhiteboardStruct s = new WhiteboardStruct(board, listeners, id);
        structs.add(s);
        return s;
    }

    /**
     * returns the WhiteboardStruct associated with the given id.
     * If there is no entry with that id, return null
     * @param id: the id of the whiteboardStruct we want
     * @return the whiteboardStruct associated with id, or null
     */
    public synchronized WhiteboardStruct getBoard(int id){
        if (id >= structs.size() || id < 0){
            return null;
        } else {
            return structs.get(id);
        }
    }
}
