package tests.common;

import org.junit.Test;

import org.mockito.verification.VerificationMode;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import common.*;
import java.net.*;
import java.io.*;

public class SocketWrapperTest {

    /**
     * SocketWrapper tests
     */

    // Port for TCP communication
    public static final int PORT = 1425;

    // mockito verify delay, wait for up to 2 seconds
    public static final VerificationMode delay = timeout(2000);


    /**
     * Test Socket creation and communication.
     */
    @Test
    public void testConstructor() throws Exception{
        // Construct and connect two sockets to each other
        ServerSocket ss = new ServerSocket(PORT);
        SocketWrapper s1 = new SocketWrapper(new Socket("localhost", PORT));
        SocketWrapper s2 = new SocketWrapper(ss.accept());

        SocketWrapperListener l1 = mock(SocketWrapperListener.class);
        SocketWrapperListener l2 = mock(SocketWrapperListener.class);

        s1.start();
        s2.start();

        s1.setSocketWrapperListener(l1);
        s2.setSocketWrapperListener(l2);


        // Test sending and receiving
        s1.writeLine("Line1");
        verify(l2, delay).onReadLine("Line1");

        s2.writeLine("Line2");
        verify(l1, delay).onReadLine("Line2");


        // Test closing and onReadError()
        s1.close();
        verify(l2, delay).onReadFinish();
        verify(l1, delay).onReadFinish();


        // Test write error
        s1.writeLine("Fail");
        verify(l1, delay).onWriteError(any(IOException.class));
    }
}
