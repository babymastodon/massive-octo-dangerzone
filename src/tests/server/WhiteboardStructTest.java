package tests.server;

import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.List;

import common.*;
import server.*;

/**
 * Test suite for WhiteboardStruct.java. A WhiteboardStruct keeps track of a
 * whiteboard, the users connected to the whiteboard, the listeners for the
 * whiteboard, and the integer id of the whiteboard.
 * 
 * Since the class is very simple, there's only one test.
 *
 * Testing strategy:
 * User Mockito to simulate some of the more complex objects.
 * Create a new WhiteboardStruct.
 * Make sure all the observer methods return the right objects.
 */
public class WhiteboardStructTest {
    /**
     * Test construction, getWhiteboard, getUsers,
     * getID and getListeners.
     */
    @SuppressWarnings("unchecked") //for mockito warnings
    @Test
    public void whiteboardStructBasic(){
        Whiteboard wb = mock(Whiteboard.class);
        List<String> users = mock(List.class);
        List<ServerMessageListener> listeners = mock(List.class);
        int id = 5;

        WhiteboardStruct ws = new WhiteboardStruct(wb, users, listeners, id);

        assertEquals(wb, ws.getWhiteboard());
        assertEquals(users, ws.getUsers());
        assertEquals(listeners, ws.getListeners());
        assertEquals(id, ws.getID());
    }

}
