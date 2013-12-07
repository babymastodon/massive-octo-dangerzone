package launcher;

import java.net.Socket;

import client.*;

/**
 * Command-line launcher for the client.
 *
 * Usage: java ClientLauncher <host> <port>
 *
 * If the command line arguments fail to parse,
 * the host "localhost" and port "7495" are used
 * by default.
 */
public class ClientLauncher {
    public static void main(String[] args){
        int port = 0;
        String host = null;
        try {
            host = args[0];
            port = Integer.parseInt(args[1]);
        } catch (Exception e){
            host = "localhost";
            port = 7495;
        }

        Socket s = null;
        try {
            s = new Socket(host, port);
        } catch (Exception e){
            System.out.println("Could not connect to host " + host + ":" + port);
            return;
        }

        ClientController c = new ClientController(s);
        System.out.println("Connected to host " + host + ":" + port);
        c.run();
    }
}
