package common;

import java.io.*;

/**
 * Implements callback functions for the SocketWrapper.
 *
 * Thread safety:
 *      Public interface may or may not be thread safe.
 */
public interface SocketWrapperListener{

    /**
     * This function gets executed for every line that
     * gets read by the socket.
     *
     * @param line a single-line string without a trailing newline
     */
    public void onReadLine(String line);

    /**
     * This function gets executed when the socket
     * throws a read error.
     *
     * @param e the Exception that was raised
     */
    public void onReadError(Exception e);

    /**
     * This function gets executed when the socket
     * closes, with or without error.
     */
    public void onReadFinish();

    /**
     * This function gets executed when the socket
     * throws a write error. This function does not
     * get called from the SocketWrapper's background
     * thread.
     *
     * @param e the Exception that was raised
     */
    public void onWriteError(Exception e);
}
