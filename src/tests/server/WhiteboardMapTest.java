package tests.server;

import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import common.*;
import server.*;

public class WhiteboardMapTest {

    /**
     * WhiteboardMap tests
     */

    /**
     * Test construction, newBoard, and getBoard
     */
    @Test
    public void testBasic(){
        WhiteboardMap w = new WhiteboardMap();
        WhiteboardStruct s1 = w.newBoard();
        WhiteboardStruct s2 = w.getBoard(s1.getID());
        assertEquals(s1, s2);
    }

    /**
     * getBoard() should return null if ID doesn't exist
     */
    @Test
    public void testGetBoardNull(){
        WhiteboardMap w = new WhiteboardMap();
        assertEquals(null, w.getBoard(-1));
    }
}
