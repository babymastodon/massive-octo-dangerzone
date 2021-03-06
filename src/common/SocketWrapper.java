package common;

import java.io.*;
import java.net.*;

/**
 * Contains generic functionality to implement a thread that reads
 * lines from a socket.
 *
 * Calling the "start" function will initiate a single background thread
 * that will call certain functions on the SocketWrapperListener as
 * messages arrive on the wire.
 *
 * The onReadLine, onReadError, and onReadFinish functions are guarenteed
 *      to be executed from the single background thread.
 *
 * The onWriteError is guarenteed to be executed from the same thread
 *      that called the "writeLine" function
 *
 * Client must set the listener with setSocketWrapperListener() before
 *      calling any other functions.
 *
 * Usage:
 *      The methods of this class must be called in the following order:
 *          - SocketWrapper()
 *          - setSocketWrapperListener()
 *          - start()
 *          - any other function
 *
 * Thread safety:
 *      Public interface is synchronized. The SocketWrapperListener
 *          may be shared between threads, but it is assumed to be thread-safe,
 *          and it may only be set once by setSocketWrapperListener().
 */
public class SocketWrapper{

    private final Socket socket;
    private SocketWrapperListener listener;
    private boolean running;
    private boolean debug;

    /**
     * Construct with the given, open socket.
     *
     * @param s an open socket
     * @param debug set True to print debug messages to stdout
     */
    public SocketWrapper(Socket s, boolean debug){
        this.socket = s;
        this.debug = debug;
    }
    public SocketWrapper(Socket s){
        this(s, false);
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
     * Listener must not be null. May only be called once.
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
                            if (debug){
                                System.out.println("receiving: " + line.substring(0, Math.min(line.length(), 100)));
                            }
                            listener.onReadLine(line.trim());
                        }
                    } catch (Exception e){
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
     * Listener must not be null. In case of failure, the
     *      "onWriteError" function of the listener is executed.
     *
     * @param line a single-line string to write to the socket
     */
    public synchronized void writeLine(String line){
        assert listener != null;

        try {
            if (debug){
                System.out.println("sending: " + line.substring(0, Math.min(line.length(), 100)));
            }
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
