package common;

import java.util.List;

/**
 * Callback functions that should be executed for events starting
 * in the server.
 * None of the methods should throw exceptions, even when there are
 * socket failures.
 * The interface is NOT threadsafe.
 */
public interface ServerMessageListener {
    /**
     * Indicate that login was successful.
     * Can only be called when in the LOGIN_PENDING state.
     */
    public void loginSuccess();

    /**
     * Indicate that an error occurred.
     * error(100) can only be called when in the LOGIN_PENDING state
     * error(200) can only be called when in the CONNECT_PENDING state
     */
    public void error(int code);

    /**
     * Indicate that the board connection successfully completed.
     * Return some data about the board connected to.
     * Can only be called when in the CONNECT_PENDING state
     * @param id: the id of the board connected to
     * @param users: list of users currently connected to the board
     * @param data: the board itself (that contains all imaging data)
     */
    public void connectToBoardSuccess(int id, List<String> users, Whiteboard data);

    /**
     * Send a message from the server saying that the pixel at Point point
     * should have Color color.
     * Can only be called in the CONNECTED or DISCONNECT_PENDING states.
     * @param point: the point that has it's color changed
     * @param color: the new color of the point
     */
    public void updatePixel(Point point, Color color);

    /**
     * Indicate that the list of users connected to the board has changed.
     * Can only be called in the CONNECTED or DISCONNECT_PENDING states
     * @param users: a list of all the users connected to the board
     */
    public void updateUsers(List<String> users);

    /**
     * Indicate that the client has successfully disconnected from
     * the board.
     * Can only be called in the DISCONNECT_PENDING state
     */
    public void disconnectFromBoardSuccess();

    /**
     * The server shuts down and sends necessary "close" messages
     * to any connected object.
     * Can be called from any state
     */
    public void serverClose();
}
