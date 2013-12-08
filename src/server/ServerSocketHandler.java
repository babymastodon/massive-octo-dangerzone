package server;

import java.io.*;
import java.util.*;

import common.*;
import static common.SocketState.*;

/**
 * Exactly like ClientSocketHandler, except 'client' and 'server' are switched
 * Specifically, 
 * 
 * 
 * Wraps around a socket, converting server events (function calls) into
 * ASCII and sends them through a socket.
 * Provides methods for registering callback functions for handling
 * responses that arrive from the client.
 * It keeps track of the system state and asserts that messages
 * are only sent or received in the right  states.
 * If a socket throws an error while writing, calls ServerMessageListener.serverClose()
 * Call listener.clientClose() if error while reading.
 *
 * Thread safety: 
 * The public interface is synchronized.
 * Code in the SocketWrapperListener (which gets run on a single, separate thread)
 *      locks the ServerSocketHandler object before changing the state.
 *      (The ClientMessageListener isn't locked, because it is never touched
 *      by the public interface)
 */
public class ServerSocketHandler implements ServerMessageListener{

    // Assert that functions are called in the proper states.
    // Only affects DEBUG mode, so thread-safety is a non-issue.
    public boolean disableStateAssertions = false;

    private final SocketWrapper socketWrapper;
    private ClientMessageListener listener;

    private SocketState state;
    private boolean serverInterfaceOpen;

