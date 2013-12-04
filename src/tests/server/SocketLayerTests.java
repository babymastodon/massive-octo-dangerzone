package tests.server;

import org.junit.*;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.net.*;
import java.io.*;
import java.util.*;

import common.*;
import server.*;
import client.*;

public class SocketLayerTests {

    /**
     * Integration tests for ServerSocketHandler,
     * ClientSocketHandler, and SocketWrapper.
     *
     * Call a function on one handler, the response
     * should arrive on the other one.
     */
    public static final int PORT = 1425;

    
    private ServerSocket ss;
    private SocketWrapper ssw;
    private SocketWrapper csw;
    private ClientMessageListener cl;
    private ServerMessageListener sl;
    private ServerSocketHandler sh;
    private ClientSocketHandler ch;

    /**
     * Set up the mock connections and the socket.
     */
    @Before
    public void setUp() throws Exception{
        // Create the socket wrappers for client and server
        ss = new ServerSocket(PORT);
        ssw = new SocketWrapper(new Socket("localhost", PORT));
        csw = new SocketWrapper(ss.accept());

        // Create the socket handlers for client and server
        ch = new ClientSocketHandler(csw);
        sh = new ServerSocketHandler(ssw);

        // Create the mock MessageListeners for client and server
        cl = mock(ClientMessageListener.class);
        sl = mock(ServerMessageListener.class);
        sh.setClientMessageListener(cl);
        ch.setServerMessageListener(sl);

        // Start the socket threads
        ssw.start();
        csw.start();
    }

    /**
     * Close the socket.
     */
    @After
    public void tearDown() throws Exception {
        ssw.close();
        csw.close();
        ss.close();
        delay();
    }

    /**
     * Test loginSuccess message
     */
    @Test
    public void testLoginSuccess() {
        sh.loginSuccess();
        delay();
        verify(sl).loginSuccess();
    }


    /**
     * Test error message = "e <code>"
     */
    @Test
    public void testError() {
        sh.error(100);
        delay();
        verify(sl).error(100);

        sh.error(200);
        delay();
        verify(sl).error(200);
    }


    /**
     * Test ConnectToBoardSuccess = "cs <id> <users> <data>"
     */
    @Test
    public void testConnectToBoardSuccess() {
        int id = 123;
        List<String> users = Arrays.asList("fred", "bob");
        Whiteboard board = new Whiteboard();
        board.setPixel(new Point(0,1), new Color(0,255,255));

        sh.connectToBoardSuccess(id, users, board);
        delay();
        verify(sl).connectToBoardSuccess(id, users, board);
    }


    private void delay(){
        try{
            Thread.sleep(100);
        } catch (Exception e){
        }
    }
}
