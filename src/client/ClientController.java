package client;

import java.net.Socket;

import common.*;

/**
 * Constructs a ClientGUI and a ClientSocketHandler and connects them
 * by adding them to each other as listeners.
 *
 * Usage:
 *      The methods of this class must be called in the following order:
 *          - ClientController()
 *          - run()
 * 
 * Thread safety:
 *      Public interface is not thread safe.
 */
public class ClientController {

    private final ClientSocketHandler socketHandler;
    private final SocketWrapper socketWrapper;
    private final ClientGUI gui;
    private boolean runCalled;

    /**
     * Construct a ClientController that communicates to a server through
     * the given socket
     * @param s: socket between the client and the server
     */
    public ClientController(Socket s){
        // Construct the socketWrapper that runs the background thread
        socketWrapper = new SocketWrapper(s);

        // Construct a SocketHandler to interpret the socket protocol
        socketHandler = new ClientSocketHandler(socketWrapper);

        // Construct the ClientGUI object
        gui = new ClientGUI();

        // Connect the ClientGUI and the ClientSocketHandler together
        socketHandler.setServerMessageListener(gui);
        gui.setClientMessageListener(socketHandler);

        runCalled = false;
    }

    /**
     * Execute the main loop.
     *
     * May only be called once.
     */
    public void run(){
        assert runCalled == false;
        runCalled = true;

        // Start the GUI
        gui.start();

        // Start the socket wrapper thread
        socketWrapper.start();
    }

}
