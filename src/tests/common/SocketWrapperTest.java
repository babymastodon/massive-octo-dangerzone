package tests.common;

import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import common.*;
import java.net.*;
import java.io.*;

public class SocketWrapperTest {

    /**
     * SocketWrapper tests
     */
    public static final int PORT = 1425;


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
        delay();
        verify(l2).onReadLine("Line1");

        s2.writeLine("Line2");
        delay();
        verify(l1).onReadLine("Line2");


        // Test closing and onReadError()
        s1.close();
        delay();
        verify(l2).onReadFinish();
        verify(l1).onReadFinish();


        // Test write error
        s1.writeLine("Fail");
        delay();
        verify(l1).onWriteError(any(IOException.class));
    }

    private void delay() throws Exception{
        Thread.sleep(100);
    }
}
