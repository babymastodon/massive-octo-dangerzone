package tests;

import org.junit.Test;

import static org.mockito.Mockito.*;
import common.ServerMessageListener;
import common.Whiteboard;
import server.AuthenticationBackend;
import server.WhiteboardMap;
import server.WhiteboardStruct;
import static org.junit.Assert.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;

public class ServerTests {

	/**
	 * AuthenticationBackend tests
	 */
	
	/**
	 * Two different users try to log in with two different usernames.
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
	 * Two users try to log in with the same username.
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
	 * Logging out works whether or not you are already logged in.
	 */
	@Test
	public void logoutBasic(){
		AuthenticationBackend ab = new AuthenticationBackend();
		String user1 = "username1";
		String user2 = "username2";
		ab.login(user1);
		ab.logout(user1);
		ab.logout(user2);
	}
	
	/**
	 * Make sure logging out can solve username conflicts.
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
	
	
	/**
	 * Test WhiteboardStructure
	 */
	
	@Test
	public void whiteboardStructBasic(){
		Whiteboard wb = mock(Whiteboard.class);
		List<String> users = mock(List.class);
		List<ServerMessageListener> listeners = mock(List.class);
		int id = 5;
		
		WhiteboardStruct ws = new WhiteboardStruct(wb, users, listeners, id);
		
		assertEquals(wb, ws.getWhiteboard());
		assertEquals(users, ws.getUsers());
		assertEquals(listeners, ws.getListeners());
		assertEquals(id, ws.getID());
	}
	
	/**
	 * Test WhiteboardMap
	 */
	
	@Test
	public void whiteboardMapBasic(){
		WhiteboardMap wm = new WhiteboardMap();
		wm.newBoard();
	}
}