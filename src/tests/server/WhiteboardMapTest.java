package tests.server;

import org.junit.Test;

import static org.junit.Assert.*;

import server.*;

/**
 * Test suite for WhiteboardMap.java. It should maintain a mapping between
 * boards and integer ids for the boards.
 * 
 * Testing strategy:
 * Construct a WhiteboardMap
 * Add boards to a whiteboardMap
 * Get legal boards from a whiteboardmap.
 * Try to get boards from a whiteboardmap with the wrong id.
 *
 */
public class WhiteboardMapTest {

    /**
     * Test construction, newBoard, and getBoard
     */
    @Test
    public void testBasic(){
        WhiteboardMap w = new WhiteboardMap();
        WhiteboardStruct s1 = w.newBoard();
        WhiteboardStruct s2 = w.getBoard(s1.getID()); 
        assertEquals(s1, s2);
        w.newBoard(); //test that WhiteboardMap can contain > 1 boards
    }

    /**
     * getBoard() should return null if ID doesn't exist
     */
    @Test
    public void testGetBoardNull(){
        WhiteboardMap w = new WhiteboardMap();
        assertEquals(null, w.getBoard(-1));
        assertEquals(null, w.getBoard(0));
        w.newBoard();
        assertEquals(false, w.getBoard(0) == null); //demonstrates the change of state in w
    }
}
