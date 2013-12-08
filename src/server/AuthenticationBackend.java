package server;

import java.util.HashSet;

/**
 * Authenticates usernames (ensures that they are unique).
 *
 * Thread safety:
 *      Is thread-safe: uses the monitor pattern, so all methods are
 *      synchronized.
 */
public class AuthenticationBackend {
    private HashSet<String> loggedInUsers;

    public AuthenticationBackend(){
        loggedInUsers = new HashSet<String>();
    }

    /**
     * returns true if the username is available, and reserves the username. Otherwise return false.
     * @param username: username that we want to know if it's free or not.
     * @return: whether the username was successfully taken.
     */
    public synchronized boolean login(String username){
        if (loggedInUsers.contains(username)){
            return false;
        }
        loggedInUsers.add(username);
        return true;
    }

    /**
     * If username was taken, make it available.
     * @param username: the username that should be made available again.
     */
    public synchronized void logout(String username){
        loggedInUsers.remove(username);
    }
}
