package tests.server;

import org.junit.*;

import static org.mockito.Mockito.*;

import java.util.*;

import common.*;
import server.*;

public class SessionHandlerTest {

    /**
     * SessionHandler integration tests
     *
     * The AuthenticationBackend, WhiteboardMap, and
     * Whiteboard objects mocked out. The other objects
     * are not mocked.
     */

    // ServerMessageListener + related mocks
    private AuthenticationBackend auth;
    private WhiteboardMap boards;
    private SessionHandler session;
    private ServerMessageListener sessionListener;

    // mock WhiteboardStruct for use by tests
    private WhiteboardStruct struct;
    private Whiteboard board;
    private List<String> users;
    private ServerMessageListener boardListener;
    private List<ServerMessageListener> boardListeners;
    private int id;


    /**
     * Set up the mocks and create the SessionHandler object
     */
    @Before
    public void setUp(){
        auth = mock(AuthenticationBackend.class);
        boards = mock(WhiteboardMap.class);
        sessionListener = mock(ServerMessageListener.class);

        session = new SessionHandler(auth, boards);
        session.setServerMessageListener(sessionListener);

        // Setup the mock WhiteboardStruct
        board = mock(Whiteboard.class);
        boardListener = mock(ServerMessageListener.class);
        id = 123;

        users = new ArrayList<String>();
        boardListeners = new ArrayList<ServerMessageListener>();
        boardListeners.add(boardListener);
        struct = new WhiteboardStruct(board, users, boardListeners, id);
    }

    /**
     * Test successful login.
     */
    @Test
    public void testLoginSuccess(){
        when(auth.login("fred")).thenReturn(true);
        session.login("fred");
        verify(sessionListener).loginSuccess();
    }

    /**
     * Test failed login.
     */
    @Test
    public void testLoginFailure(){
        when(auth.login("fred")).thenReturn(false);
        session.login("fred");
        verify(sessionListener).error(100);
    }

    /**
     * Test login and successful connection to board
     */
    @Test
    public void testConnectToBoardSuccess(){
        // login
        when(auth.login("fred")).thenReturn(true);
        session.login("fred");

        // connect to board
        when(boards.getBoard(14)).thenReturn(struct);
        session.connectToBoard(14);
        verify(sessionListener).connectToBoardSuccess(id, users, board);
        verify(boardListener).updateUsers(Arrays.asList("fred"));
    }

    /**
     * Test login and failed connection to non-existant board
     */
    @Test
    public void testConnectToBoardFailure(){
        // login
        when(auth.login("fred")).thenReturn(true);
        session.login("fred");

        // connect to non-existant board
        when(boards.getBoard(36)).thenReturn(null);
        session.connectToBoard(36);
        verify(sessionListener).error(200);
    }

    /**
     * Test login and creation of new board
     */
    @Test
    public void testNewBoard(){
        // login
        when(auth.login("fred")).thenReturn(true);
        session.login("fred");

        // create new board
        when(boards.newBoard()).thenReturn(struct);
        session.newBoard();
        verify(sessionListener).connectToBoardSuccess(id, users, board);
        verify(boardListener).updateUsers(Arrays.asList("fred"));
    }

    /**
     * Test login, connect, and disconnect
     */
    @Test
    public void testDisconnectFromBoard(){
        // login
        when(auth.login("fred")).thenReturn(true);
        session.login("fred");

        // connect to board
        when(boards.getBoard(14)).thenReturn(struct);
        session.connectToBoard(14);

        // disconnect from board
        reset(boardListener);
        session.disconnectFromBoard();
        verify(sessionListener).disconnectFromBoardSuccess();
        verify(boardListener).updateUsers(new ArrayList<String>());
    }

    /**
     * Test login, connect, and draw
     */
    @Test
    public void testDrawLine(){
        // login
        when(auth.login("fred")).thenReturn(true);
        session.login("fred");

        // connect to board
        when(boards.getBoard(14)).thenReturn(struct);
        session.connectToBoard(14);

        // draw a line
        Point p = new Point(1,1);
        Color c = new Color(1,2,3);
        when(board.drawLine(p,p,c,1)).thenReturn(Arrays.asList(p));
        when(board.getPixel(p)).thenReturn(c);

        session.drawLine(p, p, c);
        verify(sessionListener).updatePixel(p,c);
        verify(boardListener).updatePixel(p,c);
    }

    /**
     * Test login, connect, and close
     */
    @Test
    public void testClientClose(){
        // login
        when(auth.login("fred")).thenReturn(true);
        session.login("fred");

        // connect to board
        when(boards.getBoard(14)).thenReturn(struct);
        session.connectToBoard(14);

        // close
        reset(boardListener);
        session.clientClose();
        verify(sessionListener).serverClose();
        verify(auth).logout("fred");
        verify(boardListener).updateUsers(new ArrayList<String>());
    }
}
