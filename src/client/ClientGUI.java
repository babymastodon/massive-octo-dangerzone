package client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.awt.BasicStroke;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;


import common.*;


/**
 * Stores the list of all usernames currently connected to the internal
 * Whiteboard object.
 * Stores the state of the board in previously mentioned Whiteboard object.
 * Refreshes the contents of the whiteboard onto the screen at regular
 * intervals using the graphics2d.drawImage(BufferedImage, null, 0, 0) function
 * 
 * When serverClose() is called, close the GUI if it isn't already closed.
 * When the GUI is closed, call clientClose() of the ClientMessageListener
 * Instead of throwing exceptions, call serverClose()
 *
 * Thread safety:
 *      Public interface is not thread safe. Should only be accessed
 *          from a single thread.
 *      Some of the class members are shared with the Swing event loop
 *          thread. These variables are accessed only when the ClientGUI lock
 *          is held.
 *      Some of the class members may only be accessed from within the
 *          Swing event loop thread using SwingUtilities.InvokeLater(). 
 */
public class ClientGUI implements ServerMessageListener{
    // ---- begin section ------
    // variables in this section should not be accessed without
    // locking the ClientGUI object
    private ArrayList<String> users = new ArrayList<String>();
    private Whiteboard board;
    private int boardID;
    // ---- end section --------

    // ---- begin section ------
    // variables in this section may only be accessed from the
    // Java Swing thread
    private String username;
    private JFrame loginWindow;
    private JFrame connectWindow;
    private JFrame canvasWindow;

    private ClientMessageListener cmListener;

    private BoardCanvas canvas;
    private JLabel usersLabel;
    private JLabel boardIDLabel;

    private boolean shouldRefreshCanvas;

    private int penSize=PEN_WIDTH;
    private Color color=BLACK;
    // TODO: add pen size and color variables
    // ---- end section --------

    // ---- begin section ------
    // Constants for the user interface.
    private static final Color WHITE = new Color(255,255,255);
    private static final Color BLACK = new Color(0,0,0);
    private static final Color RED = new Color(230,20,20);
    private static final Color BLUE = new Color(20,20,230);
    private static final Color GREEN = new Color(20,230,20);
    private static final int ERASER_WIDTH = 40;
    private static final int PEN_WIDTH = 5;
    private static final int REFRESH_DELAY = 20;
    // ---- end section --------


    /**
     * Initialize class members. Make the GUI elements, but 
     * don't show display them yet.
     */
    public ClientGUI(){
        createLoginScreen();
        createConnectScreen();
        createCanvasScreen();
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
        synchronized(this){
            this.users = new ArrayList<String>(users);
            this.board = data;
            this.boardID = id;
        }

        hideConnectScreen();
        showCanvasScreen();
        requestRefresh();
    }

    @Override
    public void updatePixel(Point point, Color color) {
        synchronized(this){
            this.board.setPixel(point, color);
        }
        requestRefresh();
    }

    @Override
    public void updateUsers(List<String> users) {
        synchronized(this){
            this.users = new ArrayList<String>(users);
        }
        requestRefresh();
    }

    @Override
    public void disconnectFromBoardSuccess() {
        synchronized(this){
            this.board = null;
            this.boardID = -1;
            this.users = new ArrayList<String>();
        }
        hideCanvasScreen();
        showConnectScreen();
    }

