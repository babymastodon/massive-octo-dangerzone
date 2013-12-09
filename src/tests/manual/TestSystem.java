package tests.manual;

/**
 * Manual testing of system from end to end.
 * Get the client and server up and running, and then follow the instructions below.
 * 
 * Overall, tests deal with single and multiple users, and single and multiple boards.
 * @category no_didit
 */
public class TestSystem{
    /**
     * Single client single board: Single client logs in with a valid username and creates a new board.
     * 
     * Several clients same username: Single client logs in with username "user1". Another client then tries to login
     * with username "user1". The second client should be told that it must choose a different username.
     *  
     * Single client failed board connect: Single client logs in and tries to join a board that doesn't exist. The user
     * should be told that no such board exists.
     * 
     * Single client several boards: Single client logs in and creates a new board with id 1. The client draws a bit.
     * The client then disconnects from board1. The client creates a new board with id 2. The client draws a bit.
     * The client disconnects from board2. The client connects to board1 again and should see his original drawings from board1.
     * 
     * Several clients one and several boards: A client logs in with username "user1". Another client logs in
     * with username "user2". User1 creates a new board with id 5. User1 starts drawing. User2 connects to board5.
     * User2 should see the drawings that user1 has already made. Both user1 and user2 should see that they are
     * both connected to board5. User1 disconnects from board5 and creates a new board with id 6. User2 should now
     * see that it is the only one editing board5, and user1 should see that it is the only one editing board6. Both
     * users should be able to independently draw on their separate boards.
     */
}
