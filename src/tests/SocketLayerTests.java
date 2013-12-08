package tests;

import org.junit.*;

import org.mockito.verification.VerificationMode;
import static org.mockito.Mockito.*;
import java.net.*;
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

    // Port for TCP communication
    public static final int PORT = 1425;

    // mockito verify delay, wait for up to 2 seconds
    public static final VerificationMode delay = timeout(2000);

    
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
        ClientSocketHandler.disableStateAssertions = true;
        ServerSocketHandler.disableStateAssertions = true;

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
    }

    /**
     * Test loginSuccess message
     */
    @Test
    public void testLoginSuccess() {
        sh.loginSuccess();
        verify(sl, delay).loginSuccess();
    }


    /**
     * Test error message
     */
    @Test
    public void testError() {
        sh.error(100);
        verify(sl, delay).error(100);

        sh.error(200);
        verify(sl, delay).error(200);
    }


    /**
     * Test ConnectToBoardSuccess message
     */
    @Test
    public void testConnectToBoardSuccess() {
        int id = 123;
        List<String> users = Arrays.asList("fred", "bob");
        Whiteboard board = new Whiteboard();
        board.setPixel(new Point(0,1), new Color(0,255,255));

        sh.connectToBoardSuccess(id, users, board);
        verify(sl, delay).connectToBoardSuccess(123, users, board);
    }


    /**
     * Test updatePixel message
     */
    @Test
    public void testUpdatePixel() {
        Point p = new Point(12,34);
        Color c = new Color(1,2,3);

        sh.updatePixel(p,c);
        verify(sl, delay).updatePixel(p,c);
    }


    /**
     * Test updateUsers message
     */
    @Test
    public void testUpdateUsers() {
        List<String> users = Arrays.asList("fred", "bob");

        sh.updateUsers(users);
        verify(sl, delay).updateUsers(users);
    }


    /**
     * Test disconnectFromBoardSuccess message
     */
    @Test
    public void testDisconnectFromBoardSuccess() {
        sh.disconnectFromBoardSuccess();
        verify(sl, delay).disconnectFromBoardSuccess();
    }


    /**
     * Test serverClose message
     *
     * Close message should additionally be propagated
     * to the ClientMessageListener.
     */
    @Test
    public void testServerClose() {
        sh.serverClose();
        verify(sl, delay).serverClose();
        verify(cl, delay).clientClose();
    }


    /**
     * Test login message
     */
    @Test
    public void testLogin() {
        ch.login("fred");
        verify(cl, delay).login("fred");
    }


    /**
     * Test connectToBoard message
     */
    @Test
    public void testConnectToBoard() {
        ch.connectToBoard(123);
        verify(cl, delay).connectToBoard(123);
    }


    /**
     * Test newBoard message
     */
    @Test
    public void testNewBoard() {
        ch.newBoard();
        verify(cl, delay).newBoard();
    }


    /**
     * Test disconnectFromBoard message
     */
    @Test
    public void testDisconnectFromBoard() {
        ch.disconnectFromBoard();
        verify(cl, delay).disconnectFromBoard();
    }


    /**
     * Test drawLine message
     */
    @Test
    public void testDrawLine() {
        Point p1 = new Point(12,34);
        Point p2 = new Point(56,78);
        Color c = new Color(1,2,34);
        int w = 123;
        ch.drawLine(p1,p2,c,w);
        verify(cl, delay).drawLine(p1,p2,c,w);
    }

    /**
     * Test clientClose message
     *
     * Close message should additionally be propagated
     * to the ServerMessageListener.
     */
    @Test
    public void testClientClose() {
        ch.clientClose();
        verify(cl, delay).clientClose();
        verify(sl, delay).serverClose();
    }
}
