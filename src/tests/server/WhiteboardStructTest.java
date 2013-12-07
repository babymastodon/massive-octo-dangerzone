package tests.server;

import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.List;

import common.*;
import server.*;

public class WhiteboardStructTest {

    /**
     * WhiteboardStruct tests
     */

    /**
     * Test construction, getWhiteboard, getUsers,
     * getID and getListeners.
     */
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
