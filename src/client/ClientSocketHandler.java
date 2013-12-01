package client;

import java.awt.Point;
import java.net.Socket;

import common.ClientMessageListener;
import common.Color;
import common.ServerMessageListener;

/**
 * Wraps around a socket, converting client events (function calls) into
 * ASCII and sends them through a socket.
 * Provides methods for registering callback functions for handling
 * responses that arrive from the server.
 * It keeps track of the system state and asserts that messages
 * are only sent or received in the right  states.
 * If a socket throws an error, calls ServerMessageListener.serverClose()
 * Call clientClose() instead of throwing exceptions.
 * Thread safety: 
 * The public interface is synchronized, so functions block
 * as long as necessary to successfully write to the socket.
 * Arriving messages are parsed and callback functions are executed
 * in a background thread.
 */
public class ClientSocketHandler implements ClientMessageListener{

    /**
     * Creates a handler that wraps around the given socket
     * @param s: the socket that is wrapped up.
     */
    public ClientSocketHandler(Socket s){

    }

    /**
     * Use the given listener object to send server messages to the
     * client
     * @param l: the server message listener used for the messages
     */
    public void setServerMessageListener(ServerMessageListener l){

    }

    @Override
    public void login(String username) {
        // TODO Auto-generated method stub

    }

    @Override
    public void connectToBoard(int id) {
        // TODO Auto-generated method stub

    }

    @Override
    public void newBoard() {
        // TODO Auto-generated method stub

    }

    @Override
    public void disconnectFromBoard() {
        // TODO Auto-generated method stub

    }

    @Override
    public void drawLine(Point p1, Point p2, Color color) {
        // TODO Auto-generated method stub

    }

    @Override
    public void clientClose() {
        // TODO Auto-generated method stub

    }
}
