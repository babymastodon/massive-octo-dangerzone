package server;

import java.awt.Point;

import common.*;
import static common.SocketState.*;

/**
 * Listens for client events, and performs the appropriate server-side actions
 * when they occur. This includes logging in, joining/creating boards, drawing
 * lines, and cleaning up when a session closes.
 *
 * No methods should be called until setServerMessageListener has been called
 * with a non-null argument.
 * 
 * There is one session controller for each client connection.
 * This is NOT thread-safe.
 * When clientClose() is called for the first time, unregister the listener in
 * the WhiteboardMap if connected to a whiteboard.
 * if one is registered, remove the current username from the Authentication
 * Backend, and call serverClose() on the ServerMessageListener.
 * None of the methods should throw exceptions. Instead, call clientClose() if
 * something bad happens.
 *
 * Rep invariant:
 *      - connectedBoardStruct is not null when in the CONNECTED or
 *        DISCONNECT_PENDING state
 *      - username is not null in any state except the NOT_LOGGED_IN state
 */
public class SessionHandler implements ClientMessageListener{
    private final AuthenticationBackend auth;
    private final WhiteboardMap boards;
    private String username;
    private ServerMessageListener listener;
    private WhiteboardStruct connectedBoardStruct;

    private SocketState state;
    private boolean clientInterfaceOpen;

    /**
     * Construct with the given parameters
     * @param auth AuthenticationBackend for managing logins
     * @param boards WhiteboardMap which stores all of the boards
     */
    public SessionHandler(AuthenticationBackend auth, WhiteboardMap boards){
        this.auth = auth;
        this.boards = boards;
        this.listener = null;
        this.connectedBoardStruct = null;
        this.state = NOT_LOGGED_IN;
        this.username = null;
        this.clientInterfaceOpen = true;
    }

    /**
     * Use the given listener object to send messages to the client.
     * @param listener: the listener object
     */
    public void setServerMessageListener(ServerMessageListener listener){
        this.listener = listener;
    }

    /**
     * @see ClientMessageListener
     */
    @Override
    public void login(String username) {
        if (clientInterfaceOpen){
            assert listener != null;
            assert state == NOT_LOGGED_IN;

            this.username = username;

            if (auth.login(username)){
                // Successful login
                state = NOT_CONNECTED;
                listener.loginSuccess();
            } else {
                // Failed login
                // state will remain == NOT_LOGGED_IN
                listener.error(100);
            }
        }
    }

    /**
     * @see ClientMessageListener
     */
    @Override
    public void connectToBoard(int id) {
        if (clientInterfaceOpen){
            assert listener != null;
            assert state == NOT_CONNECTED;

            connectedBoardStruct = boards.getBoard(id);

            if (connectedBoardStruct == null){
                // Board does not exist
                listener.error(200);
            } else {
                // Board does exist
                _connectToBoard();
            }
        }
    }

    /**
     * @see ClientMessageListener
     */
    @Override
    public void newBoard() {
        if (clientInterfaceOpen){
            assert listener != null;
            assert state == NOT_CONNECTED;

            connectedBoardStruct = boards.newBoard();
            _connectToBoard();
        }
    }

    /**
     * @see ClientMessageListener
     */
    @Override
    public void disconnectFromBoard() {
        if (clientInterfaceOpen){
            assert listener != null;
            assert state == CONNECTED;

            _disconnectFromBoard();
        }
    }

    /**
     * @see ClientMessageListener
     */
    @Override
    public void drawLine(Point p1, Point p2, Color color) {
        if (clientInterfaceOpen){
            assert listener != null;
            assert state == CONNECTED;

            synchronized(connectedBoardStruct){
                // draw the line on the whiteboard
                Whiteboard board = connectedBoardStruct.getWhiteboard();
                Point[] changedPoints = board.drawLine(
                        p1, p2, color);

                // notify clients of new pixel values
                for (ServerMessageListener l: connectedBoardStruct.getListeners()){
                    for (Point p: changedPoints){
                        l.updatePixel(p, board.getPixel(p));
                    }
                }
            }
        }
    }

    /**
     * @see ClientMessageListener
     */
    @Override
    public void clientClose() {
        if (clientInterfaceOpen){
            assert listener != null;

            if (state == CONNECTED){
                // If currently connected to a board, disconnect from it
                _disconnectFromBoard();
            }

            if (state != NOT_LOGGED_IN){
                // If logged in, then log out
                assert username != null;
                auth.logout(username);
            }

            // notify the server listener to close
            listener.serverClose();

            // stop responding to all client messages
            clientInterfaceOpen = false;
        }
    }


    /**
     * Contains logic common to "connectToBoard" and "newBoard".
     */
    private void _connectToBoard(){
        assert connectedBoardStruct != null;
        assert listener != null;
        assert username != null;

        // change state
        state = CONNECTED;

        synchronized(connectedBoardStruct){
            // add a listener to the whiteboard object
            // to receive updates
            connectedBoardStruct.getListeners().add(listener);
            // add the username to the board
            connectedBoardStruct.getUsers().add(username);

            _notifyUsernamesChanged();

            // the response to the client must occur within the synchronized block
            // to ensure that this message arrives at the client before
            // any updatePixel or updateUsers messages
            listener.connectToBoardSuccess(
                    connectedBoardStruct.getID(),
                    connectedBoardStruct.getUsers(),
                    connectedBoardStruct.getWhiteboard());
        }
    }

    /**
     * Contains logic common to "disconnectFromBoard" and "close".
     */
    private void _disconnectFromBoard(){
        assert state == CONNECTED;
        assert listener != null;
        assert username != null;

        // change state
        state = NOT_CONNECTED;

        synchronized(connectedBoardStruct){
            // remove the whiteboard listener
            connectedBoardStruct.getListeners().remove(listener);
            // remove the username
            connectedBoardStruct.getUsers().remove(username);

            _notifyUsernamesChanged();

            listener.disconnectFromBoardSuccess();
        }
    }

    /**
     * notify all other clients that the username list has changed
     */
    private void _notifyUsernamesChanged(){
        for (ServerMessageListener l: connectedBoardStruct.getListeners()){
            l.updateUsers(connectedBoardStruct.getUsers());
        }
    }

}
