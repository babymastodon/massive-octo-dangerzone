package tests.common;

import org.junit.Test;

import static org.junit.Assert.*;

import common.*;

public class WhiteboardTest {

    /**
     * Whiteboard tests
     */

    /**
     * Test color equality and hashcode.
     */
    @Test
    public void testEquals(){
        Whiteboard w1 = new Whiteboard();
        Whiteboard w2 = new Whiteboard();
        Whiteboard w3 = new Whiteboard();

        w1.setPixel(new Point(1,2), new Color(1,2,3));
        w2.setPixel(new Point(1,2), new Color(1,2,3));

        assertEquals(true, w1.equals(w2));
        assertEquals(false, w1.equals(w3));
        assertEquals(false, w1.equals(""));
        assertEquals(w1.hashCode(), w2.hashCode());
    }
}
