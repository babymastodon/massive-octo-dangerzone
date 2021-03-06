package launcher;

import java.net.ServerSocket;

import server.*;

/**
 * Command-line launcher for the server.
 *
 * Usage: java ServerLauncher <port>
 *
 * If the command line arguments fail to parse,
 * the port "7495" is used by default.
 */
public class ServerLauncher {
    public static void main(String[] args){
        // parse the command line arguments
        int port = 0;
        try {
            port = Integer.parseInt(args[0]);
        } catch (Exception e){
            port = 7495;
        }

        // connect to the remote host
        ServerSocket s = null;
        try {
            s = new ServerSocket(port);
        } catch (Exception e){
            System.out.println("Could not reserve port " + port);
            return;
        }

        // construct and run the ServerController
        ServerController c = new ServerController(s);
        System.out.println("Server running on port " + port);
        c.run();
    }
}