    @Override
    public void serverClose() {
        cmListener.clientClose();
        System.exit(0);
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

    private void createCanvasScreen(){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                canvasWindow = new JFrame("Whiteboard");

                // Left-hand box contains the canvas and list of connected users
                JPanel canvasWithUsers = new JPanel();
                canvas = new BoardCanvas();
                usersLabel = new JLabel();

                canvasWithUsers.setLayout(new BoxLayout(canvasWithUsers, BoxLayout.Y_AXIS));
                canvasWithUsers.add(canvas);
                canvasWithUsers.add(usersLabel);

                // Right-hand box contains the buttons, and the board id
                JPanel drawAndErase = new JPanel();
                boardIDLabel = new JLabel();
                JLabel colorInstr = new JLabel("Choose your color");
                JButton eraseButton = makeButton("ERASE", WHITE, ERASER_WIDTH);
                JButton blackButton = makeButton("BLACK", BLACK, PEN_WIDTH);
                JButton redButton = makeButton("RED", RED, PEN_WIDTH);
                JButton blueButton = makeButton("BLUE", BLUE, PEN_WIDTH);
                JButton greenButton = makeButton("GREEN", GREEN, PEN_WIDTH);
                JButton exitButton = makeExitButton();

                drawAndErase.setLayout(new BoxLayout(drawAndErase, BoxLayout.Y_AXIS));
                drawAndErase.add(boardIDLabel);
                drawAndErase.add(colorInstr);
                drawAndErase.add(blackButton);
                drawAndErase.add(redButton);
                drawAndErase.add(blueButton);
                drawAndErase.add(greenButton);
                drawAndErase.add(eraseButton);
                drawAndErase.add(exitButton);

                // Add the two sub-panels to the JFrame
                canvasWindow.setLayout(new FlowLayout());
                canvasWindow.add(canvasWithUsers);
                canvasWindow.add(drawAndErase);

                // TODO: add the mouse listener and mouseMotionListener here
                addDrawingController();

                // Start a timer that repaints the canvas up to
                // 1000/REFRESH_DELAY times per second if the UI has
                // changed
                new Timer(REFRESH_DELAY, new ActionListener(){
                    public void actionPerformed(ActionEvent e) {
                        if (shouldRefreshCanvas){
                            shouldRefreshCanvas = false;
                            refreshCanvasElements();
                        }
                    }
                }).start();

                canvasWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                canvasWindow.pack();
                canvasWindow.setMinimumSize(canvasWindow.getSize());
            }
        });
    }
    /*
     * Add the mouse listener that supports the user's freehand drawing.
     */
    private void addDrawingController() {
        DrawingController controller = new DrawingController();
        canvas.addMouseListener(controller);
        canvas.addMouseMotionListener(controller);
    }

    /*
     * DrawingController handles the user's freehand drawing.
     */
    private class DrawingController implements MouseListener, MouseMotionListener {
        // store the coordinates of the last mouse event, so we can
        // draw a line segment from that last point to the point of the next mouse event.
        private int lastX, lastY; 

        /**
         * When mouse button is pressed down, draw a single point.
         */
        public void mousePressed(MouseEvent e) {
            lastX = e.getX();
            lastY = e.getY();

            Point thisPoint = new Point(lastX, board.HEIGHT-lastY);

            drawLine(thisPoint, thisPoint);
        }

        /**
         * When mouse moves while a button is pressed down,
         * draw a line segment.
         */
        public void mouseDragged(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            
            Point lastPoint = new Point(lastX, board.HEIGHT-lastY);
            Point thisPoint = new Point(x, board.HEIGHT-y);

            drawLine(lastPoint, thisPoint);

            lastX = x;
            lastY = y;
        }

        /**
         * Draw a line on the local whiteboard, and send a drawLine
         * message to the server.
         */
        private void drawLine(Point lastPoint, Point thisPoint){
            if (Whiteboard.checkPointInBounds(thisPoint) && Whiteboard.checkPointInBounds(lastPoint)){
                // draw immediately to the local board so that the
                // user gets instant feedback
                synchronized(this){
                    board.drawLine(lastPoint, thisPoint, color, penSize);
                }
                requestRefresh();

                // send to the server
                cmListener.drawLine(lastPoint, thisPoint, color, penSize);
            }
        }

        // Ignore all these other mouse events.
        public void mouseMoved(MouseEvent e) { }
        public void mouseClicked(MouseEvent e) { }
        public void mouseReleased(MouseEvent e) { }
        public void mouseEntered(MouseEvent e) { }
        public void mouseExited(MouseEvent e) { }
    }


    private JButton makeButton(String label, final Color col, final int penSi){
        JButton btn = new JButton(label);
        btn.setName(label);
        btn.setPreferredSize(new Dimension (100,100));
        btn.addActionListener( new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                color=col;
                penSize=penSi;
            }
        });
        return btn;
    }

    private JButton makeExitButton(){
        JButton btn = new JButton("EXIT");
        btn.setName("EXIT");
        btn.setPreferredSize(new Dimension (100,100));
        btn.addActionListener( new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                cmListener.disconnectFromBoard();
            }
        });
        return btn;
    }

    private void showCanvasScreen(){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                canvasWindow.setVisible(true);
            }
        });
    }

    private void hideCanvasScreen(){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                canvasWindow.setVisible(false);
            }
        });
    }

    private void requestRefresh(){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                synchronized(ClientGUI.this){
                    shouldRefreshCanvas = true;
                }
            }
        });
    }

    private void refreshCanvasElements(){
        synchronized(ClientGUI.this){
            // show the connected users
            StringBuilder ub = new StringBuilder();
            ub.append("Connected users: ");
            for (String username: users){
                ub.append(username).append(" ");
            }
            usersLabel.setText(ub.toString());

            // show the current board id
            boardIDLabel.setText("Board ID: " + boardID);

            // update the image shown in the canvas
            board.copyPixelData(canvas.getDrawingBuffer());

            // request repaint
            canvasWindow.repaint();
        }
    }
}
