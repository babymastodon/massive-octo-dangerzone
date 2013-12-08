package client;

import java.util.*;
import java.io.*;

import common.*;
import static common.SocketState.*;

/**
 * Wraps around a socket, converting client events (function calls) into
 * ASCII and sends them through a socket.
 * Provides methods for registering callback functions for handling
 * responses that arrive from the server.
 * It keeps track of the system state and asserts that messages
 * are only sent or received in the right  states.
 * If a socket throws an error while writing to the stream, calls ServerMessageListener.serverClose()
 * Call clientClose() if error while reading from the stream.
 *
 * Thread safety: 
 * The public interface is synchronized.
 * Code in the SocketWrapperListener (which gets run on a single, separate thread)
 *      locks the ServerSocketHandler object before changing the state.
 *      (The ServerMessageListener isn't locked, because it is never touched
 *      by the public interface)
 */
public class ClientSocketHandler implements ClientMessageListener{

    // Assert that functions are called in the proper states.
    // Only affects DEBUG mode, so thread-safety is a non-issue.
    public boolean disableStateAssertions = false;

    private final SocketWrapper socketWrapper;
    private ServerMessageListener listener;

    private SocketState state;
    private boolean clientInterfaceOpen;

    /**
     * Creates a handler that delegates to the given SocketWrapper.
     *
     * Automatically sets this as the SocketWrapper's listener.
     *
     * @param s: the socketWrapper to be used.
     */
    public ClientSocketHandler(SocketWrapper s){
        this.socketWrapper = s;
        this.clientInterfaceOpen = true;
        this.state = NOT_LOGGED_IN;

        // Attach a listener to the socket wrapper events
        this.socketWrapper.setSocketWrapperListener(new SocketWrapperListener(){
            public void onReadLine(String line){
                _onReadLine(line);
            }
            public void onReadError(IOException e){
                // do nothing, since onReadFinish will get called
            }
            public void onReadFinish(){
                _serverClose();
            }
            public void onWriteError(IOException e){
                clientClose();
            }
        });
    }

    /**
     * Use the given listener object handle incoming messages from the server.
     * Listener should not already be set.
     *
     * @param l: the listener for the messages
     */
    public synchronized void setServerMessageListener(ServerMessageListener l){
        assert this.listener == null;
        this.listener = l;
    }

    @Override
    public synchronized void login(String username) {
        if (clientInterfaceOpen){
            assert listener != null;
            assert state == NOT_LOGGED_IN || disableStateAssertions;

            state = LOGIN_PENDING;
            socketWrapper.writeLine("l " + username);
        }
    }

    @Override
    public synchronized void connectToBoard(int id) {
        if (clientInterfaceOpen){
            assert listener != null;
            assert state == NOT_CONNECTED || disableStateAssertions;

            state = CONNECT_PENDING;
            socketWrapper.writeLine("c " + id);
        }
    }

    @Override
    public synchronized void newBoard() {
        if (clientInterfaceOpen){
            assert listener != null;
            assert state == NOT_CONNECTED || disableStateAssertions;

            state = CONNECT_PENDING;
            socketWrapper.writeLine("n");
        }
    }

    @Override
    public synchronized void disconnectFromBoard() {
        if (clientInterfaceOpen){
            assert listener != null;
            assert state == CONNECTED || disableStateAssertions;

            state = DISCONNECT_PENDING;
            socketWrapper.writeLine("d");
        }
    }

    @Override
    public synchronized void drawLine(Point p1, Point p2, Color color, int width) {
        if (clientInterfaceOpen){
            assert listener != null;
            assert state == CONNECTED || disableStateAssertions;

            // state will remain CONNECTED
            StringBuilder b = new StringBuilder();
            b.append("dr ");
            b.append(p1.getX()).append(" ");
            b.append(p1.getY()).append(" ");
            b.append(p2.getX()).append(" ");
            b.append(p2.getY()).append(" ");
            b.append(color.getRed()).append(" ");
            b.append(color.getGreen()).append(" ");
            b.append(color.getBlue()).append(" ");
            b.append(width);
            socketWrapper.writeLine(b.toString());
        }
    }

    @Override
    public synchronized void clientClose() {
        if (clientInterfaceOpen){
            assert listener != null;

            this.clientInterfaceOpen = false;
            socketWrapper.close();
        }
    }

