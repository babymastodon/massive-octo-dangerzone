package tests.common;

import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import common.Color;

public class ColorTest {

    /**
     * Color tests
     */

    /**
     * Test color construction and methods.
     */
    @Test
    public void testConstructor(){
        Color p = new Color(1,2,3);
        assertEquals(1, p.getRed());
        assertEquals(2, p.getGreen());
        assertEquals(3, p.getBlue());
    }


    /**
     * Test color equality and hashcode.
     */
    @Test
    public void testEquals(){
        Color p1 = new Color(1,2,3);
        Color p2 = new Color(1,2,3);
        Color p3 = new Color(4,5,6);
        assertEquals(true, p1.equals(p2));
        assertEquals(false, p1.equals(p3));
        assertEquals(false, p1.equals(""));
        assertEquals(p1.hashCode(), p2.hashCode());
    }
}
