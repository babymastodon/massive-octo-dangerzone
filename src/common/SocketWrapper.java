package common;

import java.io.*;
import java.net.*;

/**
 * Contains generic functionality to implement a thread that reads lines from a socket.
 *
 * public interface is synchronized and thread safe
 * client should set the listener with setSocketWrapperListener() before
 *  calling any other functions.
 */
public class SocketWrapper{

    private final Socket socket;
    private SocketWrapperListener listener;
    private boolean running;

    /**
     * Construct with the given, open socket.
     *
     * @param s an open socket
     */
    public SocketWrapper(Socket s){
        this.socket = s;
    }

    /**
     * Set the listener. Listener should not already be set.
     *
     * @param listener the listener
     */
    public synchronized void setSocketWrapperListener(SocketWrapperListener listener){
        assert this.listener == null;
        this.listener = listener;
    }

    /**
     * Start the background listener thread.
     *
     * Listener must not be null.
     */
    public synchronized void start(){
        assert listener != null;
        assert socket != null;

        // Ensure that only one thread is ever created
        if (!running){
            new Thread(new Runnable(){
                public void run(){
                    try {
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                        for (String line = in.readLine(); line != null; line = in.readLine()) {
                            listener.onReadLine(line.trim());
                        }
                    } catch (IOException e){
                        listener.onReadError(e);
                    } finally {
                        listener.onReadFinish();
                    }
                }
            }).start();

            running = true;
        }
    }


    /**
     * Write a string to the socket. A newline character is
     * automatically appended to the string.
     *
     * Listener must not be null.
     *
     * @param line a single-line string to write to the socket
     */
    public synchronized void writeLine(String line){
        assert listener != null;

        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(line);
        } catch (IOException e){
            listener.onWriteError(e);
        }
    }


    /**
     * Closes the underlying socket if still open. This
     * will trigger future reads/writes to fail.
     */
    public synchronized void close(){
        try{
            socket.close();
        } catch (IOException e){
        }
    }
}
