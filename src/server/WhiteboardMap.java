package server;

/**
 * Keeps maps from id's to boards with listeners.
 * Is thread-safe: uses the monitor pattern by making all the methods
 * synchronized.
 */
public class WhiteboardMap {
    /**
     * create empty collection
     */
    public WhiteboardMap(){

    }

    /**
     * create a new WhiteboardStruct, add it to the map, and return it.
     * @return the newly added WhiteboardStruct
     */
    public WhiteboardStruct newBoard(){
        return null;
    }

    /**
     * returns the WhiteboardStruct associated with the given id.
     * If there is no entry with that id, return null
     * @param id: the id of the whiteboardStruct we want
     * @return the whiteboardStruct associated with id, or null
     */
    public WhiteboardStruct getBoard(int id){
        return null;
    }
}
