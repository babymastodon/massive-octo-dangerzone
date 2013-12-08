package tests.server;

import org.junit.Test;

import static org.junit.Assert.*;

import server.*;

/**
 * Suite of tests to check that AuthenticationBackend.java correctly maintains
 * a list of all users logged in to a single board. Multiple users should not
 * be able to log in with the same username.
 * 
 * Testing strategy:
 * 2 users log in with different usernames.
 * 2 users try to log in with the same username.
 * 2 users log in, one logs out. The other user stays logged in.
 * 1 user tries to log out without logging in first.
 * 2 users try to log in with the same username. The second user must wait for the first to log out before it can log in.
 */
public class AuthenticationBackendTest{
    /**
     * Two different users try to log in with two different usernames. They both log in.
     */
    @Test
    public void basicLogin(){
        AuthenticationBackend ab = new AuthenticationBackend();
        String user1 = "username1";
        String user2 = "username2";
        assertEquals(true, ab.login(user1));
        assertEquals(true, ab.login(user2));
    }

    /**
     * Two users try to log in with the same username. The second user is rejected.
     */
    @Test
    public void conflictingLogins(){
        AuthenticationBackend ab = new AuthenticationBackend();
        String user1 = "username1";
        String user2 = "username1";
        assertEquals(true, ab.login(user1));
        assertEquals(false, ab.login(user2));
    }

    /**
     * One user logging out does not log out all of the users, only the right one
     */
    @Test
    public void logoutBasic(){
        AuthenticationBackend ab = new AuthenticationBackend();
        String user1 = "username1";
        String user2 = "username2";
        String user3 = "username2";
        ab.login(user1);
        ab.login(user2);
        ab.logout(user1); //username1 should now be free, but username2 is still taken.
        assertEquals(false, ab.login(user3));
    }
    
    /**
     * Logging out without having logged in should not throw any errors.
     */
    @Test
    public void logoutWithoutLoggingIn(){
        AuthenticationBackend ab = new AuthenticationBackend();
        String user1 = "username1";
        ab.logout(user1);
    }

    /**
     * Make sure logging out can solve username conflicts. In this example, the second user has to 
     * wait for the first user to log out before it can log in.
     */
    @Test
    public void logoutConflict(){
        AuthenticationBackend ab = new AuthenticationBackend();
        String user1 = "username1";
        String user2 = "username1";
        assertEquals(true, ab.login(user1));
        assertEquals(false, ab.login(user2));
        ab.logout(user1);
        assertEquals(true, ab.login(user2));
    }
}
