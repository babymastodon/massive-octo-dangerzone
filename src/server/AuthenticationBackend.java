package server;

/**
 * Authenticates usernames (ensures that they are unique).
 * Is thread-safe: uses the monitor pattern, so all methods are
 * synchronized.
 */
public class AuthenticationBackend {
	/**
	 * returns true if the username is available. Otherwise return false.
	 * @param username: username that we want to know if it's free or not.
	 * @return: whether the username is available.
	 */
	public boolean login(String username){
		
	}
	
	/**
	 * If username was taken, make it available.
	 * @param username: the username that should be made available again.
	 */
	public void logout(String username){
		
	}
}
