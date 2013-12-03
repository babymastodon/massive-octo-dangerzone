package client;

import java.util.List;

import common.*;
//
/**
 * It is not thread-safe.
 * Stores the list of all usernames currently connected to the internal
 * Whiteboard object.
 * Stores the state of the board in previously mentioned Whiteboard object.
 * Refreshes the contents of the whiteboard onto the screen at regular
 * intervals using the graphics2d.drawImage(BufferedImage, null, 0, 0) function
 * 
 * When serverClose() is called, close the GUI if it isn't already closed.
 * When the GUI is closed, call clientClose() of the ClientMessageListener
 * Instead of throwing exceptions, call serverClose()
 */
public class ClientGUI implements ServerMessageListener{

    /**
     * Use the provided listener object to send messages to the server.
     * @param l: the listener used to send messages.
     */
    public void setClientMessageListener(ClientMessageListener l){

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
