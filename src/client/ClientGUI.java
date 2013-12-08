package client;

import java.util.ArrayList;
import java.util.List;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import common.*;


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
    // ---- begin section ------
    // variables in this section should not be accessed without
    // locking the ClientGUI object
    private ArrayList<String> users = new ArrayList<String>();
    private Whiteboard board = null;
    private int boardID = -1;
    private boolean loggedIn = false;
    // ---- end section --------
    
    // ---- begin section ------
    // variables in this section may only be accessed from the
    // Java Swing thread
    private String username;
    private JFrame loginWindow;
    private JFrame connectWindow;
    private ClientMessageListener cmListener = null;
    // ---- end section --------
    

    /**
     * Initialize class members. Make the GUI elements, but 
     * don't show display them yet.
     */
    public ClientGUI(){
        createLoginScreen();
        createConnectScreen();
    }
    
    /**
     * Use the provided listener object to send messages to the server.
     * @param l: the listener used to send messages.
     */
    public void setClientMessageListener(ClientMessageListener l){
        assert this.cmListener == null;
        this.cmListener = l;
    }

    /**
     * Display the GUI on screen, and start listening for events
     * from the user.
     *
     * precondition: setClientMessageListener must have been called
     * with a non-null argument.
     */
    public void start(){
        assert cmListener != null;
        
        showLoginScreen();
    }

    @Override
    public void loginSuccess() {
        synchronized (this){
            // TODO: maybe this is not necessary
            this.loggedIn = true;
        }
        hideLoginScreen();
        showConnectScreen();
    }

    @Override
    public void error(int code) {
        //TODO: display these in the gui
        System.err.println("Error number " + code);
        if (code == 100){
            System.err.println("The username was already taken");
        }
        else if (code == 200){
            System.err.println("There is no board with that id");
        }
        else{
            System.err.println("Unrecognized error");
        }
    }

    @Override
    public void connectToBoardSuccess(int id, List<String> users,
            Whiteboard data) {
        // TODO change gui to see the board.
        this.users = new ArrayList<String>(users);
        this.board = data;
        this.boardID = id;
    }

    @Override
    public void updatePixel(Point point, Color color) {
        this.board.setPixel(point, color);
    }

    @Override
    public void updateUsers(List<String> users) {
        this.users = new ArrayList<String>(users);
    }

    @Override
    public void disconnectFromBoardSuccess() {
        this.board = null;
        this.boardID = -1;
        this.users = new ArrayList<String>();
    }

    @Override
    public void serverClose() {
        this.board = null;
        this.boardID = -1;
        this.users = new ArrayList<String>();

        cmListener.clientClose();
    }

    private void createLoginScreen(){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                loginWindow = new JFrame("Login Page");
                loginWindow.setLayout(new BoxLayout(loginWindow.getContentPane(), BoxLayout.X_AXIS));
                final JLabel usernameLabel = new JLabel("Username:");
                final JTextField usernameBox = new JTextField(20);
                final JButton loginButton = new JButton("Login");
                loginWindow.add(usernameLabel);
                loginWindow.add(usernameBox);
                loginWindow.add(loginButton);

                loginButton.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        username = usernameBox.getText();
                        if (!username.isEmpty()) {
                            cmListener.login(username);
                        }
                    }
                });

                loginWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                loginWindow.pack();
                loginWindow.setMinimumSize(loginWindow.getSize());
            }
        });
    }

    private void showLoginScreen(){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                loginWindow.setVisible(true);
            }
        });
    }

    private void hideLoginScreen(){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                loginWindow.setVisible(false);
            }
        });
    }

    private void createConnectScreen(){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                connectWindow = new JFrame("Login Page");
                connectWindow.setLayout(new BoxLayout(connectWindow.getContentPane(), BoxLayout.X_AXIS));
                final JLabel boardIDLabel = new JLabel("Board ID:");
                final JTextField boardIDBox = new JTextField(20);
                final JButton connectToBoardButton = new JButton("Connect to board");
                final JButton newBoardButton = new JButton("New Board");
                connectWindow.add(boardIDLabel);
                connectWindow.add(boardIDBox);
                connectWindow.add(connectToBoardButton);
                connectWindow.add(newBoardButton);

                connectToBoardButton.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        try{
                            int boardID = Integer.parseInt(boardIDBox.getText());
                            cmListener.connectToBoard(boardID);
                        } catch (Exception ex) {
                            // TODO: show a message?
                        }
                    }
                });

                newBoardButton.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        cmListener.newBoard();
                    }
                });

                connectWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                connectWindow.pack();
                connectWindow.setMinimumSize(connectWindow.getSize());
            }
        });
    }

    private void showConnectScreen(){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                connectWindow.setVisible(true);
            }
        });
    }

    private void hideConnectScreen(){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                connectWindow.setVisible(false);
            }
        });
    }
}