    /**
     * Creates a handler that delegates to the given SocketWrapper.
     *
     * Automatically sets this as the SocketWrapper's listener.
     *
     * @param s: the socketWrapper to be used.
     */
    public ServerSocketHandler(SocketWrapper s){
        this.socketWrapper = s;
        this.serverInterfaceOpen = true;
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
                _clientClose();
            }
            public void onWriteError(IOException e){
                serverClose();
            }
        });
    }

    /**
     * Use the given listener object to handle incoming messages from the client.
     * Listener should not already be set.
     *
     * @param l: the listener used to handle messages
     */
    public synchronized void setClientMessageListener(ClientMessageListener l){
        assert this.listener == null;
        this.listener = l;
    }

    @Override
    public synchronized void loginSuccess() {
        if (serverInterfaceOpen){
            assert listener != null;
            assert state == LOGIN_PENDING || disableStateAssertions;

            state = NOT_CONNECTED;
            socketWrapper.writeLine("ls");
        }
    }

    @Override
    public synchronized void error(int code) {
        if (serverInterfaceOpen){
            assert listener != null;

            switch(code){
                case 100:
                    state = NOT_LOGGED_IN;
                    break;
                case 200:
                    state = NOT_CONNECTED;
                    break;
                default:
                    throw new RuntimeException("Unknown error code: " + code);
            }
            socketWrapper.writeLine("e " + code);
        }
    }

    @Override
    public synchronized void connectToBoardSuccess(int id, List<String> users, Whiteboard data) {
        if (serverInterfaceOpen){
            assert listener != null;
            assert state == CONNECT_PENDING || disableStateAssertions;

            state = CONNECTED;
            StringBuilder b = new StringBuilder();
            b.append("cs ");
            b.append(id).append(" ");

            // Users data joined with commas
            for (int i=0; i<users.size(); i++){
                b.append(users.get(i));
                if (i != users.size()-1){
                    b.append(",");
                }
            }
            b.append(" ");

            // Board data joined with commas
            for (int y=0; y<Whiteboard.HEIGHT; y++){
                for (int x=0; x<Whiteboard.WIDTH; x++){
                    Color c = data.getPixel(new Point(x, y));
                    b.append(byteToHex(c.getRed()));
                    b.append(byteToHex(c.getGreen()));
                    b.append(byteToHex(c.getBlue()));
                }
            }
            socketWrapper.writeLine(b.toString());
        }
    }

    @Override
    public synchronized void updatePixel(Point point, Color color) {
        if (serverInterfaceOpen){
            assert listener != null;
            assert state == CONNECTED || state == DISCONNECT_PENDING || disableStateAssertions;

            StringBuilder b = new StringBuilder();
            b.append("p ");
            b.append(point.getX()).append(" ");
            b.append(point.getY()).append(" ");
            b.append(color.getRed()).append(" ");
            b.append(color.getGreen()).append(" ");
            b.append(color.getBlue());
            socketWrapper.writeLine(b.toString());
        }
    }

    @Override
    public synchronized void updateUsers(List<String> users) {
        if (serverInterfaceOpen){
            assert listener != null;
            assert state == CONNECTED || state == DISCONNECT_PENDING || disableStateAssertions;

            StringBuilder b = new StringBuilder();
            b.append("u ");
            for (int i=0; i<users.size(); i++){
                b.append(users.get(i));
                if (i != users.size()-1){
                    b.append(",");
                }
            }
            socketWrapper.writeLine(b.toString());
        }
    }

    @Override
    public synchronized void disconnectFromBoardSuccess() {
        if (serverInterfaceOpen){
            assert listener != null;
            assert state == DISCONNECT_PENDING || disableStateAssertions;

            state = NOT_CONNECTED;
            socketWrapper.writeLine("ds");
        }
    }

    @Override
    public synchronized void serverClose() {
        if (serverInterfaceOpen){
            assert listener != null;

            this.serverInterfaceOpen = false;
            socketWrapper.close();
        }
    }


    /**
     * Given the input string message, parse the message and execute
     * it upon the ClientMessageListener.
     */
    private void _onReadLine(String line){
        String[] tokens = line.split(" ");
        try{
            // NOTE: the state assertions are not thread-safe
            // but they get disabled in production anyway.
            switch(tokens[0]){
                case "dr":
                    // draw-line
                    assert state == CONNECTED || disableStateAssertions;
                    assert tokens.length == 9;
                    Point p1 = new Point(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
                    Point p2 = new Point(Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]));
                    Color c = new Color(Integer.parseInt(tokens[5]), Integer.parseInt(tokens[6]), Integer.parseInt(tokens[7]));
                    int w = Integer.parseInt(tokens[8]);
                    listener.drawLine(p1, p2, c, w);
                    break;
                case "l":
                    // login
                    assert state == NOT_LOGGED_IN || disableStateAssertions;
                    assert tokens.length == 2;
                    _changeState(LOGIN_PENDING);
                    listener.login(tokens[1]);
                    break;
                case "c":
                    // connect-to-board
                    assert state == NOT_CONNECTED || disableStateAssertions;
                    assert tokens.length == 2;
                    _changeState(CONNECT_PENDING);
                    listener.connectToBoard(Integer.parseInt(tokens[1]));
                    break;
                case "d":
                    // disconnect-from-board
                    assert state == CONNECTED || disableStateAssertions;
                    assert tokens.length == 1;
                    _changeState(DISCONNECT_PENDING);
                    listener.disconnectFromBoard();
                    break;
                case "n":
                    // new-board
                    assert state == NOT_CONNECTED || disableStateAssertions;
                    assert tokens.length == 1;
                    _changeState(CONNECT_PENDING);
                    listener.newBoard();
                    break;
                default:
                    throw new RuntimeException("Unrecognized message: " + line);
            }
        } catch (Exception e){
            // Print the error, because there is a bug
            // if the execution ends up here
            e.printStackTrace();
            _clientClose();
        }
    }

    /**
     * Change the state of the ServerSocketHandler in a thread-safe manner.
     */
    private synchronized void _changeState(SocketState newState){
        state = newState;
    }

    /**
     * Send the "close" message to the ClientMessageListener.
     *
     * This gets executed whenever there is an error reading a
     * message from the socket, or whenever the message is malformed.
     * Should only be executed from the SocketWrapperListener thread.
     */
    private void _clientClose(){
        assert listener != null;
        listener.clientClose();
    }


    /**
     * Convert an integer from 0-255 into a 2-byte hex string
     */
    private String byteToHex(int num){
        assert num >= 0;
        assert num <= 255;
        char msb = nibbleToHex((num & 0xf0) >> 4);
        char lsb = nibbleToHex(num & 0xf);
        return new String(new char[]{msb, lsb});
    }

    /**
     * Convert an integer from 0-16 into a 1-byte character.
     */
    private char nibbleToHex(int nibble){
        if (nibble <= 9){
            return (char)(nibble + '0');
        }
        if (nibble <= 15){
            return (char)(nibble - 10 + 'a');
        }
        throw new RuntimeException("Cannot convert to hex: " + nibble);
    }
}
