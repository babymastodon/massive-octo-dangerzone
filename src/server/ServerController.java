package server;

import java.net.ServerSocket;
import java.net.Socket;

import common.*;

/**
 * Listens for incoming client connections. For each new client
 * connection, it constructs a new ServerSocketHandler, a new SessionController,
 * and connects them together by adding them to each other as listeners.
 * 
 * This is NOT thread-safe.
 */
public class ServerController {

    private final ServerSocket serverSocket;
    private final AuthenticationBackend auth;
    private final WhiteboardMap map;

    /**
     * Construct a ServerController that listens for new connections on
     * the given server socket.
     * @param s: the socket that the server will listen on
     */
    public ServerController(ServerSocket s){
        this.serverSocket = s;
        this.auth = new AuthenticationBackend();
        this.map = new WhiteboardMap();
    }

    /**
     * Execute the main loop.
     */
    public void run(){
        // run the server forever
        while (true){
            final Socket socket;

            // Accept a new connection
            // return if failure
            try {
                socket = serverSocket.accept();
            } catch (Exception e){
                e.printStackTrace();
                return;
            }

            // Construct the socketWrapper that runs the background thread
            SocketWrapper socketWrapper = new SocketWrapper(socket);

            // Construct a SocketHandler to interpret the socket protocol
            ServerSocketHandler socketHandler = new ServerSocketHandler(socketWrapper);
            // Construct the SessionHandler to attach to the socket
            SessionHandler sessionHandler = new SessionHandler(auth, map);

            // Connect them together
            socketHandler.setClientMessageListener(sessionHandler);
            sessionHandler.setServerMessageListener(socketHandler);

            // Start the socket wrapper thread
            socketWrapper.start();
        }
    }
}
