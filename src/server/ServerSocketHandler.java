package server;

import java.awt.Point;
import java.net.Socket;
import java.util.List;

import common.ClientMessageListener;
import common.Color;
import common.ServerMessageListener;
import common.Whiteboard;

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
 * If a socket throws an error, calls ServerMessageListener.serverClose()
 * Call clientClose() instead of throwing exceptions.
 * Thread safety: 
 * The public interface is synchronized, so functions block
 * as long as necessary to successfully write to the socket.
 * Arriving messages are parsed and callback functions are executed
 * in a background thread.
 * 
 */
public class ServerSocketHandler implements ServerMessageListener{

    /**
     * Creates a handler that wraps around the given socket.
     * Before being called here, the socket must be connected and open.
     * @param s: the socket that will be wrapped up.
     */
    public ServerSocketHandler(Socket s){

    }

    /**
     * Use the given listener object to handle incoming messages from the client
     * @param l: the listener used to handle messages
     */
    public void setClientMessageListener(ClientMessageListener l){

    }

    /**
     * Start the background listener thread;
     */
    public void start(){
    }

    @Override
    public void loginSuccess() {
        // TODO Auto-generated method stub

    }

    @Override
    public void error(int code) {
        // TODO Auto-generated method stub

    }

    @Override
    public void connectToBoardSuccess(int id, List<String> users,
            Whiteboard data) {
        // TODO Auto-generated method stub

    }

    @Override
    public void updatePixel(Point point, Color color) {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateUsers(List<String> users) {
        // TODO Auto-generated method stub

    }

    @Override
    public void disconnectFromBoardSuccess() {
        // TODO Auto-generated method stub

    }

    @Override
    public void serverClose() {
        // TODO Auto-generated method stub

    }
}
