package tests.common;

import org.junit.Test;

import org.mockito.verification.VerificationMode;
import static org.mockito.Mockito.*;
import common.*;
import java.net.*;
import java.io.*;

/**
 * Test suite for SocketWrapper.java. SocketWrapper should let a thread read in lines from a socket
 * 
 * Testing strategy:
 * Create and connect two sockets.
 * Add listeners to these sockets.
 * Have one socket read in what the other socket wrote within an acceptable time.
 * Switch the roles of the sockets and repeat the previous test.
 * Close a socketWrapper
 * Test that the closed socketWrapper cannot write.
 *
 */
public class SocketWrapperTest {
    /**
     * Test Socket creation and communication. Because of all the setup required to get sockets running,
     * all tests are contained sequentially within this method.
     */
    @Test
    public void testAll() throws Exception{ //must throw an exception because of IOExceptions from sockets
        final int PORT = 1425; //port for TCP communication
        final VerificationMode delay = timeout(2000); //mockito verify delay, wait for up to 2 seconds
        
        // Construct and connect two sockets to each other
        ServerSocket ss = new ServerSocket(PORT);
        SocketWrapper s1 = new SocketWrapper(new Socket("localhost", PORT));
        SocketWrapper s2 = new SocketWrapper(ss.accept());

        SocketWrapperListener l1 = mock(SocketWrapperListener.class); //use Mockito to create two SocketWrapperListeners
        SocketWrapperListener l2 = mock(SocketWrapperListener.class);

        s1.setSocketWrapperListener(l1);
        s2.setSocketWrapperListener(l2);

        s1.start();
        s2.start();


        // Test sending and receiving
        s1.writeLine("Line1");
        verify(l2, delay).onReadLine("Line1");

        s2.writeLine("Line2");
        verify(l1, delay).onReadLine("Line2");


        // Test closing and onReadFinish()
        s1.close();
        verify(l2, delay).onReadFinish();
        verify(l1, delay).onReadFinish();


        // Test write error
        s1.writeLine("Fail");
        verify(l1, delay).onWriteError(any(IOException.class));
    }
}
