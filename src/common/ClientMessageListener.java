package common;

/**
 * Callback functions to be executed for events originating in the client
 * It is NOT guaranteed to be threadsafe.
 * Instead of throwing exceptions, the class should call clientClose() if
 * something bad happens
 *
 */
public interface ClientMessageListener {
    /**
     * Request to login with the given username.
     * Can only be called when in the NOT_LOGGED_IN state
     * @param username: the requested username to log in with.
     */
    public void login(String username);

    /**
     * Request to connect to the board with the id given.
     * Can only be called when in the NOT_CONNECTED state
     * @param id: the id of the board to connect to
     */
    public void connectToBoard(int id);

    /**
     * request to create and connect to a new board.
     * Can only be called when in the NOT_CONNECTED state
     */
    public void newBoard();

    /**
     * Request to disconnect from the board currently connected to.
     * Can only be called when in the CONNECTED state 
     */
    public void disconnectFromBoard();

    /**
     * Request to draw a line from and including p1 to p2 with color color.
     * Can only be called when in then CONNECTED state
     * @param p1: one end of the line segment to be drawn
     * @param p2: the other end of the line segment to be drawn
     * @param color: the color of the line that should be drawn
     */
    public void drawLine(Point p1, Point p2, Color color, int width);

    /**
     * The client should be closed down (such as sending "close"
     * messages to any connected objects).
     * Can be called from any state.
     */
    public void clientClose();
}