    /**
     * Given the input string message, parse the message and execute
     * it upon the ServerMessageListener.
     */
    private void _onReadLine(String line){
        String[] tokens = line.split(" ");
        try{
            // NOTE: the state assertions are not thread-safe
            // but they get disabled in production anyway.
            switch(tokens[0]){
                case "ls":
                    // update-pixel
                    assert state == LOGIN_PENDING || disableStateAssertions;
                    assert tokens.length == 1;
                    _changeState(NOT_CONNECTED);
                    listener.loginSuccess();
                    break;
                case "p":
                    // update-pixel
                    assert state == CONNECTED || state == DISCONNECT_PENDING || disableStateAssertions;
                    assert tokens.length == 6;
                    Point p = new Point(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
                    Color c = new Color(Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]), Integer.parseInt(tokens[5]));
                    listener.updatePixel(p, c);
                    break;
                case "u":
                    // update-users
                    assert state == CONNECTED || state == DISCONNECT_PENDING || disableStateAssertions;
                    assert tokens.length == 2;
                    listener.updateUsers(Arrays.asList(tokens[1].split(",")));
                    break;
                case "e":
                    // error
                    assert state == LOGIN_PENDING || state == CONNECT_PENDING || disableStateAssertions;
                    assert tokens.length == 2;
                    int code = Integer.parseInt(tokens[1]);
                    switch(code){
                        case 100:
                            _changeState(NOT_LOGGED_IN);
                            break;
                        case 200:
                            _changeState(NOT_CONNECTED);
                            break;
                        default:
                            throw new RuntimeException("Unrecognized error code: " + code);
                    }
                    listener.error(code);
                    break;
                case "ds":
                    // disconnect-from-board-success
                    assert state == DISCONNECT_PENDING || disableStateAssertions;
                    assert tokens.length == 1;
                    _changeState(NOT_CONNECTED);
                    listener.disconnectFromBoardSuccess();
                    break;
                case "cs":
                    // connect-to-board-success
                    assert state == CONNECT_PENDING || disableStateAssertions;
                    assert tokens.length == 4;
                    int id = Integer.parseInt(tokens[1]);
                    List<String> usernames = Arrays.asList(tokens[2].split(","));
                    Whiteboard board = _parseWhiteboard(tokens[3]);
                    _changeState(CONNECTED);
                    listener.connectToBoardSuccess(id, usernames, board);
                    break;
                default:
                    throw new RuntimeException("Unrecognized message: " + line);
            }
        } catch (Exception e){
            // Print the error, because there is a bug
            // if the execution ends up here
            e.printStackTrace();
            _serverClose();
        }
    }


    /**
     * Change the state of the ClientSocketHandler in a thread-safe manner.
     */
    private synchronized void _changeState(SocketState newState){
        state = newState;
    }

    /**
     * Send the "close" message to the ServerMessageListener.
     *
     * This gets executed whenever there is an error reading a
     * message from the socket, or whenever the message is malformed.
     * Should only be executed from the SocketWrapperListener thread.
     */
    private void _serverClose(){
        assert listener != null;
        listener.serverClose();
    }

    private Whiteboard _parseWhiteboard(String data){
        Whiteboard board = new Whiteboard();

        for (int y=0; y<Whiteboard.HEIGHT; y++){
            for (int x=0; x<Whiteboard.WIDTH; x++){
                int baseIndex = (y*Whiteboard.WIDTH + x)*3*2;
                int red = hexToByte(data.charAt(baseIndex), data.charAt(baseIndex+1));
                int green = hexToByte(data.charAt(baseIndex+2), data.charAt(baseIndex+3));
                int blue = hexToByte(data.charAt(baseIndex+4), data.charAt(baseIndex+5));
                board.setPixel(new Point(x,y), new Color(red, green, blue));
            }
        }

        return board;
    }

    /**
     * Convert a two-byte hex string (msb, lsb) into
     * an integer.
     */
    private int hexToByte(char msb, char lsb){
        return (hexToNibble(msb) << 4) + hexToNibble(lsb);
    }

    /**
     * Convert a one-byte hex character into an integer.
     */
    private int hexToNibble(char hexChar){
        if (hexChar >= '0' && hexChar <= '9'){
            return hexChar - '0';
        }
        if (hexChar >= 'a' && hexChar <= 'f'){
            return hexChar - 'a' + 10;
        }
        if (hexChar >= 'A' && hexChar <= 'F'){
            return hexChar - 'A' + 10;
        }
        throw new RuntimeException("Invalid Hex Character: " + hexChar);
    }
}
